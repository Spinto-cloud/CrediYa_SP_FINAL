package factory;

import controller.EmpleadoController;
import controller.ClienteController;
import controller.PrestamosController;
import controller.PagosController;
import controller.ReportesController;

import repository.EmpleadoRepository;
import repository.ClienteRepository;
import repository.PrestamosRepository;
import repository.CuotasRepository;
import repository.PagosRepository;

public class ControllerFactory {

    // Crear controlador de empleados
    public EmpleadoController crearEmpleadoController() {

        return new EmpleadoController(
                new EmpleadoRepository()
        );
    }

    // Crear controlador de clientes
    public ClienteController crearClienteController() {

        return new ClienteController(
                new ClienteRepository()
        );
    }

    // Crear controlador de préstamos
    public PrestamosController crearPrestamosController(
            ClienteController clienteController,
            EmpleadoController empleadoController) {

        return new PrestamosController(
                clienteController,
                empleadoController,
                new PrestamosRepository(),
                new CuotasRepository()
        );
    }

    // Crear controlador de pagos
    public PagosController crearPagosController(
            PrestamosController prestamosController) {

        return new PagosController(
                prestamosController,
                new PagosRepository()
        );
    }

    // Crear controlador de reportes
    public ReportesController crearReportesController(
            PrestamosController prestamosController) {

        return new ReportesController(
                prestamosController
        );
    }
}
