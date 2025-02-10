package Andy.db4o.Database;

import Excepciones.DatabaseDeleteException;
import Excepciones.DatabaseInsertException;
import Excepciones.DatabaseQueryException;
import com.db4o.query.Query;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Abstract class that provides generic CRUD operations for db4o. Meant to be extended and used together
 * with implemented methods to query.
 *
 * @param <T> The type of entity being managed (Department, Employee).
 */

public abstract class BaseImplementation<T> {
    private final Class<T> clazz; // Like this we know what kind of class are we
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Constructs BaseImplementation and ensures that the database has been initialized.
     *
     * @param clazz The class type of the generic (T) entity.
     * @throws RuntimeException If the database was not initialized.
     */

    public BaseImplementation(Class<T> clazz) {
        if (DB4OManager.db4oContainer == null) {
            System.out.println("DatabaseManager not initialized");
            throw new RuntimeException("DatabaseManager not initialized");
        }
        this.clazz = clazz;
    }

    /**
     * Checks if any given object exists in the db4o database.
     *
     * @param object Entity instance.
     * @return {@code true} if exists, {@code false} otherwise.
     */

    private boolean isObjectInDB(T object) {
        return !(DB4OManager.db4oContainer.queryByExample(object).isEmpty());
    }

    /**
     * Stores an object in the db4o database if it does not exist already.
     *
     * @param object The entity to be stored.
     * @return {@code true} if the object was successfully stored, {@code false} if it exists.
     * @throws DatabaseInsertException if an error occurs while storing the object.
     */

    protected boolean storeObject(T object) {
        if (!isObjectInDB(object)) {
            try {
                DB4OManager.db4oContainer.store(object);
                return true;
            } catch (Exception e) {
                throw new DatabaseInsertException("Could not store the object " + object.getClass().getName() + " in db4o", e);
            }
        }
        return false;
    }

    /**
     * Updates an object in the database based on user input.
     *
     * @param id               The unique identifier (Department_ID, Employee_ID) of the object.
     * @param primaryFieldName The name of the primary key field (ID).
     * @return An {@code Optional<T>} containing the updated object, or empty if it was not found.
     */

    protected Optional<T> updateObject(Object id, String primaryFieldName) {
        Optional<T> objectOptional = getObject(id, primaryFieldName);
        if (objectOptional.isEmpty()) {
            System.out.println("Object with ID " + id + " could not be found");
            return Optional.empty();
        }

        T objectToUpdate = objectOptional.get();
        Optional<Field> fieldToUpdate = promptUserForFieldSelection(objectToUpdate);
        if (fieldToUpdate.isEmpty()) return Optional.empty();

        DB4OManager.db4oContainer.commit();
        return modifyObjectField(objectToUpdate, fieldToUpdate.get());
    }

    // Using Java Reflection, prints all the object fields and shows them to the user
    private Optional<Field> promptUserForFieldSelection(T object) {
        printObjectFields();
        int fieldIndex = scanner.nextInt();
        scanner.nextLine(); // Consume buffer

        if (isInvalidFieldIndex(fieldIndex, object)) {
            System.out.println("Invalid field selection.");
            return Optional.empty();
        }
        // +1 because index 0 start
        Field field = object.getClass().getDeclaredFields()[fieldIndex];
        System.out.println("Field " + field.getName() + " selected.");
        field.setAccessible(true);
        return Optional.of(field);
    }

    protected void printObjectFields() {
        Field[] fields = clazz.getDeclaredFields();
        System.out.println("Select one of the available fields to modify:");
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            System.out.println((i + ". " + fields[i].getName()));
        }
    }

    private boolean isInvalidFieldIndex(int fieldIndex, T object) {
        return fieldIndex > object.getClass().getDeclaredFields().length - 1 || fieldIndex < 0;
    }

    // Given the object to be updated, it asks the user for its new value and updates it. First it sets
    // the field to the new value and then stores it in db4o.
    private Optional<T> modifyObjectField(T object, Field field) {
        try {
            System.out.println("Enter new value for: " + field.getName() + ": ");

            Object newValue;

            if (field.getType() == int.class) {
                newValue = scanner.nextInt();
                scanner.nextLine();
            } else {
                newValue = scanner.nextLine();
            }

            field.set(object, newValue);
            storeObject(object);
            System.out.println("Object successfully updated");
            return Optional.of(object);
        } catch (IllegalAccessException | DatabaseInsertException exception) {
            System.out.println("Object could not be updated: " + exception.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Deletes an object from the db4o database.
     *
     * @param object The entity to be deleted.
     * @return {@code true} if the object was successfully deleted, {@code false} otherwise.
     * @throws DatabaseDeleteException if an error occurs while deleting the object.
     */

    protected boolean deleteObject(T object) {
        if (isObjectInDB(object)) {
            try {
                DB4OManager.db4oContainer.delete(object);
                DB4OManager.db4oContainer.commit();
                return true;
            } catch (Exception e) {
                DB4OManager.db4oContainer.rollback();
                throw new DatabaseDeleteException("Could not delete the object " + object.getClass().getName() + " in db4o", e);
            }
        }
        return false;
    }

    /**
     * Retrieves an object from the db4o database by its ID.
     *
     * @param id             The unique ID of the object (which would be Employee/Department ID).
     * @param fieldQueryName The name of the field to query (PK).
     * @return An {@code Optional<T>} containing the found object, or empty if it was not found.
     * @throws DatabaseQueryException if an error occurs while querying the database.
     */

    protected Optional<T> getObject(Object id, String fieldQueryName) {
        System.out.println("ID: " + id + " Query: " + fieldQueryName);
        try {
            Query query = DB4OManager.db4oContainer.query();
            // Obtain the class (Employee or Department)
            query.constrain(clazz);
            query.descend(fieldQueryName).constrain(id);

            List<T> queryResults = query.execute();

            // If the query result is empty, returns an empty Optional. Otherwise, the first match
            return queryResults.isEmpty() ? Optional.empty() : Optional.of(queryResults.get(0));
        } catch (Exception e) {
            throw new DatabaseQueryException("Error querying single object. Field: " + fieldQueryName + " in db4o", e);
        }

    }

    /**
     * Retrieves all objects of the specified type from the db4o.
     *
     * @return A {@code List<T>} containing all objects of type T (determined by the constructor).
     * @throws DatabaseQueryException if an error occurs while querying the database.
     */

    protected List<T> getObjectList() {
        try {
            Query query = DB4OManager.db4oContainer.query();
            query.constrain(clazz);
            return query.execute();
        } catch (Exception e) {
            throw new DatabaseQueryException("Error querying all objects in db4o", e);
        }
    }
}
