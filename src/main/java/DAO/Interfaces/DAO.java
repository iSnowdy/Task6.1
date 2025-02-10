package DAO.Interfaces;

import java.util.List;

/**
 * Defines the behaviour of the components of the project (CRUD operations).
 */

public interface DAO {
    public void addEmployee(Object employee);
    public Object updateEmployee(Object id);
    public boolean deleteEmployee(Object id);
    public Object findEmployeeByID(Object id);
    public List<Object> findAllEmployees();

    public boolean addDepartment(Object department);
    public Object updateDepartment(Object id);
    public Object deleteDepartment(Object id);
    public Object findDepartmentByID(Object id);
    public List<Object> findAllDepartments();
}
