package Excepciones;

public class DatabaseInsertException extends RuntimeException {
    public DatabaseInsertException(String message, Throwable cause) {
        super(message, cause);
    }
}
