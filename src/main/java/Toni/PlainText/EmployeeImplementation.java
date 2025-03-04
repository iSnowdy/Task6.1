package Toni.PlainText;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import DAO.Interfaces.EmployeeDAO;
import Models.Department;
import Models.Employee;
import Utils.Constants;

/**
 * Implementation of the EmployeeDAO interface for managing Employee data.
 */
public class EmployeeImplementation implements EmployeeDAO {
    private final DatabaseManager dbManager;
    private final File file;

    /**
     * Constructor that initializes the DatabaseManager and file.
     */
    public EmployeeImplementation(DatabaseManager databaseManager) {
        dbManager = databaseManager;
        file = new File(Constants.FILE_NAME);
        dbManager.instanceData(file);
    }

    /**
     * Adds a new employee to the database.
     * 
     * @param employee The employee to be added.
     */
    @Override
    public void addEmployee(Employee employee) {
        dbManager.saveObject(employee, file);
    }

    /**
     * Updates an existing employee's information.
     * 
     * @param id The ID of the employee to be updated.
     * @return An Optional containing the updated employee if successful, or an empty Optional if not.
     */
    @Override
    public Optional<Employee> updateEmployee(Object id) {
        Employee employee = dbManager.getEmployeeByID((int) id);
        if (employee != null) {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Enter new employee name:");
                String name = scanner.nextLine();
                System.out.println("Enter new employee position:");
                String position = scanner.nextLine();
                System.out.println("Enter new department ID:");
                int departmentID = scanner.nextInt();
                Department department = dbManager.getDepartmentById(departmentID);
                if (department != null) {
                    employee.setEmployeeName(name);
                    employee.setEmployeePosition(position);
                    employee.setDepartment(department);
                    dbManager.saveObject(employee, file);
                    return Optional.of(employee);
                } else {
                    System.out.println("Department not found.");
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Deletes an employee from the database.
     * 
     * @param id The ID of the employee to be deleted.
     * @return true if the employee was successfully deleted, false otherwise.
     */
    @Override
    public boolean deleteEmployee(Object id) {
        Employee employee = dbManager.getEmployeeByID((int) id);
        boolean result = false;
        if (employee != null) {
            dbManager.deleteEmployee((int) id, file);
            result = true;
        }
        return result;
    }

    /**
     * Finds an employee by their ID.
     * 
     * @param id The ID of the employee to be found.
     * @return An Optional containing the found employee, or an empty Optional if not found.
     */
    @Override
    public Optional<Employee> findEmployeeByID(Object id) {
        Employee employee = dbManager.getEmployeeByID((int) id);
        System.out.println(employee);
        return Optional.ofNullable(employee);
    }

    /**
     * Retrieves a list of all employees.
     * 
     * @return A list of all employees.
     */
    @Override
    public List<Employee> findAllEmployees() {
        return dbManager.getEmployeeList();
    }
}