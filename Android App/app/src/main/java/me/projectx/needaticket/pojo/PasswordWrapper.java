package me.projectx.needaticket.pojo;

public class PasswordWrapper {
    private String uID;
    private String oldPassword;
    private String newPassword;

    public PasswordWrapper(String uID, String oldPassword, String newPassword) {
        this.uID = uID;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "PasswordWrapper{" +
                "uID='" + uID + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
