package Hugo.Menu;

//import Andy.db4o.Database.DatabaseManager;
//import Andy.db4o.Database.DepartmentImplementation;
//import Andy.db4o.Database.EmployeeImplementation;

//import Hugo.PostgreSQL.DatabaseManager;
//import Hugo.PostgreSQL.DepartmentImplementation;
//import Hugo.PostgreSQL.EmployeeImplementation;

import Toni.MongoDB.DatabaseManager;
import Toni.MongoDB.DepartmentImplementation;
import Toni.MongoDB.EmployeeImplementation;

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
        int id = existDepid(findAllDepartmentInteraction());
        String name = setValidDepName();
        String address = setValidAddress();
        Department departmentInput = new Department(id, name, address);
        if(ValidationUtil.isValidObject(departmentInput, Department.class)) {
            department.addDepartment(departmentInput);
            System.out.println("Department created!");
        } else {
            System.out.println("Department is not valid");
        }
    }

    public void updateDepartmentInteraction() {
        List<Department> departmentList = findAllDepartmentInteraction();
        boolean done = false;
        do {
//            departmentList.forEach(System.out::println);
            int id = setValidDepartmentId(departmentList);
            if (departmentList.stream().noneMatch(department -> department.getDepartmentID() == id)) {
                System.out.println("Department with id: "+id+" not found!");
            }
            else {
                department.updateDepartment(id);
                done = true;
            }
        } while (!done);
    }

    public void deleteDepartmentInteraction() {
        findAllDepartmentInteraction().forEach(System.out::println);
        int id = setValidDepartmentId(findAllDepartmentInteraction());
        if (ValidationUtil.isValidDepartmentId(id)) {
            department.deleteDepartment(id);
            System.out.println("Department with id: "+id+" deleted!");
        } else {
            System.out.println("Department ID is not valid");
        }
    }

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

    private List<Department> findAllDepartmentInteraction() {
        return department.findAllDepartments();
    }

    public void printAllDepartments() {
        findAllDepartmentInteraction().forEach(System.out::println);
    }

    public void addEmployeeInteraction() {
        String name = setValidEmployeeName();
        String position = setValidEmployeePosition();
        int id = setValidDepartmentId(findAllDepartmentInteraction());

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
        int id = setValidEmployeeId(findAllEmployeeInteraction());
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
        scan.nextLine();
        int id = setValidEmployeeId(findAllEmployeeInteraction());
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

    private String getTextScanned(String textToPrint) {
        System.out.println(textToPrint);
        return scan.next();
    }


    private String setValidEmployeeName(){
        String name;
        boolean done = false;
        do {
            name = getTextScanned("Set employee Name: ");
            if (ValidationUtil.isValidEmployeeName(name)) {
                done = true;
            }else{
                System.out.println("Employee name must be Valid");
            }
        }while (!done);
        return name;
    }
    private String setValidEmployeePosition(){
        String position;
        boolean done = false;
        do {
            position = getTextScanned("Set employee position: ");
            if (ValidationUtil.isValidEmployeePosition(position)) {
                done = true;
            }else{
                System.out.println("Employee position must be Valid");
            }
        }while (!done);
        return position;
    }
    private int setValidEmployeeId(List<Employee> employeeList){
        int id;
        boolean done = false;
        do {
            id = Integer.parseInt(setValidEmpId());
            int finalId = id;
            if (!employeeList.stream().noneMatch(employee -> employee.getEmployeeID() == finalId)) {
                done = true;
            }else{
                System.out.println("Department with id: "+id+" not found!");
            }
        }while (!done);
        return id;
    }

    private String setValidEmpId(){
        String id;
        boolean done = false;
        do {
            id = getTextScanned("Set employee ID: ");
            try {
                if (ValidationUtil.isValidDepartmentId(Integer.parseInt(id))) {
                    done = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Employee ID must be a number");
            }
        }while (!done);
        return id;
    }

    private String setValidDepId(){
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
        }while (!done);
        return id;
    }

    private String setValidDepName(){
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
        }while (!done);
        return name;
    }

    private String setValidAddress(){
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
        }while (!done);
        return address;
    }

    private int setValidDepartmentId(List<Department> departmentList){
        int id;
        boolean done = false;
        do {
            departmentList.forEach(System.out::println);
            id = Integer.parseInt(setValidDepId());
            int finalId = id;
            if (departmentList.stream().noneMatch(department -> department.getDepartmentID() == finalId)) {
                System.out.println("Department with id: "+id+" not found!");
            }
            else {
                done = true;
            }
        } while (!done);
        return id;
    }

    private int existDepid(List<Department> departmentList){
        int id;
        boolean done = false;
        do {
            departmentList.forEach(System.out::println);
            id = Integer.parseInt(setValidDepId());
            int finalId = id;
            if (departmentList.stream().noneMatch(department -> department.getDepartmentID() == finalId)) {
                System.out.println("Department with id: "+id+" valid");
                done = true;
            }
            else {
                System.out.println("Department with id: "+id+" not valid!");
            }
        } while (!done);
        return id;
    }
}
