package controller;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import model.Pagos;
import model.Prestamos;
import model.Cuotas;

import repository.PagosDAO;
import repository.PagosRepository;
import repository.ArchivoPagos;

import exception.PagoInvalidoException;

public class PagosController {

    private ArrayList<Pagos> pagos = new ArrayList<>();

    private PrestamosController prestamosController;

    // Dependemos de la interfaz, no de la implementación.
    private final PagosDAO pagosRepository;

    private ArchivoPagos archivoPagos =
            new ArchivoPagos();

    // Constructor predeterminado.
    public PagosController(PrestamosController prestamosController) {
        this(prestamosController, new PagosRepository());
    }

    // Constructor con inyección de dependencias.
    public PagosController(
            PrestamosController prestamosController,
            PagosDAO pagosRepository) {

        this.prestamosController = prestamosController;
        this.pagosRepository = pagosRepository;

        // Cargar historial de pagos desde MySQL.
        pagos = pagosRepository.listar(
                prestamosController.listarPrestamos()
        );

        System.out.println(
                "Pagos cargados desde MySQL: " + pagos.size()
        );
    }
    
    private void validarPago(
        int idPrestamo,
        double monto) throws PagoInvalidoException {

    Prestamos prestamo = prestamosController.listarPrestamos()
            .stream()
            .filter(p -> p.getIdPrestamo() == idPrestamo)
            .findFirst()
            .orElseThrow(() ->
                    new PagoInvalidoException(
                            "El prestamo no existe."
                    )
            );

    if (!Double.isFinite(monto) || monto <= 0) {
        throw new PagoInvalidoException(
                "El monto del pago debe ser mayor que cero."
        );
    }

    double saldo = calcularSaldo(idPrestamo);

    if (saldo <= 0.1) {
        throw new PagoInvalidoException(
                "El prestamo ya esta cancelado en su totalidad."
        );
    }

    if (monto > saldo + 0.1) {
        throw new PagoInvalidoException(
                "El pago supera el saldo pendiente. Saldo actual: $" + saldo
        );
    }
}

    // Registrar un pago.
    public void registrarPago(int idPrestamo, double monto) {

        try {

            // Validar el préstamo y el monto del pago.
            validarPago(idPrestamo, monto);

            // Buscar el préstamo.
            Prestamos prestamoEncontrado = prestamosController
                    .listarPrestamos()
                    .stream()
                    .filter(p -> p.getIdPrestamo() == idPrestamo)
                    .findFirst()
                    .orElseThrow();

            double saldo = calcularSaldo(idPrestamo);

            // Guardar valores anteriores para poder revertir.
            ArrayList<Double> abonosAnteriores = new ArrayList<>();

            for (Cuotas cuota : prestamoEncontrado.getListaCuotas()) {
                abonosAnteriores.add(cuota.getMontoPagado());
            }

            String estadoAnterior = prestamoEncontrado.getEstado();

            // Aplicar el pago a las cuotas pendientes.
            double restante = monto;

            for (Cuotas cuota : prestamoEncontrado.getListaCuotas()) {

                if (restante <= 0.001) {
                    break;
                }

                double saldoCuota = cuota.getSaldo();

                if (saldoCuota > 0.001) {
                    double abono = Math.min(restante, saldoCuota);
                    cuota.registrarAbono(abono);
                    restante -= abono;
                }
            }

            double nuevoSaldo = calcularSaldo(idPrestamo);

            if (nuevoSaldo <= 0.1) {
                prestamoEncontrado.setEstado("CANCELADO");
            }

            LocalDate fechaPago = LocalDate.now();

            Pagos nuevoPago = new Pagos(
                    0,
                    prestamoEncontrado,
                    monto,
                    fechaPago
            );

            // Guardar pago, cuotas y estado en una transacción MySQL.
            int idGenerado = pagosRepository.registrarPagoCompleto(
                    nuevoPago,
                    prestamoEncontrado.getListaCuotas(),
                    prestamoEncontrado.getEstado()
            );

            // Si falla MySQL, revertir cambios en memoria.
            if (idGenerado == -1) {

                for (int i = 0;
                        i < prestamoEncontrado.getListaCuotas().size();
                        i++) {

                    prestamoEncontrado.getListaCuotas()
                            .get(i)
                            .cargarMontoPagado(abonosAnteriores.get(i));
                }

                prestamoEncontrado.setEstado(estadoAnterior);

                System.out.println("El pago no fue registrado.");
                return;
            }

            Pagos pagoRegistrado = new Pagos(
                    idGenerado,
                    prestamoEncontrado,
                    monto,
                    fechaPago
            );

            pagos.add(pagoRegistrado);

            archivoPagos.guardar(pagos);
            prestamosController.sincronizarArchivo();

            System.out.println("\nPago registrado correctamente.");
            System.out.println("ID del pago: " + idGenerado);
            System.out.println("Monto pagado: $" + monto);
            System.out.println("Saldo pendiente: $" + nuevoSaldo);

            if (nuevoSaldo <= 0.1) {
                System.out.println(
                        "Prestamo cancelado en su totalidad."
                );
            }

        } catch (PagoInvalidoException e) {

            System.out.println(
                    "Error al registrar pago: " + e.getMessage()
            );
        }
    }


