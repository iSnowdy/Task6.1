package DAO.Interfaces;

import Models.Employee;

import java.util.List;
import java.util.Optional;

/**
 * Defines the behaviour of the components of the project (CRUD operations).
 */

public interface EmployeeDAO {
    void addEmployee(Employee employee);
    Optional<Employee> updateEmployee(Object id);
    boolean deleteEmployee(Object id);
    Optional<Employee> findEmployeeByID(Object id);
    List<Employee> findAllEmployees();
}
