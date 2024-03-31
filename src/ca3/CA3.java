/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ca3;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author julio
 */

// Link to my github: https://github.com/Julio-Malfa/CA3.
public class CA3 {
    // Variables 
    private static Scanner scanner = new Scanner(System.in);
    private static String userType;
    private static String username;
    private static String password;
    
    // Main method with methods to login and display menu (that will go through the options)
    public static void main(String[] args) throws SQLException, IOException {
        login();
        displayMenu();
    }
    // Method to authenticate login
    private static void login() {
        System.out.println("Welcome to the CA3 system!");
        System.out.print("Please enter username: ");
        username = scanner.nextLine();
        System.out.print("Please enter password: ");
        password = scanner.nextLine();

        // For demonstration, setting userType based on hardcoded values
        userType = (username.equals("admin") && password.equals("java")) ? "Admin" :
                   (username.equals("office") && password.equals("java")) ? "Office" :
                   (username.equals("lecturer") && password.equals("java")) ? "Lecturer" : "";

        if (userType.isEmpty()) {
            System.out.println("Invalid username or password. Please try again");
            login();
        } else {
            System.out.println("Login successful as " + userType);
        }
    }
    // Method that will display menu options for admin, office, and lecturer
private static void displayMenu() throws IOException, SQLException {
    boolean running = true;
    while (running) {
        System.out.println("\n------ Menu ------");
                switch (userType) {
                case "Admin":
                    System.out.println("1. Change Username/Password");
                    System.out.println("2. Manage Users");                    
                    break;
                case "Office":
                    System.out.println("1. Change Username/Password");
                    System.out.println("2. View Reports");                    
                    break;
                case "Lecturer":
                    System.out.println("1. Change Username/Password");
                    System.out.println("2. Lecturer Reports");                    
                    break;
                default:
                    break;
            }

        System.out.println("3. Logout");

            System.out.print("\nEnter your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    changeCredentials();
                    break;
                case 2:
                switch (userType) {
                    case "Admin":
                        manageUsers();
                        break;
                    case "Office":
                        courseReports();
                        break;
                    case "Lecturer":
                        handleLecturerReport();
                        break;
                    default:
                        break;
                }
                    break;
                case 3:                    
                    logout();
                    running = false; // Stop the loop when logging out
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
// Method to change their own user name and password
private static void changeCredentials() throws SQLException, IOException {  
        DBConnector db = new DBConnector();
        String currentUser = userType; 
        db.changeCredentials(currentUser);    
}

// Method to change all kinds of user name and passwords
private static void manageUsers() throws SQLException, IOException {        
        DBConnector db = new DBConnector();
        db.manageUsers();    
}

// Method that will connect with DB and display options related to reports
private static void courseReports() throws SQLException, IOException {        
        DBConnector db = new DBConnector();
        db.courseReports();    
        System.out.println("End of the report generated...");        
}

// Method that will handle Lecturer Report options
private static void handleLecturerReport() throws SQLException, IOException {
        DBConnector db = new DBConnector();
        db.handleLecturerReport();  
        System.out.println("Generating lecturer report...");
}

// Methods to log out
private static void logout() {
        System.out.println("Logging out. See you soon!");
        System.exit(0);
}

}

