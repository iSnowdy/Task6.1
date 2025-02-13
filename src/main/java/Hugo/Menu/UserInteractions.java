package Hugo.Menu;

import Models.Department;
import Models.Employee;

import java.util.Scanner;

public class UserInteractions {

    private static Scanner scan = new Scanner(System.in);


    // TODO Añadir verificaciones
    public static Department addDepartmentInteraction() {
        System.out.println("Set department ID: ");
        int id = scan.nextInt();
        scan.nextLine();
        System.out.println("Set department name: ");
        String name = scan.nextLine();
        System.out.println("Set department address: ");
        String address = scan.nextLine();

        Department department = new Department(id, name, address);

        return department;
    }

    public static Employee addEmployeeInteraction() {
        System.out.println("Set employee name: ");
        String name = scan.nextLine();
        System.out.println("Set employee position: ");
        String position = scan.nextLine();
        System.out.println("Set employee department ID: ");
        int id = scan.nextInt();
        scan.nextLine();

        // TODO buscar departamento por el ID introducido
        Department department = null;
        Employee employee = new Employee(name, position, id, department);
        return employee;
    }

    public static Object getObjectID() {
        System.out.println("Set ID: ");
        int id = scan.nextInt();
        scan.nextLine();

        return id;
    }
}
