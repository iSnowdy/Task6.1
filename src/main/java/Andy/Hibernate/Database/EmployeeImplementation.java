package Andy.Hibernate.Database;

import Andy.Hibernate.Models.HEmployee;
import DAO.Interfaces.HibernateInterfaces.HEmployeeDAO;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link HEmployeeDAO} using Hibernate JPA.
 * <p>
 * The class extends {@link HibernateBaseImplementation} to provide CRUD operations for the
 * {@link HEmployee} entity.
 */

public class EmployeeImplementation extends HibernateBaseImplementation<HEmployee> implements HEmployeeDAO {

    /**
     * Constructs a new {@link EmployeeImplementation} instance and injects the {@link DatabaseManager} for Hibernate
     * into the BaseImplementation.
     * <p>
     * It also initializes the parent class with {@link HEmployee} to specify the entity type since it is using
     * generics.
     */

    public EmployeeImplementation(final DatabaseManager dbManager) {
        super(HEmployee.class, dbManager);
    }

    /**
     * Adds a new employee to the Hibernate JPA database.
     *
     * @param employee The {@link HEmployee} to be added.
     * @throws Exceptions.DatabaseInsertException if an error occurs during insertion.
     */

    @Override
    public void addEmployee(HEmployee employee) {
        if (storeObject(employee))
            System.out.println("Employee ID " + employee.getID() + " was added to the Hibernate JPA DB");
        else System.out.println("Employee ID " + employee.getID() + "could not be added to the Hibernate JPA DB");
    }

    /**
     * Updates an existing employee in the database.
     * <p>
     * It will also prompt the user to select a field for modification and then updates it.
     *
     * @param id The ID of the employee to update.
     * @return An {@code Optional<HEmployee>} containing the updated employee, or {@code Optional.empty()} if it
     * was not found in the database.
     * @throws Exceptions.DatabaseQueryException if an error occurs during modification.
     */

    @Override
    public Optional<HEmployee> updateEmployee(Object id) {
        return updateObject(id);
    }

    /**
     * Deletes an employee from the Hibernate JPA database.
     *
     * @param id The ID of the employee to be deleted.
     * @return {@code true} if the deletion process was successful, {@code false} otherwise.
     * @throws Exceptions.DatabaseDeleteException if an error occurs during the deletion process.
     */

    @Override
    public boolean deleteEmployee(Object id) {
        Optional<HEmployee> employeeOptional = getObject(id);
        if (employeeOptional.isEmpty()) {
            System.out.println("Employee ID " + id + " not found in the Hibernate JPA DB");
            return false;
        }

        deleteObject(employeeOptional.get());
        System.out.println("Employee ID " + id + " successfully deleted from the Hibernate JPA DB");
        return true;
    }

    /**
     * Finds an employee in the database given their ID.
     *
     * @param id The ID of the employee to be searched for.
     * @return An {@code Optional<HEmployee>} if the employee is found, {@code Optional.empty()} otherwise.
     * @throws Exceptions.DatabaseQueryException if an error occurs during the search process.
     */

    @Override
    public Optional<HEmployee> findEmployeeByID(Object id) {
        Optional<HEmployee> employeeOptional = getObject(id);
        if (employeeOptional.isEmpty()) {
            System.out.println("Employee ID " + id + " not found in the Hibernate JPA DB");
            return Optional.empty();
        }

        System.out.println("Employee ID " + id + " found in the Hibernate JPA DB");
        return employeeOptional;
    }

    /**
     * Retrieves a {@code List<HEmployee>} containing all the employees in the Hibernate JPA database.
     *
     * @return A {@code List<HEmployee>} containing all {@link HEmployee} entities.
     * @throws Exceptions.DatabaseQueryException if an error occurs during the retrieval process.
     */

    @Override
    public List<HEmployee> findAllEmployees() {
        return getObjectList();
    }
}
