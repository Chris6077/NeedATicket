package application.data;

import java.time.LocalDate;

public class Transaction {
    private int id;
    private double amount;
    private LocalDate date;
    private  Wallet payer_Wallet;
    private  Wallet receiver_Wallet;
    private Ticket ticket;

    public Transaction(int id, double amount, LocalDate date, Wallet payer_Wallet, Wallet receiver_Wallet, Ticket ticket) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.payer_Wallet = payer_Wallet;
        this.receiver_Wallet = receiver_Wallet;
        this.ticket = ticket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Wallet getPayer_Wallet() {
        return payer_Wallet;
    }

    public void setPayer_Wallet(Wallet payer_Wallet) {
        this.payer_Wallet = payer_Wallet;
    }

    public Wallet getReceiver_Wallet() {
        return receiver_Wallet;
    }

    public void setReceiver_Wallet(Wallet receiver_Wallet) {
        this.receiver_Wallet = receiver_Wallet;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", date=" + date +
                ", payer_Wallet=" + payer_Wallet +
                ", receiver_Wallet=" + receiver_Wallet +
                ", ticket=" + ticket +
                '}';
    }
}
