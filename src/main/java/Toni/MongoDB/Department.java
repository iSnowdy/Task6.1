package Toni.MongoDB;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Department extends Models.Department {

//        @Column(name = "nombre", length = 14)
//        @Column(name = "ubicacion", length = 13)

    @BsonId
    private ObjectId department_ID; // PK
    @BsonProperty("id")
    private int departmentID; // PK
    @BsonProperty("name")
    private String departmentName; // VARCHAR 14
    @BsonProperty("address")
    private String departmentAddress; // VARCHAR 13

    public Department(final int departmentID, String departmentName, String departmentAddress) {
        super(departmentID,
                departmentName,
                departmentAddress);
    }

    public Department(Models.Department dep) {
        super(dep.getDepartmentID(),
                dep.getDepartmentName(),
                dep.getDepartmentAddress());
    }

    public Department(Document doc) {
        super(
                doc.getInteger("id"),
                doc.getString("name"),
                doc.getString("address"));
        this.department_ID = doc.getObjectId("_id");
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

    public Document toDocument(){
        return new Document("_id",department_ID)
                .append("id",departmentID)
                .append("name",departmentName)
                .append("address",departmentAddress);
    }
}
