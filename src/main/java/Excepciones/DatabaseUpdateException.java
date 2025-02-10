package Excepciones;

public class DatabaseUpdateException extends RuntimeException {
    public DatabaseUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
