package Hugo.Menu;

/*import Andy.Hibernate.Database.DatabaseManager;
import Andy.Hibernate.Database.DepartmentImplementation;
import Andy.Hibernate.Database.EmployeeImplementation;*/

/*import Andy.db4o.Database.DatabaseManager;
import Andy.db4o.Database.DepartmentImplementation;
import Andy.db4o.Database.EmployeeImplementation;*/

import Hugo.PostgreSQL.DatabaseManager;
import Hugo.PostgreSQL.DepartmentImplementation;
import Hugo.PostgreSQL.EmployeeImplementation;

/*
import Toni.MongoDB.DatabaseManager;
import Toni.MongoDB.DepartmentImplementation;
import Toni.MongoDB.EmployeeImplementation;
*/

import Models.Department;
import Models.Employee;
import Utils.ValidationUtil;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * The {@code UserInteractions} class handles user interactions for managing departments and employees
 * in a database. It provides methods for adding, updating, deleting, and querying departments and employees
 * through a command-line interface.
 *
 * This class relies on {@link DepartmentImplementation} and {@link EmployeeImplementation} to perform
 * database operations and uses {@link ValidationUtil} to validate user inputs.
 */
public class UserInteractions {

    /**
     * The implementation for managing department-related database operations.
     */
    private DepartmentImplementation department;

    /**
     * The implementation for managing employee-related database operations.
     */
    private EmployeeImplementation employee;

    /**
     * Scanner object for reading user input from the console.
     */
    private static Scanner scan = new Scanner(System.in);

    /**
     * Constructs a new {@code UserInteractions} instance with the specified database manager.
     * Initializes the {@link DepartmentImplementation} and {@link EmployeeImplementation} instances
     * if they are not already initialized.
     *
     * @param dbManager The database manager used to establish connections to the database.
     */
    public UserInteractions(DatabaseManager dbManager) {
        if (department == null) {
            this.department = new DepartmentImplementation(dbManager);
        }
        if (employee == null) {
            this.employee = new EmployeeImplementation(dbManager);
        }
    }

    /**
     * Handles the interaction for adding a new department to the database.
     * Prompts the user for department details, validates the input, and stores the department
     * if it is valid.
     */
    public void addDepartmentInteraction() {
        int id = existDepid(findAllDepartmentInteraction());
        String name = setValidDepName();
        String address = setValidAddress();
        Department departmentInput = new Department(id, name, address);
        if (ValidationUtil.isValidObject(departmentInput, Department.class)) {
            department.addDepartment(departmentInput);
        } else {
            System.out.println("Department is not valid");
        }
    }

    /**
     * Handles the interaction for updating an existing department in the database.
     * Prompts the user to select a department by ID and update its details.
     */
    public void updateDepartmentInteraction() {
        List<Department> departmentList = findAllDepartmentInteraction();
        boolean done = false;
        do {
            int id = setValidDepartmentId(departmentList);
            if (departmentList.stream().noneMatch(department -> department.getDepartmentID() == id)) {
                System.out.println("Department with id: " + id + " not found!");
            } else {
                department.updateDepartment(id);
                done = true;
            }
        } while (!done);
    }

    /**
     * Handles the interaction for deleting a department from the database.
     * Prompts the user to select a department by ID and deletes it if it exists.
     */
    public void deleteDepartmentInteraction() {
        findAllDepartmentInteraction().forEach(System.out::println);
        int id = setValidDepartmentId(findAllDepartmentInteraction());
        if (ValidationUtil.isValidDepartmentId(id)) {
            department.deleteDepartment(id);
        } else {
            System.out.println("Department ID is not valid");
        }
    }

    /**
     * Handles the interaction for finding a department by its ID.
     * Prompts the user to enter a department ID and displays the department details if found.
     */
    public void findDepartmentInteraction() {
        int id = setValidDepartmentId(findAllDepartmentInteraction());
        if (ValidationUtil.isValidDepartmentId(id)) {
            if (department.findDepartmentByID(id).isPresent()) {
                System.out.println(department.findDepartmentByID(id).get());
            }
        } else {
            System.out.println("Department ID is not valid");
        }
    }

