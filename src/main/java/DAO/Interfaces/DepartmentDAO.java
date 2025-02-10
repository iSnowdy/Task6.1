package DAO.Interfaces;

import Models.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentDAO {
    public boolean addDepartment(Department department);
    public Optional<Department> updateDepartment(Object id);
    public Optional<Department> deleteDepartment(Object id);
    public Optional<Department> findDepartmentByID(Object id);
    public List<Department> findAllDepartments();
}
