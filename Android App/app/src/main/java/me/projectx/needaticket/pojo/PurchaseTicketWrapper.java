package me.projectx.needaticket.pojo;

public class PurchaseTicketWrapper {
    private String uID;
    private String tID;
    private int amount;

    public PurchaseTicketWrapper(String uID, String tID, int amount) {
        this.uID = uID;
        this.tID = tID;
        this.amount = amount;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String gettID() {
        return tID;
    }

    public void settID(String tID) {
        this.tID = tID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PurchaseTicketWrapper{" +
                "uID='" + uID + '\'' +
                ", tID='" + tID + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
