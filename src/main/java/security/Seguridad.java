package security;

import java.util.Scanner;

public class Seguridad {

    private static final String PASSWORD_ADMIN = 
            System.getenv("CREDIYA_ADMIN_PASSWORD");

    public static boolean validarAdministrador(Scanner teclado) {

        if (PASSWORD_ADMIN == null || PASSWORD_ADMIN.isBlank()) {
            System.out.println(
                "Error: No se ha configurado la contraseña del administrador."
            );
            return false;
        }

        System.out.print("Ingrese la contraseña de administrador: ");
        String password = teclado.nextLine();

        if (PASSWORD_ADMIN.equals(password)) {
            System.out.println("Acceso autorizado.");
            return true;
        }

        System.out.println("Acceso denegado. Contraseña incorrecta.");
        return false;
    }
}
