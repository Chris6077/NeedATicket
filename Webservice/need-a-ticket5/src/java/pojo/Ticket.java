/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import java.util.ArrayList;
import pojo.enums.Type;

/**
 *
 * @author Julian
 */
public class Ticket {
    private Integer id;
    private Type type;
    private int price;
    private User seller;
    private User buyer;
    private ArrayList<Concert> concerts; 

    public Ticket(Integer id, Type ticketType, int price, User seller, User buyer) {
        this.id = id;
        this.type = ticketType;
        this.price = price;
        this.seller = seller;
        this.buyer = buyer;
    }

    public Type getType() {
        return type;
    }

    public void setTicketType(Type ticketType) {
        this.type = ticketType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
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

    public void setConcerts(Concert concert) {
        this.concerts.add(concert);
    }
    
        
}
