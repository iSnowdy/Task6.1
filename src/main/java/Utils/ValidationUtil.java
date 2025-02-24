package Utils;

import Andy.Hibernate.Models.HDepartment;
import Andy.Hibernate.Models.HEmployee;
import Models.Department;
import Models.Employee;
import Models.Enum.DepartmentNames;
import Models.Enum.EmployeePosition;

public class ValidationUtil {
    private static final int DEPARTMENT_ADDRESS_LENGTH = 13;
    private static final int EMPLOYEE_NAME_LENGTH = 10;

    /**
     * Validates a standard Department model.
     *
     * @param department Department object.
     * @return {@code true} if valid, {@code false} otherwise.
     */

    public static boolean isValidDepartment(Department department) {
        if (department == null) return false;
        return isValidDepartmentName(department.getDepartmentName()) &&
                isValidDepartmentAddress(department.getDepartmentAddress()) &&
                isValidDepartmentId(department.getDepartmentID());
    }

    /**
     * Validates a Hibernate Department model (HDepartment).
     *
     * @param hDepartment Hibernate Department object.
     * @return {@code true} if valid, {@code false} otherwise.
     */

    public static boolean isValidHDepartment(HDepartment hDepartment) {
        if (hDepartment == null) return false;
        return isValidDepartmentName(hDepartment.getDepartmentName()) &&
                isValidDepartmentAddress(hDepartment.getDepartmentAddress()) &&
                isValidDepartmentId(hDepartment.getId());
    }

    /**
     * Validates a department name using a for loop.
     *
     * @param departmentNameToAnalyze The name to validate.
     * @return {@code true} if the department name is valid, {@code false} otherwise.
     */

    public static boolean isValidDepartmentName(String departmentNameToAnalyze) {
        if (departmentNameToAnalyze == null) return false;
        for (DepartmentNames departmentName : DepartmentNames.values()) {
            if (departmentName.getDepartmentNameAsString().equals(departmentNameToAnalyze)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates a department address.
     *
     * @param departmentAddressToAnalyze The address to validate.
     * @return {@code true} if valid, {@code false} otherwise.
     */

    public static boolean isValidDepartmentAddress(String departmentAddressToAnalyze) {
        if (departmentAddressToAnalyze == null) return false;
        return departmentAddressToAnalyze.length() <= DEPARTMENT_ADDRESS_LENGTH;
    }

    /**
     * Validates that the department ID is positive.
     *
     * @param departmentID The department ID.
     * @return {@code true} if positive, {@code false} otherwise.
     */

    public static boolean isValidDepartmentId(int departmentID) {
        return departmentID > 0;
    }

    /**
     * Validates a standard Employee model.
     *
     * @param employee Employee object.
     * @return {@code true} if valid, {@code false} otherwise.
     */

    public static boolean isValidEmployee(Employee employee) {
        if (employee == null) return false;
        return isValidEmployeeName(employee.getEmployeeName()) &&
                isValidEmployeePosition(employee.getEmployeePosition()) &&
                isValidEmployeeId(employee.getEmployeeID()) &&
                isValidDepartmentId(employee.getDepartmentID()); // Ensure department exists
    }

    /**
     * Validates a Hibernate Employee model (HEmployee).
     *
     * @param hEmployee Hibernate Employee object.
     * @return {@code true} if valid, {@code false} otherwise.
     */

    public static boolean isValidHEmployee(HEmployee hEmployee) {
        if (hEmployee == null) return false;
        return isValidEmployeeName(hEmployee.getEmployeeName()) &&
                isValidEmployeePosition(hEmployee.getEmployeePosition()) &&
                isValidEmployeeId(hEmployee.getId()) &&
                isValidDepartmentId(hEmployee.getDepartment().getId());
    }

    /**
     * Validates an employee name.
     *
     * @param employeeNameToAnalyze The name to validate.
     * @return {@code true} if valid, {@code false} otherwise.
     */

    public static boolean isValidEmployeeName(String employeeNameToAnalyze) {
        if (employeeNameToAnalyze == null) return false;
        return employeeNameToAnalyze.length() <= EMPLOYEE_NAME_LENGTH;
    }

    /**
     * Validates an employee position using a for loop.
     *
     * @param positionToAnalyze The position to validate.
     * @return {@code true} if valid, {@code false} otherwise.
     */

    public static boolean isValidEmployeePosition(String positionToAnalyze) {
        if (positionToAnalyze == null) return false;
        for (EmployeePosition employeePosition : EmployeePosition.values()) {
            if (employeePosition.getEmployeePositionAsString().equals(positionToAnalyze)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates that the employee ID is positive.
     *
     * @param employeeID The employee ID.
     * @return {@code true} if positive, {@code false} otherwise.
     */

    public static boolean isValidEmployeeId(int employeeID) {
        return employeeID > 0;
    }

    /**
     * Validates an object of type {@code <T>}, determining its type automatically.
     * The call to isAssignableFrom() is needed because inheritance is in play.
     *
     * @param object The object to validate.
     * @param clazz  The class of the object in order to determine its type.
     * @return {@code true} if valid, {@code false} otherwise.
     */

    public static <T> boolean isValidObject(T object, Class<T> clazz) {
        if (object == null) return false;

        if (clazz.isAssignableFrom(Department.class)) {
            return isValidDepartment((Department) object);
        } else if (clazz.isAssignableFrom(HDepartment.class)) {
            return isValidHDepartment((HDepartment) object);
        } else if (clazz.isAssignableFrom(Employee.class)) {
            return isValidEmployee((Employee) object);
        } else if (clazz.isAssignableFrom(HEmployee.class)) {
            return isValidHEmployee((HEmployee) object);
        }
        return false;
    }
}
