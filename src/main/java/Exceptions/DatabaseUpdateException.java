package Exceptions;

public class DatabaseUpdateException extends DatabaseException {
    public DatabaseUpdateException(String message) {
        super(message);
    }

    public DatabaseUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
