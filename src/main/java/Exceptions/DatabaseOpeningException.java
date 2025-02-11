package Exceptions;

public class DatabaseOpeningException extends DatabaseException {
    public DatabaseOpeningException(String message) {
        super(message);
    }

    public DatabaseOpeningException(String message, Throwable cause) {
        super(message, cause);
    }
}
