/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import Config.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import database.Database;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pojo.Artist;
import pojo.Concert;
import pojo.Ticket;
import pojo.Transaction;
import pojo.User;
import pojo.Wallet;
import pojo.enums.Type;

/**
 *
 * @author Julian
 */
public class Logic {
    
    //functions related to users
    public static String login(String email, String password) throws  SQLException, FileNotFoundException, IllegalArgumentException, UnsupportedEncodingException, Exception{
        User user = Database.getUser(email);
        if(!user.getPassword().equals(password))
            throw new Exception("password incorrect");
        Algorithm algorithm = Algorithm.HMAC256(Config.SECRET.getValue());
        Map<String, Object> headerClaims = new HashMap();
        headerClaims.put("userId", user.getId());
        String token = JWT.create()
            .withIssuer("auth0")
            .withHeader(headerClaims)
            .sign(algorithm);
        return token;
    }
    
    public static String register(String email,String password,String type) throws ClassNotFoundException, SQLException, IllegalArgumentException, UnsupportedEncodingException, Exception{
        User user = new User(null,email,password,type);
        Database.createUser(user);
        return login(email,password);
    }
    
    public static List<User> getUsers() throws SQLException, ClassNotFoundException, FileNotFoundException{
        return Database.getUsers();
    }
    
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

    //functions related to tickets
    public static List<Ticket> getTickets() throws SQLException, ClassNotFoundException, FileNotFoundException {
        return Database.getTickets();
    }
    
    public static List<Ticket> getTicketsByUserid(int userid) throws SQLException, ClassNotFoundException, FileNotFoundException{
        return Database.getTicketsByUserid(userid);
    }
    
    public static Ticket getTicket(int ticketid) throws ClassNotFoundException, SQLException, SQLException, FileNotFoundException{
        return Database.getTicket(ticketid);
    }
    
    public static void createTicket(String type, int price, int buyerid) throws ClassNotFoundException, SQLException, FileNotFoundException{
        Ticket ticket = new Ticket(null, Type.valueOf(type.toUpperCase()), price, Database.getUser(buyerid), null);
        Database.createTicket(ticket);
    }
    
    public static void buyTicket(int ticketid, int buyerid, double amount) throws ClassNotFoundException, SQLException, FileNotFoundException, Exception{
        Ticket ticket = Database.getTicket(ticketid);
        if(ticket.getBuyer() != null)
            throw new Exception("Ticket has alreaedy been sold");
        User buyer = Database.getUser(buyerid);
        User seller = Database.getUser(ticket.getSeller().getId());
        ticket.setBuyer(buyer);
        Transaction transaction = new Transaction(-1, amount, new Date(Calendar.getInstance().getTime().getTime()), buyer.getWallet(), seller.getWallet(), ticket);
        Database.createTransaction(transaction);
        buyer.getWallet().pay(amount);
        seller.getWallet().receive(amount);
        Database.updateWallet(buyer.getWallet());
        Database.updateWallet(seller.getWallet());
        Database.updateTicket(ticket);
    }
    
    public static void updateTicket(String type, int price, int buyerid) throws SQLException, ClassNotFoundException, FileNotFoundException{
        Ticket ticket = new Ticket(null,Type.valueOf(type.toUpperCase()), price, Database.getUser(buyerid), null);
        Database.updateTicket(ticket);
    }
    
    public static void deleteTicket(int ticketid) throws ClassNotFoundException, SQLException, IOException{
        Database.deleteTicket(ticketid);
    }
    
    //functions related to transactions
    public static List<Transaction> getTransactions() throws ClassNotFoundException, SQLException, FileNotFoundException{
        return Database.getTransactions();
    }
    
    public static List<Transaction> getTransactions(int userid) throws SQLException, ClassNotFoundException, FileNotFoundException {
        User user = Database.getUser(userid);
        return Database.getTransactions(user.getWallet().getId());
    }
    
    //functions related to wallets
    public static void uploadWallet(int walletid, double amount) throws SQLException, ClassNotFoundException, FileNotFoundException {
        Wallet wallet = Database.getWallet(walletid);
        wallet.receive(amount);
        Database.updateWallet(wallet);
    }

    public static void cashout(int walletid, double amount) throws SQLException, FileNotFoundException, ClassNotFoundException, Exception {
        Wallet wallet = Database.getWallet(walletid);
        wallet.payout(amount);
        Database.updateWallet(wallet);
    }

}
