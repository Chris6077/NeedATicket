package me.projectx.needaticket.pojo;
public class Artist {
    private String _id;
    private String name;
    public Artist (String _id, String name) {
        this._id = _id;
        this.name = name;
    }
    public String get_Id () {
        return _id;
    }
    public void set_Id (String _id) {
        this._id = _id;
    }
    public String getName () {
        return name;
    }
    public void setName (String name) {
        this.name = name;
    }
    @Override public String toString () {
        return "Artist{" + "_id=" + _id + ", name='" + name + '\'' + '}';
    }
}