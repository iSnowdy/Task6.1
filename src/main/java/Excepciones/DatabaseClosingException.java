package Excepciones;

public class DatabaseClosingException extends DatabaseException {
    public DatabaseClosingException(String message) {
        super(message);
    }

    public DatabaseClosingException(String message, Throwable cause) {
        super(message, cause);
    }
}
