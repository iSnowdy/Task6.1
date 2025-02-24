package Andy.Hibernate.Database;

import Andy.Hibernate.Models.DatabaseEntity;
import Andy.Hibernate.Models.HDepartment;
import Andy.Hibernate.Models.HEmployee;
import Exceptions.DatabaseDeleteException;
import Exceptions.DatabaseInsertException;
import Exceptions.DatabaseQueryException;
import Utils.ObjectFieldsUtil;
import Utils.ValidationUtil;
import org.hibernate.Session;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Provides a generic base implementation for Hibernate CRUD operations.
 * <p>
 * This abstract class manages Hibernate interactions for entities that implement {@link DatabaseEntity}, such as
 * {@link HDepartment} and {@link HEmployee}.
 * <p>
 * It ensures code reuse and consistency across multiple entity implementations.
 *
 * @param <T> The entity type extending {@link DatabaseEntity}.
 */

public abstract class HibernateBaseImplementation<T extends DatabaseEntity> {
    private final Class<T> clazz;
    private final DatabaseManager dbManager;

    public HibernateBaseImplementation(Class<T> clazz, final DatabaseManager dbManager) {
        this.clazz = clazz;
        this.dbManager = dbManager;
    }

    /**
     * Checks whether the given object exists in the database.
     * <p>
     * If the object has an assigned ID, it queries the database using that ID.
     * If the object does not have an assigned ID and is an instance of {@link HEmployee},
     * it performs a duplicate check based on its business keys (e.g., employeeName and employeePosition).
     * For other entities without an ID, it currently returns {@code false}.
     *
     * @param object the entity to be checked for existence.
     * @return {@code true} if the object exists in the database; {@code false} otherwise.
     */

    private boolean isObjectInDB(T object) {
        if (object.getID() != null) {
            Optional<T> optionalT = getObject(object.getID());
            return optionalT.isPresent();
        } else {
            if (object instanceof HEmployee employee) {
                Optional<HEmployee> duplicate = getDuplicate(employee);
                return duplicate.isPresent();
            }
        }
        return false;
    }


    /**
     * Stores a new entity in the database.
     * <p>
     * If the entity already exists, it will not be inserted.
     *
     * @param object The entity to be stored.
     * @return {@code true} if the entity was successfully stored, {@code false} otherwise.
     * @throws DatabaseInsertException if an error occurs during insertion.
     */

    protected boolean storeObject(T object) {
        if (!isObjectInDB(object)) {
            try (Session session = dbManager.openSession()) {
                session.beginTransaction();
                session.flush();
                session.persist(object);
                session.getTransaction().commit();
                return true;
            } catch (Exception e) {
                throw new DatabaseInsertException("Could not store the object ID " + object.getID() + " in Hibernate JPA", e);
            }
        }
        return false;
    }

    /**
     * Updates an entity by allowing the user to select a specific field to modify and then prompting for a new value.
     *
     * @param id The ID of the entity to be modified.
     * @return An {@code Optional<T>} containing the updated entity, or {@code Optional.empty()} if the entity
     * was not found or the update was unsuccessful.
     */

    protected Optional<T> updateObject(Object id) {
        Optional<T> objectInDB = getObject(id);
        if (objectInDB.isEmpty()) {
            System.out.println("No object found with ID: " + id);
            return Optional.empty();
        }

        T objectToUpdate = objectInDB.get();
        Set<String> excludedFields = Set.of("id", "employeesList", "HDepartment"); // Exclude ID and relations

        Optional<Field> fieldToUpdate = ObjectFieldsUtil.promptUserForFieldSelection(objectToUpdate, excludedFields);
        if (fieldToUpdate.isEmpty()) return Optional.empty();

        return modifyObjectField(objectToUpdate, fieldToUpdate.get());
    }

