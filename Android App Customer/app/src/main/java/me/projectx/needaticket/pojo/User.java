package me.projectx.needaticket.pojo;
import java.util.ArrayList;
import java.util.List;
public class User {
    private String _id;
    private String email;
    private int totalBought;
    private int passwordStrength;
    public User (String _id, String email, int totalBought, int passwordStrength) {
        this._id = _id;
        this.email = email;
        this.totalBought = totalBought;
        this.passwordStrength = passwordStrength;
    }
    public String get_id () {
        return _id;
    }
    public void set_id (String _id) {
        this._id = _id;
    }
    public String getEmail () {
        return email;
    }
    public void setEmail (String email) {
        this.email = email;
    }
    public int getTotalBought () {
        return totalBought;
    }
    public void setTotalBought (int totalBought) {
        this.totalBought = totalBought;
    }
    public String getPasswordStrength () {
        String res = "Very Weak";
        switch(passwordStrength){
            case 1: res = "Weak"; break;
            case 2: res = "Average"; break;
            case 3: res = "Strong"; break;
            case 4: res = "Very Strong"; break;
        }
        return res;
    }
    public void setPasswordStrength (int passwordStrength) {
        this.passwordStrength = passwordStrength;
    }
    @Override public String toString () {
        return "User{" + "_id='" + _id + '\'' + ", email='" + email + '\'' + ", totalBought=" + totalBought + ", passwordStrength=" + passwordStrength + '}';
    }
}