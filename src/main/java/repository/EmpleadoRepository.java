package repository;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Empleado;

public class EmpleadoRepository implements EmpleadoDAO {
    
    @Override
    public boolean guardar(Empleado empleado) {

        String sql = "INSERT INTO empleados "
                + "(id_empleado, nombre, documento, correo, rol, salario) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                return false;
            }

            try (PreparedStatement consulta =
                    conexion.prepareStatement(sql)) {

                consulta.setInt(1, empleado.getId());
                consulta.setString(2, empleado.getNombre());
                consulta.setString(3, empleado.getDocumento());
                consulta.setString(4, empleado.getCorreo());
                consulta.setString(5, empleado.getRol());
                consulta.setDouble(6, empleado.getSalario());

                consulta.executeUpdate();

                System.out.println("Empleado guardado en MySQL.");
                return true;
            }

        } catch (SQLException e) {

            System.out.println("Error al guardar empleado: "
                    + e.getMessage());

            return false;
        }
    }

    @Override
    public List<Empleado> listar() {

        ArrayList<Empleado> empleados = new ArrayList<>();

        String sql = "SELECT * FROM empleados ORDER BY id_empleado";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                return empleados;
            }

            try (PreparedStatement consulta =
                    conexion.prepareStatement(sql);
                 ResultSet resultado = consulta.executeQuery()) {

                while (resultado.next()) {

                    Empleado empleado = new Empleado(
                            resultado.getInt("id_empleado"),
                            resultado.getString("nombre"),
                            resultado.getString("documento"),
                            resultado.getString("correo"),
                            resultado.getString("rol"),
                            resultado.getDouble("salario")
                    );

                    empleados.add(empleado);
                }
            }

        } catch (SQLException e) {

            System.out.println("Error al consultar empleados: "
                    + e.getMessage());
        }

        return empleados;
    }
}
