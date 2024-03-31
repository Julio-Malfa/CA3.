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
        FileWriter csvWriter = new FileWriter("report.csv");
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
    
}
