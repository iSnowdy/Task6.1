package Andy.Hibernate.Database;

import Andy.Hibernate.Models.HEmployee;
import DAO.Interfaces.HibernateInterfaces.HEmployeeDAO;

import java.util.List;
import java.util.Optional;

public class EmployeeImplementationHibernate extends HibernateBaseImplementation<HEmployee> implements HEmployeeDAO {

    public EmployeeImplementationHibernate() {
        super(HEmployee.class);
    }

    @Override
    public void addEmployee(HEmployee employee) {
        if (storeObject(employee))
            System.out.println("Employee ID " + employee.getID() + " was added to the Hibernate JPA DB");
        else System.out.println("Employee ID " + employee.getID() + "could not be added to the Hibernate JPA DB");
    }

    @Override
    public Optional<HEmployee> updateEmployee(Object id) {
        return updateObject(id);
    }

    @Override
    public boolean deleteEmployee(Object id) {
        Optional<HEmployee> employeeOptional = getObject(id);
        if (employeeOptional.isEmpty()) {
            System.out.println("Employee ID " + id + " not found in the Hibernate JPA DB");
            return false;
        }

        deleteObject(employeeOptional.get());
        System.out.println("Employee ID " + id + " successfully deleted from the Hibernate JPA DB");
        return true;
    }

    @Override
    public Optional<HEmployee> findEmployeeByID(Object id) {
        Optional<HEmployee> employeeOptional = getObject(id);
        if (employeeOptional.isEmpty()) {
            System.out.println("Employee ID " + id + " not found in the Hibernate JPA DB");
            return Optional.empty();
        }

        System.out.println("Employee ID " + id + " found in the Hibernate JPA DB");
        return employeeOptional;
    }

    @Override
    public List<HEmployee> findAllEmployees() {
        return getObjectList();
    }
}
