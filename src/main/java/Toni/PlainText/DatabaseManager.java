package Toni.PlainText;

import Models.Department;
import Models.Employee;
import Utils.Constants;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseManager  {
    private byte[] cache;
    private List<Department> departmentList;
    private List<Employee> employeeList;
    public DatabaseManager() {
        departmentList = new ArrayList<>();
        employeeList = new ArrayList<>();
    }

    public void readFile(){
        File file = new File(Constants.FILE_PATH);
        if(file.exists()){
            updateCache(file);
        }
    }

    public void instanceData(File file){

        try (FileInputStream fis = new FileInputStream(file)){
            String fullDocument = new String(fis.readAllBytes());
            List<String>filas = List.of(fullDocument.split("\n"));
            iterTuples(filas);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private void iterTuples(List<String> filas) {
        String regex = "\\((.*?)\\)"; // Expresión regular
        List<String> columns = new ArrayList<>();
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
                    departmentList.add(new Department(Integer.parseInt(values[0]),values[1],values[2]));
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

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public static void main(String[] args) {
        DatabaseManager dm = new DatabaseManager();
        dm.instanceData(new File(Constants.FILE_PATH));
        for (Department dp : dm.getDepartmentList()){
            System.out.println(dp);
            System.out.println(dp.getDepartmentID());
            System.out.println(dp.getDepartmentName());
            System.out.println(dp.getDepartmentAddress());
        }
        System.out.println("++++++++++++++++++");
        for (Employee dp : dm.getEmployeeList()){
            System.out.println(dp);
            System.out.println(dp.getEmployeeID());
            System.out.println(dp.getEmployeeName());
            System.out.println(dp.getEmployeePosition());
            System.out.println(dp.getDepartmentID());
            System.out.println(dp.getDepartment());
        }

    }

}
