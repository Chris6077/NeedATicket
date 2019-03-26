package me.projectx.needaticket.pojo;
import java.util.ArrayList;
import java.util.List;
public class Ticket {
    private Concert concert;
    private Seller seller;
    private int available;
    private float price;
    public Ticket (Concert concert, Seller seller, int available, float price) {
        this.concert = concert;
        this.seller = seller;
        this.available = available;
        this.price = price;
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
        return "Ticket{" + "concert=" + concert + ", seller=" + seller + ", available=" + available + ", price=" + price + '}';
    }
}