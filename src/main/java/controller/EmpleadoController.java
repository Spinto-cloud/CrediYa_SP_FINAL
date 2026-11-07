package controller;

import java.util.ArrayList;
import java.util.List;

import model.Empleado;

import repository.ArchivoEmpleado;


public class EmpleadoController {

    private ArrayList<Empleado> empleados = new ArrayList<>();
    
    private ArchivoEmpleado archivoEmpleado = new ArchivoEmpleado();
    
      public EmpleadoController() {
        empleados = archivoEmpleado.cargar();

        System.out.println("Empleados cargados: " + empleados.size());
    }

    public void registrarEmpleado(String nombre, String documento,
            String correo, String rol, double salario) {

        int id;

        if (empleados.isEmpty()) {
            id = 1;
        } else {
            id = empleados.stream()
                    .mapToInt(e -> e.getId())
                    .max()
                    .getAsInt() + 1;
        }

        Empleado empleado = new Empleado(
                id, nombre, documento, correo, rol, salario
        );

        empleados.add(empleado);
        
        archivoEmpleado.guardar(empleados);

        System.out.println("\nEmpleado registrado correctamente.");
        System.out.println("Total de empleados: " + empleados.size());
    }

    public List<Empleado> listarEmpleados() {
        return new ArrayList<>(empleados);
    }
}
