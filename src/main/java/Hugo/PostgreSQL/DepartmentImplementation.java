package Hugo.PostgreSQL;

import DAO.Interfaces.DepartmentDAO;
import Exceptions.DatabaseDeleteException;
import Exceptions.DatabaseInsertException;
import Hugo.PostgreSQL.PostgreBaseImplementation;
import Models.Department;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link DepartmentDAO} interface.
 * Provides functionality to perform CRUD operations on {@link Department} objects in a PostgreSQL database.
 */
public class DepartmentImplementation extends PostgreBaseImplementation<Department> implements DepartmentDAO {

    /**
     * Constructor for the DepartmentImplementation class.
     * Initializes the class with a reference to the {@link DatabaseManager}.
     *
     * @param dbManager the database manager used for managing the connection to the database
     */
    public DepartmentImplementation(DatabaseManager dbManager) {
        super(Department.class, dbManager);
    }

    /**
     * Adds a new department to the database.
     *
     * @param department the {@link Department} object to be added
     * @return true if the department is successfully added, false otherwise
     */
    @Override
    public boolean addDepartment(Department department) {
        if (storePostgreDepartment(department)) {
            System.out.println("Department " + department.getDepartmentID() + " successfully added");
            return true;
        } else {
            System.out.println("Department " + department.getDepartmentID() + " could not be added");
            return false;
        }
    }

    /**
     * Updates an existing department in the database by its ID.
     *
     * @param id the ID of the department to be updated
     * @return an {@link Optional} containing the updated {@link Department} if successful, or an empty {@link Optional} if not
     */
    @Override
    public Optional<Department> updateDepartment(Object id) {
        return updatePostgreDepartment((Integer) id);
    }

    /**
     * Deletes a department from the database by its ID.
     *
     * @param id the ID of the department to be deleted
     * @return an {@link Optional} containing the deleted {@link Department} if successful, or an empty {@link Optional} if not
     */
    @Override
    public Optional<Department> deleteDepartment(Object id) {
        Optional<Department> departmentOptional = getDepartment((Integer) id);
        if (departmentOptional.isEmpty()) {
            System.out.println("Department " + id + " not found");
            return Optional.empty();
        }

        deletePostgreDepartment(departmentOptional.get());
        return departmentOptional;
    }

    /**
     * Finds a department by its ID.
     *
     * @param id the ID of the department to be found
     * @return an {@link Optional} containing the {@link Department} if found, or an empty {@link Optional} if not
     */
    @Override
    public Optional<Department> findDepartmentByID(Object id) {
        Optional<Department> departmentOptional = getDepartment((Integer) id);
        if (departmentOptional.isEmpty()) {
            System.out.println("Department " + id + " not found");
            return Optional.empty();
        }
        return departmentOptional;
    }

    /**
     * Retrieves all departments from the database.
     *
     * @return a {@link List} containing all {@link Department} objects in the database
     */
    @Override
    public List<Department> findAllDepartments() {
        return getAllPostgreDepartments();
    }
}
