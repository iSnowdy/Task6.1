package Exceptions;

public class DatabaseInsertException extends DatabaseException {
    public DatabaseInsertException(String message) {
        super(message);
    }

    public DatabaseInsertException(String message, Throwable cause) {
        super(message, cause);
    }
}
