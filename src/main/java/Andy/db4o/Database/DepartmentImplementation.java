package Andy.db4o.Database;

import DAO.Interfaces.DepartmentDAO;
import Excepciones.DatabaseDeleteException;
import Excepciones.DatabaseInsertException;
import Models.Department;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class DepartmentImplementation extends BaseImplementation<Department> implements DepartmentDAO {
    // Java Reflection to extract the name of the PK field
    private final String primaryFieldName = Department.class.getFields()[0].getName();
    private Scanner scanner = new Scanner(System.in);


    @Override
    public boolean addDepartment(Department department) {
        try {
            if (storeObject(department)) {
                System.out.println("Department " + department.getDepartmentID() + " successfully added");
                return true;
            }
        } catch (DatabaseInsertException e) {
            System.out.println("Department " + department.getDepartmentID() + " could not be added");
        }
        System.out.println("Department could not be added");
        return false;
    }

    @Override
    public Optional<Department> updateDepartment(Object id) {
        Optional<Department> department = getObject(id, primaryFieldName);

        if (department.isEmpty()) {
            System.out.println("Department " + department.get().getDepartmentID() + " could not be found");
            return Optional.empty();
        }

        Optional<Field> fieldToUpdate = getFieldToUpdateFromUser();
        if (fieldToUpdate.isEmpty()) {
            System.out.println("The specified field does not exist");
        }





    }

    private Optional<Field> getFieldToUpdateFromUser() {
        printObjectFields();
        int fieldIndex = scanner.nextInt();
        if (!isInvalidFieldIndex(fieldIndex)) {
            Department.class.getDeclaredFields()[fieldIndex].setAccessible(true);
        }
        return Optional.empty();
    }

    private boolean isInvalidFieldIndex(int fieldIndex) {
        return fieldIndex > Department.class.getFields().length - 1 || fieldIndex < 0;
    }


    @Override
    public Optional<Department> deleteDepartment(Object id) {
        Optional<Department> departmentOptional = getObject(id, primaryFieldName);
        if (departmentOptional.isEmpty()) {
            System.out.println("Department " + id + " not found");
        }

        try {
            deleteObject(departmentOptional.get());
            return departmentOptional;
        } catch (DatabaseDeleteException e) {
            System.out.println("Department " + id + " could not be deleted");
        }
        return Optional.empty();
    }

    @Override
    public Optional<Department> findDepartmentByID(Object id) {
        Optional<Department> departmentOptional = getObject(id, primaryFieldName);
        if (departmentOptional.isEmpty()) {
            System.out.println("Department " + id + " not found");
            return Optional.empty();
        }
        return departmentOptional;
    }

    @Override
    public List<Department> findAllDepartments() {
        return getObjectList();
    }
}
