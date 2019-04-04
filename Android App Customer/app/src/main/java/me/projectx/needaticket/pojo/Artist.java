package me.projectx.needaticket.pojo;
public class Artist {
    private String _id;
    private String name;
    public Artist (String _id, String name) {
        this._id = _id;
        this.name = name;
    }
    public String getName () {
        return name;
    }
    @Override public String toString () {
        return "Artist{" + "_id=" + _id + ", name='" + name + '\'' + '}';
    }
}