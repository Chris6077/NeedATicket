package me.projectx.needaticketqr.pojo;
public class CheckWrapper {
    private String ticketKey;
    public CheckWrapper (String ticketKey) {
        this.ticketKey = ticketKey;
    }
    public String getTicketKey () {
        return ticketKey;
    }
    public void setTicketKey (String ticketKey) {
        this.ticketKey = ticketKey;
    }
    @Override public String toString () {
        return "CheckWrapper{" + "ticketKey='" + ticketKey + '\'' + '}';
    }
}
