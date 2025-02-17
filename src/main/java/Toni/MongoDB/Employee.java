package Toni.MongoDB;


import jakarta.persistence.Column;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Employee extends Models.Employee {
//    @Column(name = "nombre", length = 10)
//    @Column(name = "puesto", length = 15)


    @BsonId
    private ObjectId employe_ID;
    @BsonProperty("id")
    private int employeeID; // FK auto-generated
    @BsonProperty("name")
    private String employeeName;
    @BsonProperty("position")
    private String employeePosition;

    private Department department; // FK references Department

    public Employee(String employeeName, String employeePosition, Department department) {
        this.employeeName = employeeName;
        this.employeePosition = employeePosition;
        this.department = department;
    }

    public Employee(int employeeID, String employeeName, String employeePosition, Department department) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.employeePosition = employeePosition;
        this.department = department;
    }
    public Employee(Models.Employee employee) {
        this.employeeID = employee.getEmployeeID();
        this.employeeName = employee.getEmployeeName();
        this.employeePosition = employee.getEmployeePosition();
        this.department = (Department) employee.getDepartment();
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
                "Employee Position: " + employeePosition + "\n" +
                "Department: " + department + "\n" +
                "-----------------------\n";
    }

    public Document toDocument(){
        return new Document("_id",employe_ID)
                .append("id",employeeID)
                .append("name",employeeName)
                .append("position",employeePosition);
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

    public String getEmployeePosition() {
        return employeePosition;
    }
    public void setEmployeePosition(String employeePosition) {
        this.employeePosition = employeePosition;
    }

    public Department getDepartment() {
        return department;
    }
    public void setDepartment(Department department) {
        this.department = department;
    }
}
