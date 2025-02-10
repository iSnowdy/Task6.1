package Excepciones;

public class DatabaseReadException extends RuntimeException {
    public DatabaseReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