    /**
     * Retrieves all departments from the database.
     *
     * @return A list of all departments in the database.
     */
    private List<Department> findAllDepartmentInteraction() {
        return department.findAllDepartments();
    }

    /**
     * Prints all departments in the database to the console.
     */
    public void printAllDepartments() {
        findAllDepartmentInteraction().forEach(System.out::println);
    }

    /**
     * Handles the interaction for adding a new employee to the database.
     * Prompts the user for employee details, validates the input, and stores the employee
     * if it is valid.
     */
    public void addEmployeeInteraction() {
        String name = setValidEmployeeName();
        String position = setValidEmployeePosition();
        int id = setValidDepartmentId(findAllDepartmentInteraction());

        Optional<Department> optionalDepartment = department.findDepartmentByID(id);
        if (optionalDepartment.isPresent()) {
            Department employeeDepartment = optionalDepartment.get();
            Employee employeeInput = new Employee(name, position, id, employeeDepartment);
            System.out.println(employeeInput);
            if (ValidationUtil.isValidObject(employeeInput, Employee.class)) {
                employee.addEmployee(employeeInput);
                //System.out.println("Employee created!");
            } else {
                System.out.println("Employee is not valid");
            }
        } else {
            System.out.println("Object with ID " + id + " could not be found.");
        }
    }

    /**
     * Handles the interaction for updating an existing employee in the database.
     * Prompts the user to select an employee by ID and update their details.
     */
    public void updateEmployeeInteraction() {
        int id = setValidEmployeeId(findAllEmployeeInteraction());
        if (ValidationUtil.isValidEmployeeId(id)) {
            employee.updateEmployee(id);
        } else {
            System.out.println("Employee ID is not valid");
        }
    }

    /**
     * Handles the interaction for deleting an employee from the database.
     * Prompts the user to select an employee by ID and deletes them if they exist.
     */
    public void deleteEmployeeInteraction() {
        findAllEmployeeInteraction().forEach(System.out::println);
        System.out.println("Employee ID to delete: ");
        int id = scan.nextInt();
        scan.nextLine();
        if (ValidationUtil.isValidEmployeeId(id)) {
            if (employee.deleteEmployee(id))
                System.out.println("Employee with id: " + id + " deleted!");
        } else {
            System.out.println("Employee ID is not valid");
        }
    }

    /**
     * Handles the interaction for finding an employee by their ID.
     * Prompts the user to enter an employee ID and displays the employee details if found.
     */
    public void findEmployeeInteraction() {
        //System.out.println("Employee ID to find: ");
        //scan.nextLine();
        int id = setValidEmployeeId(findAllEmployeeInteraction());
        if (ValidationUtil.isValidEmployeeId(id)) {
            employee.findEmployeeByID(id);
        } else {
            System.out.println("Employee ID is not valid");
        }
    }

    /**
     * Retrieves all employees from the database.
     *
     * @return A list of all employees in the database.
     */
    private List<Employee> findAllEmployeeInteraction() {
        return employee.findAllEmployees();
    }

    /**
     * Prints all employees in the database to the console.
     */
    public void printAllEmployees() {
        findAllEmployeeInteraction().forEach(System.out::println);
    }

    /**
     * Prompts the user for input and returns the entered text.
     *
     * @param textToPrint The message to display to the user.
     * @return The text entered by the user.
     */
    private String getTextScanned(String textToPrint) {
        System.out.println(textToPrint);
        return scan.next();
    }

    /**
     * Prompts the user to enter a valid employee name.
     *
     * @return A valid employee name entered by the user.
     */
    private String setValidEmployeeName() {
        String name;
        boolean done = false;
        do {
            name = getTextScanned("Set employee Name: ");
            if (ValidationUtil.isValidEmployeeName(name)) {
                done = true;
            } else {
                System.out.println("Employee name must be Valid");
            }
        } while (!done);
        return name;
    }

    /**
     * Prompts the user to enter a valid employee position.
     *
     * @return A valid employee position entered by the user.
     */
    private String setValidEmployeePosition() {
        String position;
        boolean done = false;
        do {
            position = getTextScanned("Set employee position: ");
            if (ValidationUtil.isValidEmployeePosition(position)) {
                done = true;
            } else {
                System.out.println("Employee position must be Valid");
            }
        } while (!done);
        return position;
    }

