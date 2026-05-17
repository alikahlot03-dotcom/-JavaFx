package Utils;


import java.sql.Connection;
import java.sql.DriverManager;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author msi
 */
public class DBConnection {
    public static Connection connect() {

        try {

            return DriverManager.getConnection( 

                    "jdbc:mysql://localhost:3306/finance_tracker",

                    "root",

                    ""
            );

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }
    
}
