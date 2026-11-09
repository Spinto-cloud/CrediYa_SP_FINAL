package repository;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Cuotas;

public class CuotasRepository implements CuotasDAO {

    @Override
    public boolean guardarTodas(int idPrestamo, List<Cuotas> cuotas) {

        String eliminar = "DELETE FROM cuotas WHERE id_prestamo = ?";

        String insertar = "INSERT INTO cuotas "
                + "(id_prestamo, numero_cuota, valor, interes, "
                + "capital, monto_pagado, fecha_vencimiento) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = Conexion.getInstancia().conectar()) {

            if (conexion == null) {
                return false;
            }

            conexion.setAutoCommit(false);

            try {
                // Eliminar cuotas anteriores del préstamo
                try (PreparedStatement consulta =
                        conexion.prepareStatement(eliminar)) {

                    consulta.setInt(1, idPrestamo);
                    consulta.executeUpdate();
                }

                // Insertar las cuotas actualizadas
                try (PreparedStatement consulta =
                        conexion.prepareStatement(insertar)) {

                    for (Cuotas cuota : cuotas) {

                        consulta.setInt(1, idPrestamo);
                        consulta.setInt(2, cuota.getNumero());
                        consulta.setDouble(3, cuota.getValor());
                        consulta.setDouble(4, cuota.getInteres());
                        consulta.setDouble(5, cuota.getCapital());
                        consulta.setDouble(6, cuota.getMontoPagado());

                        consulta.setDate(7,
                                java.sql.Date.valueOf(
                                        cuota.getFechaVencimiento()
                                )
                        );

                        consulta.addBatch();
                    }

                    consulta.executeBatch();
                }

                conexion.commit();

                System.out.println("Cuotas guardadas en MySQL.");
                return true;

            } catch (SQLException e) {

                conexion.rollback();

                System.out.println(
                        "Error al guardar cuotas: " + e.getMessage()
                );

                return false;

            } finally {
                conexion.setAutoCommit(true);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error de conexión al guardar cuotas: "
                    + e.getMessage()
            );

            return false;
        }
    }

    @Override
    public List<Cuotas> listar(int idPrestamo) {

        ArrayList<Cuotas> lista = new ArrayList<>();

        String sql = "SELECT * FROM cuotas "
                + "WHERE id_prestamo = ? "
                + "ORDER BY numero_cuota";

        try (Connection conexion = Conexion.getInstancia().conectar()) {

            if (conexion == null) {
                return lista;
            }

            try (PreparedStatement consulta =
                    conexion.prepareStatement(sql)) {

                consulta.setInt(1, idPrestamo);

                try (ResultSet resultado = consulta.executeQuery()) {

                    while (resultado.next()) {

                        Cuotas cuota = new Cuotas(
                                resultado.getInt("numero_cuota"),
                                resultado.getDouble("valor"),
                                resultado.getDouble("interes"),
                                resultado.getDouble("capital"),
                                resultado.getDate("fecha_vencimiento")
                                        .toLocalDate()
                        );

                        cuota.cargarMontoPagado(
                                resultado.getDouble("monto_pagado")
                        );

                        lista.add(cuota);
                    }
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al consultar cuotas: " + e.getMessage()
            );
        }

        return lista;
    }
}
