package Hugo.PostgreSQL;

import DAO.Interfaces.EmployeeDAO;
import Models.Employee;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link EmployeeDAO} interface.
 * Provides functionality to perform CRUD operations on {@link Employee} objects in a PostgreSQL database.
 */
public class EmployeeImplementation extends PostgreBaseImplementation<Employee> implements EmployeeDAO {
    // Java Reflection to extract the name of the primary key (PK) field
    private final String primaryFieldName = Employee.class.getDeclaredFields()[0].getName();

    /**
     * Constructor for the EmployeeImplementation class.
     * Initializes the implementation with a reference to the {@link DatabaseManager}.
     *
     * @param dbManager the database manager used for managing the connection to the database
     */
    public EmployeeImplementation(DatabaseManager dbManager) {
        super(Employee.class, dbManager);
    }

    /**
     * Adds a new employee to the database.
     *
     * @param employee the {@link Employee} object to be added
     */
    @Override
    public void addEmployee(Employee employee) {
        if (storePostgreEmployee(employee)) {
            System.out.println("Employee successfully added");
        } else {
            System.out.println("Employee could not be added");
        }
    }

    /**
     * Updates an existing employee in the database by its ID.
     *
     * @param id the ID of the employee to be updated
     * @return an {@link Optional} containing the updated {@link Employee} if successful, or an empty {@link Optional} if not
     */
    @Override
    public Optional<Employee> updateEmployee(Object id) {
        return updatePostgreEmployee((Integer) id);
    }

    /**
     * Deletes an employee from the database by its ID.
     *
     * @param id the ID of the employee to be deleted
     * @return true if the employee was successfully deleted, false otherwise
     */
    @Override
    public boolean deleteEmployee(Object id) {
        Optional<Employee> employeeOptional = getEmployee((Integer) id);
        if (employeeOptional.isEmpty()) {
            System.out.println("Employee " + id + " not found");
            return false;
        }

        deletePostgreEmployee(employeeOptional.get());
        return true;
    }

    /**
     * Finds an employee by its ID.
     *
     * @param id the ID of the employee to be found
     * @return an {@link Optional} containing the {@link Employee} if found, or an empty {@link Optional} if not
     */
    @Override
    public Optional<Employee> findEmployeeByID(Object id) {
        Optional<Employee> employeeOptional = getEmployee((Integer) id);
        if (employeeOptional.isEmpty()) {
            System.out.println("Employee " + id + " not found");
            employeeOptional = Optional.empty();
        } else {
            System.out.println(employeeOptional.get().toString());
        }
        return employeeOptional;
    }

    /**
     * Retrieves all employees from the database.
     *
     * @return a {@link List} containing all {@link Employee} objects in the database
     */
    @Override
    public List<Employee> findAllEmployees() {
        return getAllPostgreEmployees();
    }
}
