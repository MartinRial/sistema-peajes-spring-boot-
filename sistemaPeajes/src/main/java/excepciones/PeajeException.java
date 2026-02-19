package excepciones;

public class PeajeException extends Exception {
    public PeajeException(String message) {
        super(message);
    }


public PeajeException(String message, Throwable cause) {
        super(message, cause);
    }
}
