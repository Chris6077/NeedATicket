package me.projectx.needaticket.pojo;
import java.util.ArrayList;
import java.util.List;
public class Seller {
    private String id;
    private String name;
    public Seller (String id, String name) {
        this.id = id;
        this.name = name;
    }
    public String getId () {
        return id;
    }
    public void setId (String id) {
        this.id = id;
    }
    public String getName () {
        return name;
    }
    public void setName (String name) {
        this.name = name;
    }
    @Override public String toString () {
        return "Seller{" + "id=" + id + ", name='" + name + '}';
    }
}