package Toni.PlainText;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import DAO.Interfaces.DepartmentDAO;
import Models.Department;
import Utils.Constants;

/**
 * Implementation of the DepartmentDAO interface for managing Department data.
 */
public class DepartmentImplementation implements DepartmentDAO {
    private final DatabaseManager dbManager;
    private final File file;

    /**
     * Constructor that initializes the DatabaseManager and file.
     */
    public DepartmentImplementation() {
        dbManager = new DatabaseManager();
        file = new File(Constants.FILE_PATH);
        dbManager.instanceData(file);
    }

    /**
     * Adds a new department to the database.
     * 
     * @param department The department to be added.
     * @return true if the department was successfully added.
     */
    @Override
    public boolean addDepartment(Department department) {
        dbManager.saveObject(department, file);
        return true;
    }

    /**
     * Updates an existing department's information.
     * 
     * @param id The ID of the department to be updated.
     * @return An Optional containing the updated department if successful, or an empty Optional if not.
     */
    @Override
    public Optional<Department> updateDepartment(Object id) {
        Department department = dbManager.getDepartmentById((int) id);
        if (department != null) {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Enter new department name:");
                String name = scanner.nextLine();
                System.out.println("Enter new department address:");
                String address = scanner.nextLine();
                department.setDepartmentName(name);
                department.setDepartmentAddress(address);
            }
            dbManager.saveObject(department, file);
            return Optional.of(department);
        }
        return Optional.empty();
    }

    /**
     * Deletes a department from the database.
     * 
     * @param id The ID of the department to be deleted.
     * @return An Optional containing the deleted department if successful, or an empty Optional if not.
     */
    @Override
    public Optional<Department> deleteDepartment(Object id) {
        Department department = dbManager.getDepartmentById((int) id);
        if (department != null) {
            dbManager.deleteDepartment((int) id, file);
            return Optional.of(department);
        }
        return Optional.empty();
    }

    /**
     * Finds a department by its ID.
     * 
     * @param id The ID of the department to be found.
     * @return An Optional containing the found department, or an empty Optional if not found.
     */
    @Override
    public Optional<Department> findDepartmentByID(Object id) {
        return Optional.ofNullable(dbManager.getDepartmentById((int) id));
    }

    /**
     * Retrieves a list of all departments.
     * 
     * @return A list of all departments.
     */
    @Override
    public List<Department> findAllDepartments() {
        return dbManager.getDepartmentList();
    }
}