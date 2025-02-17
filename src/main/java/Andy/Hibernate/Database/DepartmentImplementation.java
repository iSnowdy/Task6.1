package Andy.Hibernate.Database;

import Andy.Hibernate.Models.HDepartment;
import DAO.Interfaces.HibernateInterfaces.HDepartmentDAO;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link HDepartmentDAO} using Hibernate JPA.
 * <p>
 * This class extends {@link HibernateBaseImplementation} to provide CRUD operations for the
 * {@link HDepartment} entity.
 */

public class DepartmentImplementation extends HibernateBaseImplementation<HDepartment> implements HDepartmentDAO {

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
    public boolean addDepartment(HDepartment department) {
        if (storeObject(department)) {
            System.out.println("Department ID " + department.getId() + " succesfully added");
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
    public Optional<HDepartment> updateDepartment(Object id) {
        return updateObject(id);
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
    public Optional<HDepartment> deleteDepartment(Object id) {
        Optional<HDepartment> departmentOptional = getObject(id);
        if (departmentOptional.isEmpty()) {
            System.out.println("Department ID " + id + " could not be found");
            return Optional.empty();
        }

        deleteObject(departmentOptional.get());
        System.out.println("Department ID " + id + " successfully deleted");
        return departmentOptional;
    }

    /**
     * Finds a specific department given its ID.
     *
     * @param id The ID of the department to be searched for.
     * @return An {@code Optional<HDepartment>} if it was found, {@code Optional.empty()} otherwise.
     * @throws Exceptions.DatabaseQueryException if an error occurs during the search process.
     */

    @Override
    public Optional<HDepartment> findDepartmentByID(Object id) {
        Optional<HDepartment> departmentOptional = getObject(id);
        if (departmentOptional.isEmpty()) {
            System.out.println("Department ID " + id + " could not be found");
            return Optional.empty();
        }
        System.out.println("Department ID " + id + " successfully found");
        return departmentOptional;
    }

    /**
     * Retrieves a {@code List<HDepartment>} of all departments inside the Hibernate JPA database.
     *
     * @return A {@code List<HDepartment>} containing all {@link HDepartment} entities.
     * @throws Exceptions.DatabaseQueryException if an error occurs during the retrieval process.
     */

    @Override
    public List<HDepartment> findAllDepartments() {
        return getObjectList();
    }
}
