package DAO.Interfaces;

import Models.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentDAO {
    boolean addDepartment(Department department);
    Optional<Department> updateDepartment(Object id);
    Optional<Department> deleteDepartment(Object id);
    Optional<Department> findDepartmentByID(Object id);
    List<Department> findAllDepartments();
}
