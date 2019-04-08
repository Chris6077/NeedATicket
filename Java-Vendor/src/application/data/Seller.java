package application.data;


public class Seller {

    private String _id;
    private String username;

    public Seller(String id, String name) {
        this._id = id;
        this.username = name;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Seller{" + "id=" + _id + ", name=" + username + '}';
    }

}
