package repository;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Prestamos;
import model.Cliente;
import model.Empleado;

public class PrestamosRepository implements PrestamosDAO {

    @Override
    public boolean guardar(Prestamos prestamo) {

        String sql = "INSERT INTO prestamos "
                + "(id_prestamo, id_cliente, id_empleado, monto, "
                + "tasa_interes, numero_cuotas, fecha_inicio, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                return false;
            }

            try (PreparedStatement consulta =
                    conexion.prepareStatement(sql)) {

                consulta.setInt(1, prestamo.getIdPrestamo());
                consulta.setInt(2, prestamo.getCliente().getId());
                consulta.setInt(3, prestamo.getEmpleado().getId());
                consulta.setDouble(4, prestamo.getMonto());
                consulta.setDouble(5, prestamo.getTasaInteres());
                consulta.setInt(6, prestamo.getNumeroCuotas());
                consulta.setDate(7,
                        java.sql.Date.valueOf(prestamo.getFechaInicio()));
                consulta.setString(8, prestamo.getEstado());

                consulta.executeUpdate();

                System.out.println("Prestamo guardado en MySQL.");

                return true;
            }

        } catch (SQLException e) {

            System.out.println("Error al guardar prestamo: "
                    + e.getMessage());

            return false;
        }
    }

    @Override
    public List<Prestamos> listar(
            List<Cliente> clientes,
            List<Empleado> empleados) {

        ArrayList<Prestamos> prestamos = new ArrayList<>();

        String sql = "SELECT * FROM prestamos ORDER BY id_prestamo";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                return prestamos;
            }

            try (PreparedStatement consulta =
                    conexion.prepareStatement(sql);
                 ResultSet resultado = consulta.executeQuery()) {

                while (resultado.next()) {

                    int idCliente = resultado.getInt("id_cliente");
                    int idEmpleado = resultado.getInt("id_empleado");

                    Cliente cliente = clientes.stream()
                            .filter(c -> c.getId() == idCliente)
                            .findFirst()
                            .orElse(null);

                    Empleado empleado = empleados.stream()
                            .filter(e -> e.getId() == idEmpleado)
                            .findFirst()
                            .orElse(null);

                    if (cliente == null || empleado == null) {
                        System.out.println(
                                "No se encontraron los datos relacionados "
                                + "del prestamo "
                                + resultado.getInt("id_prestamo")
                        );
                        continue;
                    }

                    Prestamos prestamo = new Prestamos(
                            resultado.getInt("id_prestamo"),
                            cliente,
                            empleado,
                            resultado.getDouble("monto"),
                            resultado.getDouble("tasa_interes"),
                            resultado.getInt("numero_cuotas"),
                            resultado.getDate("fecha_inicio").toLocalDate()
                    );

                    prestamo.cargarEstado(
                            resultado.getString("estado")
                    );

                    prestamos.add(prestamo);
                }
            }

        } catch (SQLException e) {

            System.out.println("Error al consultar prestamos: "
                    + e.getMessage());
        }

        return prestamos;
    }
    
    @Override
    public boolean actualizarEstado(Prestamos prestamo) {

        String sql = "UPDATE prestamos SET estado = ? WHERE id_prestamo = ?";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                return false;
            }

            try (PreparedStatement consulta =
                    conexion.prepareStatement(sql)) {

                consulta.setString(1, prestamo.getEstado());
                consulta.setInt(2, prestamo.getIdPrestamo());

                return consulta.executeUpdate() > 0;

            }

        } catch (SQLException e) {

            System.out.println("Error al actualizar el estado del prestamo: "
                    + e.getMessage());

            return false;
        }
    }
}

