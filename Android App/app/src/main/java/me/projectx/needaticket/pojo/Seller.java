package me.projectx.needaticket.pojo;
import java.util.ArrayList;
import java.util.List;
public class Seller {
    private String id;
    private String name;
    private ArrayList<Ticket> tickets;
    public Seller (String id, String name, List<Ticket> tickets) {
        this.id = id;
        this.name = name;
        this.tickets = (ArrayList<Ticket>) tickets;
    }
    public String getId () {
        return id;
    }
    public void setId (String id) {
        this.id = id;
    }
    public String getName () {
        return name;
    }
    public void setName (String name) {
        this.name = name;
    }
    public List<Ticket> getTickets () {
        return tickets;
    }
    public void setTickets (List<Ticket> tickets) {
        this.tickets = (ArrayList<Ticket>) tickets;
    }
    @Override public String toString () {
        return "Seller{" + "id=" + id + ", name='" + name + '\'' + ", tickets=" + tickets + '}';
    }
}