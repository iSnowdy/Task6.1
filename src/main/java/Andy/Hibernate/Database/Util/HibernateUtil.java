package Andy.Hibernate.Database.Util;

import Excepciones.DatabaseClosingException;
import Excepciones.DatabaseOpeningException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory SESSION_FACTORY;

    static {
        SessionFactory tempSessionFactory;
        try {
            tempSessionFactory = new Configuration().configure().buildSessionFactory();
            System.out.println("SessionFactory initialized successfully");
        } catch (Exception e) {
            System.out.println("SessionFactory initialization failed");
            tempSessionFactory = null;
            throw new DatabaseOpeningException("Could not initialize SessionFactory", e);
        }
        SESSION_FACTORY = tempSessionFactory;
    }

    public static Session openSession() {
        if (SESSION_FACTORY == null) throw new DatabaseOpeningException("Could not open the Session");

        return SESSION_FACTORY.openSession();
    }

    public static void closeSession(Session session) {
        if (session != null) {
            try {
                session.close();
                System.out.println("Session closed successfully");
            } catch (Exception e) {
                throw new DatabaseClosingException("Could not close Session", e);
            }
        }
    }

    public static void closeSessionFactory() {
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
