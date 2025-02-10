package Andy.db4o.Database;

import Excepciones.DatabaseDeleteException;
import Excepciones.DatabaseInsertException;
import Excepciones.DatabaseQueryException;
import com.db4o.query.Query;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public abstract class BaseImplementation<T> {
    private final Scanner scanner = new Scanner(System.in);

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
        // For testing purposes. Will remove later on
        System.out.println("Class type: " + this.getClass().getName() + ", superclass:" + this.getClass().getSuperclass().getName());
        return this.getClass().getSuperclass();
    }

    /**
     * Checks if any given object is inside the db4o database already.
     *
     * @param object Instance of Employee or Department
     * @return true if exists, false otherwise
     */
    private boolean isObjectInDB(T object) {
        return !(DatabaseManager.db4oContainer.queryByExample(object).isEmpty());
    }

    protected boolean storeObject(T object) {
        if (!isObjectInDB(object)) {
            try {
                DatabaseManager.db4oContainer.store(object);
                return true;
            } catch (Exception e) {
                throw new DatabaseInsertException("Could not store the object " + object.getClass().getName() + " in db4o", e);
            }
        }
        return false;
    }

    protected Optional<T> updateObject(Object id, String primaryFieldName) {
        Optional<T> objectOptional = getObject(id, primaryFieldName);
        if (objectOptional.isEmpty()) {
            System.out.println("Object with ID " + id + " could not be found");
            return Optional.empty();
        }

        T objectToUpdate = objectOptional.get();
        Optional<Field> fieldToUpdate = promptUserForFieldSelection(objectToUpdate);
        if (fieldToUpdate.isEmpty()) return Optional.empty();

        DatabaseManager.db4oContainer.commit();
        return modifyObjectField(objectToUpdate, fieldToUpdate.get());
    }

    private Optional<Field> promptUserForFieldSelection(T object) {
        printObjectFields();
        int fieldIndex = scanner.nextInt();
        scanner.nextLine(); // Consume buffer

        if (isInvalidFieldIndex(fieldIndex, object)) {
            System.out.println("Invalid field selection.");
            return Optional.empty();
        }

        Field field = object.getClass().getDeclaredFields()[fieldIndex];
        field.setAccessible(true);
        return Optional.of(field);
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

    private boolean isInvalidFieldIndex(int fieldIndex, T object) {
        return fieldIndex > object.getClass().getDeclaredFields().length - 1 || fieldIndex < 0;
    }

    private Optional<T> modifyObjectField(T object, Field field) {
        try {
            System.out.println("Enter new value for: " + field.getName() + ": ");
            Object newValue;

            if (field.getType() == int.class) {
                newValue = scanner.nextInt();
                scanner.nextLine(); // Consumir buffer
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

    protected boolean deleteObject(T object) {
        if (isObjectInDB(object)) {
            try {
                DatabaseManager.db4oContainer.delete(object);
                DatabaseManager.db4oContainer.commit();
                return true;
            } catch (Exception e) {
                DatabaseManager.db4oContainer.rollback();
                throw new DatabaseDeleteException("Could not delete the object " + object.getClass().getName() + " in db4o", e);
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
        try {
            Query query = DatabaseManager.db4oContainer.query();
            // Obtain the class (Employee or Department)
            query.constrain(getClassType());
            query.descend(fieldQueryName).constrain(id);

            List<T> queryResults = query.execute();

            // If the query result is empty, returns an empty Optional. Otherwise, the first match
            return queryResults.isEmpty() ? Optional.empty() : Optional.of(queryResults.get(0));
        } catch (Exception e) {
            throw new DatabaseQueryException("Error querying single object. Field: " + fieldQueryName + " in db4o", e);
        }

    }

    protected List<T> getObjectList() {
        try {
            Query query = DatabaseManager.db4oContainer.query();
            query.constrain(getClassType());
            return (List<T>) query.execute();
        } catch (Exception e) {
            throw new DatabaseQueryException("Error querying all objects in db4o", e);
        }
    }
}
