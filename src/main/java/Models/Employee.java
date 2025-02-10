package Models;

import Models.Enum.EmployeePosition;

public class Employee {
    private int employeeID; // FK auto-generated
    private String employeeName;
    private EmployeePosition employeePosition;

    private int departmentID;
    private Department department; // FK references Department

    public Employee(String employeeName, EmployeePosition employeePosition, int departmentID, Department department) {
        this.employeeName = employeeName;
        this.employeePosition = employeePosition;
        this.departmentID = departmentID;
        this.department = department;
    }


    // Compares if the given object as parameter is the same as the one calling the method
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Employee employee = (Employee) object;
        return this.employeeID == employee.getEmployeeID();
    }

    @Override
    public String toString() {
        return
                "-----------------------\n" +
                "Employee Information\n" +
                "-----------------------\n" +
                "Employee ID: " + employeeID + "\n" +
                "Employee Name: " + employeeName + "\n" +
                "Employee Position: " + employeePosition.getEmployeePositionAsString() + "\n" +
                "Department ID: " + departmentID;
    }


    // Getters and Setters
    public int getEmployeeID() {
        return employeeID;
    }
    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public EmployeePosition getEmployeePosition() {
        return employeePosition;
    }
    public void setEmployeePosition(EmployeePosition employeePosition) {
        this.employeePosition = employeePosition;
    }

    public int getDepartmentID() {
        return departmentID;
    }
    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public Department getDepartment() {
        return department;
    }
    public void setDepartment(Department department) {
        this.department = department;
    }
}
