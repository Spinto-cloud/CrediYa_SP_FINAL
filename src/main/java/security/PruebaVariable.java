package security;

public class PruebaVariable {

    public static void main(String[] args) {

        String password = System.getenv(
            "CREDIYA_ADMIN_PASSWORD"
        );

        if (password != null) {
            System.out.println("Variable configurada correctamente.");
        } else {
            System.out.println("Variable no encontrada.");
        }
    }
}