package ca3;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    
}
