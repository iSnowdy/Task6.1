package Andy.Hibernate.Database;

import Andy.db4o.Database.BaseImplementation;
import DAO.Interfaces.DepartmentDAO;
import Models.Department;

import java.util.List;
import java.util.Optional;

public class DepartmentImplementation extends BaseImplementation<Department> implements DepartmentDAO {

    public DepartmentImplementation() {
        super(Department.class);
    }



    @Override
    public boolean addDepartment(Department department) {
        return false;
    }

    @Override
    public Optional<Department> updateDepartment(Object id) {
        return Optional.empty();
    }

    @Override
    public Optional<Department> deleteDepartment(Object id) {
        return Optional.empty();
    }

    @Override
    public Optional<Department> findDepartmentByID(Object id) {
        return Optional.empty();
    }

    @Override
    public List<Department> findAllDepartments() {
        return List.of();
    }
}
