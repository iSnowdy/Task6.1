package Hugo.PostgreSQL;

import Exceptions.DatabaseDeleteException;
import Exceptions.DatabaseInsertException;
import Exceptions.DatabaseQueryException;
import Models.Department;
import Models.Employee;
import Utils.ObjectFieldsUtil;
import Utils.ValidationUtil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Abstract base implementation for managing PostgreSQL database operations for a specific type T.
 *
 * @param <T> The type of the entity being managed.
 */
public abstract class PostgreBaseImplementation<T> {
    private final Class<T> clazz; // To know the type of class being managed
    private final DatabaseManager dbManager;

    private final String pk;

    /**
     * Constructor to initialize the PostgreBaseImplementation with the specified class type and database manager.
     *
     * @param clazz     The class type of the entity being managed.
     * @param dbManager The database manager for handling database connections.
     */
    public PostgreBaseImplementation(Class<T> clazz, DatabaseManager dbManager) {
        this.clazz = clazz;
        this.dbManager = dbManager;
        this.pk = clazz.getDeclaredFields()[0].getName();
    }

    /**
     * Checks if a department exists in the database.
     *
     * @param department The department to check.
     * @return true if the department exists, false otherwise.
     */
    private boolean isDepartmentInDB(Department department) {
        String query = "SELECT * FROM department WHERE depno = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, department.getDepartmentID());
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new DatabaseQueryException("Error checking if department exists in PostgreSQL", e);
        }
    }

    /**
     * Stores a department in the PostgreSQL database.
     *
     * @param department The department to store.
     * @return true if the department was successfully stored, false otherwise.
     */
    public boolean storePostgreDepartment(Department department) {
        if (!isDepartmentInDB(department)) {
            String query = "INSERT INTO department (depno, nombre, ubicacion) VALUES (?, ?, ?)";
            try {
                Connection connection = dbManager.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, department.getDepartmentID());
                pstmt.setString(2, department.getDepartmentName());
                pstmt.setString(3, department.getDepartmentAddress());
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                throw new DatabaseInsertException("Could not store the department in PostgreSQL", e);
            }
        }
        return false;
    }

    /**
     * Updates a department in the PostgreSQL database.
     *
     * @param depno The department number to update.
     * @return An Optional containing the updated department if successful, or an empty Optional if not.
     */
    public Optional<Department> updatePostgreDepartment(Integer depno) {
        Optional<Department> departmentOptional = getDepartment(depno);
        if (departmentOptional.isEmpty()) {
            System.out.println("Department with ID " + depno + " could not be found.");
            return Optional.empty();
        }

        Department departmentToUpdate = departmentOptional.get();
        Set<String> excludedFields = Set.of("departmentID"); // Exclude primary key from modification

        Optional<Field> fieldToUpdate = ObjectFieldsUtil.promptUserForFieldSelection(departmentToUpdate, excludedFields);
        if (fieldToUpdate.isEmpty()) return Optional.empty();

        return modifyDepartmentField(departmentToUpdate, fieldToUpdate.get());
    }

    /**
     * Modifies a specific field of a department in the PostgreSQL database.
     *
     * @param department The department to modify.
     * @param field      The field to modify.
     * @return An Optional containing the modified department if successful, or an empty Optional if not.
     */
    private Optional<Department> modifyDepartmentField(Department department, Field field) {
        String column;
        try {
            Object newValue = ObjectFieldsUtil.promptUserForNewValue(field);
            field.set(department, newValue);

            if (!ValidationUtil.isValidObject(department, Department.class)) return Optional.empty();

            if (field.getName().equalsIgnoreCase("departmentName")) {
                column = "nombre";
            } else {
                column = "ubicacion";
            }
            String query = "UPDATE department SET " + column + " = ? WHERE depno = ?";
            try (Connection connection = dbManager.getConnection();
                 PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setObject(1, newValue);
                pstmt.setInt(2, department.getDepartmentID());
                pstmt.executeUpdate();
                System.out.println("Department successfully updated");
                return Optional.of(department);
            }
        } catch (Exception e) {
            System.out.println("Department could not be updated");
            throw new DatabaseQueryException("Department could not be updated in PostgreSQL", e);
        }
    }

    /**
     * Deletes a department from the PostgreSQL database.
     *
     * @param department The department to delete.
     * @return true if the department was successfully deleted, false otherwise.
     */
    public boolean deletePostgreDepartment(Department department) {
        if (isDepartmentInDB(department)) {
            String checkEmployeesQuery = "SELECT COUNT(*) AS employee_count FROM employee WHERE depno = ?";
            try (Connection connection = dbManager.getConnection();
                 PreparedStatement checkStmt = connection.prepareStatement(checkEmployeesQuery)) {
                checkStmt.setInt(1, department.getDepartmentID());
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    int employeeCount = rs.getInt("employee_count");
                    if (employeeCount > 0) {
                        System.out.println("The department cannot be deleted because it has employees assigned to it");
                        return false;
                    }
                }

                String deleteQuery = "DELETE FROM department WHERE depno = ?";
                try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                    deleteStmt.setInt(1, department.getDepartmentID());
                    int rowsAffected = deleteStmt.executeUpdate();
                    System.out.println("Department successfully deleted");
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                throw new DatabaseDeleteException("Could not delete the department in PostgreSQL", e);
            }
        }
        return false;
    }

    /**
     * Retrieves a department from the PostgreSQL database by its department number.
     *
     * @param depno The department number to retrieve.
     * @return An Optional containing the department if found, or an empty Optional if not.
     */
    public Optional<Department> getDepartment(Integer depno) {
        String query = "SELECT * FROM department WHERE depno = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, depno);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Department department = new Department();
                department.setDepartmentID(rs.getInt("depno"));
                department.setDepartmentName(rs.getString("nombre"));
                department.setDepartmentAddress(rs.getString("ubicacion"));
                return Optional.of(department);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseQueryException("Error querying department in PostgreSQL", e);
        }
    }

    /**
     * Retrieves all departments from the PostgreSQL database.
     *
     * @return A list of all departments.
     */
    public List<Department> getAllPostgreDepartments() {
        String query = "SELECT * FROM department";
        List<Department> departments = new ArrayList<>();

        try (Connection connection = dbManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Department department = new Department();
                department.setDepartmentID(rs.getInt("depno"));
                department.setDepartmentName(rs.getString("nombre"));
                department.setDepartmentAddress(rs.getString("ubicacion"));
                departments.add(department);
            }
            return departments;
        } catch (SQLException e) {
            throw new DatabaseQueryException("Error querying all departments in PostgreSQL", e);
        }
    }

    /**
     * Stores an employee in the PostgreSQL database.
     *
     * @param employee The employee to store.
     * @return true if the employee was successfully stored, false otherwise.
     */
    public boolean storePostgreEmployee(Employee employee) {
        String query = "INSERT INTO employee (nombre, puesto, depno) VALUES (?,?,?)";
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, employee.getEmployeeName());
            pstmt.setString(2, employee.getEmployeePosition());
            pstmt.setInt(3, employee.getDepartmentID());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DatabaseInsertException("Could not store the employee in PostgreSQL", e);
        }
    }

    /**
     * Checks if an employee exists in the database.
     *
     * @param employee The employee to check.
     * @return true if the employee exists, false otherwise.
     */
    private boolean isEmployeeInDB(Employee employee) {
        String query = "SELECT * FROM employee WHERE empno = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, employee.getEmployeeID());
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new DatabaseQueryException("Error checking if employee exists in PostgreSQL", e);
        }
    }

    /**
     * Updates an employee in the PostgreSQL database.
     *
     * @param empno The employee number to update.
     * @return An Optional containing the updated employee if successful, or an empty Optional if not.
     */
    public Optional<Employee> updatePostgreEmployee(Integer empno) {
        Optional<Employee> employeeOptional = getEmployee(empno);
        if (employeeOptional.isEmpty()) {
            System.out.println("Department with ID " + empno + " could not be found.");
            return Optional.empty();
        }

        Employee employeeToUpdate = employeeOptional.get();
        Set<String> excludedFields = Set.of("employeeID", "departmentID", "department"); // Exclude primary key from modification

        Optional<Field> fieldToUpdate = ObjectFieldsUtil.promptUserForFieldSelection(employeeToUpdate, excludedFields);
        if (fieldToUpdate.isEmpty()) return Optional.empty();

        return modifyEmployeeField(employeeToUpdate, fieldToUpdate.get());
    }

    /**
     * Modifies a specific field of an employee in the PostgreSQL database.
     *
     * @param employee The employee to modify.
     * @param field    The field to modify.
     * @return An Optional containing the modified employee if successful, or an empty Optional if not.
     */
    private Optional<Employee> modifyEmployeeField(Employee employee, Field field) {
        String column;
        try {
            Object newValue = ObjectFieldsUtil.promptUserForNewValue(field);
            field.set(employee, newValue);

            if (!ValidationUtil.isValidObject(employee, Employee.class)) return Optional.empty();

            if (field.getName().equalsIgnoreCase("employeeName")) {
                column = "nombre";
            } else if (field.getName().equalsIgnoreCase("employeePosition")) {
                column = "puesto";
            } else {
                column = "depno";
            }
            String query = "UPDATE employee SET " + column + " = ? WHERE empno = ?";
            try (Connection connection = dbManager.getConnection();
                 PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setObject(1, newValue);
                pstmt.setInt(2, employee.getEmployeeID());
                pstmt.executeUpdate();
                System.out.println("Employee successfully updated");
                return Optional.of(employee);
            }
        } catch (Exception e) {
            System.out.println("Employee could not be updated");
            throw new DatabaseQueryException("Employee could not be updated in PostgreSQL", e);
        }
    }

    /**
     * Deletes an employee from the PostgreSQL database.
     *
     * @param employee The employee to delete.
     * @return true if the employee was successfully deleted, false otherwise.
     */
    public boolean deletePostgreEmployee(Employee employee) {
        if (isEmployeeInDB(employee)) {
            String query = "DELETE FROM employee WHERE empno = ?";
            try (Connection connection = dbManager.getConnection();
                 PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, employee.getEmployeeID());
                pstmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                throw new DatabaseDeleteException("Could not delete the employee in PostgreSQL", e);
            }
        }
        return false;
    }

    /**
     * Retrieves an employee from the PostgreSQL database by its employee number.
     *
     * @param empno The employee number to retrieve.
     * @return An Optional containing the employee if found, or an empty Optional if not.
     */
    public Optional<Employee> getEmployee(Integer empno) {
        String query = "SELECT * FROM employee WHERE empno = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, empno);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeID(rs.getInt("empno"));
                employee.setEmployeeName(rs.getString("nombre"));
                employee.setEmployeePosition(rs.getString("puesto"));
                employee.setDepartmentID(rs.getInt("depno"));
                return Optional.of(employee);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseQueryException("Error querying employee in PostgreSQL", e);
        }
    }

    /**
     * Retrieves all employees from the PostgreSQL database.
     *
     * @return A list of all employees.
     */
    public List<Employee> getAllPostgreEmployees() {
        String query = "SELECT * FROM employee";
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = dbManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeID(rs.getInt("empno"));
                employee.setEmployeeName(rs.getString("nombre"));
                employee.setEmployeePosition(rs.getString("puesto"));
                employee.setDepartmentID(rs.getInt("depno"));
                employees.add(employee);
            }
            return employees;
        } catch (SQLException e) {
            throw new DatabaseQueryException("Error querying all employees in PostgreSQL", e);
        }
    }
}