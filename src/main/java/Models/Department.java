package Models;

public class Department {
    private final int departmentID; // PK
    private String departmentName; // VARCHAR 14
    private String departmentAddress; // VARCHAR 13

    public Department(final int departmentID, String departmentName, String departmentAddress) {
        this.departmentID = departmentID;
        this.departmentName = departmentName;
        this.departmentAddress = departmentAddress;
    }


    // Compares if the given object as parameter is the same as the one calling the method
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Department department = (Department) object;
        return this.departmentID == department.getDepartmentID()
                && this.departmentName.equals(department.getDepartmentName());
    }

    @Override
    public String toString() {
        return
                "-----------------------\n" +
                "Department Information\n" +
                "-----------------------\n" +
                "Department ID: " + departmentID + "\n" +
                "Department Name: " + departmentName + "\n" +
                "Department Address: " + departmentAddress + "\n" +
                "-----------------------\n";
    }


    // Getters and Setters
    public int getDepartmentID() {
        return departmentID;
    }

    public String getDepartmentName() {
        return departmentName;
    }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentAddress() {
        return departmentAddress;
    }
    public void setDepartmentAddress(String departmentAddress) {
        this.departmentAddress = departmentAddress;
    }
}
