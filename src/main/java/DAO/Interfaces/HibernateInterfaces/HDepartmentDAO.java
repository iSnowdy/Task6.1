package DAO.Interfaces.HibernateInterfaces;

// TODO: Pascual :)

import Andy.Hibernate.Models.HDepartment;

import java.util.List;
import java.util.Optional;

public interface HDepartmentDAO {
    boolean addDepartment(HDepartment department);
    Optional<HDepartment> updateDepartment(Object id);
    Optional<HDepartment> deleteDepartment(Object id);
    Optional<HDepartment> findDepartmentByID(Object id);
    List<HDepartment> findAllDepartments();
}
