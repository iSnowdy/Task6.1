package Toni.MongoDB;


import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Employee extends Models.Employee {
//    @Column(name = "nombre", length = 10)
//    @Column(name = "puesto", length = 15)


    @BsonId
    private ObjectId employee_ID;
    @BsonProperty("id")
    private int employeeID; // FK auto-generated
    @BsonProperty("name")
    private String employeeName;
    @BsonProperty("position")
    private String employeePosition;

    private int departmentID; // FK references Department

    public Employee(String employeeName, String employeePosition, Department department) {
        this.employee_ID = new ObjectId();
        this.employeeName = employeeName;
        this.employeePosition = employeePosition;
    }

//    public Employee(int employeeID, String employeeName, String employeePosition, Department department) {
//        this.employee_ID = new ObjectId();
//        this.employeeID = employeeID;
//        this.employeeName = employeeName;
//        this.employeePosition = employeePosition;
//        this.departmentID = department;
//    }

    public Employee(Models.Employee employee) {
        this.employee_ID = new ObjectId();
        this.employeeID = employee.getEmployeeID();
        this.employeeName = employee.getEmployeeName();
        this.employeePosition = employee.getEmployeePosition();
        this.departmentID = employee.getDepartmentID();
    }

    public Employee(Document doc) {
        this.employee_ID = doc.getObjectId("_id");
        this.employeeID = doc.getInteger("id");
        this.employeeName = doc.getString("name");
        this.employeePosition = doc.getString("position");
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
                """
                -----------------------
                Employee Information
                -----------------------
                Employee ID: %d \n
                Employee Name: %s \n
                Employee Position: %s \n
                Department ID: %s \n
                -----------------------\n""".formatted(getEmployeeID(),getEmployeeName(),getEmployeePosition(), getDepartmentID());
    }

    public Document toDocument(){
        return new Document("_id",employee_ID)
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

    public int getDepartmentID() {
        return departmentID;
    }
    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

}
