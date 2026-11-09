package repository;

import java.util.List;
import model.Prestamos;
import model.Cliente;
import model.Empleado;

public interface PrestamosDAO {

    boolean guardar(Prestamos prestamo);

    List<Prestamos> listar(
            List<Cliente> clientes,
            List<Empleado> empleados
    );

    boolean actualizarEstado(Prestamos prestamo);
}
