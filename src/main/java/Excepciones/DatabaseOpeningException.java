package Excepciones;

public class DatabaseOpeningException extends RuntimeException {
    public DatabaseOpeningException(String message, Throwable cause) {
        super(message, cause);
    }
}
