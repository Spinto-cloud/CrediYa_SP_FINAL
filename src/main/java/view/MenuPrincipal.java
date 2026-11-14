package view;

import java.util.Scanner;

import controller.EmpleadoController;
import controller.ClienteController;
import controller.PrestamosController;
import controller.PagosController;
import controller.ReportesController;
import controller.GestorPagos;

import factory.ControllerFactory;

import model.Empleado;
import model.Cliente;
import model.Prestamos;
import model.Pagos;

import security.Seguridad;

import exception.PrestamoNoEncontradoException;

public class MenuPrincipal {

    private final Scanner teclado = new Scanner(System.in);

    private final EmpleadoController empleadoController;
    private final ClienteController clienteController;
    private final PrestamosController prestamosController;
    private final PagosController pagosController;
    private final ReportesController reportesController;
    private final GestorPagos gestorPagos;

    // Constructor: utiliza Factory para crear los controladores
    public MenuPrincipal() {

        ControllerFactory factory = new ControllerFactory();

        empleadoController = factory.crearEmpleadoController();

        clienteController = factory.crearClienteController();

        prestamosController = factory.crearPrestamosController(
                clienteController,
                empleadoController
        );

        pagosController = factory.crearPagosController(
                prestamosController
        );
        
        gestorPagos = new GestorPagos(
                prestamosController,
                pagosController
        );

        reportesController = factory.crearReportesController(
                prestamosController
        );
    }

