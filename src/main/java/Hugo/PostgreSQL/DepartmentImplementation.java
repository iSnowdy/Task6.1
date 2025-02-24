package Hugo.PostgreSQL;

import DAO.Interfaces.DepartmentDAO;
import Exceptions.DatabaseDeleteException;
import Exceptions.DatabaseInsertException;
import Hugo.PostgreSQL.PostgreBaseImplementation;
import Models.Department;

import java.util.List;
import java.util.Optional;

public class DepartmentImplementation extends PostgreBaseImplementation<Department> implements DepartmentDAO {
    // Java Reflection to extract the name of the PK field

    public DepartmentImplementation(DatabaseManager dbManager) {
        super(Department.class, dbManager);
    }

    @Override
    public boolean addDepartment(Department department) {
        if (storePostgreDepartment(department)) {
            System.out.println("Department " + department.getDepartmentID() + " successfully added");
            return true;
        } else {
            System.out.println("Department " + department.getDepartmentID() + " could not be added");
            return false;
        }
    }

    @Override
    public Optional<Department> updateDepartment(Object id) {
        return updatePostgreDepartment((Integer) id);
    }

    @Override
    public Optional<Department> deleteDepartment(Object id) {
        Optional<Department> departmentOptional = getDepartment((Integer) id);
        if (departmentOptional.isEmpty()) {
            System.out.println("Department " + id + " not found");
            return Optional.empty();
        }

        deletePostgreDepartment(departmentOptional.get());
        return departmentOptional;
    }

    @Override
    public Optional<Department> findDepartmentByID(Object id) {
        Optional<Department> departmentOptional = getDepartment((Integer) id);
        if (departmentOptional.isEmpty()) {
            System.out.println("Department " + id + " not found");
            return Optional.empty();
        }
        return departmentOptional;
    }

    @Override
    public List<Department> findAllDepartments() {
        return getAllPostgreDepartments();
    }
}
