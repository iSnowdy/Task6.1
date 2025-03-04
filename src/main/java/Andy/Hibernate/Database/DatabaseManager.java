package Andy.Hibernate.Database;

import DAO.Interfaces.DatabaseImplementation;
import Exceptions.DatabaseClosingException;
import Exceptions.DatabaseOpeningException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Manages the Hibernate SessionFactory and database sessions.
 */

public class DatabaseManager implements DatabaseImplementation {
    private SessionFactory sessionFactory;
    private Session session;
    private boolean isOpen = false;

    /**
     * Opens the Hibernate SessionFactory for database operations.
     *
     * @throws DatabaseOpeningException if initialization fails.
     */

    public DatabaseManager() {
        openDB();
    }

    @Override
    public void openDB() {
        if (!isOpen) {
            try {
                sessionFactory = new Configuration().configure().buildSessionFactory();
                System.out.println("SessionFactory initialized successfully.");
                isOpen = true;
            } catch (Exception e) {
                throw new DatabaseOpeningException("Could not initialize SessionFactory", e);
            }
        } else {
            System.out.println("SessionFactory is already initialized.");
        }
    }

    /**
     * Opens a new Hibernate session.
     *
     * @return A new session.
     * @throws DatabaseOpeningException if the SessionFactory is not initialized.
     */

    public Session openSession() {
        if (!isOpen) {
            throw new DatabaseOpeningException("Cannot open session: SessionFactory is not initialized.");
        }
        session = sessionFactory.openSession();
        return session;
    }

    /**
     * Closes the currently active session, if any.
     */

    public void closeSession() {
        if (session != null && session.isOpen()) {
            try {
                session.close();
                System.out.println("Session closed successfully.");
            } catch (Exception e) {
                throw new DatabaseClosingException("Could not close session", e);
            } finally {
                session = null;
            }
        }
    }

    /**
     * Closes the Hibernate SessionFactory and releases resources.
     */

    @Override
    public void closeDB() {
        closeSession(); // Ensure active sessions are closed before shutting down.
        if (isOpen && sessionFactory != null) {
            try {
                sessionFactory.close();
                isOpen = false;
                System.out.println("SessionFactory closed successfully.");
            } catch (Exception e) {
                throw new DatabaseClosingException("Could not close SessionFactory", e);
            }
        }
    }
}
