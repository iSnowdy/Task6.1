package Andy.db4o.Database;

import DAO.Interfaces.EmployeeDAO;
import Exceptions.DatabaseDeleteException;
import Exceptions.DatabaseInsertException;
import Models.Employee;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link EmployeeDAO} using db4o.
 * Provides CRUD operations for managing {@link Employee} entities in the db4o database.
 */

public class EmployeeImplementation extends DB4OBaseImplementation<Employee> implements EmployeeDAO {
    // Java Reflection to extract the name of the PK field
    private final String primaryFieldName = Employee.class.getDeclaredFields()[0].getName();

    /**
     * Constructs an {@code EmployeeImplementation} instance while specifying the class type for the db4o operations and
     * injects the {@link DatabaseManager} for DB4O operations into {@link DB4OBaseImplementation},
     * inside {@link DB4OBaseImplementation}.
     */

    public EmployeeImplementation(DatabaseManager dbManager) {
        super(Employee.class, dbManager);
    }

    @Override
    public String getPrimaryKeyFieldName() {
        return primaryFieldName;
    }

    /**
     * Adds a new employee to the database.
     * If the employee already exists, the insertion is ignored.
     *
     * @param employee The employee object to be added.
     * @throws DatabaseInsertException if an error occurs during the insertion.
     */

    @Override
    public void addEmployee(Employee employee) {
        if (storeObject(employee)) System.out.println("Employee " + employee.getEmployeeID() + " successfully added");
        else System.out.println("Employee " + employee.getEmployeeID() + " could not be added");
    }

    /**
     * Updates an existing employee's information in the database.
     * The user will be prompted to select which field to modify.
     *
     * @param id The unique identifier (ID, PK) of the employee to be updated.
     * @return An {@code Optional<Employee>} containing the updated employee, or empty if not found.
     * @throws Exceptions.DatabaseQueryException if an error occurs during the update.
     */

    @Override
    public Optional<Employee> updateEmployee(Object id) {
        return updateObject(id);
    }

    /**
     * Deletes an employee from the database.
     *
     * @param id The unique identifier of the employee to be deleted.
     * @return {@code true} if the deletion was successful, {@code false} if the employee was not found.
     * @throws DatabaseDeleteException if an error occurs during the deletion.
     */

    @Override
    public boolean deleteEmployee(Object id) {
        Optional<Employee> employeeOptional = getObject(id);

        if (employeeOptional.isEmpty()) {
            System.out.println("Employee " + id + " could not be found");
            return false;
        }

        deleteObject(employeeOptional.get());
        System.out.println("Employee " + id + " successfully deleted");
        return true;
    }

    /**
     * Finds an employee by their unique ID.
     *
     * @param id The unique identifier of the employee.
     * @return An {@code Optional<Employee>} containing the found employee, or empty if not found.
     * @throws Exceptions.DatabaseQueryException if an error occurs during the querying process.
     */

    @Override
    public Optional<Employee> findEmployeeByID(Object id) {
        Optional<Employee> employeeOptional = getObject(id);
        if (employeeOptional.isEmpty()) {
            System.out.println("Employee " + id + " could not be found");
            return Optional.empty();
        }
        System.out.println("Employee " + id + " found");
        return employeeOptional;
    }

    /**
     * Retrieves all employees stored in the database.
     *
     * @return A {@code List<Employee>} containing all the employees in the database.
     * @throws Exceptions.DatabaseQueryException if an error occurs during the querying process.
     */

    @Override
    public List<Employee> findAllEmployees() {
        return getObjectList();
    }
}
