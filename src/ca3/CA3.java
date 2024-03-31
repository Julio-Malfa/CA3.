/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ca3;

import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author julio
 */
public class CA3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, IOException{               
        DBConnector db = new DBConnector();
        db.generateCourseTextReport();    
        System.out.println("End Course report...");           
    }
    
}
