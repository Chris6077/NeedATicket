package application.data;

import java.util.List;

public class User {
    private  int id;
    private String email;
    private List<Ticket> tickets;
    private Wallet wallet;

    public User(int id, String email, String password, List<Ticket> tickets, Wallet wallet) {
        this.id = id;
        this.email = email;
        this.tickets = tickets;
        this.wallet = wallet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
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
                ", email='" + email + '\'' +
                ", tickets=" + tickets +
                ", wallet=" + wallet +
                '}';
    }
}
