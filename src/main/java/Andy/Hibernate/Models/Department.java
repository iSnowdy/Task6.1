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
    private String Department_Name;

    @Column(name = "ubicacion", length = 13)
    private String Department_Address;

    @OneToMany
    private Set<Andy.Hibernate.Models.Employee> EmployeesList = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepartment_Name() {
        return Department_Name;
    }
    public void setDepartment_Name(String Department_Name) {
        this.Department_Name = Department_Name;
    }

    public String getDepartment_Address() {
        return Department_Address;
    }
    public void setDepartment_Address(String Department_Address) {
        this.Department_Address = Department_Address;
    }

    public Set<Andy.Hibernate.Models.Employee> getEmployeesList() {
        return EmployeesList;
    }
    public void setEmployeesList(Set<Andy.Hibernate.Models.Employee> EmployeesList) {
        this.EmployeesList = EmployeesList;
    }
}