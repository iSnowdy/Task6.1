package Hugo.PostgreSQL;

import Exceptions.DatabaseDeleteException;
import Exceptions.DatabaseInsertException;
import Exceptions.DatabaseQueryException;
import Models.Department;
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

public abstract class PostgreBaseImplementation<T> {
    private final Class<T> clazz; // To know the type of class being managed
    private final DatabaseManager dbManager;

    private final String pk;

    public PostgreBaseImplementation(Class<T> clazz, DatabaseManager dbManager) {
        this.clazz = clazz;
        this.dbManager = dbManager;
        this.pk = clazz.getDeclaredFields()[0].getName();
    }

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

    public boolean storePostgreDepartment(Department department) {
        if (!isDepartmentInDB(department)) {
            String query = "INSERT INTO department (depno, nombre, ubicacion) VALUES (?, ?, ?)";
            try {
                Connection connection = dbManager.getConnection();
                System.out.println(dbManager.isConnectionOpen());
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

    public Optional<Department> updatePostgreDepartment(Integer depno) {
        Optional<Department> departmentOptional = getDepartment(depno);
        if (departmentOptional.isEmpty()) {
            System.out.println("Department with ID " + depno + " could not be found.");
            return Optional.empty();
        }

        Department departmentToUpdate = departmentOptional.get();
        Set<String> excludedFields = Set.of("depno"); // Exclude primary key from modification

        Optional<Field> fieldToUpdate = ObjectFieldsUtil.promptUserForFieldSelection(departmentToUpdate, excludedFields);
        if (fieldToUpdate.isEmpty()) return Optional.empty();

        return modifyDepartmentField(departmentToUpdate, fieldToUpdate.get());
    }

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

    public boolean deletePostgreDepartment(Department department) {
        if (isDepartmentInDB(department)) {
            String query = "DELETE FROM department WHERE depno = ?";
            try (Connection connection = dbManager.getConnection();
                 PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, department.getDepartmentID());
                pstmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                throw new DatabaseDeleteException("Could not delete the department in PostgreSQL", e);
            }
        }
        return false;
    }

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
}