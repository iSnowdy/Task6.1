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


    public static boolean isValidDepartment(Department department) {
        return isValidDepartmentName(department.getDepartmentName()) &&
                isValidTextLength(department.getDepartmentAddress(), DEPARTMENT_ADDRESS_LENGTH);
    }

    public static boolean isValidHDepartment(HDepartment hDepartment) {
        return isValidDepartmentName(hDepartment.getDepartmentName()) &&
                isValidTextLength(hDepartment.getDepartmentAddress(), DEPARTMENT_ADDRESS_LENGTH);
    }

    private static boolean isValidDepartmentName(String departmentNameToAnalyze) {
        for (DepartmentNames departmentName : DepartmentNames.values()) {
            if (!departmentName.toString().equals(departmentNameToAnalyze)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidTextLength(String text, int lengthToValidate) {
        return text.length() <= lengthToValidate;
    }

    public static boolean isValidEmployee(Employee employee) {
        return isValidEmployeePosition(employee.getEmployeePosition()) &&
                isValidTextLength(employee.getEmployeeName(), EMPLOYEE_NAME_LENGTH);
    }

    public static boolean isValidHEmployee(HEmployee hEmployee) {
        return isValidEmployeePosition(hEmployee.getEmployeePosition()) &&
                isValidTextLength(hEmployee.getEmployeeName(), EMPLOYEE_NAME_LENGTH);
    }

    private static boolean isValidEmployeePosition(String positionToAnalyze) {
        for (EmployeePosition employeePosition : EmployeePosition.values()) {
            if (!employeePosition.toString().equals(positionToAnalyze)) {
                return false;
            }
        }
        return true;
    }
}
