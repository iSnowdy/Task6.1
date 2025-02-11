package Andy.Hibernate.Database;

import Andy.Hibernate.Database.Util.HibernateManager;
import Andy.Hibernate.Models.DatabaseEntity;
import Excepciones.DatabaseDeleteException;
import Excepciones.DatabaseInsertException;
import Excepciones.DatabaseQueryException;
import org.hibernate.Session;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public abstract class HibernateBaseImplementation<T extends DatabaseEntity> {
    private final HibernateManager hibernateManager;
    private final Class<T> clazz;
    private final Scanner scanner = new Scanner(System.in);

    public HibernateBaseImplementation(Class<T> clazz) {
        this.clazz = clazz;
        this.hibernateManager = new HibernateManager();
    }

    private boolean isObjectInDB(T object) {
        Optional<T> objectInDB = getObject(object.getID());
        return objectInDB.isPresent();
    }

    protected boolean storeObject(T object) {
        if (!isObjectInDB(object)) {
            try (Session session = hibernateManager.getSessionFactory().openSession()) {
                session.beginTransaction();
                session.save(object);
                session.getTransaction().commit();
                return true;
            } catch (Exception e) {
                throw new DatabaseInsertException("Could not store the object ID " + object.getID() + " in Hibernate JPA", e);
            }
        }
        return false;
    }

    protected Optional<T> updateObject(Object id) {
        Optional<T> objectInDB = getObject(id);
        if (objectInDB.isEmpty()) {
            System.out.println("No such object ID " + id);
            return Optional.empty();
        }

        T objectedToUpdate = objectInDB.get();
        Optional<Field> fieldToUpdate = promptUserForFieldSelection(objectedToUpdate);
        if (fieldToUpdate.isEmpty()) return Optional.empty();

        return modifyObjectField(objectedToUpdate, fieldToUpdate.get());
    }

    private Optional<Field> promptUserForFieldSelection(T object) {
        printObjectFields();
        int fieldIndex = scanner.nextInt();
        scanner.nextLine();

        if (isInvalidFieldIndex(fieldIndex, object)) {
            System.out.println("Invalid field selection");
            return Optional.empty();
        }
        // Indexes?
        Field field = object.getClass().getDeclaredFields()[fieldIndex];
        System.out.println("Field " + field.getName() + " selected");
        field.setAccessible(true);
        return Optional.of(field);
    }

    private void printObjectFields() {
        Field[] fields = clazz.getDeclaredFields();
        System.out.println("Select one of the available fields to modify:");
        // TODO: Consider making it so that the user cannot change the ID (int i = 1)
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            System.out.println(i + ". " + fields[i].getName());
        }
    }

    private boolean isInvalidFieldIndex(int fieldIndex, T object) {
        return fieldIndex > object.getClass().getDeclaredFields().length - 1 || fieldIndex < 0;
    }

    private Optional<T> modifyObjectField(T object, Field field) {
        try (Session session = hibernateManager.getSessionFactory().openSession()) {
            System.out.println("Enter the new value for " + field.getName() + ": ");
            Object newValue;

            if (field.getType() == int.class) {
                newValue = scanner.nextInt();
                scanner.nextLine();
            } else newValue = scanner.nextLine();

            session.beginTransaction();
            field.set(object, newValue);
            session.merge(object); // Overwrites
            session.getTransaction().commit();

            System.out.println("Object successfully modified");
            return Optional.of(object);
        } catch (Exception e) {
            System.out.println("Object ID " + object.getID() + " could not be updated");
            throw new DatabaseQueryException("Could not update the object ID " + object.getID() + " in Hibernate JPA", e);
        }
    }

    protected boolean deleteObject(T object) {
        if (isObjectInDB(object)) {
            try (Session session = hibernateManager.getSessionFactory().openSession()) {
                session.beginTransaction();
                session.delete(object);
                session.getTransaction().commit();
                return true;
            } catch (Exception e) {
                throw new DatabaseDeleteException("Could not delete the object ID" + object.getID() + " in Hibernate JPA", e);
            }
        }
        return false;
    }

    protected Optional<T> getObject(Object id) {
        String clazzSimpleName = clazz.getSimpleName();
        String query =
                "FROM " + clazzSimpleName + " o " +
                "WHERE o.id = :id";

        try (Session session = hibernateManager.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.createQuery(query, clazz)
                    .setParameter("id", id)
                    .setReadOnly(false)
                    .setMaxResults(1)
                    .uniqueResult());
        } catch (Exception e) {
            throw new DatabaseQueryException("Could not retrieve the object ID " + id + " from Hibernate JPA", e);
        }
    }

    protected List<T> getObjectList() {
        String clazzSimpleName = clazz.getSimpleName();
        // SELECT * FROM in HQL
        String query =
                "FROM " + clazzSimpleName;

        try (Session session = hibernateManager.getSessionFactory().openSession()) {
            return session.createQuery(query, clazz)
                    .setReadOnly(false)
                    .getResultList();
        } catch (Exception e) {
            throw new DatabaseQueryException("Could not retrieve object list from Hibernate JPA", e);
        }
    }
}
