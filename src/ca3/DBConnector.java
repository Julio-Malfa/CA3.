package ca3;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author julio
 */
public class DBConnector {
     // final - constant, cannot change (I Know it is bad practise to have user and password saved like this
    private final String DB_URL = "jdbc:mysql://localhost";
    private final String USER = "pooa2024";
    private final String PASSWORD = "pooa2024";
    
    //Menu of Student's course reports (Display, Write CSV and Write TEXT)
    public void courseReports() throws SQLException, IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a Report to view:");
        System.out.println("1. Course Report");
        System.out.println("2. Student Report");
        System.out.println("3. Lecturer Report");
        int choice = scanner.nextInt();

       switch (choice) {
            case 1:
                handleCourseReport();
                break;
            case 2:
                handleStudentReport();
                break;
            case 3:
                handleLecturerReport();
                break;
            default:
                System.out.println("Invalid choice.");
                courseReports();
                break;
        }
    }
    // Method that cover options of displaying and generating the course report
    private void handleCourseReport() throws SQLException, IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose format for Course Report:");
        System.out.println("1. Display Course report");
        System.out.println("2. Generate CSV Course report");
        System.out.println("3. Generate Text Course report");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                displayCourseReport();
                break;
            case 2:
                generateCourseCSVReport();
                break;
            case 3:
                generateCourseTextReport();
                break;
            default:
                System.out.println("Invalid choice.");
                courseReports();
                break;
        }
    }
    // Method that cover options of displaying and generating the student report
    private void handleStudentReport() throws SQLException, IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose format for Student Report:");
        System.out.println("1. Display Student report");
        System.out.println("2. Generate CSV Student report");
        System.out.println("3. Generate Text Student report");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                displayStudentReport();
                break;
            case 2:
                generateStudentCSVReport();
                break;
            case 3:
                generateStudentTextReport();
                break;
            default:
                System.out.println("Invalid choice.");
                courseReports();
                break;
        }
    }
    // Method that cover options of displaying and generating the lecturer report
    void handleLecturerReport() throws SQLException, IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose format for Lecturer Report:");
        System.out.println("1. Display Lecturer report");
        System.out.println("2. Generate CSV Lecturer report");
        System.out.println("3. Generate Text Lecturer report");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                displayLecturerReport();
                break;
            case 2:
                generateLecturerCSVReport();
                break;
            case 3:
                generateLecturerTextReport();
                break;
            default:
                System.out.println("Invalid choice.");
                courseReports();
                break;
        }
    }   
    
    // Method to display course report 
    void displayCourseReport() throws SQLException {
        // Connecting to Database 
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        Statement stmt = conn.createStatement();
        stmt.execute("USE ca3;");

        // Executing SQL query
        ResultSet rs = stmt.executeQuery("SELECT \n" +
                "    m.module_name,\n" +
                "    c.program AS program,\n" +
                "    COUNT(e.student_id) AS num_students_enrolled,\n" +
                "    m.lecturer_name,\n" +
                "    COALESCE(m.room_name, 'Online') AS room_name\n" +
                "FROM \n" +
                "    modules m\n" +
                "JOIN courses c ON m.course_name = c.course_name\n" +
                "LEFT JOIN enrollments e ON m.module_id = e.module_id\n" +
                "GROUP BY \n" +
                "    m.module_id;");

        // Printing out report header
        System.out.println("Module Name                            | Program                    | Number of Students Enrolled   | Lecturer Name             | Room");

        // Printing out report
        while (rs.next()) {
            // Getting the variables from Database
            String moduleName = rs.getString("module_name");
            String program = rs.getString("program");
            int numStudentsEnrolled = rs.getInt("num_students_enrolled");
            String lecturerName = rs.getString("lecturer_name");
            String roomName = rs.getString("room_name");

            // Adjust column widths based on the maximum length of data in each column
            System.out.printf("%-38s| %-28s| %-30d| %-25s| %-10s\n",
                    moduleName,
                    program,
                    numStudentsEnrolled,
                    lecturerName,
                    roomName);
        }
    }
    
    //Method to generate Course Report in CSV File
    void generateCourseCSVReport() throws SQLException, IOException {
        FileWriter csvWriter = new FileWriter("course_report.csv");
        csvWriter.append("Module Name,Program,Number of Students Enrolled,Lecturer Name,Room\n");

        // Connecting to Database
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        Statement stmt = conn.createStatement();
        stmt.execute("USE ca3;");

        // Executing query
        ResultSet rs = stmt.executeQuery("SELECT \n" +
                "    m.module_name,\n" +
                "    c.program AS program,\n" +
                "    COUNT(e.student_id) AS num_students_enrolled,\n" +
                "    m.lecturer_name,\n" +
                "    COALESCE(m.room_name, 'Online') AS room_name\n" +
                "FROM \n" +
                "    modules m\n" +
                "JOIN courses c ON m.course_name = c.course_name\n" +
                "LEFT JOIN enrollments e ON m.module_id = e.module_id\n" +
                "GROUP BY \n" +
                "    m.module_id;");

        // Writing to CSV file
        while (rs.next()) {
            String moduleName = rs.getString("module_name");
            String program = rs.getString("program");
            int numStudentsEnrolled = rs.getInt("num_students_enrolled");
            String lecturerName = rs.getString("lecturer_name");
            String roomName = rs.getString("room_name");
            csvWriter.append(String.format("%s,%s,%d,%s,%s\n", moduleName, program, numStudentsEnrolled, lecturerName, roomName));
        }
        // Closing writers
        csvWriter.flush();
        csvWriter.close();
    }
    
    //Method to generate Course Report in TEXT File
    void generateCourseTextReport() throws SQLException, IOException {
        FileWriter textWriter = new FileWriter("course_report.txt");
        textWriter.write("Module Name                          | Program                      | Number of Students Enrolled   | Lecturer Name           | Room\n");

        // Connecting to Database
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        Statement stmt = conn.createStatement();
        stmt.execute("USE ca3;");

        // Executing query
        ResultSet rs = stmt.executeQuery("SELECT \n" +
                "    m.module_name,\n" +
                "    c.program AS program,\n" +
                "    COUNT(e.student_id) AS num_students_enrolled,\n" +
                "    m.lecturer_name,\n" +
                "    COALESCE(m.room_name, 'Online') AS room_name\n" +
                "FROM \n" +
                "    modules m\n" +
                "JOIN courses c ON m.course_name = c.course_name\n" +
                "LEFT JOIN enrollments e ON m.module_id = e.module_id\n" +
                "GROUP BY \n" +
                "    m.module_id;");

        // Writing to text file
        while (rs.next()) {
            String moduleName = rs.getString("module_name");
            String program = rs.getString("program");
            int numStudentsEnrolled = rs.getInt("num_students_enrolled");
            String lecturerName = rs.getString("lecturer_name");
            String roomName = rs.getString("room_name");
            textWriter.write(String.format("%-38s| %-28s| %-30d| %-25s| %-10s\n",
            moduleName, program, numStudentsEnrolled, lecturerName, roomName));
        }
        // Closing writers
        textWriter.flush();
        textWriter.close();
    }
    
    // Method to display student report  
    void displayStudentReport() throws SQLException{        
           //Connecting to Database
           Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
           Statement stmt = conn.createStatement();
           stmt.execute("USE ca3;");
            
            // Executing query
            ResultSet rs = stmt.executeQuery("SELECT \n" +
                                "    s.student_id,\n" +
                                "    s.student_name,\n" +
                                "    s.course_name AS program,\n" +
                                "    m.module_name,\n" +
                                "    g.grade\n" +
                                "FROM \n" +
                                "    students s\n" +
                                "JOIN enrollments e ON s.student_id = e.student_id\n" +
                                "JOIN modules m ON e.module_id = m.module_id\n" +
                                "LEFT JOIN grades g ON e.enrollment_id = g.enrollment_id;");

       // Printing out report header
        System.out.println("Student ID | Student Name        | Program                            | Module Name                   | Grade");

        // Printing out report
        while (rs.next()) {
            // Getting the variables from Database
            int studentID = rs.getInt("student_id");
            String studentName = rs.getString("student_name");
            String program = rs.getString("program");
            String moduleName = rs.getString("module_name");
            Double grade = rs.getDouble("grade");
        //  Adjust column widths based on the maximum length of data in each column
        System.out.printf("%-11s| %-20s| %-35s| %-30s| %.1f\n",
                studentID,
                studentName,
                program,
                moduleName,
                grade);
        }   
    }  
    
    // Method to generate student report in CSV File
    void generateStudentCSVReport() throws SQLException, IOException {
        FileWriter csvWriter = new FileWriter("student_report.csv");
        csvWriter.append("Student ID,Student Name,Program,Module Name,Grade\n");
        
        // Connecting to Database
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        Statement stmt = conn.createStatement();
        stmt.execute("USE ca3;");
            
            // Executing query
            ResultSet rs = stmt.executeQuery("SELECT \n" +
                                "    s.student_id,\n" +
                                "    s.student_name,\n" +
                                "    s.course_name AS program,\n" +
                                "    m.module_name,\n" +
                                "    g.grade\n" +
                                "FROM \n" +
                                "    students s\n" +
                                "JOIN enrollments e ON s.student_id = e.student_id\n" +
                                "JOIN modules m ON e.module_id = m.module_id\n" +
                                "LEFT JOIN grades g ON e.enrollment_id = g.enrollment_id;");

        // Writing to CSV file
        while (rs.next()) {
        int studentID = rs.getInt("student_id");
        String studentName = rs.getString("student_name");
        String program = rs.getString("program");
        String moduleName = rs.getString("module_name");
        Double grade = rs.getDouble("grade");
        csvWriter.append(String.format("%d,%s,%s,%s,%s\n", 
        studentID, studentName, program, moduleName, grade));
    }

        // Closing writers
        csvWriter.flush();
        csvWriter.close();
    }     
    
    // Method to generate student report in Text File
    void generateStudentTextReport() throws SQLException, IOException {
        FileWriter textWriter = new FileWriter("student_report.txt");
        textWriter.write("Student ID | Student Name        | Program                            | Module Name                   | Grade\n");

        // Connecting to Database
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        Statement stmt = conn.createStatement();
        stmt.execute("USE ca3;");

            // Executing query
            ResultSet rs = stmt.executeQuery("SELECT \n" +
                    "    s.student_id,\n" +
                    "    s.student_name,\n" +
                    "    s.course_name AS program,\n" +
                    "    m.module_name,\n" +
                    "    g.grade\n" +
                    "FROM \n" +
                    "    students s\n" +
                    "JOIN enrollments e ON s.student_id = e.student_id\n" +
                    "JOIN modules m ON e.module_id = m.module_id\n" +
                    "LEFT JOIN grades g ON e.enrollment_id = g.enrollment_id;");

        // Writing to text file
        while (rs.next()) {
        int studentID = rs.getInt("student_id");
        String studentName = rs.getString("student_name");
        String program = rs.getString("program");
        String moduleName = rs.getString("module_name");
        Double grade = rs.getDouble("grade");
        textWriter.write(String.format("%-11d| %-20s| %-35s| %-30s| %.1f\n", studentID, studentName, program, moduleName, grade));
    }

        // Closing writers
        textWriter.flush();
        textWriter.close();
    }
    
    // Method to display lecturer report  
    void displayLecturerReport() throws SQLException{        
           //Connecting to Database
           Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
           Statement stmt = conn.createStatement();
           stmt.execute("USE ca3;");
            
            // Executing query
            ResultSet rs = stmt.executeQuery("SELECT \n" +
                                            "    l.lecturer_name,\n" +
                                            "    l.lecturer_role,\n" +
                                            "    m.module_name,\n" +
                                            "    c.program AS program,\n" +
                                            "    COUNT(DISTINCT e.student_id) AS num_students_taking_module\n" +
                                            "FROM \n" +
                                            "    lecturer l\n" +
                                            "JOIN modules m ON l.lecturer_name = m.lecturer_name\n" +
                                            "JOIN courses c ON m.course_name = c.course_name\n" +
                                            "LEFT JOIN enrollments e ON m.module_id = e.module_id\n" +
                                            "GROUP BY \n" +
                                            "    l.lecturer_id, m.module_id;");

            // Printing out report header
            System.out.println("Lecturer Name     | Role                | Module Name                        | Program                          | Students Enrolled");

            // Printing out report            
            while (rs.next()) {
                // Getting the variables from Database                
                String lecturerName = rs.getString("lecturer_name");
                String lecturerRole = rs.getString("lecturer_role");
                String moduleName = rs.getString("module_name");
                String program = rs.getString("program");
                int numberOfStudents = rs.getInt("num_students_taking_module");
            
            // Adjust column widths based on the maximum length of data in each column    
            System.out.printf("%-18s| %-20s| %-35s| %-33s| %d\n",
                    lecturerName,
                    lecturerRole,
                    moduleName,
                    program,
                    numberOfStudents);
        }     
    }    
    
    // Method to generate lecturer report in CSV File
    void generateLecturerCSVReport() throws SQLException, IOException{        
           FileWriter csvWriter = new FileWriter("lecturer_report.csv");
           csvWriter.append("Lecturer Name,Role,Module Name,Program,Students Enrolled\n");
        
           //Connecting to Database
           Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
           Statement stmt = conn.createStatement();
           stmt.execute("USE ca3;");
            
            // Executing query
            ResultSet rs = stmt.executeQuery("SELECT \n" +
                                            "    l.lecturer_name,\n" +
                                            "    l.lecturer_role,\n" +
                                            "    m.module_name,\n" +
                                            "    c.program AS program,\n" +
                                            "    COUNT(DISTINCT e.student_id) AS num_students_taking_module\n" +
                                            "FROM \n" +
                                            "    lecturer l\n" +
                                            "JOIN modules m ON l.lecturer_name = m.lecturer_name\n" +
                                            "JOIN courses c ON m.course_name = c.course_name\n" +
                                            "LEFT JOIN enrollments e ON m.module_id = e.module_id\n" +
                                            "GROUP BY \n" +
                                            "    l.lecturer_id, m.module_id;");
            
            // Printing out report            
            while (rs.next()) {
                // Getting the variables from Database                
                String lecturerName = rs.getString("lecturer_name");
                String lecturerRole = rs.getString("lecturer_role");
                String moduleName = rs.getString("module_name");
                String program = rs.getString("program");
                int numberOfStudents = rs.getInt("num_students_taking_module");
                // Appending data to CSV file
                csvWriter.append(String.format("%s,%s,%s,%s,%d\n", lecturerName, lecturerRole, moduleName, program, numberOfStudents));
    }
        // Closing writers
        csvWriter.flush();
        csvWriter.close();
    }
    
    // Method to generate lecturer report in Text File
    void generateLecturerTextReport() throws SQLException, IOException{        
           FileWriter textWriter = new FileWriter("lecturer_report.txt");
           textWriter.write("Lecturer Name     | Role                | Module Name                        | Program                          | Students Enrolled\n");
        
           //Connecting to Database
           Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
           Statement stmt = conn.createStatement();
           stmt.execute("USE ca3;");
            
            // Executing query
            ResultSet rs = stmt.executeQuery("SELECT \n" +
                                            "    l.lecturer_name,\n" +
                                            "    l.lecturer_role,\n" +
                                            "    m.module_name,\n" +
                                            "    c.program AS program,\n" +
                                            "    COUNT(DISTINCT e.student_id) AS num_students_taking_module\n" +
                                            "FROM \n" +
                                            "    lecturer l\n" +
                                            "JOIN modules m ON l.lecturer_name = m.lecturer_name\n" +
                                            "JOIN courses c ON m.course_name = c.course_name\n" +
                                            "LEFT JOIN enrollments e ON m.module_id = e.module_id\n" +
                                            "GROUP BY \n" +
                                            "    l.lecturer_id, m.module_id;");
            
            // Printing out report            
            while (rs.next()) {
                // Getting the variables from Database                
                String lecturerName = rs.getString("lecturer_name");
                String lecturerRole = rs.getString("lecturer_role");
                String moduleName = rs.getString("module_name");
                String program = rs.getString("program");
                int numberOfStudents = rs.getInt("num_students_taking_module");
                // Appending data to CSV file
                textWriter.write(String.format("%-18s| %-20s| %-35s| %-33s| %d\n", lecturerName, lecturerRole, moduleName, program, numberOfStudents));
    }
         // Closing writers
        textWriter.flush();
        textWriter.close();
    }  
    // Method that will be used by admin to change username and password from other users
    public void manageUsers() throws SQLException {
    // Get the username and password that will be changed
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter username that will change: ");
    String currentUsername = scanner.nextLine();
    System.out.print("Enter new username: ");
    String newUsername = scanner.nextLine();
    System.out.print("Enter new password: ");
    String newPassword = scanner.nextLine();

    // Connecting to the Database
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
        // Selecting the database
        String useDatabaseQuery = "USE ca3";
        try (PreparedStatement useDatabaseStmt = conn.prepareStatement(useDatabaseQuery)) {
            useDatabaseStmt.executeUpdate();
        }

        // Update query to change username and password
        String query = "UPDATE users SET username = ?, password = ? WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newUsername);
            stmt.setString(2, newPassword);
            stmt.setString(3, currentUsername);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Credentials changed successfully!");
            } else {
                System.out.println("Error no changes made.");
            }
        }
    }
    }
    
    // Method that will allow to change their own user and password
    void changeCredentials(String currentUser) throws SQLException {
    // Get the new credentials from the user
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter your new username: ");
    String newUsername = scanner.nextLine();
    System.out.print("Enter your new password: ");
    String newPassword = scanner.nextLine();

    // Connecting to the Database
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
        // Selecting the database
        String useDatabaseQuery = "USE ca3";
        try (PreparedStatement useDatabaseStmt = conn.prepareStatement(useDatabaseQuery)) {
            useDatabaseStmt.executeUpdate();
        }

        // Update query to change username and password
        String query = "UPDATE users SET username = ?, password = ? WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newUsername);
            stmt.setString(2, newPassword);
            stmt.setString(3, currentUser);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Your credentials have been changed successfully!");
            } else {
                System.out.println("Username not found or no changes were made.");
            }
        }
    }
    }    
}
