package Andy.db4o.Database;

import DAO.Interfaces.DepartmentDAO;
import Exceptions.DatabaseDeleteException;
import Exceptions.DatabaseInsertException;
import Models.Department;
import Models.Employee;
import com.db4o.query.Query;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link DepartmentDAO} using db4o.
 * Provides CRUD operations for managing {@link Department} entities inside the db4o database.
 */

public class DepartmentImplementation extends DB4OBaseImplementation<Department> implements DepartmentDAO {
    // Java Reflection to extract the name of the PK field
    private final String primaryFieldName = Department.class.getDeclaredFields()[0].getName();

    /**
     * Constructs a {@code DepartmentImplementation} instance while specifying the class type for the db4o operations and
     * injects the {@link DatabaseManager} for DB4O operations into {@link DB4OBaseImplementation},
     * inside {@link DB4OBaseImplementation}.
     */

    public DepartmentImplementation(DatabaseManager dbManager) {
        super(Department.class, dbManager);
    }

    @Override
    public String getPrimaryKeyFieldName() {
        return primaryFieldName;
    }

    /**
     * Adds a new department to the database.
     * If it already exists, the insertion is then ignored.
     *
     * @param department The department object to be added to the database.
     * @return {@code true} if the department was successfully added, {@code false} if it already exists.
     * @throws DatabaseInsertException if an error occurs while storing the department.
     */

    @Override
    public boolean addDepartment(Department department) {
        if (storeObject(department)) {
            System.out.println("Department " + department.getDepartmentID() + " successfully added");
            return true;
        } else {
            System.out.println("Department " + department.getDepartmentID() + " could not be added");
            return false;
        }
    }

    /**
     * Updates an existing department's information in the db4o database.
     * The user will be prompted to select which field they want to modify.
     *
     * @param id The unique identifier of the department to update.
     * @return An {@code Optional<Department>} containing the updated department, or empty if it was not found.
     * @throws Exceptions.DatabaseQueryException if an error occurs while updating the department.
     */

    @Override
    public Optional<Department> updateDepartment(Object id) {
        return updateObject(id);
    }

    /**
     * Deletes a department from the database.
     *
     * @param id The unique identifier of the department to delete.
     * @return An {@code Optional<Department>} containing the deleted department, or empty if it was not found.
     * @throws DatabaseDeleteException if an error occurs during the deletion process.
     */

    @Override
    public Optional<Department> deleteDepartment(Object id) {
        Optional<Department> departmentOptional = getObject(id);
        if (departmentOptional.isEmpty()) {
            System.out.println("Department " + id + " not found");
            return Optional.empty();
        }

        deleteObject(departmentOptional.get());
        return departmentOptional;
    }

    // From superclass
    @Override
    protected boolean canDeleteObject(Department department) {
        boolean canDelete;
        Query query = getDb4oQuery(Employee.class); // Get a query for Employee class
        query.descend("departmentID").constrain(department.getDepartmentID());

        List<Employee> employees = query.execute();
        canDelete = employees.isEmpty();;
        if (!canDelete) {
            System.out.println("Department " + department.getDepartmentID() +
                    " could not be deleted because it contains employees");
        }
        return canDelete; // Only allow deletion if there are no employees
    }

    /**
     * Finds a department by its given ID.
     *
     * @param id The unique identifier of the department.
     * @return An {@code Optional<Department>} containing the found department, or empty if it was not found.
     * @throws Exceptions.DatabaseQueryException if an error occurs during the querying process.
     */

    @Override
    public Optional<Department> findDepartmentByID(Object id) {
        Optional<Department> departmentOptional = getObject(id);
        if (departmentOptional.isEmpty()) {
            System.out.println("Department " + id + " not found");
            return Optional.empty();
        }
        return departmentOptional;
    }

    /**
     * Retrieves all departments stored in the database.
     *
     * @return A {@code List<Department} containing all the departments in the database.
     * @throws Exceptions.DatabaseQueryException if an error occurs during the querying process.
     */

    @Override
    public List<Department> findAllDepartments() {
        return getObjectList();
    }
}
