package DAO.Interfaces;

import java.util.List;

public interface DepartmentDAO {
    public boolean addDepartment(Object department);
    public Object updateDepartment(Object id);
    public Object deleteDepartment(Object id);
    public Object findDepartmentByID(Object id);
    public List<Object> findAllDepartments();
}
