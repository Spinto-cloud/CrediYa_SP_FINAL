package database;

import java.sql.Connection;

public class PruebaConexion {

    public static void main(String[] args) {

        Connection conexion = Conexion.getInstancia().conectar();

        if (conexion != null) {
            System.out.println("CrediYa esta conectado a MySQL.");

            try {
                conexion.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("No fue posible conectar.");
        }
    }
}
