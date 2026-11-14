package controller;

import exception.PrestamoNoEncontradoException;
import java.util.Comparator;
import java.util.List;
import model.Pagos;

public class GestorPagos {
    
    private PrestamosController prestamosController;
    private PagosController pagosController;
    
    public GestorPagos(
            PrestamosController prestamosController,
            PagosController pagosController) {
        
        this.prestamosController = prestamosController;
        this.pagosController = pagosController;
    }
    
    public void registrarPago(int, idPrestamo, double monto)
            throws PrestamoNoEncontradoException {
        
        boolean existe = prestamosController.listarPrestamos()
                .stream()
                .anymatch(p -> p.getIdPrestamo() == idPrestamo);
        
        if (!existe) {
            throw new PrestamoNoEncontradoException(
                    "No existe el prestamo con ID: " + idPrestamo
            );
        }
    
        pagosController.registrarPago(idPrestamo, monto);  
    }
    
    public void mostrarHistorial() {
        
        List<Pagos> historial = pagosController.listarPagos();
        
        System.out.println("----- Historial de Pagos -----");
        
        if (historial.isEmpty()) {
            System.out.println("No hay pagos registrados");
            return;
        }
        
        historial.stream()
                .sorted(Comparator.comparing(Pagos::getFecha))
                .forEach(Pagos::mostrarInformacion);
    }
    
    public void mostrarPagosUltimoMes() {
        
        System.out.println("----- Pagos del ultimo mes -----");
        
        List<Pagos> pagosMes = pagosController.listarPagos();
        
        pagosMes.stream()
                .filter(p -> !p.getFecha().isBefore(
                        java.time.LocalDate.now()minusMonths(1)))
                .filter(p -> !p.getFecha().isAfter(
                        java.time.LocalDate.now()))
                .sorted(Comparator.comparing(Pagos::getFecha))
                .forEach(Pagos::mostrarInformacion);
                
        if (pagosMes.stream().noneMatch(p ->
                !p.getFecha().isBefore(
                        java.time.LocalDate.now().minusMonths(1))
                && !p.getFecha().isAfter(
                        java.time.LocalDate.now()))) {
            
            System.out.println("No hay pagos registrados durante el ultimo mes");
        }
    }
}