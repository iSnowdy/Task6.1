package Toni.MongoDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import org.bson.Document;

import DAO.Interfaces.DepartmentDAO;

/**
 * Implementation of DepartmentDAO interface for MongoDB.
 */
public class DepartmentImplementation implements DepartmentDAO {
    private final DatabaseManager dbAccess;
    private final Scanner scanner;

    /**
     * Constructor initializes DatabaseManager and Scanner.
     */
    public DepartmentImplementation(DatabaseManager databaseManager) {
        dbAccess = databaseManager;
        scanner = new Scanner(System.in);
    }

    /**
     * Adds a new department to the database.
     * @param department The department to add.
     * @return True if the department was added, false otherwise.
     */
    @Override
    public boolean addDepartment(Models.Department department) {
        Department dep = new Department(department);
        return dbAccess.saveDepartament(dep);
    }

    /**
     * Updates an existing department in the database.
     * @param id The ID of the department to update.
     * @return An Optional containing the updated department, if found.
     */
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

    /**
     * Prompts the user for values to update a department.
     * @return A map of the values to update.
     */
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

    /**
     * Deletes a department from the database.
     * @param id The ID of the department to delete.
     * @return An Optional containing the deleted department, if found.
     */
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

    /**
     * Finds a department by ID.
     * @param id The ID of the department to find.
     * @return An Optional containing the found department, if any.
     */
    @Override
    public Optional<Models.Department> findDepartmentByID(Object id) {
        Optional<Models.Department> result = Optional.empty();
        Document depDoc = dbAccess.getDepartamentDoc((int)id);
        if (depDoc != null){
            result = Optional.of(new Department(depDoc));
        }

        return result;
    }

    /**
     * Finds all departments in the database.
     * @return A list of all departments.
     */
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
