package Andy.db4o.Database;

import DAO.Interfaces.DatabaseImplementation;
import Excepciones.DatabaseClosingException;
import Excepciones.DatabaseOpeningException;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

public class DatabaseManager implements DatabaseImplementation {
    public static ObjectContainer db4oContainer;

    @Override
    public void openDB() {
        try {
            Db4oEmbedded.openFile(db4oDatabaseName);
            System.out.println("Opened database " + db4oDatabaseName + " successfully");
        } catch (Exception e) {
            throw new DatabaseOpeningException("Error opening database " + db4oDatabaseName, e);
        }
    }

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
}
