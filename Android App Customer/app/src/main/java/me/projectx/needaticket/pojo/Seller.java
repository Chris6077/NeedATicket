package me.projectx.needaticket.pojo;
import java.util.ArrayList;
import java.util.List;
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
    public void set_id (String _id) {
        this._id = _id;
    }
    public String getUsername () {
        return username;
    }
    public void setUsername (String username) {
        this.username = username;
    }
}