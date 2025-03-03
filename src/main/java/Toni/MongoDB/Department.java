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
        super();
        department_ID = new ObjectId();
        this.departmentID = departmentID;
        this.departmentName = departmentName;
        this.departmentAddress = departmentAddress;
    }

    public Department(Models.Department dep) {
        super();
        department_ID = new ObjectId();
        this.departmentID = dep.getDepartmentID();
        this.departmentName = dep.getDepartmentName();
        this.departmentAddress = dep.getDepartmentAddress();

    }

    public Department(Document doc) {
        super();
        this.department_ID = doc.getObjectId("_id");
        this.departmentID = doc.getInteger("id");
        this.departmentName = doc.getString("name");
        this.departmentAddress = doc.getString("address");
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
