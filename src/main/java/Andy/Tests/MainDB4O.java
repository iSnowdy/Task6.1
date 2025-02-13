package Andy.Tests;

import Andy.db4o.Database.DatabaseManager;
import Andy.db4o.Database.DepartmentImplementation;
import Andy.db4o.Database.EmployeeImplementation;
import Exceptions.DatabaseClosingException;
import Exceptions.DatabaseOpeningException;
import Exceptions.DatabaseDeleteException;
import Exceptions.DatabaseInsertException;
import Exceptions.DatabaseQueryException;
import Models.Department;
import Models.Employee;
import Models.Enum.DepartmentNames;
import Models.Enum.EmployeePosition;

import java.util.List;

public class MainDB4O {
    private static DatabaseManager databaseManager;
    private static DepartmentImplementation departmentDAO;
    private static EmployeeImplementation employeeDAO;

    public static void main(String[] args) {
        System.out.println("Testing mainDB4O...");

        databaseManager = new DatabaseManager();

        openDatabase();

        departmentDAO = new DepartmentImplementation();
        employeeDAO = new EmployeeImplementation();

        insertData();
        queryData();
        updateData();
        deleteData();
        closeDatabase();
    }

    private static void openDatabase() {
        System.out.println("\nOpening DB4O...");
        try {
            databaseManager.openDB();
            if (DatabaseManager.db4oContainer == null) {
                throw new RuntimeException("DatabaseManager was not initialized correctly");
            }
            System.out.println("Database opened successfully!");
        } catch (DatabaseOpeningException e) {
            System.out.println("Error opening database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void insertData() {
        System.out.println("\nPreparing to INSERT...");

        try {
            // Departments
            Department departmentContabilidad = new Department(10, DepartmentNames.CONTABILIDAD.getDepartmentNameAsString(), "Madrid");
            Department departmentMarketing = new Department(20, DepartmentNames.MARKETING.getDepartmentNameAsString(), "Barcelona");
            Department departmentVentas = new Department(30, DepartmentNames.VENTAS.getDepartmentNameAsString(), "Alicante");
            Department departmentLogistica = new Department(40, DepartmentNames.LOGISTICA.getDepartmentNameAsString(), "Valencia");

            departmentDAO.addDepartment(departmentContabilidad);
            departmentDAO.addDepartment(departmentMarketing);
            departmentDAO.addDepartment(departmentVentas);
            departmentDAO.addDepartment(departmentLogistica);

            // Employees
            Employee employeeGarcia = new Employee(1, "García", EmployeePosition.DEPENDIENTE.getEmployeePositionAsString(), 20, departmentContabilidad);
            Employee employeeLopez = new Employee(2, "López", EmployeePosition.VENDEDOR.getEmployeePositionAsString(), 30, departmentVentas);
            Employee employeePerez = new Employee(3, "Pérez", EmployeePosition.VENDEDOR.getEmployeePositionAsString(), 30, departmentVentas);
            Employee employeeGomez = new Employee(4, "Gómez", EmployeePosition.RESPONSABLE.getEmployeePositionAsString(), 20, departmentMarketing);

            employeeDAO.addEmployee(employeeGarcia);
            employeeDAO.addEmployee(employeeLopez);
            employeeDAO.addEmployee(employeePerez);
            employeeDAO.addEmployee(employeeGomez);

            System.out.println("Insert completed successfully!");
        } catch (DatabaseInsertException e) {
            System.out.println("Error inserting data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void queryData() {
        System.out.println("\nFetching Data...");
        try {
            System.out.println("---- ALL DEPARTMENTS ----");
            List<Department> departmentList = departmentDAO.findAllDepartments();
            System.out.println("Size of departments: " + departmentList.size());
            departmentList.forEach(System.out::println);

            System.out.println("---- ALL EMPLOYEES ----");
            List<Employee> employeeList = employeeDAO.findAllEmployees();
            System.out.println("Size of employees: " + employeeList.size());
            employeeList.forEach(System.out::println);

            System.out.println("Finding Department By ID:");
            System.out.println(departmentDAO.findDepartmentByID(10).orElseThrow(() -> new DatabaseQueryException("Department not found")));

            System.out.println("Finding Employee By ID:");
            System.out.println(employeeDAO.findEmployeeByID(1).orElseThrow(() -> new DatabaseQueryException("Employee not found")));

        } catch (DatabaseQueryException e) {
            System.out.println("Error querying data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void updateData() {
        System.out.println("\nModifying Data...");

        try {
            System.out.println("Modifying Department By ID:");
            departmentDAO.updateDepartment(20);
            System.out.println("After modifying Department...");
            System.out.println(departmentDAO.findDepartmentByID(10).orElseThrow(() -> new DatabaseQueryException("Department not found")));

            System.out.println("Modifying Employee By ID:");
            employeeDAO.updateEmployee(1);
            System.out.println("After modifying Employee...");
            System.out.println(employeeDAO.findEmployeeByID(1).orElseThrow(() -> new DatabaseQueryException("Employee not found")));

        } catch (DatabaseQueryException e) {
            System.out.println("Error updating data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void deleteData() {
        System.out.println("\nDeleting Data...");

        try {
            System.out.println("Deleting Department By ID:");
            departmentDAO.deleteDepartment(10);
            System.out.println("After deleting Department...");
            System.out.println(departmentDAO.findDepartmentByID(10));

            System.out.println("Deleting Employee By ID:");
            employeeDAO.deleteEmployee(1);
            System.out.println("After deleting Employee...");
            System.out.println(employeeDAO.findEmployeeByID(1));

        } catch (DatabaseDeleteException e) {
            System.out.println("Error deleting data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void closeDatabase() {
        System.out.println("\nClosing DB4O...");
        try {
            databaseManager.closeDB();
            System.out.println("Database closed successfully!");
        } catch (DatabaseClosingException e) {
            System.out.println("Error closing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
