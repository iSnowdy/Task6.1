package Andy.Hibernate.Database;

import Andy.Hibernate.Models.HDepartment;
import DAO.Interfaces.HibernateInterfaces.HDepartmentDAO;

import java.util.List;
import java.util.Optional;

public class DepartmentImplementationHibernate extends HibernateBaseImplementation<HDepartment> implements HDepartmentDAO {

    public DepartmentImplementationHibernate() {
        super(HDepartment.class);
    }


    @Override
    public boolean addDepartment(HDepartment department) {
        if (storeObject(department)) {
            System.out.println("Department ID " + department.getId() + " succesfully added");
            return true;
        }
        return false;
    }

    @Override
    public Optional<HDepartment> updateDepartment(Object id) {
        return updateObject(id);
    }

    @Override
    public Optional<HDepartment> deleteDepartment(Object id) {
        Optional<HDepartment> departmentOptional = getObject(id);
        if (departmentOptional.isEmpty()) {
            System.out.println("Department ID " + id + " could not be found");
            return Optional.empty();
        }

        deleteObject(departmentOptional.get());
        System.out.println("Department ID " + id + " successfully deleted");
        return departmentOptional;
    }

    @Override
    public Optional<HDepartment> findDepartmentByID(Object id) {
        Optional<HDepartment> departmentOptional = getObject(id);
        if (departmentOptional.isEmpty()) {
            System.out.println("Department ID " + id + " could not be found");
            return Optional.empty();
        }
        System.out.println("Department ID " + id + " successfully found");
        return departmentOptional;
    }

    @Override
    public List<HDepartment> findAllDepartments() {
        return getObjectList();
    }
}
