package application.data;

import java.util.List;

public class User {
    private  int id;
    private String email;
    private String password;
    private List<Ticket> tickets;
    private Wallet wallet;

    public User(int id, String email, String password, List<Ticket> tickets, Wallet wallet) {
        this.id = id;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
                ", password='" + password + '\'' +
                ", tickets=" + tickets +
                ", wallet=" + wallet +
                '}';
    }
}
