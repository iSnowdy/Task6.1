package Andy.Hibernate.Models;

/**
 * Common interface used by Department and Employee. Since they both have ID's, and we need it
 * to query the database, a common interface would provide us such ID.
 */

public interface DatabaseEntity {
    int getID();
}
