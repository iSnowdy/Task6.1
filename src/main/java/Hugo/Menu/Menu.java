package Hugo.Menu;

import Andy.db4o.Database.DatabaseManager;

import java.util.Scanner;

public class Menu {
    private static Scanner scan = new Scanner(System.in);
    private static boolean departmentOpened = false;
    private static boolean employeeOpened = false;
    private static DatabaseManager manager;

    private UserInteractions userInteractions;

    public Menu() {
        userInteractions = new UserInteractions();
    }
    private void menuStart() {
        boolean close = false;

        while (!close) {
            menu();
            manager.openDB();
            int option = scan.nextInt();
            scan.nextLine();

            switch (option) {
                case 1 -> {
                    departmentMenu();
                }
                case 2 -> {
                    employeeMenu();
                }
                case 3 -> {
                    close = true;
                    manager.closeDB();
                }
                default -> System.out.println("Invalid option. Try again: ");
            }

        }
    }

    public void menu() {
        System.out.println("\n====================================");
        System.out.println("|            MENU                   |");
        System.out.println("====================================");
        System.out.println("| 1. Department                     |");
        System.out.println("| 2. Employee                       |");
        System.out.println("| 6. Close                          |");
        System.out.println("====================================");
        System.out.print("Select an option: ");
    }

    public void departmentMenu() {
        System.out.println("\n====================================");
        System.out.println("|        DEPARTMENT MENU            |");
        System.out.println("====================================");
        System.out.println("| 1. Add department                 |");
        System.out.println("| 2. Update department              |");
        System.out.println("| 3. Delete department              |");
        System.out.println("| 4. Find department by ID          |");
        System.out.println("| 5. Find all departments           |");
        System.out.println("| 6. Close                          |");
        System.out.println("====================================");
        System.out.print("Select an option: ");
        departmentSwitch();
    }

    public void departmentSwitch() {
        boolean close = false;

        while (!close) {
            int option = scan.nextInt();
            scan.nextLine();

            switch (option) {
                case 1 -> userInteractions.addDepartmentInteraction();
                case 2 -> userInteractions.updateDepartmentInteraction();
                case 3 -> userInteractions.deleteDepartmentInteraction();
                case 4 -> userInteractions.findDepartmentInteraction();
                case 5 -> userInteractions.findAllDepartmentInteraction();
                case 6 -> close = true;
                default -> System.out.println("Invalid option. Try again: ");
            }

        }
    }

    public void employeeMenu() {
        System.out.println("\n====================================");
        System.out.println("|         EMPLOYEE MENU             |");
        System.out.println("====================================");
        System.out.println("| 1. Add employee                   |");
        System.out.println("| 2. Update employee                |");
        System.out.println("| 3. Delete employee                |");
        System.out.println("| 4. Find employee by ID            |");
        System.out.println("| 5. Find all employees             |");
        System.out.println("| 6. Close                          |");
        System.out.println("====================================");
        System.out.print("Select an option: ");
        employeeSwitch();
    }

    public void employeeSwitch() {
        boolean close = false;

        while (!close) {
            int option = scan.nextInt();
            scan.nextLine();

            switch (option) {
                case 1 -> userInteractions.addEmployeeInteraction();
                case 2 -> userInteractions.updateEmployeeInteraction();
                case 3 -> userInteractions.deleteEmployeeInteraction();
                case 4 -> userInteractions.findEmployeeInteraction();
                case 5 -> userInteractions.findAllEmployeeInteraction();
                case 6 -> close = true;
                default -> System.out.println("Invalid option. Try again: ");
            }

        }
    }
}
