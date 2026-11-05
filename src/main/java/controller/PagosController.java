package controller;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import model.Pagos;
import model.Prestamos;

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
        
        if (monto>saldo) {
            System.out.println("El pago supera el saldo pendiente");
            System.out.println("Saldo actual: $" + saldo);
            return;
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
        
        System.out.println("Saldo pendiente: $" + nuevoSaldo);
        
        if (nuevoSaldo == 0) {
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
                return p.calcularTotal()
                        - calcularTotalPagado(idPrestamo);
            }
        }
        
        return 0;
    }
    
    public List<Pagos> listarPagos() {
        return new ArrayList<>(pagos);
    }
}
