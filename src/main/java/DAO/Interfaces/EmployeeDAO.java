package DAO.Interfaces;

import Models.Employee;

import java.util.List;
import java.util.Optional;

/**
 * Defines the behaviour of the components of the project (CRUD operations).
 */

public interface EmployeeDAO {
    public void addEmployee(Employee employee);
    public Optional<Employee> updateEmployee(Object id);
    public boolean deleteEmployee(Object id);
    public Optional<Employee> findEmployeeByID(Object id);
    public List<Employee> findAllEmployees();
}
