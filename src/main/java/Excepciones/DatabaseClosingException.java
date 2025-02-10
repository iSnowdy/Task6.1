package Excepciones;

public class DatabaseClosingException extends RuntimeException {
    public DatabaseClosingException(String message, Throwable cause) {
        super(message, cause);
    }
}
