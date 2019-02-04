package me.projectx.needaticket.pojo;
import java.util.ArrayList;
import java.util.List;
public class Ticket {
    private int id;
    private TicketType type;
    private String title;
    private float price;
    private Seller seller;
    private User buyer;
    private ArrayList<Concert> concerts;
    public Ticket (int id, TicketType type, String title, float price, Seller seller, User buyer, List<Concert> concerts) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.price = price;
        this.seller = seller;
        this.buyer = buyer;
        this.concerts = (ArrayList<Concert>) concerts;
    }
    public int getId () {
        return id;
    }
    public void setId (int id) {
        this.id = id;
    }
    public TicketType getType () {
        return type;
    }
    public void setType (TicketType type) {
        this.type = type;
    }
    public String getTitle () {
        return title;
    }
    public void setTitle (String title) {
        this.title = title;
    }
    public float getPrice () {
        return price;
    }
    public void setPrice (float price) {
        this.price = price;
    }
    public Seller getSeller () {
        return seller;
    }
    public void setSeller (Seller seller) {
        this.seller = seller;
    }
    public User getBuyer () {
        return buyer;
    }
    public void setBuyer (User buyer) {
        this.buyer = buyer;
    }
    public List<Concert> getConcerts () {
        return concerts;
    }
    public void setConcerts (List<Concert> concerts) {
        this.concerts = (ArrayList<Concert>) concerts;
    }
    @Override public String toString () {
        return "Ticket{" + "id=" + id + ", type=" + type + ", title=" + title + ", price=" + price + '}';
    }
    public String getSeats () {
        //ToDo implement
        return "";
    }
}