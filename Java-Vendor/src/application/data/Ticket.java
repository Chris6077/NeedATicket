package application.data;

public class Ticket {

    private String _id;
    private String type;
    private float price;
    private Concert concert;
    private Seller seller;
    private int amount;

    public Ticket(String id, String type, float price, int amount,Concert concert, Seller seller, User buyer) {
        this._id = id;
        this.type = type;
        this.price = price;
        this.seller = seller;
        this.concert = concert;
        this.amount = amount;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Concert getConcert() {
        return concert;
    }

    public void setConcert(Concert concert) {
        this.concert = concert;
    }

    @Override
    public String toString() {
        return "Ticket{" + "id=" + _id + ", type=" + type + ", price=" + price + ", concert=" + concert + ", seller=" + seller + '}';
    }

}
