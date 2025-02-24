package Andy.Hibernate.Models;

import jakarta.persistence.*;

import java.util.Objects;

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
    private String employeePosition;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depno")
    private HDepartment HDepartment;

    public HEmployee() {}

    // Employee ID is auto generated. We should not need to pass it as a parameter to the constructor
    public HEmployee(String departmentName, String departmentAddress, HDepartment HDepartment) {
        this.employeeName = departmentName;
        this.employeePosition = departmentAddress;
        this.HDepartment = HDepartment;
    }

    /**
     * Overrides equals to check equality based on the primary key if available,
     * or on a combination of business keys if not yet persisted.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HEmployee)) return false;

        HEmployee employee = (HEmployee) o;
        return
                employee.employeeName.equals(employeeName) &&
                employee.employeePosition.equals(employeePosition) &&
                employee.HDepartment.getId().equals(HDepartment.getId());
    }

    @Override
    public String toString() {
        return
                "-----------------------\n" +
                "Employee Information\n" +
                "-----------------------\n" +
                "Employee ID: " + id + "\n" +
                "Employee Name: " + employeeName + "\n" +
                "Employee Position: " + employeePosition + "\n" +
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

    public String getEmployeePosition() {
        return employeePosition;
    }
    public void setEmployeePosition(String Employee_Job) {
        this.employeePosition = Employee_Job;
    }

    public HDepartment getDepartment() {
        return HDepartment;
    }
    public void setDepartment(HDepartment HDepartment_ID) {
        this.HDepartment = HDepartment_ID;
    }
}