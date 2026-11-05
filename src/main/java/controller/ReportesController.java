package controller;

import java.util.List;

import model.Prestamos;

public class ReportesController {
    
    private PrestamosController prestamosController;
    private PagosController pagosController;
    
    public ReportesController(
            PrestamosController prestamosController,
            PagosController pagosController) {
        
        this.prestamosController = prestamosController;
        this.pagosController = pagosController;
    }
    
    // Reporte de prestamos activos
    public void mostrarPrestamosActivos() {
        
        System.out.println("\n--- PRESTAMOS ACTIVOS ---");
        
        List<Prestamos> activos = prestamosController.listarPrestamos()
                .stream()
                .filter(p -> p.getEstado().equalsIgnoreCase("pendiente"))
                .toList();
        
        if (activos.isEmpty()) {
            System.out.println("No hay prestamos activos");
            return;
        }
        
        activos.forEach(p -> {
            System.out.println("Prestamo:" + p.getIdPrestamo());
            System.out.println("Cliente: " + p.getCliente().getNombre());
            System.out.println("Saldo pendiente: $"
                    + pagosController.calcularSaldo(p.getIdPrestamo()));
            System.out.println("-------------------------------");
        });
    }
}
