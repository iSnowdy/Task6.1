package Andy.Tests;

import Andy.Hibernate.Database.DepartmentImplementation;
import Andy.Hibernate.Database.EmployeeImplementation;
import Andy.Hibernate.Database.DatabaseManager;
import Andy.Hibernate.Models.HDepartment;
import Andy.Hibernate.Models.HEmployee;
import Exceptions.DatabaseDeleteException;
import Exceptions.DatabaseException;
import Exceptions.DatabaseInsertException;
import Exceptions.DatabaseQueryException;
import Models.Enum.DepartmentNames;
import Models.Enum.EmployeePosition;

public class MainHibernate {
    private static DepartmentImplementation dImpl;
    private static EmployeeImplementation eImpl;

    public static void main(String[] args) {

        System.out.println("Testing Hibernate JPA...");
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            databaseManager.openDB();

            dImpl = new DepartmentImplementation(databaseManager);
            eImpl = new EmployeeImplementation(databaseManager);

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

            databaseManager.closeDB();
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
