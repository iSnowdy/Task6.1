package Andy.db4o.Database;

import DAO.Interfaces.DatabaseImplementation;
import Exceptions.DatabaseClosingException;
import Exceptions.DatabaseOpeningException;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

/**
 * Manages the lifecycle of a db4o database instance (ObjectContainer).
 * Provides methods to open and close such connection.
 */

public class DatabaseManager implements DatabaseImplementation {
    private ObjectContainer db4oContainer;


    /**
     * Opens a connection to the db4o database file.
     * If it did not exist before, it will be created automatically.
     *
     * @throws DatabaseOpeningException if an error occurs while opening the database.
     */

    @Override
    public void openDB() {
        try {
            db4oContainer = Db4oEmbedded.openFile(db4oDatabaseName);
            System.out.println("Opened database " + db4oDatabaseName + " successfully");
        } catch (Exception e) {
            throw new DatabaseOpeningException("Error opening database " + db4oDatabaseName, e);
        }
    }

    /**
     * Closes the connection to the db4o database.
     * Ensures that all pending transactions are saved before closing.
     *
     * @throws DatabaseClosingException if an error occurs while closing the database.
     */

    @Override
    public void closeDB() {
        if (db4oContainer != null) {
            try {
                db4oContainer.close();
                System.out.println("Closed database " + db4oDatabaseName + " successfully");
            } catch (Exception e) {
                throw new DatabaseClosingException("Error closing database " + db4oDatabaseName, e);
            }
        }
    }

    /**
     * Retrieves the db4o ObjectContainer for database operations.
     *
     * @return The ObjectContainer instance.
     */

    public ObjectContainer getDb4oContainer() {
        if (db4oContainer == null) {
            throw new IllegalStateException("Database is not open. Call openDB() first.");
        }
        return db4oContainer;
    }
}
