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
    
    
}
