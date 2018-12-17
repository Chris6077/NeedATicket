/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import java.sql.Date;

/**
 *
 * @author Marcel Judth
 */
public class Transaction {
    private int id;
    private double amount;
    private Date date;
    private Wallet payerWallet;
    private Wallet receiverWallet;
    private Ticket ticket;

    public Transaction(int id, double amount, Date date, Wallet payerWallet, Wallet receiverWallet, Ticket ticket) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.payerWallet = payerWallet;
        this.receiverWallet = receiverWallet;
        this.ticket = ticket;
    }
    
    public Transaction(int amount, Date date, Wallet payerWallet, Wallet receiverWallet, Ticket ticket) {
        this.amount = amount;
        this.date = date;
        this.payerWallet = payerWallet;
        this.receiverWallet = receiverWallet;
        this.ticket = ticket;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public Wallet getPayerWallet() {
        return payerWallet;
    }

    public Wallet getReceiverWallet() {
        return receiverWallet;
    }

    public Ticket getTicket() {
        return ticket;
    }

    @Override
    public String toString() {
        return "Transaction{" + "id=" + id + ", amount=" + amount + ", date=" + date + ", payerWallet=" + payerWallet + ", receiverWallet=" + receiverWallet + ", ticket=" + ticket + '}';
    }
}
