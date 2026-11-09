package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL =
            "jdbc:mysql://localhost:3306/crediya_sp_db?useSSL=false&serverTimezone=UTC";

    private static final String USUARIO = "root";

    private static final Conexion instancia = new Conexion();

    // Constructor privado
    private Conexion() {
    }

    // Única instancia de Conexion
    public static Conexion getInstancia() {
        return instancia;
    }

    // Crea una conexión para cada operación
    public Connection conectar() {

        String password = System.getenv("CREDIYA_DB_PASSWORD");

        if (password == null || password.isBlank()) {
            System.out.println(
                    "No se encontró la variable CREDIYA_DB_PASSWORD."
            );
            return null;
        }

        try {
            Connection conexion = DriverManager.getConnection(
                    URL,
                    USUARIO,
                    password
            );

            System.out.println("Conexion con MySQL exitosa.");

            return conexion;

        } catch (SQLException e) {
            System.out.println("Error al conectar con MySQL:");
            System.out.println(e.getMessage());

            return null;
        }
    }
}