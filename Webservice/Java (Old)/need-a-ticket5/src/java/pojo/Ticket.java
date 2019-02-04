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
    private double price;
    private User seller;
    private User buyer;
    private Concert concert; 
    private Double seat;

    public Ticket(Integer id, Type ticketType, double price, User seller, User buyer, Double seat) {
        this.id = id;
        this.type = ticketType;
        this.price = price;
        this.seller = seller;
        this.buyer = buyer;
        this.seat = seat;
        
    }
    
        public Ticket(Integer id, Type ticketType, double price, User seller, User buyer, Double seat, Concert concert) {
        this.id = id;
        this.type = ticketType;
        this.price = price;
        this.seller = seller;
        this.buyer = buyer;
        this.seat = seat;
        this.concert = concert;
    }

    public Integer getId() {
        return id;
    }
    
    public Type getType() {
        return type;
    }

    public void setTicketType(Type ticketType) {
        this.type = ticketType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Double getSeat() {
        return seat;
    }

    public void setSeat(Double seat) {
        this.seat = seat;
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

    public Concert getConcert() {
        return concert;
    }

    public void setConcert(Concert concert) {
        this.concert = concert;
    }
    
        
}
