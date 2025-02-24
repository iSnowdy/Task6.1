package Hugo.PostgreSQL;

import DAO.Interfaces.EmployeeDAO;
import Models.Department;
import Models.Employee;

import java.util.List;
import java.util.Optional;


public class EmployeeImplementation extends PostgreBaseImplementation<Employee> implements EmployeeDAO {
    // Java Reflection to extract the name of the PK field
    private final String primaryFieldName = Employee.class.getDeclaredFields()[0].getName();

    public EmployeeImplementation(DatabaseManager dbManager) {
        super(Employee.class, dbManager);
    }


    @Override
    public void addEmployee(Employee employee) {
        if (storePostgreEmployee(employee)) {
            System.out.println("Employee " + employee.getEmployeeID() + " successfully added");
        } else {
            System.out.println("Employee " + employee.getEmployeeID() + " could not be added");
        }
    }

    @Override
    public Optional<Employee> updateEmployee(Object id) {
        return updatePostgreEmployee((Integer) id);

    }

    @Override
    public boolean deleteEmployee(Object id) {
        Optional<Employee> employeeOptional = getEmployee((Integer) id);
        if (employeeOptional.isEmpty()) {
            System.out.println("employee " + id + " not found");
            return false;
        }

        deletePostgreDepartment(employeeOptional.get());
        return true;
    }

    @Override
    public Optional<Employee> findEmployeeByID(Object id) {
        Optional<Employee> employeeOptional= getEmployee((Integer) id);
        if (employeeOptional.isEmpty()) {
            System.out.println("Employee " + id + " not found");
            return Optional.empty();
        }
        return employeeOptional;
    }

    @Override
    public List<Employee> findAllEmployees() {
        return getAllPostgreEmployees();
    }
}
