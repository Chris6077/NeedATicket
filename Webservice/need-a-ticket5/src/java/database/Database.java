/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Julian
 */
public class Database {
    private static Connection connection;
    
   // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:oracle:thin:@192.168.128.152:1521:ora11g";
    
    //  Database credentials
    static final String USER = "d5a03";
    static final String PASS = "d5a";
    
    public static void Connect() throws ClassNotFoundException, SQLException{
    
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        connection = DriverManager.getConnection(DB_URL,USER,PASS);
    }
    
}
