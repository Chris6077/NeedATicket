/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import pojo.Artist;

/**
 *
 * @author Julian
 */
public class Database {
    private static Connection connection;
    
   // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:oracle:thin:@192.168.128.152:1521:ora11g";
    static final String DB_URL_EXT = "jdbc:oracle:thin:@212.152.179.117:1521:ora11g";
    
    //  Database credentials
    static final String USER = "d5a03";
    static final String PASS = "d5a";
    
    // sql
    
    static final String select_artists = "select * from artist";
    static final String select_artist_by_id = "select * from artist where id = ?";
    
    public static void Connect() throws ClassNotFoundException, SQLException{
    
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        connection = DriverManager.getConnection(DB_URL_EXT,USER,PASS);
    }
    
    //functions related to artists
    
    public static ArrayList<Artist> getArtists() throws SQLException{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(select_artists);
        ArrayList<Artist> artists = new ArrayList<Artist>();
        //extract data from result set
        while(resultSet.next()){
            Integer id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            artists.add(new Artist(id,name));
        }
        //clean up
        resultSet.close();
        statement.close();
        return artists;
    }
    
    public static Artist getArtist(Integer id) throws SQLException, FileNotFoundException{
        PreparedStatement preparedStatement = connection.prepareStatement(select_artist_by_id);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Artist artist = null;
        //extract data from result set
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            artist = new Artist(id,name);
        }
        //clean up
        resultSet.close();
        preparedStatement.close();
        if(artist == null)
            throw new FileNotFoundException("artist not found");
        return artist;
    }
    
    
}
