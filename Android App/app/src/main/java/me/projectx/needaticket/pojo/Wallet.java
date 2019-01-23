package me.projectx.needaticket.pojo;
public class Wallet {
    private int id;
    private float balance;
    public Wallet (int id, float balance) {
        this.id = id;
        this.balance = balance;
    }
    public int getId () {
        return id;
    }
    public void setId (int id) {
        this.id = id;
    }
    public float getBalance () {
        return balance;
    }
    public void setBalance (float balance) {
        this.balance = balance;
    }
    @Override public String toString () {
        return "Wallet{" + "id=" + id + ", balance=" + balance + '}';
    }
}