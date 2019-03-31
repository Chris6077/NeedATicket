package me.projectx.needaticket.pojo;
import java.util.ArrayList;
import java.util.List;
public class Ticket {
    private String _id;
    private TicketType type;
    private Concert concert;
    private Seller seller;
    private int available;
    private float price;
    public Ticket (String _id, TicketType type, Concert concert, Seller seller, int available, float price) {
        this._id = _id;
        this.type = type;
        this.concert = concert;
        this.seller = seller;
        this.available = available;
        this.price = price;
    }
    public String get_id () {
        return _id;
    }
    public void set_id (String _id) {
        this._id = _id;
    }
    public TicketType getType () {
        return type;
    }
    public void setType (TicketType type) {
        this.type = type;
    }
    public Concert getConcert () {
        return concert;
    }
    public void setConcert (Concert concert) {
        this.concert = concert;
    }
    public Seller getSeller () {
        return seller;
    }
    public void setSeller (Seller seller) {
        this.seller = seller;
    }
    public int getAvailable () {
        return available;
    }
    public void setAvailable (int available) {
        this.available = available;
    }
    public float getPrice () {
        return price;
    }
    public void setPrice (float price) {
        this.price = price;
    }
    @Override public String toString () {
        return "Ticket{" + "_id='" + _id + '\'' + ", type=" + type + ", concert=" + concert + ", seller=" + seller + ", available=" + available + ", price=" + price + '}';
    }
}