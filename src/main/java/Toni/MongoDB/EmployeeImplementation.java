package Toni.MongoDB;

import DAO.Interfaces.EmployeeDAO;
import Utils.ValidationUtil;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.*;

public class EmployeeImplementation implements EmployeeDAO {
    private DatabaseManager dbAccess;
    private final Scanner scanner;

    public EmployeeImplementation() {
        dbAccess = new DatabaseManager();
        scanner = new Scanner(System.in);
    }

    @Override
    public void addEmployee(Models.Employee employee) {
        MongoCollection<Document> equiposCollection = dbAccess.getCollection("departamento");
        Employee dep = new Employee(employee);
        equiposCollection.insertOne(dep.toDocument());
    }


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

    @Override
    public boolean deleteEmployee(Object id) {
        boolean result = false;
        Document depDoc = dbAccess.getEmployeeDocById((int)id);
        if (depDoc != null){
            result = dbAccess.deleteEmployee((int)id);
        }
        return result;
    }

    @Override
    public Optional<Models.Employee> findEmployeeByID(Object id) {
        Optional<Models.Employee> result = Optional.empty();
        Document employeeDoc = dbAccess.getEmployeeDocById((int)id);
        if (employeeDoc != null) {
            result = Optional.of(new Employee(employeeDoc));
        }

        return result;
    }

    @Override
    public List<Models.Employee> findAllEmployees() {
        List<Models.Employee> departmentList = new ArrayList<>();
        for (Document doc : dbAccess.getAllEmlpoyees()){
            Employee employee = new Employee(doc);
            departmentList.add(employee);
        }
        return departmentList;
    }

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
