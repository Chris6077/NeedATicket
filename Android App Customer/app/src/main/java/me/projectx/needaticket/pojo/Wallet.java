package me.projectx.needaticket.pojo;
public class Wallet {
    private String _id;
    private float balance;
    public Wallet (String _id, float balance) {
        this._id = _id;
        this.balance = balance;
    }
    public float getBalance () {
        return balance;
    }
}