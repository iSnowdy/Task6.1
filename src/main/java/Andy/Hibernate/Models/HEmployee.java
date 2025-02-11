package Andy.Hibernate.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "empleado")
public class HEmployee implements DatabaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empleado_id_gen")
    @SequenceGenerator(name = "empleado_id_gen", sequenceName = "empleado_empno_seq", allocationSize = 1)
    @Column(name = "empno", nullable = false)
    private Integer id;
    @Column(name = "nombre", length = 10)
    private String employeeName;
    @Column(name = "puesto", length = 15)
    private String employeeJob;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depno")
    private HDepartment HDepartment;

    public HEmployee() {}

    // Employee ID is auto generated. We should not need to pass it as a parameter to the constructor
    public HEmployee(String departmentName, String departmentAddress, HDepartment HDepartment) {
        this.employeeName = departmentName;
        this.employeeJob = departmentAddress;
        this.HDepartment = HDepartment;
    }


    @Override
    public String toString() {
        return
                "-----------------------\n" +
                "Employee Information\n" +
                "-----------------------\n" +
                "Employee ID: " + id + "\n" +
                "Employee Name: " + employeeName + "\n" +
                "Employee Position: " + employeeJob + "\n" +
                "Department ID: " + HDepartment.getId() + "\n" +
                "-----------------------\n";
    }

    @Override
    public Integer getID() {
        return id;
    }


    // Getters and Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }
    public void setEmployeeName(String Employee_Name) {
        this.employeeName = Employee_Name;
    }

    public String getEmployeeJob() {
        return employeeJob;
    }
    public void setEmployeeJob(String Employee_Job) {
        this.employeeJob = Employee_Job;
    }

    public HDepartment getDepartment() {
        return HDepartment;
    }
    public void setDepartment(HDepartment HDepartment_ID) {
        this.HDepartment = HDepartment_ID;
    }
}