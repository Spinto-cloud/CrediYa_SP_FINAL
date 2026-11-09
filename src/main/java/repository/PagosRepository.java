package repository;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Pagos;
import model.Prestamos;
import model.Cuotas;

public class PagosRepository implements PagosDAO {

    @Override
    public boolean guardar(Pagos pago) {

        String sql = "INSERT INTO pagos "
                + "(id_pago, id_prestamo, monto, fecha) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                return false;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql)) {

                consulta.setInt(1, pago.getIdPago());
                consulta.setInt(2, pago.getPrestamo().getIdPrestamo());
                consulta.setDouble(3, pago.getMonto());
                consulta.setDate(4,
                        java.sql.Date.valueOf(pago.getFecha()));

                consulta.executeUpdate();

                System.out.println("Pago guardado en MySQL.");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error al guardar el pago: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ArrayList<Pagos> listar(List<Prestamos> prestamos) {

        ArrayList<Pagos> lista = new ArrayList<>();

        String sql = "SELECT * FROM pagos ORDER BY id_pago";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                return lista;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql);
                 ResultSet resultado = consulta.executeQuery()) {

                while (resultado.next()) {

                    int idPrestamo = resultado.getInt("id_prestamo");

                    Prestamos prestamoEncontrado = null;

                    for (Prestamos prestamo : prestamos) {
                        if (prestamo.getIdPrestamo() == idPrestamo) {
                            prestamoEncontrado = prestamo;
                            break;
                        }
                    }

                    if (prestamoEncontrado != null) {

                        Pagos pago = new Pagos(
                                resultado.getInt("id_pago"),
                                prestamoEncontrado,
                                resultado.getDouble("monto"),
                                resultado.getDate("fecha").toLocalDate()
                        );

                        lista.add(pago);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar pagos: " + e.getMessage());
        }

        return lista;
    }
     
    @Override
    public int registrarPagoCompleto(
            Pagos pago,
            List<Cuotas> cuotas,
            String estadoPrestamo) {

        String sqlPago = "INSERT INTO pagos "
                + "(id_prestamo, monto, fecha) VALUES (?, ?, ?)";

        String sqlCuota = "UPDATE cuotas "
                + "SET monto_pagado = ? "
                + "WHERE id_prestamo = ? AND numero_cuota = ?";

        String sqlPrestamo = "UPDATE prestamos "
                + "SET estado = ? WHERE id_prestamo = ?";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                return -1;
            }

            conexion.setAutoCommit(false);

            try {

                int idGenerado;

                // 1. Registrar el pago y recuperar el ID automático
                try (PreparedStatement consulta = conexion.prepareStatement(
                        sqlPago,
                        java.sql.Statement.RETURN_GENERATED_KEYS)) {

                    consulta.setInt(1, pago.getPrestamo().getIdPrestamo());
                    consulta.setDouble(2, pago.getMonto());
                    consulta.setDate(3,
                            java.sql.Date.valueOf(pago.getFecha()));

                    consulta.executeUpdate();

                    try (ResultSet resultado = consulta.getGeneratedKeys()) {

                        if (!resultado.next()) {
                            throw new SQLException(
                                    "No se pudo generar el ID del pago."
                            );
                        }

                        idGenerado = resultado.getInt(1);
                    }
                }

                // 2. Actualizar las cuotas
                try (PreparedStatement consulta = conexion.prepareStatement(sqlCuota)) {

                    for (Cuotas cuota : cuotas) {

                        consulta.setDouble(1, cuota.getMontoPagado());
                        consulta.setInt(2, pago.getPrestamo().getIdPrestamo());
                        consulta.setInt(3, cuota.getNumero());

                        int filas = consulta.executeUpdate();

                        if (filas != 1) {
                            throw new SQLException(
                                    "No se pudo actualizar la cuota "
                                    + cuota.getNumero()
                            );
                        }
                    }
                }

                // 3. Actualizar el estado del préstamo
                try (PreparedStatement consulta = conexion.prepareStatement(sqlPrestamo)) {

                    consulta.setString(1, estadoPrestamo);
                    consulta.setInt(2, pago.getPrestamo().getIdPrestamo());

                    int filas = consulta.executeUpdate();

                    if (filas != 1) {
                        throw new SQLException(
                                "No se pudo actualizar el estado del préstamo."
                        );
                    }
                }

                // 4. Confirmar la transacción
                conexion.commit();

                System.out.println("Transacción completada correctamente.");

                return idGenerado;

            } catch (SQLException e) {

                conexion.rollback();

                System.out.println("Transacción cancelada: " + e.getMessage());

                return -1;

            } finally {
                conexion.setAutoCommit(true);
            }

        } catch (SQLException e) {

            System.out.println("Error en la transacción: " + e.getMessage());
            return -1;
        }
    }     
}
