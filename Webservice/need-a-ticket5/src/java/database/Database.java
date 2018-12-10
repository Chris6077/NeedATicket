/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import database.statements.statements;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import pojo.Artist;
import pojo.Concert;
import pojo.Ticket;
import pojo.Transaction;
import pojo.User;
import pojo.Wallet;
import pojo.enums.Role;
import pojo.enums.Type;

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
    
    public static void Connect() throws ClassNotFoundException, SQLException{
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        connection = DriverManager.getConnection(DB_URL ,USER,PASS);
    }
    
    //functions related to users
    public static ArrayList<User> getUsers() throws SQLException, ClassNotFoundException, FileNotFoundException{
        Connect();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(statements.SELECT_USERS.getStatement());
        ArrayList<User> users = new ArrayList<User>();
        //extract data from result set
        while(resultSet.next()){
            Integer id = resultSet.getInt("id");
            String email = resultSet.getString("email");
            String password  = resultSet.getString("password");
            Role type = Role.valueOf(resultSet.getString("type").toUpperCase());
            Wallet wallet = getWallet(resultSet.getInt("id_wallet"));
            users.add(new User(id,email,password,type,wallet));
        }
        //clean up
        resultSet.close();
        statement.close();
        connection.close();
        return users;
    }
    
    public static User getUser (String email) throws ClassNotFoundException, SQLException, FileNotFoundException{
        Connect();
        PreparedStatement preparedStatement = connection.prepareStatement(statements.SELECT_USER_BY_EMAIL.getStatement());
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        User user = null;
        //extract data from result set
        while (resultSet.next()) {
            Integer id = resultSet.getInt("id");
            String password  = resultSet.getString("password");
            Role type = Role.valueOf(resultSet.getString("type").toUpperCase());
            user = new User (id,email,password,type);
        }
        //clean up
        resultSet.close();
        preparedStatement.close();
        connection.close();
        if(user == null)
            throw new FileNotFoundException("user not found");
        return user;
    }
    
    public static User getUser(int id) throws SQLException, ClassNotFoundException, FileNotFoundException{
        Connect();
        PreparedStatement preparedStatement = connection.prepareStatement(statements.SElECT_USER_BY_ID.getStatement());
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        User user = null;
        //extract data from result set
        while (resultSet.next()) {
            String email = resultSet.getString("email");
            String password  = resultSet.getString("password");
            Role type = Role.valueOf(resultSet.getString("type").toUpperCase());
            int walletid = resultSet.getInt("id_wallet");
            Wallet wallet = null;
            if(walletid != 0)
                wallet = Database.getWallet(walletid);
            user = new User (id, email, password, type, wallet);    
        }
        //clean up
        resultSet.close();
        preparedStatement.close();
        connection.close();
        if(user == null)
            throw new FileNotFoundException("user not found");
        return user;
    }
    
    public static void createUser(User user) throws ClassNotFoundException, SQLException{
        Integer walletid = createWallet(new Wallet(null, 0.0));
        Connect();
        PreparedStatement statement = connection.prepareStatement(statements.INSERT_USER.getStatement());
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getRole().toString());
        statement.setInt(4, walletid);
        statement.executeUpdate();
        connection.close();
    }
    
    
    //functions related to wallets
    public static Integer createWallet(Wallet wallet) throws ClassNotFoundException, SQLException{
        Connect();
        PreparedStatement statement = connection.prepareStatement(statements.INSERT_WALLET.getStatement());
        statement.setDouble(1, wallet.getBalance());
        Integer id = statement.executeUpdate();
        connection.close();
        return id;
    }
    
    public static Wallet getWallet(Integer id) throws SQLException, FileNotFoundException, ClassNotFoundException{
        Connect();
        PreparedStatement preparedStatement = connection.prepareStatement(statements.SELECT_WALLET_BY_ID.getStatement());
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Wallet wallet = null;
        //extract data from result set
        while (resultSet.next()) {
            Double balance = resultSet.getDouble("balance");
            wallet = new Wallet(id,balance);
        }
        //clean up
        resultSet.close();
        preparedStatement.close();
        connection.close();
        if(wallet == null)
            throw new FileNotFoundException("wallet not found");
        return wallet;
    }
    
    public static void updateWallet(Wallet wallet) throws ClassNotFoundException, ClassNotFoundException, SQLException {
        Connect();
        PreparedStatement statement = connection.prepareStatement(statements.UPDATE_WALLET.getStatement());
        statement.setDouble(1, wallet.getBalance());
        statement.setInt(2, wallet.getId());
        statement.executeUpdate();
        connection.close();
    }
    
    //functions related to tickets
    public static ArrayList<Ticket> getTickets() throws SQLException, ClassNotFoundException, FileNotFoundException{
        Connect();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(statements.SElECT_TICKETS.getStatement());
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        //extract data from result set
        while(resultSet.next()){
            Integer id = resultSet.getInt("id");
            Type type = Type.valueOf(resultSet.getString("type").toUpperCase());
            int price = resultSet.getInt("PRICE");
            int sellerId = resultSet.getInt("ID_SELLER");
            int buyerId = resultSet.getInt("ID_BUYER");
            User seller = Database.getUser(sellerId);
            User buyer = null;
            if(buyerId != 0)
                buyer = Database.getUser(buyerId);
            tickets.add(new Ticket(id, type, price, seller, buyer));
        }
        //clean up
        resultSet.close();
        statement.close();
        connection.close();
        return tickets;
    }
    
    public static Ticket getTicket(int id) throws ClassNotFoundException, SQLException, FileNotFoundException{
        Connect();
        PreparedStatement preparedStatement = connection.prepareStatement(statements.SElECT_TICKET_BY_ID.getStatement());
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Ticket ticket = null;
        //extract data from result set
        while (resultSet.next()) {
            int price = resultSet.getInt("PRICE");
            User seller = Database.getUser(resultSet.getInt("ID_SELLER"));
            int buyerid = resultSet.getInt("ID_BUYER");
            User buyer = null;
            if(buyerid != 0)
                buyer = Database.getUser(buyerid);
            Type type = Type.valueOf(resultSet.getString("type").toUpperCase());
            ticket = new Ticket (id, type, price, seller, buyer);
        }
        //clean up
        resultSet.close();
        preparedStatement.close();
        connection.close();
        if(ticket == null)
            throw new FileNotFoundException("ticket not found");
        return ticket;
    }
    
    public static void createTicket(Ticket ticket) throws ClassNotFoundException, SQLException{
        Connect();
        PreparedStatement statement = connection.prepareStatement(statements.INSERT_TICKET.getStatement());
        statement.setString(1, ticket.getType().toString());
        statement.setDouble(2, ticket.getPrice());
        statement.setInt(3, ticket.getSeller().getId());
        statement.executeUpdate();
        connection.close();
    }
    
    public static void updateTicket(Ticket ticket) throws ClassNotFoundException, SQLException, SQLException {
        Connect();
        PreparedStatement statement = connection.prepareStatement(statements.UPDATE_TICKET.getStatement());
        statement.setString(1, ticket.getType().toString());
        statement.setDouble(2, ticket.getPrice());
        statement.setInt(3, ticket.getSeller().getId());
        statement.setInt(4, ticket.getBuyer().getId());
        statement.setInt(5, ticket.getId());
        statement.executeUpdate();
        connection.close();
    }
    
    public static void deleteTicket(int id) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException{
        Connect();
        if(Database.getTicket(id).getBuyer() != null)
                throw new IOException("ticket has already been sold");
        PreparedStatement statement = connection.prepareStatement(statements.DELETE_TICKET.getStatement());
        statement.setInt(1, id);
        statement.executeUpdate();
        connection.close();
    }
    
    //functions related to artists
    public static ArrayList<Artist> getArtists() throws SQLException, ClassNotFoundException{
        Connect();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(statements.SELECT_ARTISTS.getStatement());
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
        connection.close();
        return artists;
    }
    
    public static Artist getArtist(Integer id) throws SQLException, FileNotFoundException, ClassNotFoundException{
        Connect();
        PreparedStatement preparedStatement = connection.prepareStatement(statements.SELECT_ARTIST_BY_ID.getStatement());
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
        connection.close();
        if(artist == null)
            throw new FileNotFoundException("artist not found");
        return artist;
    }
    
    public static void createArtist(Artist artist) throws SQLException, ClassNotFoundException{
        Connect();
        PreparedStatement statement = connection.prepareStatement(statements.INSERT_ARTIST.getStatement());
        statement.setString(1, artist.getName());
        statement.executeUpdate();
        connection.close();
    }
    
    public static void updateArtist(Artist artist) throws ClassNotFoundException, ClassNotFoundException, SQLException {
        Connect();
        PreparedStatement statement = connection.prepareStatement(statements.UPDATE_ARTIST.getStatement());
        statement.setString(1, artist.getName());
        statement.setInt(2, artist.getId());
        statement.executeUpdate();
        connection.close();
    }
    
    public static void deleteArtist(Integer id) throws ClassNotFoundException, ClassNotFoundException, SQLException{
        Connect();
        PreparedStatement statement = connection.prepareStatement(statements.DELETE_ARTIST.getStatement());
        statement.setInt(1, id);
        statement.executeUpdate();
        connection.close();
    }
    
    //functions related to concerts
    
    public static ArrayList<Concert> getConcerts() throws ClassNotFoundException, SQLException, SQLException, FileNotFoundException{
        Connect();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(statements.SELECT_CONCERTS.getStatement());
        ArrayList<Concert> concerts = new ArrayList<Concert>();
        //extract data from result set
        while(resultSet.next()){
            Integer id = resultSet.getInt("id");
            String title = resultSet.getString("title");
            Date date = resultSet.getDate("cdate");
            String genre = resultSet.getString("genre");
            String address = resultSet.getString("address");
            Artist artist = getArtist(resultSet.getInt("id_artist"));
            concerts.add(new Concert(id,title,date,genre,address,artist));
        }
        //clean up
        resultSet.close();
        statement.close();
        connection.close();
        return concerts;
    }

    public static Concert getConcert(Integer id) throws SQLException, FileNotFoundException, ClassNotFoundException{
        Connect();
        PreparedStatement preparedStatement = connection.prepareStatement(statements.SELECT_CONCERT_BY_ID.getStatement());
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Concert concert = null;
        //extract data from result set
        while (resultSet.next()) {
            String title = resultSet.getString("title");
            Date date = resultSet.getDate("cdate");
            String genre = resultSet.getString("genre");
            String address = resultSet.getString("address");
            Artist artist = getArtist(resultSet.getInt("id_artist"));
            concert = new Concert(id,title,date,genre,address,artist);
        }
        //clean up
        resultSet.close();
        preparedStatement.close();
        connection.close();
        if(concert == null)
            throw new FileNotFoundException("concert not found");
        return concert;
    }
    
    public static void createConcert(Concert concert) throws SQLException, ClassNotFoundException{
        Connect();
        PreparedStatement statement = connection.prepareStatement(statements.INSERT_CONCERT.getStatement());
        statement.setString(1, concert.getTitle());
        statement.setDate(2, concert.getDate());
        statement.setString(3, concert.getGenre());
        statement.setString(4, concert.getAddress());
        statement.setInt(5, concert.getArtist().getId());
        statement.executeUpdate();
        connection.close();
    }
    
    public static void updateConcert(Concert concert) throws ClassNotFoundException, ClassNotFoundException, SQLException {
        Connect();
        PreparedStatement statement = connection.prepareStatement(statements.UPDATE_CONCERT.getStatement());
        statement.setString(1, concert.getTitle());
        statement.setDate(2, concert.getDate());
        statement.setString(3, concert.getGenre());
        statement.setString(4, concert.getAddress());
        statement.setInt(5, concert.getArtist().getId());
        statement.setInt(6, concert.getId());
        statement.executeUpdate();
        connection.close();
    }

    //functions related to transactions
    
    public static ArrayList<Transaction> getTransactions() throws ClassNotFoundException, SQLException, FileNotFoundException{
        Connect();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(statements.SElECT_TRANSACTIONS.getStatement());
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        //extract data from result set
        while(resultSet.next()){
            Integer id = resultSet.getInt("ID");
            double amount = resultSet.getDouble("AMOUNT");
            Date date = resultSet.getDate("CDATE");
            Wallet payerWallet = Database.getWallet(resultSet.getInt("ID_PAYER_WALLET"));
            Wallet recieverWallet = Database.getWallet(resultSet.getInt("ID_RECIEVER_WALLET"));
            Ticket ticket = Database.getTicket(resultSet.getInt("ID_TICKET"));

            transactions.add(new Transaction(id, amount, date, payerWallet, recieverWallet, ticket));
        }
        //clean up
        resultSet.close();
        statement.close();
        connection.close();
        return transactions;
    }
        
    public static void createTransaction(Transaction transaction) throws SQLException, ClassNotFoundException, FileNotFoundException, IOException {
        Connect();
        if(Database.getTicket(transaction.getTicket().getId()).getBuyer() != null)
                throw new IOException("ticket has already been sold");
        PreparedStatement statement = connection.prepareStatement(statements.INSERT_TRANSACTION.getStatement());
        statement.setInt(1, transaction.getPayerWallet().getId());
        statement.setInt(2, transaction.getReceiverWallet().getId());
        statement.setInt(3, transaction.getTicket().getId());
        statement.setDouble(4, transaction.getAmount());
        statement.setDate(5, transaction.getDate());
        statement.executeUpdate();
        connection.close();
    }
    
}
