package Andy.Hibernate.Models;

/**
 * Represents a common interface for database entities that have an ID.
 * <p>
 * This interface is implemented by both {@link HDepartment} and {@link HEmployee},
 * as both entities contain a unique identifier ({@code ID}) required for database
 * related operations.
 * <p>
 * Using this interface allows generic handling of database queries that require and entity's
 * ID's.
 */

public interface DatabaseEntity {

    /**
     * Retrieves the unique identifier of the entity.
     *
     * @return The ID of the entity, which will be used for database queries.
     */

    Integer getID();
}
