package me.projectx.needaticketqr.pojo;
public class LoginWrapper {
    private String hash;
    public LoginWrapper (String hash) {
        this.hash = hash;
    }
    public String getHash () {
        return hash;
    }
    public void setHash (String hash) {
        this.hash = hash;
    }
    @Override public String toString () {
        return "LoginWrapper{" + "hash='" + hash + '\'' + '}';
    }
}