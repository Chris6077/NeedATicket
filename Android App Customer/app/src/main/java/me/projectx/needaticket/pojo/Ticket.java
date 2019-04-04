package me.projectx.needaticket.pojo;
public class Ticket {
    private String _id;
    private TicketType type;
    private Concert concert;
    private Seller seller;
    private int available;
    private float price;
    private boolean redeemed;
    public Ticket (String _id, TicketType type, Concert concert, Seller seller, int available, float price, boolean redeemed) {
        this._id = _id;
        this.type = type;
        this.concert = concert;
        this.seller = seller;
        this.available = available;
        this.price = price;
        this.redeemed = redeemed;
    }
    public String get_id () {
        return _id;
    }
    public TicketType getType () {
        return type;
    }
    public Concert getConcert () {
        return concert;
    }
    public Seller getSeller () {
        return seller;
    }
    public int getAvailable () {
        return available;
    }
    public float getPrice () {
        return price;
    }
    public boolean isRedeemed () {
        return redeemed;
    }
    @Override public String toString () {
        return "Ticket{" + "_id='" + _id + '\'' + ", type=" + type + ", concert=" + concert + ", seller=" + seller + ", available=" + available + ", price=" + price + ", redeemed=" + redeemed + '}';
    }
}