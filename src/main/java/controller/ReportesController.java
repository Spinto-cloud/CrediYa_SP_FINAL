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
    
    public void mostrarPrestamosActivos() {
        
        System.out.println("\n--- PRESTAMOS ACTIVOS ---");
        
        List<Prestamos> activos = prestamosController.listarPrestamos()
                .stream()
                .filter(p -> "pendiente".equalsIgnoreCase("pendiente"))
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
    
    public void mostrarPrestamosPagados() {
        
        System.out.println("\n--- PRESTAMOS CANCELADOS ---");
        
        List<Prestamos> pagados = prestamosController.listarPrestamos()
                .stream()
                .filter(p -> pagosController.calcularSaldo(
                        p.getIdPrestamo()) <= 0.1)
                .toList();
        
        if (pagados.isEmpty()) {
            System.out.println("No hay prestamos cancelados");
            return;
        }
        
        pagados.forEach(p -> {
            System.out.println("Prestamo: " + p.getIdPrestamo());
            System.out.println("Cliente: " + p.getCliente().getNombre());
            System.out.println("Estado: Cancelado");
            System.out.println("-------------------------------");
        });
    }
    
    public void mostrarClientesConDeuda() {
        
        System.out.println("\n--- CLIENTES CON DEUDA ---");
        
        List<Prestamos> deudores = prestamosController.listarPrestamos()
                .stream()
                .filter(p -> pagosController.calcularSaldo(
                        p.getIdPrestamo()) > 0.1)
                .toList();
        
        if (deudores.isEmpty()) {
            System.out.println("No hay clientes con deuda");
            return;
        }
        
        deudores.forEach(p -> {
            System.out.println("Cliente: " + p.getCliente().getNombre());
            System.out.println("Documento: "
                    + p.getCliente().getDocumento());
            System.out.println("Prestamo: " + p.getIdPrestamo());
            System.out.println("Deuda: $" 
                    + pagosController.calcularSaldo(p.getIdPrestamo()));
            System.out.println("------------------------------");
        });
    }
    
    public void mostrarPrestamosVencidos() {

        System.out.println("\n--- PRESTAMOS VENCIDOS ---");

        List<Prestamos> vencidos = prestamosController.listarPrestamos()
                .stream()
                .filter(p -> pagosController.calcularSaldo(
                        p.getIdPrestamo()) > 0.1)
                .filter(p -> p.calcularDiasMora() > 0)
                .toList();

        if (vencidos.isEmpty()) {
            System.out.println("No hay prestamos vencidos");
            return;
        }

        vencidos.forEach(p -> {
            System.out.println("Prestamo: " + p.getIdPrestamo());
            System.out.println("Cliente: " + p.getCliente().getNombre());
            System.out.println("Fecha de vencimiento: "
                    + p.calcularFechaVencimiento());
            System.out.println("Dias de mora: "
                    + p.calcularDiasMora());
            System.out.println("Saldo pendiente: $"
                    + pagosController.calcularSaldo(p.getIdPrestamo()));
            System.out.println("-------------------------------");
        });
    }
}
