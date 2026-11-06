package controller;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import model.Pagos;
import model.Prestamos;
import model.Cuotas;

public class PagosController {
    
    private ArrayList<Pagos> pagos = new ArrayList();
    
    private PrestamosController prestamosController;
    
    public PagosController(PrestamosController prestamosController) {
        this.prestamosController = prestamosController;
    }
    
    public void registrarPago(int idPrestamo, double monto) {
        
        Prestamos prestamoEncontrado = null;
        
        for (Prestamos p : prestamosController.listarPrestamos()) {
            
            if (p.getIdPrestamo() ==idPrestamo) {
                prestamoEncontrado = p;
                break;
            }
        }
        
        if (prestamoEncontrado == null) {
            System.out.println("Prestamo no encontrado");
            return;
        }
        
        if (monto <=0) {
            System.out.println("El monto debe ser mayor a 0");
            return;
        }
        
        double saldo = calcularSaldo(idPrestamo);
        
        if (saldo <= 0.1) {
            System.out.println("El prestamo ya esta cancelado en su totalidad");
            return;
        }
        
        if (monto > saldo + 0.1) {
            System.out.println("El pago supera el saldo pendiente");
            System.out.println("Saldo actual: $" + saldo);
            return;
        }
        
        double restante = monto;
        
        //Se aplica el pago a las cuotas vencidas mas antiguas
        for (Cuotas cuota : prestamoEncontrado.getListaCuotas()) {

            if (restante <= 0.1) {
                break;
            }

            double saldoCuota = cuota.getSaldo();

            if (saldoCuota > 0.1) {

                double abono = Math.min(restante, saldoCuota);

                cuota.registrarAbono(abono);

                restante -= abono;
            }
        }
        
        int id = pagos.size() +1;
        
        Pagos nuevoPago = new Pagos(
                id,
                prestamoEncontrado,
                monto,
                LocalDate.now()
        );
        
        pagos.add(nuevoPago);
        
        System.out.println("Pago registrado correctamente");
        
        double nuevoSaldo = calcularSaldo(idPrestamo);
        
        System.out.println("Saldo pendiente: $" + Math.round (nuevoSaldo));
        
        if (nuevoSaldo <= 0.1) {
            prestamoEncontrado.setEstado("Pagado");
            System.out.println("Prestamo cancelado en su totalidad");
        }
    }   
    
    public double calcularTotalPagado(int idPrestamo) {
        
        double total = 0;
        
        for (Pagos p : pagos) {
            if (p.getPrestamo().getIdPrestamo() == idPrestamo) {
                total += p.getMonto();
            }
        }
        return total;
    }
    
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
    
    public void mostrarCuotas(int idPrestamo) {

        for (Prestamos p : prestamosController.listarPrestamos()) {

            if (p.getIdPrestamo() == idPrestamo) {

                System.out.println("\n----- CUOTAS DEL PRESTAMO -----");
                System.out.println("Cliente: " + p.getCliente().getNombre());

                for (Cuotas cuota : p.getListaCuotas()) {

                    System.out.println("\nCuota: " + cuota.getNumero());
                    System.out.println("Valor: $" + cuota.getValor());
                    System.out.println("Monto Pagado: $" + cuota.getMontoPagado());
                    System.out.println("Interes: $" + cuota.getInteres());
                    System.out.println("Abono a capital: $" + cuota.getCapital());
                    System.out.println("Saldo adeudado: $" 
                            + p.calcularSaldoCapital(cuota.getNumero()));
                    System.out.println("Vencimiento: " + cuota.getFechaVencimiento());
                    System.out.println("Estado: " + cuota.getEstado());
                    System.out.println("Dias de mora: " + cuota.calcularDiasMora());
                }

                return;
            }
        }

        System.out.println("Prestamo no encontrado");
    }
    
    
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
                    System.out.println("Vencimiento: " + cuota.getFechaVencimiento());
                    System.out.println("Dias de mora: " + cuota.calcularDiasMora());
                    System.out.println("Saldo de la cuota: $" + cuota.getSaldo());
                    System.out.println("------------------------------");
                }
            }
        }

        if (!encontrado) {
            System.out.println("No hay cuotas vencidas");
        }
    }
    
    public List<Pagos> listarPagos() {
        return new ArrayList<>(pagos);
    }
}
