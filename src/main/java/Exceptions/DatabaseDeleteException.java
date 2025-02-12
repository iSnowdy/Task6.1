package Exceptions;

public class DatabaseDeleteException extends DatabaseException {
    public DatabaseDeleteException(String message) {
        super(message);
    }

    public DatabaseDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
