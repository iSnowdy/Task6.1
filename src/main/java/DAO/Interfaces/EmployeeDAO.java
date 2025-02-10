package DAO.Interfaces;

import java.util.List;

/**
 * Defines the behaviour of the components of the project (CRUD operations).
 */

public interface EmployeeDAO {
    public void addEmployee(Object employee);
    public Object updateEmployee(Object id);
    public boolean deleteEmployee(Object id);
    public Object findEmployeeByID(Object id);
    public List<Object> findAllEmployees();
}
