package Andy.db4o.Database;

import DAO.Interfaces.DepartmentDAO;
import DAO.Interfaces.EmployeeDAO;
import Excepciones.DatabaseDeleteException;
import Excepciones.DatabaseInsertException;
import Models.Department;
import Models.Employee;

import java.util.List;
import java.util.Optional;

public class EmployeeImplementation extends BaseImplementation<Employee> implements EmployeeDAO {
    // Java Reflection to extract the name of the PK field
    private final String primaryFieldName = Employee.class.getFields()[0].getName();


    @Override
    public void addEmployee(Employee employee) {
        try {
            if (storeObject(employee)) {
                System.out.println("Employee " + employee.getEmployeeID() + " successfully added");
            }
        } catch (DatabaseInsertException e) {
            System.out.println("Employee " + employee.getEmployeeID() + " could not be added");
        }
    }

    @Override
    public Optional<Employee> updateEmployee(Object id) {
        return null;
    }

    @Override
    public boolean deleteEmployee(Object id) {
        Optional<Employee> employeeOptional = getObject(id, primaryFieldName);

        if (employeeOptional.isEmpty()) {
            System.out.println("Employee " + id + " could not be found");
            return false;
        }

        try {
            deleteObject(employeeOptional.get());
            System.out.println("Employee " + id + " successfully deleted");
            return true;
        } catch (DatabaseDeleteException e) {
            System.out.println("Employee " + id + " could not be deleted");
        }
        return false;
    }

    @Override
    public Optional<Employee> findEmployeeByID(Object id) {
        Optional<Employee> employeeOptional = getObject(id, primaryFieldName);
        if (employeeOptional.isEmpty()) {
            System.out.println("Employee " + id + " could not be found");
        }
        System.out.println("Employee " + id + " found");
        return employeeOptional;
    }

    @Override
    public List<Employee> findAllEmployees() {
        return getObjectList();
    }
}
