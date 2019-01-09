package me.projectx.needaticket.pojo;

public class EmailWrapper {
    private String uID;
    private String email;
    private String password;

    public EmailWrapper(String uID, String email, String password) {
        this.uID = uID;
        this.email = email;
        this.password = password;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "EmailWrapper{" +
                "uID='" + uID + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
