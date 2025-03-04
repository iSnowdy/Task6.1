package Andy.Hibernate.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "empleado")
public class HEmployee extends Models.Employee implements DatabaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empleado_id_gen")
    @SequenceGenerator(name = "empleado_id_gen", sequenceName = "empleado_empno_seq", allocationSize = 1)
    @Column(name = "empno", nullable = false)
    private Integer id;
    @Column(name = "nombre", length = 10)
    private String employeeName;
    @Column(name = "puesto", length = 15)
    private String employeePosition;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "depno")
    private HDepartment HDepartment;

    public HEmployee() {
        super();
    }

    // Employee ID is auto generated. We should not need to pass it as a parameter to the constructor
    public HEmployee(Models.Employee employee) {
        this.employeeName = employee.getEmployeeName();
        this.employeePosition = employee.getEmployeePosition();
        this.HDepartment = new HDepartment(employee.getDepartment());
    }

    /**
     * Overrides equals to check equality based on the primary key if available,
     * or on a combination of business keys if not yet persisted.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HEmployee)) return false;

        HEmployee HEmployee = (HEmployee) o;
        return
                HEmployee.employeeName.equals(employeeName) &&
                HEmployee.employeePosition.equals(employeePosition) &&
                HEmployee.HDepartment.getId().equals(HDepartment.getID());
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
                "Department ID: " + HDepartment.getID() + "\n" +
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