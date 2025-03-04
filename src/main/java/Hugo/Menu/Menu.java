package Hugo.Menu;

//import Andy.Hibernate.Database.DatabaseManager;
//import Andy.db4o.Database.DatabaseManager;
import Hugo.PostgreSQL.DatabaseManager;
//import Toni.PlainText.DatabaseManager;

import java.util.Scanner;

/**
 * The {@code Menu} class provides a command-line interface for interacting with the database.
 * It allows users to manage departments and employees through a series of menus and options.
 * This class relies on {@link DatabaseManager} for database connections and {@link UserInteractions}
 * for handling user input and performing operations.
 */
public class Menu {

    /**
     * Scanner object for reading user input from the console.
     */
    private Scanner scan = new Scanner(System.in);

    /**
     * The database manager used to establish and manage connections to the database.
     */
    private final DatabaseManager manager;

    /**
     * The user interactions handler for performing operations on departments and employees.
     */
    private final UserInteractions userInteractions;

    /**
     * Constructs a new {@code Menu} instance.
     * Initializes the {@link DatabaseManager} and {@link UserInteractions} instances.
     */
    public Menu() {
        manager = new DatabaseManager();
        userInteractions = new UserInteractions(manager);
    }

    /**
     * Starts the main menu loop, allowing the user to interact with the system.
     * The user can choose to manage departments, employees, or close the application.
     */
    public void menuStart() {
        boolean close = false;

        while (!close) {
            menu();
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

    /**
     * Displays the main menu options to the user.
     */
    private void menu() {
        System.out.println("\n====================================");
        System.out.println("|            MENU                   |");
        System.out.println("====================================");
        System.out.println("| 1. Department                     |");
        System.out.println("| 2. Employee                       |");
        System.out.println("| 3. Close                          |");
        System.out.println("====================================");
        System.out.print("Select an option: ");
    }

    /**
     * Displays the department menu options to the user.
     */
    private void departmentMenu() {
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
        departmentSwitch();
    }

    /**
     * Handles the user's selection from the department menu.
     * Calls the appropriate method in {@link UserInteractions} based on the user's choice.
     */
    private void departmentSwitch() {
        System.out.print("Select an option: ");

        int option = scan.nextInt();
        scan.nextLine();

        switch (option) {
            case 1 -> userInteractions.addDepartmentInteraction();
            case 2 -> userInteractions.updateDepartmentInteraction();
            case 3 -> userInteractions.deleteDepartmentInteraction();
            case 4 -> userInteractions.findDepartmentInteraction();
            case 5 -> userInteractions.printAllDepartments();
            case 6 -> System.out.println("Closing menu...");
            default -> System.out.println("Invalid option. Try again: ");
        }
    }

    /**
     * Displays the employee menu options to the user.
     */
    private void employeeMenu() {
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
        employeeSwitch();
    }

    /**
     * Handles the user's selection from the employee menu.
     * Calls the appropriate method in {@link UserInteractions} based on the user's choice.
     */
    private void employeeSwitch() {
        System.out.print("Select an option: ");

        int option = scan.nextInt();
        scan.nextLine();

        switch (option) {
            case 1 -> userInteractions.addEmployeeInteraction();
            case 2 -> userInteractions.updateEmployeeInteraction();
            case 3 -> userInteractions.deleteEmployeeInteraction();
            case 4 -> userInteractions.findEmployeeInteraction();
            case 5 -> userInteractions.printAllEmployees();
            case 6 -> System.out.println("Closing menu...");
            default -> System.out.println("Invalid option. Try again: ");
        }
    }
}
