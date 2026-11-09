package repository;

import java.util.List;
import model.Cliente;

public interface ClienteDAO {

    int guardar(Cliente cliente);

    List<Cliente> listar();
    
    boolean actualizar(Cliente cliente);
    
    boolean eliminar(int id);
}
