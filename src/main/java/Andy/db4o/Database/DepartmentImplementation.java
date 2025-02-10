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
    private final String primaryFieldName = Department.class.getDeclaredFields()[0].getName();


    @Override
    public boolean addDepartment(Department department) {
        if (storeObject(department)) {
            System.out.println("Department " + department.getDepartmentID() + " successfully added");
            return true;
        } else {
            System.out.println("Department " + department.getDepartmentID() + " could not be added");
            return false;
        }
    }

    @Override
    public Optional<Department> updateDepartment(Object id) {
        return updateObject(id, primaryFieldName);
    }

    @Override
    public Optional<Department> deleteDepartment(Object id) {
        Optional<Department> departmentOptional = getObject(id, primaryFieldName);
        if (departmentOptional.isEmpty()) {
            System.out.println("Department " + id + " not found");
            return Optional.empty();
        }

        deleteObject(departmentOptional.get());
        return departmentOptional;
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
