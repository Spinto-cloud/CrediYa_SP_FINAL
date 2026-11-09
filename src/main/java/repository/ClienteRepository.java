package repository;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;

public class ClienteRepository implements ClienteDAO {

    @Override
    public int guardar(Cliente cliente) {

        String sql = "INSERT INTO clientes "
                + "(nombre, documento, correo, telefono) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conexion = Conexion.getInstancia().conectar()) {

            if (conexion == null) {
                return -1;
            }

            try (PreparedStatement consulta =
                    conexion.prepareStatement(
                            sql,
                            java.sql.Statement.RETURN_GENERATED_KEYS)) {

                consulta.setString(1, cliente.getNombre());
                consulta.setString(2, cliente.getDocumento());
                consulta.setString(3, cliente.getCorreo());
                consulta.setString(4, cliente.getTelefono());

                consulta.executeUpdate();

                try (ResultSet resultado = consulta.getGeneratedKeys()) {

                    if (resultado.next()) {

                        int idGenerado = resultado.getInt(1);

                        System.out.println(
                                "Cliente guardado en MySQL. ID: " + idGenerado
                        );

                        return idGenerado;
                    }
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al guardar cliente: " + e.getMessage()
            );
        }

        return -1;
    }

    @Override
    public List<Cliente> listar() {

        ArrayList<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT * FROM clientes ORDER BY id_cliente";

        try (Connection conexion = Conexion.getInstancia().conectar()) {

            if (conexion == null) {
                return clientes;
            }

            try (PreparedStatement consulta =
                    conexion.prepareStatement(sql);
                 ResultSet resultado = consulta.executeQuery()) {

                while (resultado.next()) {

                    Cliente cliente = new Cliente(
                            resultado.getInt("id_cliente"),
                            resultado.getString("nombre"),
                            resultado.getString("documento"),
                            resultado.getString("correo"),
                            resultado.getString("telefono")
                    );

                    clientes.add(cliente);
                }
            }

        } catch (SQLException e) {

            System.out.println("Error al consultar clientes: "
                    + e.getMessage());
        }

        return clientes;
    }
    
    @Override
    public boolean actualizar(Cliente cliente) {

        String sql = "UPDATE clientes SET "
                + "nombre = ?, documento = ?, correo = ?, telefono = ? "
                + "WHERE id_cliente = ?";

        try (Connection conexion = Conexion.getInstancia().conectar()) {

            if (conexion == null) {
                return false;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql)) {

                consulta.setString(1, cliente.getNombre());
                consulta.setString(2, cliente.getDocumento());
                consulta.setString(3, cliente.getCorreo());
                consulta.setString(4, cliente.getTelefono());
                consulta.setInt(5, cliente.getId());

                return consulta.executeUpdate() > 0;
            }

        } catch (SQLException e) {

            System.out.println("Error al actualizar cliente: "
                    + e.getMessage());

            return false;
        }
    }
    
    @Override
    public boolean eliminar(int id) {

        String sqlVerificar =
                "SELECT COUNT(*) FROM prestamos WHERE id_cliente = ?";

        String sqlEliminar =
                "DELETE FROM clientes WHERE id_cliente = ?";

        try (Connection conexion = Conexion.getInstancia().conectar()) {

            if (conexion == null) {
                return false;
            }

            // Verificar si tiene prestamos asociados
            try (PreparedStatement consulta =
                    conexion.prepareStatement(sqlVerificar)) {

                consulta.setInt(1, id);

                try (ResultSet resultado = consulta.executeQuery()) {

                    if (resultado.next() && resultado.getInt(1) > 0) {

                        System.out.println(
                                "No se puede eliminar: el cliente tiene prestamos asociados."
                        );

                        return false;
                    }
                }
            }

            // Eliminar cliente si no tiene prestamos
            try (PreparedStatement consulta =
                    conexion.prepareStatement(sqlEliminar)) {

                consulta.setInt(1, id);

                int filasEliminadas = consulta.executeUpdate();

                return filasEliminadas > 0;
            }

        } catch (SQLException e) {

            System.out.println("Error al eliminar cliente: "
                    + e.getMessage());

            return false;
        }
    }
}
