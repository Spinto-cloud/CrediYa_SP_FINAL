package controller;

import java.util.List;

import model.Prestamos;
import model.Cuotas;

public class ReportesController {
    
    private PrestamosController prestamosController;
    
    public ReportesController(PrestamosController prestamosController) {
        this.prestamosController = prestamosController;
    }
    
    private double calcularSaldo(Prestamos prestamo) {
        return prestamo.getListaCuotas()
                .stream()
                .mapToDouble(c -> c.getSaldo())
                .sum();
    }
    
    public void mostrarPrestamosActivos() {
        
        System.out.println("\n--- PRESTAMOS ACTIVOS ---");
        
        List<Prestamos> activos = prestamosController.listarPrestamos()
                .stream()
                .filter(p -> calcularSaldo(p) > 0.1)
                .toList();
        
        if (activos.isEmpty()) {
            System.out.println("No hay prestamos activos");
            return;
        }
        
        activos.forEach(p -> {
            System.out.println("Cliente: " + p.getCliente().getNombre());
            System.out.println("Saldo pendiente: $" + calcularSaldo(p));
            System.out.println("-------------------------------");
        });
        
        System.out.println("Total de prestamos vigentes: " + activos.size());
    }
    
    public void mostrarPrestamosPagados() {
        
        System.out.println("\n--- PRESTAMOS CANCELADOS ---");
        
        List<Prestamos> pagados = prestamosController.listarPrestamos()
                .stream()
                .filter(p -> calcularSaldo(p) <= 0.1)
                .toList();
        
        if (pagados.isEmpty()) {
            System.out.println("No hay prestamos cancelados");
            return;
        }
        
        pagados.forEach(p -> {
            System.out.println("Cliente: " + p.getCliente().getNombre());
            System.out.println("Estado: Cancelado");
            System.out.println("-------------------------------");
        });
        
        System.out.println("Total de prestamos cancelados: " + pagados.size());
    }
    
    public void mostrarClientesConDeuda() {
        
        System.out.println("\n--- CLIENTES CON DEUDA ---");
        
        List<Prestamos> deudores = prestamosController.listarPrestamos()
                .stream()
                .filter(p -> calcularSaldo(p) > 0.1)
                .toList();
        
        if (deudores.isEmpty()) {
            System.out.println("No hay clientes con deudas pendientes");
            return;
        }
        
        deudores.forEach(p -> {
            System.out.println("Cliente: " + p.getCliente().getNombre());
            System.out.println("Documento: "+ p.getCliente().getDocumento());
            System.out.println("Deuda: $" + calcularSaldo(p));
            System.out.println("------------------------------");
        });
    }
    
    public void mostrarPrestamosVencidos() {

        System.out.println("\n--- PRESTAMOS VENCIDOS ---");

        List<Prestamos> vencidos = prestamosController.listarPrestamos()
                .stream()
                .filter(p -> p.getListaCuotas()
                        .stream()
                        .anyMatch(c -> c.getSaldo() > 0.1
                        && c.calcularDiasMora() > 0))
                .toList();

        if (vencidos.isEmpty()) {
            System.out.println("No hay prestamos en mora");
            return;
        }

        vencidos.forEach(p -> {
            
            System.out.println("Cliente: " + p.getCliente().getNombre());
            
            double deudaVencida = p.getListaCuotas()
                    .stream()
                    .filter(c -> c.getSaldo() > 0.1
                    && c.calcularDiasMora() > 0)
                    .mapToDouble(c -> c.getSaldo())
                    .sum();
            
            long cuotasVencidas = p.getListaCuotas()
                    .stream()
                    .filter(c -> c.getSaldo() > 0.1
                    && c.calcularDiasMora() > 0)
                    .mapToDouble(c -> c.getSaldo())
                    .count();
            
            System.out.println("Cuotas en mora: " + cuotasVencidas);
            System.out.println("Valor en mora: $" + deudaVencida);
            System.out.println("------------------------------");             
        });
        
        System.out.println("Total de prestamos en mora: " + vencidos.size());
    }
    
    public void mostrarResumenGeneral() {
        
        List<Prestamos> lista = prestamosController.listarPrestamos();
        
        long activos = lista.stream()
                .filter(p -> calcularSaldo(p) > 0.1)
                .count();
        
        long pagados = lista.stream()
                .filter(p -> calcularSaldo(p) <= 0.1)
                .count();
        
        double deudaTotal = lista.stream()
                .mapToDouble(p -> calcularSaldo(p))
                .sum();
        
        System.out.println("\n--- RESUMEN GENERAL ---");
        System.out.println("Total de prestamos: " + lista.size());
        System.out.println("Prestamos activos: " + activos);
        System.out.println("Prestamos cancelados: " + pagados);
        System.out.println("Deuda total al corte: " + deudaTotal);         
    }
}
