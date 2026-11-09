package repository;

import java.util.List;
import model.Cuotas;

public interface CuotasDAO {

    boolean guardarTodas(
            int idPrestamo,
            List<Cuotas> cuotas
    );

    List<Cuotas> listar(int idPrestamo);
}
