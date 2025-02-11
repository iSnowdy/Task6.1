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
    private String employeeName;
    @Column(name = "puesto", length = 15)
    private String employeeJob;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depno")
    private Department department;

    public Employee() {}

    // Employee ID is auto generated. We should not need to pass it as a parameter to the constructor
    public Employee(String departmentName, String departmentAddress, Department department) {
        this.employeeName = departmentName;
        this.employeeJob = departmentAddress;
        this.department = department;
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
                "Department ID: " + department.getId() + "\n" +
                "-----------------------\n";
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

    public Department getDepartment() {
        return department;
    }
    public void setDepartment(Department Department_ID) {
        this.department = Department_ID;
    }
}