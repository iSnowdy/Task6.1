package Hugo.Menu;

import Andy.Hibernate.Models.HDepartment;
import Models.Department;
import Models.Employee;

import java.util.List;
import java.util.Scanner;

public class UserInteractions {

    public UserInteractions() {
    }

    private static Scanner scan = new Scanner(System.in);

    // TODO Añadir verificaciones
    public Department addDepartmentInteraction() {
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

    public Employee addEmployeeInteraction() {
        System.out.println("Set employee name: ");
        String name = scan.nextLine();
        System.out.println("Set employee position: ");
        String position = scan.nextLine();
        System.out.println("Set employee department ID: ");
        int id = scan.nextInt();
        scan.nextLine();

        // TODO buscar departamento por el ID introducido
        List<Department> department = null;
        Employee employee = new Employee(name, position, id, (Department) department);
        return employee;
    }

    public Object getObjectID() {
        System.out.println("Set ID: ");
        int id = scan.nextInt();
        scan.nextLine();

        return id;
    }
}
