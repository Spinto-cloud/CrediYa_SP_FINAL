package controller;

import java.util.ArrayList;
import java.util.List;

import model.Empleado;

import repository.EmpleadoDAO;
import repository.EmpleadoRepository;
import repository.ArchivoEmpleado;

public class EmpleadoController {

    private ArrayList<Empleado> empleados = new ArrayList<>();

    // Dependemos de la interfaz, no de la implementación
    private final EmpleadoDAO empleadoRepository;

    private final ArchivoEmpleado archivoEmpleado =
            new ArchivoEmpleado();

    public EmpleadoController() {
        this(new EmpleadoRepository());
    }

    // Constructor que permite inyectar otra implementación
    public EmpleadoController(EmpleadoDAO empleadoRepository) {

        this.empleadoRepository = empleadoRepository;

        // Cargar empleados desde MySQL
        empleados = new ArrayList<>(
                empleadoRepository.listar()
        );

        // Sincronizar archivo TXT con MySQL
        archivoEmpleado.guardar(empleados);

        System.out.println(
                "Empleados cargados desde MySQL: "
                + empleados.size()
        );
    }

    public void registrarEmpleado(
            String nombre,
            String documento,
            String correo,
            String rol,
            double salario) {

        if (nombre == null || nombre.trim().isEmpty()
                || documento == null || documento.trim().isEmpty()
                || correo == null || correo.trim().isEmpty()
                || rol == null || rol.trim().isEmpty()
                || salario <= 0) {

            System.out.println(
                    "Los datos del empleado son inválidos."
            );
            return;
        }

        // Validar documento duplicado
        boolean documentoExiste = empleados.stream()
                .anyMatch(e ->
                        e.getDocumento().equals(documento));

        if (documentoExiste) {
            System.out.println(
                    "Ya existe un empleado con ese documento."
            );
            return;
        }

        // Generar identificador
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
                id,
                nombre,
                documento,
                correo,
                rol,
                salario
        );

        // Guardar primero en MySQL
        boolean guardado = empleadoRepository.guardar(empleado);

        if (guardado) {

            empleados.add(empleado);

            // Actualizar archivo TXT
            archivoEmpleado.guardar(empleados);

            System.out.println(
                    "Empleado registrado correctamente."
            );

            System.out.println("ID del empleado: " + id);

        } else {
            System.out.println(
                    "No fue posible registrar el empleado en MySQL."
            );
        }
    }

    public List<Empleado> listarEmpleados() {
        return new ArrayList<>(empleados);
    }
    
    public void actualizarEmpleado(
            int id,
            String nombre,
            String documento,
            String correo,
            String rol,
            double salario) {

        if (nombre == null || nombre.trim().isEmpty()
                || documento == null || documento.trim().isEmpty()
                || correo == null || correo.trim().isEmpty()
                || rol == null || rol.trim().isEmpty()
                || salario <= 0) {

            System.out.println("Los datos del empleado son inválidos.");
            return;
        }

        // Verificar que el empleado exista
        Empleado empleadoExistente = empleados.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);

        if (empleadoExistente == null) {
            System.out.println("No existe un empleado con ese ID.");
            return;
        }

        // Validar que el documento no pertenezca a otro empleado
        boolean documentoDuplicado = empleados.stream()
                .anyMatch(e -> e.getDocumento().equals(documento)
                        && e.getId() != id);

        if (documentoDuplicado) {
            System.out.println("Otro empleado ya tiene ese documento.");
            return;
        }

        Empleado empleadoActualizado = new Empleado(
                id,
                nombre,
                documento,
                correo,
                rol,
                salario
        );

        // Actualizar primero en MySQL
        boolean actualizado = empleadoRepository.actualizar(
                empleadoActualizado
        );

        if (actualizado) {

            int posicion = empleados.indexOf(empleadoExistente);

            empleados.set(posicion, empleadoActualizado);

            // Sincronizar archivo TXT
            archivoEmpleado.guardar(empleados);

            System.out.println("Empleado actualizado correctamente.");

        } else {
            System.out.println("No fue posible actualizar el empleado.");
        }
    }
    
        public void eliminarEmpleado(int id) {

        // Verificar que el empleado exista
        Empleado empleadoExistente = empleados.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);

        if (empleadoExistente == null) {
            System.out.println("No existe un empleado con ese ID.");
            return;
        }

        // Eliminar primero de MySQL
        boolean eliminado = empleadoRepository.eliminar(id);

        if (eliminado) {

            empleados.remove(empleadoExistente);

            // Actualizar archivo TXT
            archivoEmpleado.guardar(empleados);

            System.out.println("Empleado eliminado correctamente.");

        } else {
            System.out.println("No fue posible eliminar el empleado.");
        }
    }
}
