package Toni.MongoDB;

import DAO.Interfaces.DepartmentDAO;
import org.bson.Document;

import java.util.*;

public class DepartmentImplementation implements DepartmentDAO {
    private DatabaseManager dbAccess;
    private final Scanner scanner;


    public DepartmentImplementation() {
        dbAccess = new DatabaseManager();
        scanner = new Scanner(System.in);
    }

    @Override
    public boolean addDepartment(Models.Department department) {
        Department dep = new Department(department);
        return dbAccess.saveDepartament(dep);
    }


    @Override
    public Optional<Models.Department> updateDepartment(Object id) {
        Optional<Models.Department> result = Optional.empty();
        Map<String, String> data = getValuesToUpdate();
        dbAccess.updateDepartment((int)id, data.get("name"), data.get("address"));
        Document depDoc = dbAccess.getDepartamentDoc((int)id);
        if (depDoc != null){
            result = Optional.of(new Department(depDoc));
        }
        return result;
    }
    private Map<String,String> getValuesToUpdate(){
        boolean setted = false;
        String nameScanned ="";
        String addressScanned ="";
        do {
            System.out.println("%n Text can be empty if won't want to be update");
            System.out.println("Type here the new name: ");
            nameScanned = scanner.next();
            System.out.println("%n Type here the new address: ");
            addressScanned = scanner.next();
            if (!nameScanned.isEmpty() || !addressScanned.isEmpty()){
                setted = true;
            }
        }while(!setted);
        return Map.of("name",nameScanned,"address",addressScanned);
    }

    @Override
    public Optional<Models.Department> deleteDepartment(Object id) {
        Optional<Models.Department> result = Optional.empty();
        Document depDoc = dbAccess.getDepartamentDoc((int)id);
        if (depDoc != null){
            dbAccess.deleteDepartamento((int)id);
            result = Optional.of(new Department(depDoc));
        }

        return result;
    }

    @Override
    public Optional<Models.Department> findDepartmentByID(Object id) {
        Optional<Models.Department> result = Optional.empty();
        Document depDoc = dbAccess.getDepartamentDoc((int)id);
        if (depDoc != null){
            result = Optional.of(new Department(depDoc));
        }

        return result;
    }

    @Override
    public List<Models.Department> findAllDepartments() {
        List<Models.Department> departmentList = new ArrayList<>();
        for (Document doc : dbAccess.getAllDepartments()){
            Department dep = new Department(doc);
            departmentList.add(dep);
        }
        return departmentList;
    }

}
