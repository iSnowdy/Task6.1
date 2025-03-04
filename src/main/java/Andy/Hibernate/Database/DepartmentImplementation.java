package Andy.Hibernate.Database;

import Andy.Hibernate.Models.HDepartment;
import DAO.Interfaces.DepartmentDAO;
import Exceptions.DatabaseQueryException;
import Models.Department;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link DepartmentDAO} using Hibernate JPA.
 * <p>
 * This class extends {@link HibernateBaseImplementation} to provide CRUD operations for the
 * {@link HDepartment} entity.
 */

public class DepartmentImplementation extends HibernateBaseImplementation<HDepartment> implements DepartmentDAO {

    /**
     * Constructs a new {@link DepartmentImplementation} instance and injects the {@link DatabaseManager} for Hibernate
     * into the BaseImplementation.
     * <p>
     * It also initializes the parent class {@link HibernateBaseImplementation} with {@link HDepartment} to specify
     * the entity type.
     */

    public DepartmentImplementation(final DatabaseManager dbManager) {
        super(HDepartment.class, dbManager);
    }

    /**
     * Adds a new department to the Hibernate JPA database.
     *
     * @param department The {@link HDepartment} to be added.
     * @return {@code true} if the department was successfully added, {@code false} otherwise.
     * @throws Exceptions.DatabaseInsertException if an error occurs during insertion.
     */

    @Override
    public boolean addDepartment(Models.Department department) {
        HDepartment hDepartment =
                new HDepartment(department);

        if (storeObject(hDepartment)) {
            System.out.println("Department ID " + department.getDepartmentID() + " successfully added");
            return true;
        }
        return false;
    }

    /**
     * Updates an existing department in the database.
     * <p>
     * It will also prompt the user to select which field they want to modify and proceed to update it.
     *
     * @param id The ID of the department to be modified.
     * @return An {@code Optional<HDepartment>} containing the updated department, or {@code Optional.empty()} if
     * the department was not found in the database.
     * @throws Exceptions.DatabaseQueryException if an error occurs during the update process.
     */

    @Override
    public Optional<Models.Department> updateDepartment(Object id) {
        Optional<HDepartment> hDepartmentOptional = updateObject(id);

        // Convert HDepartment to Models.Department if present
        return hDepartmentOptional.map(hDept -> new Models.Department(
                hDept.getID(),
                hDept.getDepartmentName(),
                hDept.getDepartmentAddress()
        ));
    }

    /**
     * Deletes a department that already exists from the Hibernate JPA database.
     *
     * @param id The ID of the department to be deleted.
     * @return An {@code Optional<HDepartment>} containing the deleted department, {@code Optional.empty()} if
     * the department was not found in the database.
     * @throws Exceptions.DatabaseDeleteException if an error occurs during the deletion process.
     */

    @Override
    public Optional<Models.Department> deleteDepartment(Object id) {
        Optional<HDepartment> hDepartmentOptional = getObject(id);

        if (hDepartmentOptional.isEmpty()) {
            System.out.println("Department ID " + id + " could not be found");
            return Optional.empty();
        }

        deleteObject(hDepartmentOptional.get());
        System.out.println("Department ID " + id + " successfully deleted");

        return hDepartmentOptional.map(hDept ->
                new Models.Department(hDept.getDepartmentID(), hDept.getDepartmentName(), hDept.getDepartmentAddress()));
    }

    @Override
    protected boolean canDeleteObject(HDepartment department) {
        String hql = "SELECT COUNT(e) FROM HEmployee e WHERE e.HDepartment.id = :depID";

        try (Session session = openSession()) {
            Long count = session.createQuery(hql, Long.class)
                    .setParameter("depID", department.getID())
                    .getSingleResult();

            if (count > 0) {
                System.out.println("Department " + department.getDepartmentID() + " could not be deleted because it contains employees");
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new DatabaseQueryException("Could not check employees for department ID " + department.getID(), e);
        }
    }

    /**
     * Finds a specific department given its ID.
     *
     * @param id The ID of the department to be searched for.
     * @return An {@code Optional<HDepartment>} if it was found, {@code Optional.empty()} otherwise.
     * @throws Exceptions.DatabaseQueryException if an error occurs during the search process.
     */

    @Override
    public Optional<Models.Department> findDepartmentByID(Object id) {
        Optional<HDepartment> hDepartmentOptional = getObject(id);
        return hDepartmentOptional.map(hDept -> new Models.Department(hDept.getID(), hDept.getDepartmentName(), hDept.getDepartmentAddress()));
    }

    /**
     * Retrieves a {@code List<HDepartment>} of all departments inside the Hibernate JPA database.
     *
     * @return A {@code List<HDepartment>} containing all {@link HDepartment} entities.
     * @throws Exceptions.DatabaseQueryException if an error occurs during the retrieval process.
     */

    @Override
    public List<Models.Department> findAllDepartments() {
        List<HDepartment> hDepartments = getObjectList(); // Retrieve HDepartment from DB

        return hDepartments.stream()
                .map(hDept -> new Models.Department(
                        hDept.getID(),
                        hDept.getDepartmentName(),
                        hDept.getDepartmentAddress()
                ))
                .toList(); // Convert to List<Models.Department>
    }
}
