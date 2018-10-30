/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import database.Database;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import pojo.Artist;
import pojo.Concert;

/**
 *
 * @author Julian
 */
public class Logic {
    
    //functions related to artists
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
    
    //functions related to concerts
    public static List<Concert> getConcerts() throws ClassNotFoundException, SQLException, FileNotFoundException {
        return Database.getConcerts();
    }
    
    public static Concert getConcert(Integer id) throws SQLException, FileNotFoundException, ClassNotFoundException{
        return Database.getConcert(id);
    }
   
    public static void createConcert(String title,String dateUnformatted, String genre, String address, Integer artistid) throws NoSuchFieldException, SQLException, ClassNotFoundException{
        Date date = Date.valueOf(LocalDate.parse(dateUnformatted));
        Database.createConcert(new Concert(-1,title,date,genre,address,new Artist(artistid,null)));
    }
    
    public static void updateConcert(Integer id, String title,String dateUnformatted, String genre, String address, Integer artistid) throws NoSuchFieldException, SQLException, ClassNotFoundException, FileNotFoundException{
        Date date = Date.valueOf(LocalDate.parse(dateUnformatted));
        getArtist(artistid);
        Database.updateConcert(new Concert(id,title,date,genre,address,new Artist(artistid,null)));
    }

}
