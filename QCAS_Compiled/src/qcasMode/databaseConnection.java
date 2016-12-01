
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcasMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *Makes the connection to the database using the url, username and password
 * @author paridhichoudhary
 */
public class databaseConnection {
    
    String url;
    String username;
    String password;
    Connection con;

    /**
     *
     * @param url : URL of the database connection
     * @param username : Username for the user
     * @param password : Password for the user
     * @throws SQLException
     */
    public databaseConnection(String url, String username, String password) throws SQLException {
        Connection con = DriverManager.getConnection(url, username, password);
        this.con = con;
    }
    
    public Connection getConnection(){
        return this.con;
    }
    
    
    
    
}