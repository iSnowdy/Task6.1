package Andy.Hibernate.Database;

import Andy.Hibernate.Database.Util.HibernateManager;
import Andy.Hibernate.Models.DatabaseEntity;
import Andy.Hibernate.Models.Department;
import Andy.Hibernate.Models.Employee;
import Excepciones.DatabaseDeleteException;
import Excepciones.DatabaseInsertException;
import Excepciones.DatabaseQueryException;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public abstract class BaseImplementation<T extends DatabaseEntity> {
    private final HibernateManager hibernateManager;
    private final Class<T> clazz;
    private int updatedRows;

    public BaseImplementation(Class<T> clazz) {
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
        return Optional.empty();
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
