package Toni.MongoDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import DAO.Interfaces.EmployeeDAO;
import Utils.ValidationUtil;

/**
 * Implementation of EmployeeDAO interface for MongoDB.
 */
public class EmployeeImplementation implements EmployeeDAO {
    private DatabaseManager dbAccess;
    private final Scanner scanner;

    /**
     * Constructor initializes DatabaseManager and Scanner.
     */
    public EmployeeImplementation() {
        dbAccess = new DatabaseManager();
        scanner = new Scanner(System.in);
    }

    /**
     * Adds a new employee to the database.
     * @param employee The employee to add.
     */
    @Override
    public void addEmployee(Models.Employee employee) {
        MongoCollection<Document> equiposCollection = dbAccess.getCollection("departamento");
        Employee dep = new Employee(employee);
        equiposCollection.insertOne(dep.toDocument());
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
        Department department = new Department(dbAccess.getDepartamentDoc(Integer.parseInt(data.get("department"))));
        dbAccess.updateEmployee((int)id, data.get("name"), data.get("position"),department);
        Document employeeDoc = dbAccess.getEmployeeDocById((int)id);
        if (employeeDoc != null){
            result = Optional.of(new Employee(employeeDoc));
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
            System.out.println("%n Text can be empty if won't want to be update");
            System.out.println("Type here the new name: ");
            nameScanned = scanner.next();
            System.out.println("%n Type here the position: ");
            positionScanned = scanner.next();
            System.out.println("%n Type here future Department from this choice using index: ");
            for (Document dep : allDep){
                System.out.printf("%n| %s: %s", dep.get("id"),dep.get("name"));
            }
            departmentScanned = scanner.next();
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
}
