package application.data;

public class TicketConcert {
    private String seat;

    public TicketConcert(String seat) {
        this.seat = seat;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }
}
