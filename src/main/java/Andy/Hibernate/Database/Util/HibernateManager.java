package Andy.Hibernate.Database.Util;

import DAO.Interfaces.DatabaseImplementation;
import Excepciones.DatabaseClosingException;
import Excepciones.DatabaseOpeningException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateManager implements DatabaseImplementation {
    private final SessionFactory SESSION_FACTORY;
    private Session session;

    public HibernateManager() {
        try {
            this.SESSION_FACTORY = new Configuration().configure().buildSessionFactory();
            System.out.println("SessionFactory initialized successfully");
        } catch (Exception e) {
            System.out.println("SessionFactory initialization failed");
            throw new DatabaseOpeningException("Could not initialize SessionFactory", e);
        }
    }

    @Override
    public void openDB() {}

    public SessionFactory getSessionFactory() {
        if (SESSION_FACTORY == null) throw new DatabaseOpeningException("SessionFactory has not been initialized");
        return SESSION_FACTORY;
    }

    public Session openSession() {
        if (SESSION_FACTORY == null) throw new DatabaseOpeningException("Could not open the Session");
        this.session = SESSION_FACTORY.openSession();
        return session;
    }

    public Session getCurrentSession() {
        if ((session == null) || !session.isOpen()) {
            this.session = openSession();
        }
        return session;
    }

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

    @Override
    public void closeDB() {
        closeSession();
        if (SESSION_FACTORY != null) {
            try {
                SESSION_FACTORY.close();
                System.out.println("SessionFactory closed successfully");
            } catch (Exception e) {
                throw new DatabaseClosingException("Could not close SessionFactory", e);
            }
        }
    }
}
