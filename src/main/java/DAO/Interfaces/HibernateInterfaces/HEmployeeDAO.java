package DAO.Interfaces.HibernateInterfaces;

import Andy.Hibernate.Models.HEmployee;

import java.util.List;
import java.util.Optional;

public interface HEmployeeDAO {
    void addEmployee(HEmployee employee);
    Optional<HEmployee> updateEmployee(Object id);
    boolean deleteEmployee(Object id);
    Optional<HEmployee> findEmployeeByID(Object id);
    List<HEmployee> findAllEmployees();
}