    public void mostrarMenu() {

        int opcion = -1;

        do {
            System.out.println("\n==============================");
            System.out.println("       CREDIYA SP S.A.S");
            System.out.println("  SISTEMA DE COBROS DE CARTERA");
            System.out.println("==============================");
            System.out.println("1. Registrar empleado");
            System.out.println("2. Listar empleados");
            System.out.println("3. Registrar cliente");
            System.out.println("4. Listar clientes");
            System.out.println("5. Registrar prestamo");
            System.out.println("6. Listar prestamos");
            System.out.println("7. Consultar prestamos en mora");
            System.out.println("8. Consultar cuotas de un prestamo");
            System.out.println("9. Consultar cuotas vencidas");
            System.out.println("10. Registrar pago");
            System.out.println("11. Listar pagos");
            System.out.println("12. Reporte de prestamos activos");
            System.out.println("13. Reporte de prestamos pagados");
            System.out.println("14. Reporte de clientes con deuda");
            System.out.println("15. Reporte de prestamos en mora");
            System.out.println("16. Resumen general");
            System.out.println("17. Actualizar cliente");
            System.out.println("18. Eliminar cliente");
            System.out.println("19. Actualizar empleado");
            System.out.println("20. Eliminar empleado");
            System.out.println("21. Consultar pagos del ultimo mes");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = Integer.parseInt(teclado.nextLine());

                switch (opcion) {

                    case 1:
                        if (Seguridad.validarAdministrador(teclado)) {
                            registrarEmpleado();
                        }
                        break;

                    case 2:
                        if (Seguridad.validarAdministrador(teclado)) {
                            listarEmpleados();
                        }
                        break;

                    case 3:
                        registrarCliente();
                        break;

                    case 4:
                        listarClientes();
                        break;

                    case 5:
                        registrarPrestamo();
                        break;

                    case 6:
                        listarPrestamos();
                        break;

                    case 7:
                        reportesController.mostrarPrestamosVencidos();
                        break;

                    case 8:
                        System.out.print("Ingrese el ID del prestamo: ");
                        int idPrestamo = Integer.parseInt(teclado.nextLine());

                        pagosController.mostrarCuotas(idPrestamo);
                        break;

                    case 9:
                        pagosController.mostrarCuotasVencidas();
                        break;

                    case 10:
                        registrarPago();
                        break;

                    case 11:
                        listarPagos();
                        break;

                    case 12:
                        reportesController.mostrarPrestamosActivos();
                        break;

                    case 13:
                        reportesController.mostrarPrestamosPagados();
                        break;

                    case 14:
                        reportesController.mostrarClientesConDeuda();
                        break;

                    case 15:
                        reportesController.mostrarPrestamosVencidos();
                        break;

                    case 16:
                        reportesController.mostrarResumenGeneral();
                        break;
                        
                    case 17:
                        actualizarCliente();
                        break;
                        
                    case 18:
                        if (Seguridad.validarAdministrador(teclado)) {
                            eliminarCliente();
                        }
                        break;
                        
                    case 19:
                        if (Seguridad.validarAdministrador(teclado)) {
                            actualizarEmpleado();
                        }
                        break;
                        
                    case 20:
                        if (Seguridad.validarAdministrador(teclado)) {
                            eliminarEmpleado();
                        }
                        break;
                        
                    case 21:
                        gestorPagos.mostrarPagosUltimoMes();
                        }
                        break;

                    case 0:
                        System.out.println("Gracias por utilizar CrediYa.");
                        break;

                    default:
                        System.out.println("Opcion invalida.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero.");
            }

        } while (opcion != 0);
    }

    private void registrarEmpleado() {

        System.out.println("\n--- REGISTRAR EMPLEADO ---");

        System.out.print("Nombre: ");
        String nombre = teclado.nextLine();

        System.out.print("Documento: ");
        String documento = teclado.nextLine();

        System.out.print("Correo: ");
        String correo = teclado.nextLine();

        System.out.print("Rol: ");
        String rol = teclado.nextLine();

        try {
            System.out.print("Salario: ");
            double salario = Double.parseDouble(teclado.nextLine());

            empleadoController.registrarEmpleado(
                    nombre, documento, correo, rol, salario
            );

        } catch (NumberFormatException e) {
            System.out.println("El salario debe ser un numero.");
        }
    }

    private void listarEmpleados() {

        System.out.println("\n--- LISTA DE EMPLEADOS ---");

        if (empleadoController.listarEmpleados().isEmpty()) {
            System.out.println("No hay empleados registrados.");
        } else {
            for (Empleado empleado : empleadoController.listarEmpleados()) {
                empleado.mostrarInformacion();
                System.out.println("------------------------------");
            }
        }
    }
    
    private void actualizarEmpleado() {

        System.out.println("\n--- ACTUALIZAR EMPLEADO ---");

        // Mostrar empleados registrados
        listarEmpleados();

        try {
            System.out.print("Ingrese el ID del empleado: ");
            int id = Integer.parseInt(teclado.nextLine());

            // Verificar que el empleado exista
            Empleado empleadoExistente = empleadoController.listarEmpleados()
                    .stream()
                    .filter(e -> e.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (empleadoExistente == null) {
                System.out.println("No existe un empleado con ese ID.");
                return;
            }

            System.out.print("Nuevo nombre: ");
            String nombre = teclado.nextLine();

            System.out.print("Nuevo documento: ");
            String documento = teclado.nextLine();

            System.out.print("Nuevo correo: ");
            String correo = teclado.nextLine();

            System.out.print("Nuevo rol: ");
            String rol = teclado.nextLine();

            System.out.print("Nuevo salario: ");
            double salario = Double.parseDouble(teclado.nextLine());

            empleadoController.actualizarEmpleado(
                    id, nombre, documento, correo, rol, salario
            );

        } catch (NumberFormatException e) {
            System.out.println("El ID y el salario deben ser valores numericos.");
        }
    }
    
        private void eliminarEmpleado() {

        System.out.println("\n--- ELIMINAR EMPLEADO ---");

        // Mostrar empleados registrados
        listarEmpleados();

        try {
            System.out.print("Ingrese el ID del empleado que desea eliminar: ");
            int id = Integer.parseInt(teclado.nextLine());

            // Verificar que el empleado exista
            Empleado empleadoExistente = empleadoController.listarEmpleados()
                    .stream()
                    .filter(e -> e.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (empleadoExistente == null) {
                System.out.println("No existe un empleado con ese ID.");
                return;
            }

            System.out.print("¿Confirma que desea eliminar este empleado? (S/N): ");
            String confirmacion = teclado.nextLine();

            if (confirmacion.equalsIgnoreCase("S")) {
                empleadoController.eliminarEmpleado(id);
            } else {
                System.out.println("Operacion cancelada.");
            }

        } catch (NumberFormatException e) {
            System.out.println("El ID debe ser un numero.");
        }
    }

    private void registrarCliente() {

        System.out.println("\n--- REGISTRAR CLIENTE ---");

        System.out.print("Nombre: ");
        String nombre = teclado.nextLine();

        System.out.print("Documento: ");
        String documento = teclado.nextLine();

        System.out.print("Correo: ");
        String correo = teclado.nextLine();

        System.out.print("Telefono: ");
        String telefono = teclado.nextLine();

        clienteController.registrarCliente(
                nombre, documento, correo, telefono
        );
    }

    private void listarClientes() {

        System.out.println("\n--- LISTA DE CLIENTES ---");

        if (clienteController.listarClientes().isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            for (Cliente cliente : clienteController.listarClientes()) {
                cliente.mostrarInformacion();
                System.out.println("------------------------------");
            }
        }
    }
    
    private void actualizarCliente() {

        System.out.println("\n--- ACTUALIZAR CLIENTE ---");

        // Mostrar clientes registrados
        listarClientes();

        try {
            System.out.print("Ingrese el ID del cliente: ");
            int id = Integer.parseInt(teclado.nextLine());

            // Verificar que el cliente exista
            Cliente clienteExistente = clienteController.listarClientes()
                    .stream()
                    .filter(c -> c.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (clienteExistente == null) {
                System.out.println("No existe un cliente con ese ID.");
                return;
            }

            System.out.print("Nuevo nombre: ");
            String nombre = teclado.nextLine();

            System.out.print("Nuevo documento: ");
            String documento = teclado.nextLine();

            System.out.print("Nuevo correo: ");
            String correo = teclado.nextLine();

            System.out.print("Nuevo telefono: ");
            String telefono = teclado.nextLine();

            clienteController.actualizarCliente(
                    id, nombre, documento, correo, telefono
            );

        } catch (NumberFormatException e) {
            System.out.println("El ID debe ser un numero.");
        }
    }
    
    private void eliminarCliente() {

        System.out.println("\n--- ELIMINAR CLIENTE ---");

        listarClientes();

        try {
            System.out.print("Ingrese el ID del cliente que desea eliminar: ");
            int id = Integer.parseInt(teclado.nextLine());

            Cliente clienteExistente = clienteController.listarClientes()
                    .stream()
                    .filter(c -> c.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (clienteExistente == null) {
                System.out.println("No existe un cliente con ese ID.");
                return;
            }

            System.out.print("¿Confirma que desea eliminar este cliente? (S/N): ");
            String confirmacion = teclado.nextLine();

            if (confirmacion.equalsIgnoreCase("S")) {
                clienteController.eliminarCliente(id);
            } else {
                System.out.println("Operacion cancelada.");
            }

        } catch (NumberFormatException e) {
            System.out.println("El ID debe ser un numero.");
        }
    }

    private void registrarPrestamo() {

        System.out.println("\n--- REGISTRAR PRESTAMO ---");

        try {
            System.out.println("ID del cliente: ");
            int idCliente = Integer.parseInt(teclado.nextLine());

            System.out.println("ID del empleado: ");
            int idEmpleado = Integer.parseInt(teclado.nextLine());

            System.out.println("Monto del prestamo: ");
            double monto = Double.parseDouble(teclado.nextLine());

            System.out.println("Interes (%): ");
            double interes = Double.parseDouble(teclado.nextLine());

            System.out.println("Numero de cuotas: ");
            int cuotas = Integer.parseInt(teclado.nextLine());

            prestamosController.registrarPrestamo(
                    idCliente, idEmpleado, monto, interes, cuotas
            );

        } catch (NumberFormatException e) {
            System.out.println(
                    "Error: Por favor ingrese valores numericos validos"
            );
        }
    }

    private void listarPrestamos() {

        System.out.println("\n--- LISTA DE PRESTAMOS ---");

        if (prestamosController.listarPrestamos().isEmpty()) {
            System.out.println("No hay prestamos registrados.");
            return;
        }

        for (Prestamos p : prestamosController.listarPrestamos()) {
            p.mostrarInformacion();
            System.out.println("----------------------------");
        }
    }

    private void registrarPago() {

        System.out.println("\n--- REGISTRAR PAGO ---");

        try {
            System.out.println("ID del prestamo: ");
            int idPrestamo = Integer.parseInt(teclado.nextLine());

            System.out.println("Monto del pago: ");
            double monto = Double.parseDouble(teclado.nextLine());

            gestorPagos.registrarPago(idPrestamo, monto);

        } catch (NumberFormatException e) {
            System.out.println(
                    "Error: Por favor ingrese valores numericos validos");
        } catch (PrestamoNoEncontradoException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listarPagos() {

        gestorPagos.mostrarHistorial();

        System.out.println("\n--- HISTORIAL DE PAGOS ---");

        if (pagosController.listarPagos().isEmpty()) {
            System.out.println("No hay pagos registrados.");
            return;
        }

        for (Pagos p : pagosController.listarPagos()) {
            p.mostrarInformacion();
            System.out.println("----------------------------");
        }
    }
}
