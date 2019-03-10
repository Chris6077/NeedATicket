package application.data;

import java.util.List;

public class Seller {
    private int id;
    private String name;
    private Wallet wallet;
    private List<Ticket> tickets;

    public Seller(int id, String name, Wallet wallet, List<Ticket> tickets) {
        this.id = id;
        this.name = name;
        this.wallet = wallet;
        this.tickets = tickets;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "Seller{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", wallet=" + wallet +
                ", tickets=" + tickets +
                '}';
    }
}
