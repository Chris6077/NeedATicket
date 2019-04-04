package me.projectx.needaticket.pojo;
public class Seller {
    private String _id;
    private String username;
    public Seller (String _id, String username) {
        this.username = username;
        this._id = _id;
    }
    public String get_id () {
        return _id;
    }
    public String getUsername () {
        return username;
    }
}