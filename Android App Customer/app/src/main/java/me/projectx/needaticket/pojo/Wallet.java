package me.projectx.needaticket.pojo;
public class Wallet {
    private String _id;
    private float balance;
    public Wallet (String _id, float balance) {
        this._id = _id;
        this.balance = balance;
    }
    public String get_id () {
        return _id;
    }
    public void set_id (String _id) {
        this._id = _id;
    }
    public float getBalance () {
        return balance;
    }
    public void setBalance (float balance) {
        this.balance = balance;
    }
}