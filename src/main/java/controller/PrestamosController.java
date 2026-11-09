package controller;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import model.Prestamos;
import model.Cliente;
import model.Empleado;
import model.Cuotas;

import repository.PrestamosDAO;
import repository.PrestamosRepository;
import repository.CuotasDAO;
import repository.CuotasRepository;
import repository.ArchivoPrestamos;

public class PrestamosController {

    private ArrayList<Prestamos> prestamos = new ArrayList<>();

    private final ClienteController clienteController;
    private final EmpleadoController empleadoController;

    // Dependencia mediante interfaz DAO
    private final PrestamosDAO prestamoRepository;

    // Dependencia mediante interfaz DAO
    private final CuotasDAO cuotasRepository;

    // Manejo de archivos TXT
    private final ArchivoPrestamos archivoPrestamos =
            new ArchivoPrestamos();

    // Constructor utilizado por MenuPrincipal
    public PrestamosController(
            ClienteController clienteController,
            EmpleadoController empleadoController) {

        this(
                clienteController,
                empleadoController,
                new PrestamosRepository(),
                new CuotasRepository()
        );
    }

    // Constructor con inyección de dependencias
    public PrestamosController(
            ClienteController clienteController,
            EmpleadoController empleadoController,
            PrestamosDAO prestamoRepository,
            CuotasDAO cuotasRepository) {

        this.clienteController = clienteController;
        this.empleadoController = empleadoController;
        this.prestamoRepository = prestamoRepository;
        this.cuotasRepository = cuotasRepository;

        // Cargar préstamos desde MySQL
        prestamos = new ArrayList<>(
                prestamoRepository.listar(
                        clienteController.listarClientes(),
                        empleadoController.listarEmpleados()
                )
        );

        // Cargar cuotas guardadas de cada préstamo
        for (Prestamos prestamo : prestamos) {

            List<Cuotas> cuotasGuardadas =
                    cuotasRepository.listar(
                            prestamo.getIdPrestamo()
                    );

            if (!cuotasGuardadas.isEmpty()) {
                prestamo.cargarCuotas(cuotasGuardadas);
            }
        }

        System.out.println(
                "Prestamos cargados desde MySQL: "
                + prestamos.size()
        );

        // Sincronizar archivos al cargar los datos
        sincronizarArchivo();
    }

    // Registrar un préstamo
    public void registrarPrestamo(
            int idCliente,
            int idEmpleado,
            double monto,
            double interes,
            int cuotas) {

        Cliente cliente = null;
        Empleado empleado = null;

        // Buscar cliente
        for (Cliente c : clienteController.listarClientes()) {

            if (c.getId() == idCliente) {
                cliente = c;
                break;
            }
        }

        // Buscar empleado
        for (Empleado e : empleadoController.listarEmpleados()) {

            if (e.getId() == idEmpleado) {
                empleado = e;
                break;
            }
        }

        if (cliente == null || empleado == null) {
            System.out.println(
                    "Cliente o empleado no encontrado."
            );
            return;
        }

        // Validar datos
        if (!Double.isFinite(monto)
                || !Double.isFinite(interes)
                || monto <= 0
                || interes < 0
                || cuotas <= 0) {

            System.out.println(
                    "Los datos del prestamo son invalidos."
            );
            return;
        }

        // Generar identificador
        int id = prestamos.isEmpty()
                ? 1
                : prestamos.stream()
                        .mapToInt(p -> p.getIdPrestamo())
                        .max()
                        .getAsInt() + 1;

        // Crear préstamo y generar sus cuotas
        Prestamos nuevoPrestamo = new Prestamos(
                id,
                cliente,
                empleado,
                monto,
                interes,
                cuotas,
                LocalDate.now()
        );

        // Guardar préstamo en MySQL
        boolean guardado =
                prestamoRepository.guardar(nuevoPrestamo);

        if (!guardado) {
            System.out.println(
                    "No fue posible registrar el prestamo."
            );
            return;
        }

        // Guardar cuotas en MySQL
        boolean cuotasGuardadas =
                cuotasRepository.guardarTodas(
                        id,
                        nuevoPrestamo.getListaCuotas()
                );

        if (!cuotasGuardadas) {
            System.out.println(
                    "No fue posible guardar las cuotas."
            );
            System.out.println(
                    "El prestamo pudo quedar registrado sin cuotas."
            );
            return;
        }

        // Agregar préstamo a la lista
        prestamos.add(nuevoPrestamo);

        // Sincronizar archivos TXT
        sincronizarArchivo();

        System.out.println(
                "Prestamo registrado correctamente."
        );

        System.out.println(
                "ID del prestamo: " + id
        );
    }

    // Listar préstamos
    public List<Prestamos> listarPrestamos() {
        return new ArrayList<>(prestamos);
    }

    // Mostrar préstamos en mora
    public void mostrarPrestamosEnMora(
            PagosController pagosController) {

        System.out.println("\n--- PRESTAMOS EN MORA ---");

        pagosController.mostrarCuotasVencidas();
    }

    // Sincronizar archivos de préstamos y cuotas
    public void sincronizarArchivo() {

        try {

            archivoPrestamos.guardar(prestamos);

            System.out.println(
                    "Archivos de prestamos y cuotas sincronizados correctamente."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error al sincronizar archivos: "
                    + e.getMessage()
            );
        }
    }

    // Actualizar cuotas y estados en MySQL
    public void guardarCambios() {

        boolean todoCorrecto = true;

        for (Prestamos prestamo : prestamos) {

            boolean cuotasGuardadas =
                    cuotasRepository.guardarTodas(
                            prestamo.getIdPrestamo(),
                            prestamo.getListaCuotas()
                    );

            boolean estadoActualizado =
                    prestamoRepository.actualizarEstado(
                            prestamo
                    );

            if (!cuotasGuardadas || !estadoActualizado) {

                todoCorrecto = false;

                System.out.println(
                        "Error al actualizar el prestamo "
                        + prestamo.getIdPrestamo()
                );
            }
        }

        if (todoCorrecto) {

            sincronizarArchivo();

            System.out.println(
                    "Cuotas y estados actualizados correctamente en MySQL."
            );

        } else {

            System.out.println(
                    "Algunos registros no pudieron actualizarse."
            );
        }
    }
}


