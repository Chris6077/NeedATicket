/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import database.Database;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import pojo.Artist;

/**
 *
 * @author Julian
 */
public class Logic {
    
    public static List<Artist> getArtists() throws SQLException, ClassNotFoundException{
        return Database.getArtists();
    }
    
    public static Artist getArtist(Integer id) throws SQLException, FileNotFoundException, ClassNotFoundException{
        return Database.getArtist(id);
    }
   
    public static void createArtist(String name) throws NoSuchFieldException, SQLException, ClassNotFoundException{
        if(name.isEmpty())
            throw new NoSuchFieldException("field `name` is required.");
        Artist artist = new Artist(null,name);
        Database.createArtist(artist);
    }
    
    public static void updateArtist(Integer id, String name) throws NoSuchFieldException, ClassNotFoundException, SQLException, FileNotFoundException{
        if(id == null)
            throw new NoSuchFieldException("field `id` is required.");
        if(id < 0) 
            throw new IndexOutOfBoundsException("field `id` is invalid.");
        if(name.isEmpty())
            throw new NoSuchFieldException("field `name` is required.");
        Artist artist = new Artist(id,name);
        getArtist(id);
        Database.updateArtist(artist);
    }
    
    public static void deleteArtist(Integer id) throws NoSuchFieldException, ClassNotFoundException, SQLException, FileNotFoundException{
        if(id == null)
            throw new NoSuchFieldException("field `id` is required.");
        if(id < 0) 
            throw new IndexOutOfBoundsException("field `id` is invalid.");
        getArtist(id);
        Database.deleteArtist(id);
    }
}
