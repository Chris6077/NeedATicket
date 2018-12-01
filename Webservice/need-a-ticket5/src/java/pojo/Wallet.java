/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

/**
 *
 * @author Julian
 */
public class Wallet {
    private Integer id;
    private Double balance;

    public Wallet(Integer id, Double balance) {
        this.id = id;
        this.balance = balance;
    }
    
    public void pay(double amount) throws Exception{
        if(this.balance < amount)
            throw new Exception("not enough money");
        this.balance = this.balance - amount;
    }
    
    public void receive(double amount){
        this.balance = this.balance + amount;
    }
    
    public void payout(double amount) throws Exception{
        if(this.balance < amount)
            throw new Exception("not enough money");
        this.balance = this.balance - amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }    
}
