package Toni.PlainText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Models.Department;
import Models.Employee;
import Utils.Constants;


/**
 * The DatabaseManager class is responsible for managing the database operations
 * such as reading from a file, updating cache, saving and deleting departments and employees,
 * and retrieving departments and employees by their IDs.
 */
public class DatabaseManager  {
    private byte[] cache;
    private List<Department> departmentList;
    private List<Employee> employeeList;
    public DatabaseManager() {
        departmentList = new ArrayList<>();
        employeeList = new ArrayList<>();
        readFile();
    }

    /**
     * Reads the file specified in the Constants.FILE_PATH and updates the cache if the file exists.
     */
    public void readFile(){
        File file = new File(Constants.FILE_NAME);
        if(file.exists()){
            updateCache(file);
        }
    }

    /**
     * Initializes data from the given file by reading its contents and processing the data.
     * @param file The file to read data from.
     */
    public void instanceData(File file){

        try (FileInputStream fis = new FileInputStream(file)){
            String fullDocument = new String(fis.readAllBytes());
            List<String>filas = List.of(fullDocument.split("\n"));

            iterTuples(filas);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public void closeDB(){
        departmentList.clear();
        employeeList.clear();
    }
    /**
     * Iterates through the list of strings (tuples) and processes each one to populate the department and employee lists.
     * @param filas The list of strings representing the tuples.
     */
    private void iterTuples(List<String> filas) {
        employeeList = new ArrayList<>();
        departmentList = new ArrayList<>();
        String regex = "\\((.*?)\\)"; // Expresión regular
        List<String> columns = new ArrayList<>(); // Sobra por el momento.
        String entity = "";
        Pattern pattern = Pattern.compile(regex);
        for (String fila :filas){
            if (fila.split(":").length==2){
                entity = fila.split(":")[0].split(" ")[1];
                String definition = fila.split(":")[1].strip();
                Matcher matcher = pattern.matcher(definition);
                String valuesInsideParentheses;
                if (matcher.find()){
                    valuesInsideParentheses = matcher.group(1);// Obtener el contenido dentro de los paréntesis
                    columns = List.of(valuesInsideParentheses.split(","));
                }
            }else{
                Matcher matcher = pattern.matcher(fila);
                String valuesInsideParentheses = "";
                if (matcher.find()){
                    valuesInsideParentheses = matcher.group(1); // Obtener el contenido dentro de los paréntesis
                    columns = List.of(valuesInsideParentheses.split(","));
                }
                String[] values = valuesInsideParentheses.split(",");
                if (entity.equals(Constants.ENTITY_DEPARTMENT)){
                    verifyCanInstanceDep(values);
                }else if(entity.equals(Constants.ENTITY_EMPLOYEE)){
                    Department dep = new Department(0,"","");
                    for (Department depart : departmentList){
                        if (depart.getDepartmentID()==Integer.parseInt(values[3])){
                            dep = depart;
                        }
                    }
                    employeeList.add(new Employee(Integer.parseInt(values[0]),values[1],values[2],Integer.parseInt(values[3]),dep));
                }
            }
        }
    }

    private void verifyCanInstanceDep(String[] values){
        Department newDep = new Department(Integer.parseInt(values[0]), values[1], values[2]);
        boolean exists = departmentList.stream()
                .anyMatch(department -> department.getDepartmentID() == newDep.getDepartmentID());

        if (!exists) {
            departmentList.add(newDep);
        }
    }

    /**
     * Updates the cache with the contents of the given file. If the cache is updated, it also initializes the data.
     * @param file The file to read data from.
     */
    private void updateCache(File file){
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            if (cache != bos.toByteArray()){
                cache = bos.toByteArray();
                instanceData(file);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Gets the list of departments.
     * @return The list of departments.
     */
    public List<Department> getDepartmentList() {
        return departmentList;
    }

    /**
     * Sets the list of departments.
     * @param departmentList The list of departments to set.
     */
    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    /**
     * Gets the list of employees.
     * @return The list of employees.
     */
    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    /**
     * Sets the list of employees.
     * @param employeeList The list of employees to set.
     */
    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    /**
     * Saves the given object (either a Department or an Employee) to the specified file.
     * @param object The object to save.
     * @param file The file to save the object to.
     */
    public void saveObject(Object object, File file){
        switch (object) {
            case Department department -> saveDepartment(department,file);
            case Employee employee -> saveEmployee(employee,file);
            default -> {
            }
        }
    }

    /**
     * Saves the given employee to the specified file. If the employee already exists, it updates the existing entry.
     * @param employee The employee to save.
     * @param file The file to save the employee to.
     */
    public void saveEmployee(Employee employee, File file) {
        File tempFile = new File("tempFile.txt");
        boolean found = false;
        boolean employeeSection = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String currentLine;
            String lastId = "";
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.startsWith("-- employees:")) {
                    employeeSection = true;
                }
                if (employeeSection && currentLine.startsWith("employee(" + employee.getEmployeeID() + ",")) {
                    writer.write("employee(" + employee.getEmployeeID() + "," + employee.getEmployeeName() + "," + employee.getEmployeePosition() + "," + employee.getDepartmentID() + ")" + System.getProperty("line.separator"));
                    found = true;
                } else {
                    if (employeeSection && currentLine.startsWith("employee(")){
                        lastId = currentLine.split(",")[0].split("\\(")[1];
                    }
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
            }
            if (!found) {
                writer.write("employee(" + (Integer.parseInt(lastId)+1) + "," + employee.getEmployeeName() + "," + employee.getEmployeePosition() + "," + employee.getDepartmentID() + ")" + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        file.delete();
        tempFile.renameTo(file);
        updateCache(file);
    }

    /**
     * Saves the given department to the specified file. If the department already exists, it updates the existing entry.
     * @param department The department to save.
     * @param file The file to save the department to.
     */
    public void saveDepartment(Department department, File file) {
        File tempFile = new File("tempFile.txt");
        boolean departmentSection = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.startsWith("-- departments:")) {
                    departmentSection = true;
                }
                if (departmentSection && currentLine.startsWith("department(" + department.getDepartmentID() + ",")) {
                    writer.write("department(" + department.getDepartmentID() + "," + department.getDepartmentName() + "," + department.getDepartmentAddress() + ")" + System.getProperty("line.separator"));
                } else if (currentLine.startsWith("-- employees:")) {
                    System.out.println("entra");
                    String txt = "department(" + department.getDepartmentID() + "," + department.getDepartmentName() + "," + department.getDepartmentAddress() + ")" + System.getProperty("line.separator");
                    String extra = txt + currentLine + System.getProperty("line.separator");
                    writer.write(extra,0,extra.length());
                } else {
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        file.delete();
        tempFile.renameTo(file);
        updateCache(file);
    }

    /**
     * Deletes the employee with the given ID from the specified file.
     * @param employeeID The ID of the employee to delete.
     * @param file The file to delete the employee from.
     */
    public void deleteEmployee(int employeeID, File file) {
        File tempFile = new File("tempFile.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (!currentLine.startsWith("employee(" + employeeID + ",")) {
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        file.delete();
        tempFile.renameTo(file);
        updateCache(file);

    }

    /**
     * Deletes the department with the given ID from the specified file.
     * @param departmentID The ID of the department to delete.
     * @param file The file to delete the department from.
     */
    public void deleteDepartment(int departmentID, File file) {
        File tempFile = new File("tempFile.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (!currentLine.startsWith("department(" + departmentID + ",")) {
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        file.delete();
        tempFile.renameTo(file);
        updateCache(file);
    }

    /**
     * Retrieves the department with the given ID from the list of departments.
     * @param departmentID The ID of the department to retrieve.
     * @return The department with the given ID, or null if not found.
     */
    public Department getDepartmentById(int departmentID) {
        Department result = null;
        for (Department department : departmentList) {
            if (department.getDepartmentID() == departmentID) {
                result = department;
            }
        }
        return result;
    }

    /**
     * Retrieves the employee with the given ID from the list of employees.
     * @param employeeID The ID of the employee to retrieve.
     * @return The employee with the given ID, or null if not found.
     */
    public Employee getEmployeeByID(int employeeID) {
        Employee result = null;
        for (Employee employee : employeeList) {
            if (employee.getEmployeeID() == employeeID) {
                result = employee;
            }
        }
        return result;
    }

}
