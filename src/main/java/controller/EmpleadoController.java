package controller;

import java.util.ArrayList;
import java.util.List;
import model.Empleado;

public class EmpleadoController {

    private ArrayList<Empleado> empleados = new ArrayList<>();

    public void registrarEmpleado(String nombre, String documento,
            String correo, String rol, double salario) {

        int id = empleados.size() + 1;

        Empleado empleado = new Empleado(
                id, nombre, documento, correo, rol, salario
        );

        empleados.add(empleado);

        System.out.println("\nEmpleado registrado correctamente.");
        System.out.println("Total de empleados: " + empleados.size());
    }

    public List<Empleado> listarEmpleados() {
        return new ArrayList<>(empleados);
    }
}
