package Exceptions;

public class DatabaseQueryException extends DatabaseException {
    public DatabaseQueryException(String message) {
        super(message);
    }

    public DatabaseQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
