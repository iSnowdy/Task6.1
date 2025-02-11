package Andy.Hibernate.Database;

import Andy.db4o.Database.BaseImplementation;
import DAO.Interfaces.EmployeeDAO;
import Models.Employee;

import java.util.List;
import java.util.Optional;

public class EmployeeImplementation extends BaseImplementation<Employee> implements EmployeeDAO {

    public EmployeeImplementation() {
        super(Employee.class);
    }


    @Override
    public void addEmployee(Employee employee) {

    }

    @Override
    public Optional<Employee> updateEmployee(Object id) {
        return Optional.empty();
    }

    @Override
    public boolean deleteEmployee(Object id) {
        return false;
    }

    @Override
    public Optional<Employee> findEmployeeByID(Object id) {
        return Optional.empty();
    }

    @Override
    public List<Employee> findAllEmployees() {
        return List.of();
    }
}
