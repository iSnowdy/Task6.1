package DAO.Interfaces;

import Models.Department;

import java.util.List;

public interface DepartmentDAO {
    public boolean addDepartment(Department department);
    public Department updateDepartment(Object id);
    public Department deleteDepartment(Object id);
    public Department findDepartmentByID(Object id);
    public List<Department> findAllDepartments();
}
