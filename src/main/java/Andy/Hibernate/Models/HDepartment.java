package Andy.Hibernate.Models;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "departamento")
public class HDepartment implements DatabaseEntity {
    @Id
    @Column(name = "depno", nullable = false)
    private Integer id;
    @Column(name = "nombre", length = 14)
    private String departmentName;
    @Column(name = "ubicacion", length = 13)
    private String departmentAddress;
    @OneToMany
    private Set<HEmployee> employeesList = new LinkedHashSet<>();

    public HDepartment() {}

    public HDepartment(final int departmentID, String departmentName, String departmentAddress) {
        this.id = departmentID;
        this.departmentName = departmentName;
        this.departmentAddress = departmentAddress;
    }


    @Override
    public String toString() {
        return
                "-----------------------\n" +
                "Department Information\n" +
                "-----------------------\n" +
                "Department ID: " + id + "\n" +
                "Department Name: " + departmentName + "\n" +
                "Department Address: " + departmentAddress + "\n" +
                "-----------------------\n";
    }

    @Override
    public int getID() {
        return id;
    }


    // Getters and Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }
    public void setDepartmentName(String Department_Name) {
        this.departmentName = Department_Name;
    }

    public String getDepartmentAddress() {
        return departmentAddress;
    }
    public void setDepartmentAddress(String Department_Address) {
        this.departmentAddress = Department_Address;
    }

    public Set<HEmployee> getEmployeesList() {
        return employeesList;
    }
    public void setEmployeesList(Set<HEmployee> employeesList) {
        this.employeesList = employeesList;
    }
}