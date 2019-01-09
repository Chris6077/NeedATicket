package me.projectx.needaticket.pojo;

import java.util.ArrayList;

public class BuyTicket {
    private String id;
    private Seller seller;
    private User buyer;
    private ArrayList<Concert> concerts;
    private int amount;
    private float price;

    public BuyTicket(String id, Seller seller, User buyer, ArrayList<Concert> concerts, int amount, float price) {
        this.id = id;
        this.seller = seller;
        this.buyer = buyer;
        this.concerts = concerts;
        this.amount = amount;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public ArrayList<Concert> getConcerts() {
        return concerts;
    }

    public void setConcerts(ArrayList<Concert> concerts) {
        this.concerts = concerts;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "BuyTicket{" +
                "id='" + id + '\'' +
                ", seller=" + seller +
                ", buyer=" + buyer +
                ", concerts=" + concerts +
                ", amount=" + amount +
                ", price=" + price +
                '}';
    }
}