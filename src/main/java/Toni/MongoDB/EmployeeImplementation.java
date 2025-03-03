package Toni.MongoDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import Utils.Constants;
import org.bson.Document;

import com.mongodb.client.MongoCollection;

import DAO.Interfaces.EmployeeDAO;
import Utils.ValidationUtil;

/**
 * Implementation of EmployeeDAO interface for MongoDB.
 */
public class EmployeeImplementation implements EmployeeDAO {
    private final DatabaseManager dbAccess;
    private final Scanner scanner;

    /**
     * Constructor initializes DatabaseManager and Scanner.
     */
    public EmployeeImplementation(DatabaseManager databaseManager) {
        dbAccess = databaseManager;
        scanner = new Scanner(System.in);
    }

    /**
     * Adds a new employee to the database.
     * @param employee The employee to add.
     */
    @Override
    public void addEmployee(Models.Employee employee) {
        int lastId = findAllEmployees().size();
        MongoCollection<Document> equiposCollection = dbAccess.getCollection(Constants.EMPLOYEE_COLLECTION);
        Employee emp = new Employee(employee);
        emp.setDepartmentID(employee.getDepartmentID());
        emp.setEmployeeID(lastId+1);

        equiposCollection.insertOne(emp.toDocument());
    }

    /**
     * Updates an existing employee in the database.
     * @param id The ID of the employee to update.
     * @return An Optional containing the updated employee, if found.
     */
    @Override
    public Optional<Models.Employee> updateEmployee(Object id) {
        Optional<Models.Employee> result = Optional.empty();
        Map<String, String> data = getValuesToUpdate();
        for (String key : data.keySet()) {
            System.out.println(key + " : " + data.get(key));
        }
        Department department = new Department(dbAccess.getDepartamentDoc(Integer.parseInt(data.get("department"))));

        System.out.println(department.getDepartmentName());
        System.out.println(department.getDepartmentID());
        // Ensure the department document is valid
        if (department != null) {
            dbAccess.updateEmployee((int) id, data.get("name"), data.get("position"), department);
            Document employeeDoc = dbAccess.getEmployeeDocById((int) id);
            if (employeeDoc != null) {
                result = Optional.of(new Employee(employeeDoc));
            }
        } else {
            System.out.println("Invalid department ID provided.");
        }

        return result;
    }

    /**
     * Deletes an employee from the database.
     * @param id The ID of the employee to delete.
     * @return True if the employee was deleted, false otherwise.
     */
    @Override
    public boolean deleteEmployee(Object id) {
        boolean result = false;
        Document depDoc = dbAccess.getEmployeeDocById((int)id);

        if (depDoc != null){
            result = dbAccess.deleteEmployee((int)id);
        }
        return result;
    }

    /**
     * Finds an employee by ID.
     * @param id The ID of the employee to find.
     * @return An Optional containing the found employee, if any.
     */
    @Override
    public Optional<Models.Employee> findEmployeeByID(Object id) {
        Optional<Models.Employee> result = Optional.empty();
        Document employeeDoc = dbAccess.getEmployeeDocById((int)id);
        if (employeeDoc != null) {
            result = Optional.of(new Employee(employeeDoc));
        }

        return result;
    }

    /**
     * Finds all employees in the database.
     * @return A list of all employees.
     */
    @Override
    public List<Models.Employee> findAllEmployees() {
        List<Models.Employee> departmentList = new ArrayList<>();
        for (Document doc : dbAccess.getAllEmlpoyees()){
            Employee employee = new Employee(doc);
            departmentList.add(employee);
        }
        return departmentList;
    }

    /**
     * Prompts the user for values to update an employee.
     * @return A map of the values to update.
     */
    private Map<String,String> getValuesToUpdate(){
        boolean setted = false;
        String nameScanned ="";
        String positionScanned ="";
        String departmentScanned ="";
        List<Document> allDep = dbAccess.getAllDepartments();
        do {
            nameScanned = setValidEmployeeName();
            positionScanned = setValidEmployeePosition();
            System.out.println("Type here future Department from this choice using index: ");
            departmentScanned = String.valueOf(setValidDepartmentId(allDep));
            if (ValidationUtil.isValidDepartmentId(Integer.parseInt(departmentScanned)) &&
                ValidationUtil.isValidEmployeeName(nameScanned) &&
                ValidationUtil.isValidEmployeePosition(positionScanned)){
                setted = true;
            }else{
                System.out.println("Some field is not correct. Try again.");
            }
        }while(!setted);
        return Map.of("name",nameScanned,"position",positionScanned, "department",departmentScanned);
    }

    private String getTextScanned(String textToPrint) {
        System.out.println(textToPrint);
        return scanner.next();
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

    private int setValidDepartmentId(List<Document> departmentDocList){
        List<Department> departmentList = new ArrayList<>();
        for (Document doc : departmentDocList){
            departmentList.add(new Department(doc));
        }
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


}
