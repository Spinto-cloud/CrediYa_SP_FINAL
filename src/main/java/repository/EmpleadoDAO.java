package repository;

import java.util.List;
import model.Empleado;

public interface EmpleadoDAO {

    boolean guardar(Empleado empleado);

    List<Empleado> listar();
    
    boolean actualizar(Empleado empleado);
    
    boolean eliminar(int id);
}
