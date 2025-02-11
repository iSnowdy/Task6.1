package Andy.Hibernate.Models;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "departamento")
public class Department {
    @Id
    @Column(name = "depno", nullable = false)
    private Integer id;
    @Column(name = "nombre", length = 14)
    private String departmentName;
    @Column(name = "ubicacion", length = 13)
    private String departmentAddress;
    @OneToMany
    private Set<Andy.Hibernate.Models.Employee> EmployeesList = new LinkedHashSet<>();

    public Department() {}

    public Department(final int departmentID, String departmentName, String departmentAddress) {
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

    public Set<Andy.Hibernate.Models.Employee> getEmployeesList() {
        return EmployeesList;
    }
    public void setEmployeesList(Set<Andy.Hibernate.Models.Employee> EmployeesList) {
        this.EmployeesList = EmployeesList;
    }
}