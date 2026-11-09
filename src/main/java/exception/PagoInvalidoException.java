package exception;

public class PagoInvalidoException extends Exception {

    public PagoInvalidoException(String mensaje) {
        super(mensaje);
    }
}