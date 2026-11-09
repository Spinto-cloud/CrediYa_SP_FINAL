package repository;

import java.util.ArrayList;
import java.util.List;
import model.Pagos;
import model.Prestamos;
import model.Cuotas;

public interface PagosDAO {

    boolean guardar(Pagos pago);

    ArrayList<Pagos> listar(List<Prestamos> prestamos);

    int registrarPagoCompleto(
            Pagos pago,
            List<Cuotas> cuotas,
            String estadoPrestamo
    );
}
