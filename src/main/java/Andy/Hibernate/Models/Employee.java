package Andy.Hibernate.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "empleado")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empleado_id_gen")
    @SequenceGenerator(name = "empleado_id_gen", sequenceName = "empleado_empno_seq", allocationSize = 1)
    @Column(name = "empno", nullable = false)
    private Integer id;

    @Column(name = "nombre", length = 10)
    private String Employee_Name;

    @Column(name = "puesto", length = 15)
    private String Employee_Job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depno")
    private Department Department_ID;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmployee_Name() {
        return Employee_Name;
    }
    public void setEmployee_Name(String Employee_Name) {
        this.Employee_Name = Employee_Name;
    }

    public String getEmployee_Job() {
        return Employee_Job;
    }
    public void setEmployee_Job(String Employee_Job) {
        this.Employee_Job = Employee_Job;
    }

    public Department getDepartment_ID() {
        return Department_ID;
    }
    public void setDepartment_ID(Department Department_ID) {
        this.Department_ID = Department_ID;
    }
}