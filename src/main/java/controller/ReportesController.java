package controller;

import java.util.List;

import model.Prestamos;

public class ReportesController {

    private PrestamosController prestamosController;

    public ReportesController(PrestamosController prestamosController) {
        this.prestamosController = prestamosController;
    }

    // Calcular el saldo pendiente de un préstamo.
    private double calcularSaldo(Prestamos prestamo) {

        return prestamo.getListaCuotas()
                .stream()
                .mapToDouble(c -> c.getSaldo())
                .sum();
    }

    // Identificar si un préstamo tiene cuotas vencidas pendientes.
    private boolean tieneCuotasVencidas(Prestamos prestamo) {

        return prestamo.getListaCuotas()
                .stream()
                .anyMatch(c -> c.getSaldo() > 0.1
                        && c.calcularDiasMora() > 0);
    }

    // Calcular el valor total de las cuotas vencidas.
    private double calcularDeudaVencida(Prestamos prestamo) {

        return prestamo.getListaCuotas()
                .stream()
                .filter(c -> c.getSaldo() > 0.1
                        && c.calcularDiasMora() > 0)
                .mapToDouble(c -> c.getSaldo())
                .sum();
    }

    // Mostrar préstamos activos.
    public void mostrarPrestamosActivos() {

        System.out.println("\n--- PRESTAMOS ACTIVOS ---");

        List<Prestamos> activos = prestamosController.listarPrestamos()
                .stream()
                .filter(p -> calcularSaldo(p) > 0.1)
                .toList();

        if (activos.isEmpty()) {
            System.out.println("No hay prestamos activos.");
            return;
        }

        activos.forEach(p -> {

            System.out.println(
                    "Prestamo: " + p.getIdPrestamo()
            );

            System.out.println(
                    "Cliente: " + p.getCliente().getNombre()
            );

            System.out.println(
                    "Saldo pendiente: $" + calcularSaldo(p)
            );

            System.out.println("-------------------------------");
        });

        System.out.println(
                "Total de prestamos activos: " + activos.size()
        );
    }

    // Mostrar préstamos pagados.
    public void mostrarPrestamosPagados() {

        System.out.println("\n--- PRESTAMOS CANCELADOS ---");

        List<Prestamos> pagados = prestamosController.listarPrestamos()
                .stream()
                .filter(p -> calcularSaldo(p) <= 0.1)
                .toList();

        if (pagados.isEmpty()) {
            System.out.println("No hay prestamos cancelados.");
            return;
        }

        pagados.forEach(p -> {

            System.out.println(
                    "Prestamo: " + p.getIdPrestamo()
            );

            System.out.println(
                    "Cliente: " + p.getCliente().getNombre()
            );

            System.out.println("Estado: Cancelado");

            System.out.println("-------------------------------");
        });

        System.out.println(
                "Total de prestamos cancelados: " + pagados.size()
        );
    }

    // Mostrar clientes que tienen préstamos con cuotas vencidas.
    public void mostrarClientesConDeuda() {

        System.out.println("\n--- CLIENTES MOROSOS ---");

        List<Prestamos> deudores = prestamosController.listarPrestamos()
                .stream()
                .filter(this::tieneCuotasVencidas)
                .toList();

        if (deudores.isEmpty()) {
            System.out.println("No hay clientes morosos.");
            return;
        }

        deudores.forEach(p -> {

            System.out.println(
                    "Cliente: " + p.getCliente().getNombre()
            );

            System.out.println(
                    "Documento: " + p.getCliente().getDocumento()
            );

            System.out.println(
                    "Prestamo: " + p.getIdPrestamo()
            );

            System.out.println(
                    "Deuda vencida: $" + calcularDeudaVencida(p)
            );

            System.out.println("------------------------------");
        });

        long totalClientes = deudores.stream()
                .map(p -> p.getCliente().getId())
                .distinct()
                .count();

        System.out.println(
                "Total de clientes morosos: " + totalClientes
        );

        System.out.println(
                "Total de prestamos en mora: " + deudores.size()
        );
    }

    // Mostrar préstamos con cuotas vencidas.
    public void mostrarPrestamosVencidos() {

        System.out.println("\n--- PRESTAMOS VENCIDOS ---");

        List<Prestamos> vencidos = prestamosController.listarPrestamos()
                .stream()
                .filter(this::tieneCuotasVencidas)
                .toList();

        if (vencidos.isEmpty()) {
            System.out.println("No hay prestamos en mora.");
            return;
        }

        vencidos.forEach(p -> {

            long cuotasVencidas = p.getListaCuotas()
                    .stream()
                    .filter(c -> c.getSaldo() > 0.1
                            && c.calcularDiasMora() > 0)
                    .count();

            System.out.println(
                    "Prestamo: " + p.getIdPrestamo()
            );

            System.out.println(
                    "Cliente: " + p.getCliente().getNombre()
            );

            System.out.println(
                    "Cuotas en mora: " + cuotasVencidas
            );

            System.out.println(
                    "Valor en mora: $" + calcularDeudaVencida(p)
            );

            System.out.println("------------------------------");
        });

        System.out.println(
                "Total de prestamos en mora: " + vencidos.size()
        );
    }

    // Mostrar resumen general de la cartera.
    public void mostrarResumenGeneral() {

        List<Prestamos> lista = prestamosController.listarPrestamos();

        long activos = lista.stream()
                .filter(p -> calcularSaldo(p) > 0.1)
                .count();

        long pagados = lista.stream()
                .filter(p -> calcularSaldo(p) <= 0.1)
                .count();

        long vencidos = lista.stream()
                .filter(this::tieneCuotasVencidas)
                .count();

        double deudaTotal = lista.stream()
                .mapToDouble(this::calcularSaldo)
                .sum();

        double deudaEnMora = lista.stream()
                .mapToDouble(this::calcularDeudaVencida)
                .sum();

        System.out.println("\n--- RESUMEN GENERAL ---");

        System.out.println(
                "Total de prestamos: " + lista.size()
        );

        System.out.println(
                "Prestamos activos: " + activos
        );

        System.out.println(
                "Prestamos cancelados: " + pagados
        );

        System.out.println(
                "Prestamos en mora: " + vencidos
        );

        System.out.println(
                "Deuda total pendiente: $" + deudaTotal
        );

        System.out.println(
                "Deuda total vencida: $" + deudaEnMora
        );
    }
}