    // Calcular total pagado de un préstamo.
    public double calcularTotalPagado(int idPrestamo) {

        double total = 0;

        for (Pagos p : pagos) {

            if (p.getPrestamo().getIdPrestamo() == idPrestamo) {
                total += p.getMonto();
            }
        }

        return total;
    }

    // Calcular saldo pendiente de un préstamo.
    public double calcularSaldo(int idPrestamo) {

        for (Prestamos p : prestamosController.listarPrestamos()) {

            if (p.getIdPrestamo() == idPrestamo) {

                double saldo = 0;

                for (Cuotas cuota : p.getListaCuotas()) {
                    saldo += cuota.getSaldo();
                }

                return saldo;
            }
        }

        return 0;
    }

    // Mostrar cuotas de un préstamo.
    public void mostrarCuotas(int idPrestamo) {

        for (Prestamos p : prestamosController.listarPrestamos()) {

            if (p.getIdPrestamo() == idPrestamo) {

                System.out.println(
                        "\n----- CUOTAS DEL PRESTAMO -----"
                );

                System.out.println(
                        "Cliente: " + p.getCliente().getNombre()
                );

                for (Cuotas cuota : p.getListaCuotas()) {

                    System.out.println("\nCuota: " + cuota.getNumero());
                    System.out.println("Valor: $" + cuota.getValor());
                    System.out.println("Monto Pagado: $" + cuota.getMontoPagado());
                    System.out.println("Interes: $" + cuota.getInteres());
                    System.out.println("Abono a capital: $" + cuota.getCapital());

                    System.out.println(
                            "Saldo adeudado: $"
                            + p.calcularSaldoCapital(cuota.getNumero())
                    );

                    System.out.println(
                            "Vencimiento: " + cuota.getFechaVencimiento()
                    );

                    System.out.println("Estado: " + cuota.getEstado());

                    System.out.println(
                            "Dias de mora: " + cuota.calcularDiasMora()
                    );
                }

                return;
            }
        }

        System.out.println("Prestamo no encontrado.");
    }

    // Mostrar cuotas vencidas.
    public void mostrarCuotasVencidas() {

        System.out.println("\n----- CUOTAS VENCIDAS -----");

        boolean encontrado = false;

        for (Prestamos p : prestamosController.listarPrestamos()) {

            for (Cuotas cuota : p.getListaCuotas()) {

                if (cuota.getEstado().equals("Vencida")) {

                    encontrado = true;

                    System.out.println("Prestamo: " + p.getIdPrestamo());
                    System.out.println("Cliente: " + p.getCliente().getNombre());
                    System.out.println("Cuota: " + cuota.getNumero());

                    System.out.println(
                            "Vencimiento: " + cuota.getFechaVencimiento()
                    );

                    System.out.println(
                            "Dias de mora: " + cuota.calcularDiasMora()
                    );

                    System.out.println(
                            "Saldo de la cuota: $" + cuota.getSaldo()
                    );

                    System.out.println("------------------------------");
                }
            }
        }

        if (!encontrado) {
            System.out.println("No hay cuotas vencidas.");
        }
    }

    // Consultar historial de pagos.
    public List<Pagos> listarPagos() {
        return new ArrayList<>(pagos);
    }
}

