package Andy.Tests;

import Andy.Hibernate.Database.DepartmentImplementationHibernate;
import Andy.Hibernate.Database.EmployeeImplementationHibernate;
import Andy.Hibernate.Database.Util.HibernateManager;
import Andy.Hibernate.Models.HDepartment;
import Andy.Hibernate.Models.HEmployee;
import Exceptions.DatabaseDeleteException;
import Exceptions.DatabaseException;
import Exceptions.DatabaseInsertException;
import Exceptions.DatabaseQueryException;
import Models.Enum.DepartmentNames;
import Models.Enum.EmployeePosition;

/*
Optional<T>
It is a safe way of returning an object without verification if it is null or not, making
the code more legible and secure, as it does not throw NullPointerExceptions either.

There are 3 ways of creating an optional:
    1. Optional.of() Does not allow for null, so it does throw NullPointerException. So we
    must make sure the value inside is not null.
    2. Optional.empty() Simply returns and empty Optional.
    3. Optional.ofNullable() Allows null, but it does not throw NullPointerException.

There are 3 ways of recovering an optional:
    1. Optional.get() It is not safe, because it throws an exception if the Optional is empty.
    Therefore, defeats the purpose of the tool.
    2. Optional.orElse() Throws a default parameter if it is empty. This parameter must be of the same
    type of the generic <T>.
    3. Optional.orElseThrow() Throws a specific, controlled exception.
    4. Optional.orElseGet() Calls a backup method is the Optional is empty.
    5. Optional.ifPresent() Executes the given Predicate only if it has a value inside. It is the
    safe and elegant way of Optional.get(), since it does not act if the Optional is empty.
 */

public class MainHibernate {
    private static DepartmentImplementationHibernate dImpl;
    private static EmployeeImplementationHibernate eImpl;

    public static void main(String[] args) {

        System.out.println("Testing Hibernate JPA...");
        try {
            HibernateManager hibernateManager = HibernateManager.getInstance();
            hibernateManager.openDB();

            dImpl = new DepartmentImplementationHibernate();
            eImpl = new EmployeeImplementationHibernate();

            /*System.out.println(eImpl.findEmployeeByID(18));
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("Before printing...");
            eImpl.findEmployeeByID(18).ifPresent(System.out::println);
            System.out.println(eImpl.findEmployeeByID(18).orElse(null));
            System.out.println(eImpl.findEmployeeByID(18).get());*/

            insertData();
            queryData();
            updateData();
            deleteData();

            hibernateManager.closeDB();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        System.out.println("Closing DB...");
    }

    public static void insertData() {
        System.out.println("\nInserting data...");
        try {
            // Departments
            HDepartment department1 = new HDepartment(1, DepartmentNames.CONTABILIDAD.getDepartmentNameAsString(), "Madrid");
            HDepartment department2 = new HDepartment(2, DepartmentNames.MARKETING.getDepartmentNameAsString(), "Barcelona");
            HDepartment department3 = new HDepartment(3, DepartmentNames.VENTAS.getDepartmentNameAsString(), "Alicante");
            HDepartment department4 = new HDepartment(4, DepartmentNames.LOGISTICA.getDepartmentNameAsString(), "Valencia");

            dImpl.addDepartment(department1);
            dImpl.addDepartment(department2);
            dImpl.addDepartment(department3);
            dImpl.addDepartment(department4);

            // Employees
            HEmployee employee1 = new HEmployee("Juan", EmployeePosition.ANALISTA.getEmployeePositionAsString(), department1);
            HEmployee employee2 = new HEmployee("María", EmployeePosition.VENDEDOR.getEmployeePositionAsString(), department2);
            HEmployee employee3 = new HEmployee("Carlos", EmployeePosition.RESPONSABLE.getEmployeePositionAsString(), department3);
            HEmployee employee4 = new HEmployee("Ana", EmployeePosition.PRESIDENTE.getEmployeePositionAsString(), department4);

            eImpl.addEmployee(employee1);
            eImpl.addEmployee(employee2);
            eImpl.addEmployee(employee3);
            eImpl.addEmployee(employee4);
        } catch (DatabaseInsertException e) {
            e.printStackTrace();
        }

    }

    public static void queryData() {
        System.out.println("\nQuerying data...");
        try {
            System.out.println("---- ALL DEPARTMENTS ----");
            dImpl.findAllDepartments().forEach(System.out::println);
            System.out.println("Finding Department By ID:");
            System.out.println(dImpl.findDepartmentByID(3).get());


            System.out.println("---- ALL EMPLOYEES ----");
            eImpl.findAllEmployees().forEach(System.out::println);
            System.out.println("Finding Employee By ID:");
            System.out.println(eImpl.findEmployeeByID(20).get());
        } catch (DatabaseQueryException e) {
            e.printStackTrace();
        }

    }

    public static void updateData() {
        System.out.println("\nUpdating data...");
        try {
            System.out.println("Modifying Department...");
            System.out.println(dImpl.findDepartmentByID(1).get());
            dImpl.updateDepartment(1);
            System.out.println(dImpl.findDepartmentByID(1).get());


            System.out.println("Modifying Employee...");
            System.out.println(eImpl.findEmployeeByID(17));
            eImpl.updateEmployee(17);
            System.out.println(eImpl.findEmployeeByID(17).get());
        } catch (DatabaseQueryException e) {
            e.printStackTrace();
        }

    }

    public static void deleteData() {
        System.out.println("\nDeleting data...");
        try {
            System.out.println("Deleting Employee...");
            System.out.println(eImpl.findEmployeeByID(18).get());
            eImpl.deleteEmployee(18);
            System.out.println(eImpl.findEmployeeByID(18).get());

            // TODO: Printing an empty get still sends fatal exception?
            // TODO: FK exception. Cannot delete a department with employees in it

            System.out.println("Deleting Department...");
            System.out.println(dImpl.findDepartmentByID(4).get());
            dImpl.deleteDepartment(4);
            System.out.println(dImpl.findDepartmentByID(4).get());

        } catch (DatabaseDeleteException e) {
            e.printStackTrace();
        }
    }
}
