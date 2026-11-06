package controller;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import model.Prestamos;
import model.Cliente;
import model.Empleado;

public class PrestamosController {
    
    private ArrayList<Prestamos> prestamos = new ArrayList();
    
    private ClienteController clienteController;
    private EmpleadoController empleadoController;
    
    public PrestamosController(ClienteController clienteController,
            EmpleadoController empleadoController) {
        
        this.clienteController = clienteController;
        this.empleadoController = empleadoController;
    }
    
    public void registrarPrestamo(int idCliente, int idEmpleado,
            double monto, double interes, int cuotas) {
        
        Cliente cliente = null;
        Empleado empleado = null;
        
        for (Cliente c : clienteController.listarClientes()) {
            if (c.getId() == idCliente) {
                cliente = c;
                break;
            }
        }
        
        for (Empleado e : empleadoController.listarEmpleados()) {
            if (e.getId() == idEmpleado) {
                empleado = e;
                break;
            }
        }
       
        if (cliente == null || empleado == null) {
            System.out.println("Cliente o empleado no encontrado");
            return;
        }
        
        if (monto <= 0 || interes < 0 || cuotas <= 0) {
            System.out.println("Los datos del prestamo son invalidos");
            return;
        }
        
        int id = prestamos.size() + 1;
        
        Prestamos nuevoPrestamo = new Prestamos(
                id, 
                cliente, 
                empleado, 
                monto,
                interes, 
                cuotas, 
                LocalDate.now()
        );
        
        prestamos.add(nuevoPrestamo);
        
        System.out.println("Prestamo registrado correctamente");
        System.out.println("ID del prestamo: " + id);
    }
    
    public List<Prestamos> listarPrestamos() {
        return new ArrayList<>(prestamos);
    }
    
    public void mostrarPrestamosEnMora(PagosController pagosController) {
        System.out.println("\n--- PRESTAMOS EN MORA ---");
        pagosController.mostrarCuotasVencidas();     
    }
}