    /**
     * Prompts the user to enter a valid employee ID from the list of employees.
     *
     * @param employeeList The list of employees to validate the ID against.
     * @return A valid employee ID entered by the user.
     */
    private int setValidEmployeeId(List<Employee> employeeList) {
        int id;
        boolean done = false;
        do {
            //employeeList.forEach(System.out::println);
            id = Integer.parseInt(setValidEmpId());
            int finalId = id;
            if (!employeeList.stream().noneMatch(employee -> employee.getEmployeeID() == finalId)) {
                done = true;
            } else {
                System.out.println("Employee with id: " + id + " not found!");
            }
        } while (!done);
        return id;
    }

    /**
     * Prompts the user to enter a valid employee ID.
     *
     * @return A valid employee ID entered by the user.
     */
    private String setValidEmpId() {
        String id;
        boolean done = false;
        do {
            id = getTextScanned("Set employee ID: ");
            try {
                if (ValidationUtil.isValidEmployeeId(Integer.parseInt(id))) {
                    done = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Employee ID must be a number");
            }
        } while (!done);
        return id;
    }

    /**
     * Prompts the user to enter a valid department ID.
     *
     * @return A valid department ID entered by the user.
     */
    private String setValidDepId() {
        String id;
        boolean done = false;
        do {
            id = getTextScanned("Set department ID: ");
            try {
                if (ValidationUtil.isValidDepartmentId(Integer.parseInt(id))) {
                    done = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Department ID must be a number");
            }
        } while (!done);
        return id;
    }

    /**
     * Prompts the user to enter a valid department name.
     *
     * @return A valid department name entered by the user.
     */
    private String setValidDepName() {
        String name;
        boolean done = false;
        do {
            name = getTextScanned("Set department Name: ");
            try {
                if (ValidationUtil.isValidDepartmentName(name)) {
                    done = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Department name must be Valid");
            }
        } while (!done);
        return name;
    }

    /**
     * Prompts the user to enter a valid department address.
     *
     * @return A valid department address entered by the user.
     */
    private String setValidAddress() {
        String address;
        boolean done = false;
        do {
            address = getTextScanned("Set department address: ");
            try {
                if (!ValidationUtil.isValidDepartmentName(address)) {
                    done = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Department Address must be Valid");
            }
        } while (!done);
        return address;
    }

    /**
     * Prompts the user to enter a valid department ID from the list of departments.
     *
     * @param departmentList The list of departments to validate the ID against.
     * @return A valid department ID entered by the user.
     */
    private int setValidDepartmentId(List<Department> departmentList) {
        int id;
        boolean done = false;
        do {
            //departmentList.forEach(System.out::println);
            id = Integer.parseInt(setValidDepId());
            int finalId = id;
            if (departmentList.stream().noneMatch(department -> department.getDepartmentID() == finalId)) {
                System.out.println("Department with id: " + id + " not found!");
            } else {
                done = true;
            }
        } while (!done);
        return id;
    }

    /**
     * Prompts the user to enter a department ID and ensures it does not already exist in the database.
     *
     * @param departmentList The list of departments to validate the ID against.
     * @return A unique department ID entered by the user.
     */
    private int existDepid(List<Department> departmentList) {
        int id;
        boolean done = false;
        do {
            id = Integer.parseInt(setValidDepId());
            int finalId = id;
            if (departmentList.stream().noneMatch(department -> department.getDepartmentID() == finalId)) {
                System.out.println("Department with id: " + id + " valid");
                done = true;
            } else {
                System.out.println("Department with id: " + id + " not valid!");
            }
        } while (!done);
        return id;
    }

    /**
     * Prompts the user to enter an ID and returns the entered value.
     *
     * @param message The message to display to the user.
     * @return The ID entered by the user.
     */
    private int askForID(String message) {
        try {
            System.out.println(message);
            int id = scan.nextInt();
            scan.nextLine();
            return id;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        }
        return 0;
    }

    /**
     * Prompts the user to enter text and returns the entered value.
     *
     * @param message The message to display to the user.
     * @return The text entered by the user.
     */
    private String askForText(String message) {
        System.out.println(message);
        return scan.nextLine();
    }
}
