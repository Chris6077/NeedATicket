package me.projectx.needaticket.pojo;
public class User {
    private String _id;
    private String email;
    private int totalBought;
    private PasswordStrength passwordStrength;
    public User (String _id, String email, int totalBought, PasswordStrength passwordStrength) {
        this._id = _id;
        this.email = email;
        this.totalBought = totalBought;
        this.passwordStrength = passwordStrength;
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
    public PasswordStrength getPasswordStrength () {
        return passwordStrength;
    }
    @Override public String toString () {
        return "User{" + "_id='" + _id + '\'' + ", email='" + email + '\'' + ", totalBought=" + totalBought + ", passwordStrength=" + passwordStrength + '}';
    }
}