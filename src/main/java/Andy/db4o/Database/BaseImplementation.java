package Andy.db4o.Database;

import Excepciones.DatabaseDeleteException;
import Excepciones.DatabaseInsertException;
import com.db4o.query.Query;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public abstract class BaseImplementation<T> {

    public BaseImplementation() {
        if (DatabaseManager.db4oContainer == null) {
            System.out.println("DatabaseManager not initialized");
            throw new RuntimeException("DatabaseManager not initialized");
        }
    }

    /**
     * Retrieves the class of the generic
     *
     * @return The .class that is being implemented
     */
    private Class<?> getClassType() {
        return this.getClass().getSuperclass();
    }

    /**
     * Uses Java Reflection to retrieve all the fields inside a class and prints them to the user.
     */
    protected void printObjectFields() {
        Field[] fields = this.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            System.out.println((i + 1) + ". " + fields[i].getName());
        }
    }

    /**
     * Checks if any given object is inside the db4o database already.
     *
     * @param object Instance of Employee or Department
     * @return true if exists, false otherwise
     */
    private boolean isObjectInDB(T object) {
        return (DatabaseManager.db4oContainer.queryByExample(object).isEmpty());
    }

    protected boolean storeObject(T object) {
        if (!isObjectInDB(object)) {
            try {
                DatabaseManager.db4oContainer.store(object);
                return true;
            } catch (Exception e) {
                throw new DatabaseInsertException("Could not store object", e);
            }
        }
        return false;
    }

    protected boolean deleteObject(T object) {
        if (isObjectInDB(object)) {
            try {
                DatabaseManager.db4oContainer.delete(object);
                DatabaseManager.db4oContainer.commit();
                return true;
            } catch (Exception e) {
                DatabaseManager.db4oContainer.rollback();
                throw new DatabaseDeleteException("Could not delete object", e);
            }
        }
        return false;
    }

    /**
     * Retrieves an object from the DB given their ID and the field to query.
     *
     * @param id             The unique ID of the object (Employee / Department ID).
     * @param fieldQueryName The name of the field to query (PK field).
     * @return Optional<T>. Returns an Optional of the object to query if it was found, else an empty Optional.
     */
    protected Optional<T> getObject(Object id, String fieldQueryName) {
        Query query = DatabaseManager.db4oContainer.query();
        // Obtain the class (Employee or Department)
        query.constrain(getClassType());
        query.descend(fieldQueryName).constrain(id);

        Object queryResult = query.execute();

        if (queryResult != null) {
            return Optional.of((T) queryResult);
        }
        return Optional.empty();
    }

    protected List<T> getObjectList() {
        Query query = DatabaseManager.db4oContainer.query();
        query.constrain(getClassType());
        return (List<T>) query.execute();
    }
}