    // Given the object which the user wants to modify and the field, it prompts for the new value and then
    // updates it
    private Optional<T> modifyObjectField(T object, Field field) {
        try (Session session = dbManager.openSession()) {
            Object newValue = ObjectFieldsUtil.promptUserForNewValue(field);

            session.beginTransaction();
            session.flush();
            field.set(object, newValue);

            if (!ValidationUtil.isValidObject(object, clazz)) return Optional.empty();

            session.merge(object); // Overwrites
            session.getTransaction().commit();

            System.out.println("Object successfully modified");
            return Optional.of(object);
        } catch (Exception e) {
            System.out.println("Object ID " + object.getID() + " could not be updated");
            throw new DatabaseQueryException("Could not update the object ID " + object.getID() + " in Hibernate JPA", e);
        }
    }

    /**
     * Deletes an entity from the database.
     *
     * @param object The entity to be deleted.
     * @return {@code true} if the entity was successfully deleted, {@code false} otherwise.
     * @throws DatabaseDeleteException if an error occurs during the deletion process.
     */

    protected boolean deleteObject(T object) {
        if (isObjectInDB(object)) {
            try (Session session = dbManager.openSession()) {
                session.beginTransaction();
                session.flush();
                session.remove(object); //delete()
                session.getTransaction().commit();
                return true;
            } catch (Exception e) {
                throw new DatabaseDeleteException("Could not delete the object ID" + object.getID() + " in Hibernate JPA", e);
            }
        }
        return false;
    }

    /**
     * Retrieves an object from the database using HQL given its ID.
     *
     * @param id The ID of the object to be retrieved.
     * @return An {@code Optional<T>} containing the object if it was found, {@code Optional.empty()} otherwise.
     * @throws DatabaseQueryException if an error occurs during retrieval.
     */

    protected Optional<T> getObject(Object id) {
        String clazzSimpleName = clazz.getSimpleName();
        String query =
                "FROM " + clazzSimpleName + " o " +
                        "WHERE o.id = :id";

        try (Session session = dbManager.openSession()) {
            return Optional.ofNullable(session.createQuery(query, clazz)
                    .setParameter("id", id)
                    .setReadOnly(false)
                    .setMaxResults(1)
                    .uniqueResult());
        } catch (Exception e) {
            throw new DatabaseQueryException("Could not retrieve the object ID " + id + " from Hibernate JPA", e);
        }
    }

    /**
     * Retrieves all objects of the given type from the database.
     *
     * @return A {@code List<T>} containing all entities in the database of {@code <T>}
     * @throws DatabaseQueryException if an error occurs during the retrieval process.
     */

    protected List<T> getObjectList() {
        String clazzSimpleName = clazz.getSimpleName();
        // SELECT * FROM in HQL
        String query =
                "FROM " + clazzSimpleName;

        try (Session session = dbManager.openSession()) {
            return session.createQuery(query, clazz)
                    .setReadOnly(false)
                    .getResultList();
        } catch (Exception e) {
            throw new DatabaseQueryException("Could not retrieve object list from Hibernate JPA", e);
        }
    }

    /**
     * Retrieves a duplicate HEmployee entity based on unique business keys.
     * <p>
     * This method queries the database for an HEmployee with the same employeeName and employeePosition.
     * It is used to check for duplicates when the entity does not yet have an assigned ID.
     *
     * @param employee the HEmployee entity to check for duplicates.
     * @return an {@code Optional<HEmployee>} containing the duplicate if found, or {@code Optional.empty()} otherwise.
     * @throws DatabaseQueryException if an error occurs while querying the database.
     */

    private Optional<HEmployee> getDuplicate(HEmployee employee) {
        String hql =
                "FROM HEmployee e " +
                "WHERE e.employeeName = :name " +
                "AND e.employeePosition = :position";

        try (Session session = dbManager.openSession()) {
            HEmployee duplicate = session.createQuery(hql, HEmployee.class)
                    .setParameter("name", employee.getEmployeeName())
                    .setParameter("position", employee.getEmployeePosition())
                    .setMaxResults(1)
                    .uniqueResult();
            return Optional.ofNullable(duplicate);
        } catch (Exception e) {
            throw new DatabaseQueryException("Could not retrieve duplicate for employee " + employee.getEmployeeName(), e);
        }
    }
}
