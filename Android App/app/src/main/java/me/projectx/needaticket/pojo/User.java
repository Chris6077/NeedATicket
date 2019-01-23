package me.projectx.needaticket.pojo;

import java.util.ArrayList;

public class User {
    private String id;
    private String email;
    private ArrayList<Ticket> tickets;
    private Wallet wallet;

    public User(String id, String email, ArrayList<Ticket> tickets, Wallet wallet) {
        this.id = id;
        this.email = email;
        this.tickets = tickets;
        this.wallet = wallet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                "email=" + email +
                ", tickets=" + tickets +
                ", wallet=" + wallet +
                '}';
    }
}