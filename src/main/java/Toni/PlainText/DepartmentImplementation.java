package Toni.PlainText;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import DAO.Interfaces.DepartmentDAO;
import Models.Department;
import Utils.Constants;
import Utils.ValidationUtil;

/**
 * Implementation of the DepartmentDAO interface for managing Department data.
 */
public class DepartmentImplementation implements DepartmentDAO {
    private final DatabaseManager dbManager;
    private final File file;
    private final Scanner scanner;

    /**
     * Constructor that initializes the DatabaseManager and file.
     */
    public DepartmentImplementation(DatabaseManager databaseManager) {
        dbManager = databaseManager;
        file = new File(Constants.FILE_NAME);
        dbManager.instanceData(file);
        scanner = new Scanner(System.in);
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
        Department department = dbManager.getDepartmentById(Integer.parseInt(String.valueOf(id)));
        if (department != null) {
            String name = setValidName();
            String address = setValidAddress();
            department.setDepartmentName(name);
            department.setDepartmentAddress(address);
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


    private String getTextScanned(String textToPrint) {
        System.out.println(textToPrint);
        while (!scanner.hasNext()) {
            System.out.println("No input detected. Please enter a valid input:");
            scanner.next(); // consume the invalid input
        }
        return scanner.next();
    }
    private String setValidId(){
        String id = "0";
        boolean done = false;
        do {
            try {
                id = getTextScanned("Set department ID: ");
                scanner.nextLine();
                if (!ValidationUtil.isValidDepartmentId(Integer.parseInt(id))) {
                    done = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Department ID must be a number");
            }
        }while (!done);
        return id;
    }

    private String setValidName(){
        String name = "";
        boolean done = false;
        do {
            try {
                name = getTextScanned("Set department Name: ");
                scanner.nextLine();

                if (ValidationUtil.isValidDepartmentName(name)) {
                    done = true;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }while (!done);
        return name;
    }
    private String setValidAddress(){
        String address = "";
        boolean done = false;
        do {
            try{
                address = getTextScanned("Set department address: ");
                scanner.nextLine();

                if (ValidationUtil.isValidDepartmentAddress(address)) {
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
            id = Integer.parseInt(setValidId());
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