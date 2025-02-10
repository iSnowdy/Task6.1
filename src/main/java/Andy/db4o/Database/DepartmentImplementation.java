package Andy.db4o.Database;

import DAO.Interfaces.DepartmentDAO;
import Models.Department;

import java.util.List;

public class DepartmentImplementation implements DepartmentDAO {

    public DepartmentImplementation() {

    }

    @Override
    public boolean addDepartment(Department department) {
        return false;
    }

    @Override
    public Department updateDepartment(Object id) {
        return null;
    }

    @Override
    public Department deleteDepartment(Object id) {
        return null;
    }

    @Override
    public Department findDepartmentByID(Object id) {
        return null;
    }

    @Override
    public List<Department> findAllDepartments() {
        return List.of();
    }
}
