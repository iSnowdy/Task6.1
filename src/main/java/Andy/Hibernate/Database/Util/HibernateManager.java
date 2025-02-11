package Andy.Hibernate.Database.Util;

import DAO.Interfaces.DatabaseImplementation;
import Exceptions.DatabaseClosingException;
import Exceptions.DatabaseOpeningException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Manages the Hibernate SessionFactory and database sessions using the Singleton pattern.
 * <p>
 * This class ensures that only one instance of {@link HibernateManager} exists in the application, preventing
 * multiple {@code SessionFactory} instances from being created.
 * <p>
 * The {@code openDB()} method initializes the SessionFactory, required before performing any action on the
 * database. Then the session can be retrieved and managed using {@code openSession()} and {@code closeSession()}.
 */

public class HibernateManager implements DatabaseImplementation {
    private static HibernateManager lazyInstance;
    private SessionFactory sessionFactory;
    private Session session;
    private boolean isOpen = false;

    private HibernateManager() {} // Singleton

    /**
     * Retrieves the singleton lay instance of {@link HibernateManager}.
     * <p>
     * The instance is created only when this method is called for the first time (Lazy Initialization).
     *
     * @return The single instance of {@link HibernateManager}.
     */

    public static HibernateManager getInstance() {
        // Lazy instance of the Singleton. It will only be opened if this method is called
        if (lazyInstance == null) {
            lazyInstance = new HibernateManager();
        }
        return lazyInstance;
    }

    /**
     * Initializes the Hibernate {@code SessionFactory}, allowing for database operations to be performed.
     * <p>
     * This method must be called before opening any sessions. If the SessionFactory is already initialized,
     * it does nothing to prevent such redundant creation.
     *
     * @throws DatabaseOpeningException if the SessionFactory fails to initialize.
     */

    @Override
    public void openDB() {
        if (!isOpen) {
            try {
                this.sessionFactory = new Configuration().configure().buildSessionFactory();
                System.out.println("SessionFactory initialized successfully");
                isOpen = true;
            } catch (Exception e) {
                throw new DatabaseOpeningException("Could not initialize SessionFactory", e);
            }
        } else System.out.println("SessionFactory is already initialized");
    }

    /**
     * Retrieves the Hibernate {@code SessionFactory}.
     *
     * @return The active {@code SessionFactory}.
     * @throws DatabaseOpeningException if the SessionFactory has not been initialized yet.
     */

    public SessionFactory getSessionFactory() {
        if (!isOpen) throw new DatabaseOpeningException("SessionFactory is not open");
        return sessionFactory;
    }

    /**
     * Opens a new Hibernate session for database queries and transactions.
     * <p>
     * This method must be called after {@code openDB()} to ensure that {@code SessionFactory} has been
     * properly initialized.
     *
     * @return A new {@code Session} for interacting with the database.
     * @throws DatabaseOpeningException if the SessionFactory has not been initialized.
     */

    public Session openSession() {
        if (!isOpen) throw new DatabaseOpeningException("Could not open the Session because the SessionFactory " +
                "has not been initialized");
        this.session = sessionFactory.openSession();
        return session;
    }

    /**
     * Closes the currently active session if it is open.
     * <p>
     * If no session is active, this method does nothing.
     *
     * @throws DatabaseClosingException if an error occurs while closing the session.
     */

    public void closeSession() {
        if (session != null && session.isOpen()) {
            try {
                session.close();
                System.out.println("Session closed successfully");
            } catch (Exception e) {
                throw new DatabaseClosingException("Could not close Session", e);
            } finally {
                session = null; // Cleanses the Session no matter what happens
            }
        }
    }

    /**
     * Closes the Hibernate {@code SessionFactory} and releases database resources.
     * <p>
     * This method must be called once the application no longer needs to use Hibernate.
     * <p>
     * It also ensures that any active session is closed before shutting down the connection.
     *
     * @throws DatabaseClosingException if an error occurs while trying to close the {@code SessionFactory}.
     */

    @Override
    public void closeDB() {
        closeSession();
        if (isOpen && sessionFactory != null) {
            try {
                sessionFactory.close();
                isOpen = false;
                System.out.println("SessionFactory closed successfully");
            } catch (Exception e) {
                throw new DatabaseClosingException("Could not close SessionFactory", e);
            }
        }
    }
}
