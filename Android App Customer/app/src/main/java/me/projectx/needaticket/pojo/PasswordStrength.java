package me.projectx.needaticket.pojo;
public class PasswordStrength {
    private String status;
    public PasswordStrength (String status) {
        this.status = status;
    }
    public String getStatus () {
        return status;
    }
    public void setStatus (String status) {
        this.status = status;
    }
    @Override public String toString () {
        return "PasswordStrength{" + "status='" + status + '\'' + '}';
    }
}
