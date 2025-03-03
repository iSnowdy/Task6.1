package Hugo.Menu;

/*import Andy.db4o.Database.DatabaseManager;
import Andy.db4o.Database.DepartmentImplementation;
import Andy.db4o.Database.EmployeeImplementation;*/

import Hugo.PostgreSQL.DatabaseManager;
import Hugo.PostgreSQL.DepartmentImplementation;
import Hugo.PostgreSQL.EmployeeImplementation;

import Models.Department;
import Models.Employee;
import Utils.ValidationUtil;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserInteractions {

    private DepartmentImplementation department;
    private EmployeeImplementation employee;

    public UserInteractions(DatabaseManager dbManager) {
        if (department == null) {
            this.department = new DepartmentImplementation(dbManager);
        }
        if (employee == null) {
            this.employee = new EmployeeImplementation(dbManager);
        }
    }

    private static Scanner scan = new Scanner(System.in);

    public void addDepartmentInteraction() {
        System.out.println("Set department ID: ");
        int id = scan.nextInt();
        scan.nextLine();
        System.out.println("Set department name: ");
        String name = scan.nextLine();
        System.out.println("Set department address: ");
        String address = scan.nextLine();

        Department departmentInput = new Department(id, name, address);
        if(ValidationUtil.isValidObject(departmentInput, Department.class)) {
            department.addDepartment(departmentInput);
            System.out.println("Department created!");
        } else {
            System.out.println("Department is not valid");
        }
    }

    public void updateDepartmentInteraction() {
        findAllDepartmentInteraction().forEach(System.out::println);
        System.out.println("Set department ID: ");
        int id = scan.nextInt();
        scan.nextLine();
        if (ValidationUtil.isValidDepartmentId(id)) {
            department.updateDepartment(id);
        } else {
            System.out.println("Department ID is not valid");
        }
    }

    public void deleteDepartmentInteraction() {
        findAllDepartmentInteraction().forEach(System.out::println);
        System.out.println("Set department ID: ");
        int id = scan.nextInt();
        scan.nextLine();
        if (ValidationUtil.isValidDepartmentId(id)) {
            department.deleteDepartment(id);
        } else {
            System.out.println("Department ID is not valid");
        }
    }

    public void findDepartmentInteraction() {
        System.out.println("Set department ID: ");
        int id = scan.nextInt();
        scan.nextLine();
        if (ValidationUtil.isValidDepartmentId(id)) {
            if (department.findDepartmentByID(id).isPresent()) {
                System.out.println(department.findDepartmentByID(id).get());
            }
        } else {
            System.out.println("Department ID is not valid");
        }
    }

    private List<Department> findAllDepartmentInteraction() {
        return department.findAllDepartments();
    }

    public void printAllDepartments() {
        findAllDepartmentInteraction().forEach(System.out::println);
    }

    public void addEmployeeInteraction() {
        System.out.println("Set employee name: ");
        String name = scan.nextLine();
        System.out.println("Set employee position: ");
        String position = scan.nextLine();
        System.out.println("Set employee department ID: ");
        int id = scan.nextInt();
        scan.nextLine();

        Optional<Department> optionalDepartment = department.findDepartmentByID(id);
        if (optionalDepartment.isPresent()) {
            Department employeeDepartment = optionalDepartment.get();
            Employee employeeInput = new Employee(name, position, id, employeeDepartment);
            System.out.println(employeeInput);
            if(ValidationUtil.isValidObject(employeeInput, Employee.class)) {
                employee.addEmployee(employeeInput);
                System.out.println("Employee created!");
            } else {
                System.out.println("Employee is not valid");
            }
        } else {
            System.out.println("Object with ID " + id + " could not be found.");
        }
    }

    public void updateEmployeeInteraction() {
        findAllEmployeeInteraction().forEach(System.out::println);
        System.out.println("Set employee ID for update: ");
        int id = scan.nextInt();
        scan.nextLine();
        if (ValidationUtil.isValidEmployeeId(id)) {
            employee.updateEmployee(id);
        } else {
            System.out.println("Employee ID is not valid");
        }
    }

    public void deleteEmployeeInteraction() {
        findAllEmployeeInteraction().forEach(System.out::println);
        System.out.println("Set employee ID for update: ");
        int id = scan.nextInt();
        scan.nextLine();
        if (ValidationUtil.isValidEmployeeId(id)) {
            employee.deleteEmployee(id);
            System.out.println("Employee with id: "+id+" deleted!");
        } else {
            System.out.println("Employee ID is not valid");
        }
    }

    public void findEmployeeInteraction() {
        System.out.println("Set employee ID for update: ");
        int id = scan.nextInt();
        scan.nextLine();
        if (ValidationUtil.isValidEmployeeId(id)) {
            employee.findEmployeeByID(id);
        } else {
            System.out.println("Employee ID is not valid");
        }
    }

    private List<Employee> findAllEmployeeInteraction() {
        return employee.findAllEmployees();
    }

    public void printAllEmployees() {
        findAllEmployeeInteraction().forEach(System.out::println);
    }
}
