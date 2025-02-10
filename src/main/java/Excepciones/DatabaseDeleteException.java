package Excepciones;

public class DatabaseDeleteException extends RuntimeException {
    public DatabaseDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
