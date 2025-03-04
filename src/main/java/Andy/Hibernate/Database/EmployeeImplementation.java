package Andy.Hibernate.Database;

import Andy.Hibernate.Models.HDepartment;
import Andy.Hibernate.Models.HEmployee;
import DAO.Interfaces.EmployeeDAO;
import Models.Employee;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link EmployeeDAO} using Hibernate JPA.
 * <p>
 * The class extends {@link HibernateBaseImplementation} to provide CRUD operations for the
 * {@link HEmployee} entity.
 */

public class EmployeeImplementation extends HibernateBaseImplementation<HEmployee> implements EmployeeDAO {

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
     * @param employee The {@link Employee} to be added.
     * @throws Exceptions.DatabaseInsertException if an error occurs during insertion.
     */

    @Override
    public void addEmployee(Models.Employee employee) {
        HEmployee hEmployee = new HEmployee(employee);

        if (storeObject(hEmployee)) {
            System.out.println("Employee " + employee.getEmployeeName() + " was added to the Hibernate JPA DB");
        } else {
            System.out.println("Employee " + employee.getEmployeeName() + " could not be added to the Hibernate JPA DB");
        }
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
    public Optional<Models.Employee> updateEmployee(Object id) {
        Optional<HEmployee> hEmployeeOptional = updateObject(id);

        return hEmployeeOptional.map(hEmp -> new Models.Employee(
                hEmp.getEmployeeName(),
                hEmp.getEmployeePosition(),
                hEmp.getDepartment().getID(),
                new Models.Department(
                        hEmp.getDepartment().getID(),
                        hEmp.getDepartment().getDepartmentName(),
                        hEmp.getDepartment().getDepartmentAddress()
                )
        ));
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
    public Optional<Models.Employee> findEmployeeByID(Object id) {
        Optional<HEmployee> hEmployeeOptional = getObject(id);

        return hEmployeeOptional.map(hEmp -> new Models.Employee(
                hEmp.getEmployeeName(),
                hEmp.getEmployeePosition(),
                hEmp.getDepartment().getID(),
                new Models.Department(
                        hEmp.getDepartment().getID(),
                        hEmp.getDepartment().getDepartmentName(),
                        hEmp.getDepartment().getDepartmentAddress()
                )
        ));
    }

    /**
     * Retrieves a {@code List<HEmployee>} containing all the employees in the Hibernate JPA database.
     *
     * @return A {@code List<HEmployee>} containing all {@link HEmployee} entities.
     * @throws Exceptions.DatabaseQueryException if an error occurs during the retrieval process.
     */

    @Override
    public List<Models.Employee> findAllEmployees() {
        List<HEmployee> hEmployees = getObjectList();

        return hEmployees.stream()
                .map(hEmp -> new Models.Employee(
                        hEmp.getID(),
                        hEmp.getEmployeeName(),
                        hEmp.getEmployeePosition(),
                        hEmp.getDepartment().getID(),
                        new Models.Department(
                                hEmp.getDepartment().getID(),
                                hEmp.getDepartment().getDepartmentName(),
                                hEmp.getDepartment().getDepartmentAddress()
                        )
                ))
                .toList();
    }
}
