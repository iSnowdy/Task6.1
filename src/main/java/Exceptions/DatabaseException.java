package Exceptions;

/**
 * This will encapsulate the rest of specific exceptions.
 * <p>
 * When using an INSERT method, for example, throw new DatabaseInsertException. And so on.
 * <p>
 * If we are not sure what kind of exception is going to be thrown and/or we know there will be an exception
 * thrown, then catch DatabaseException, since it is the superclass for the rest.
 */

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
