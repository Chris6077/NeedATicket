package me.projectx.needaticket.pojo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class Concert {
    private String _id;
    private String title;
    private String date;
    private String address;
    private Genre genre;
    private Artist artist;
    public Concert (String _id, String title, String date, String address, Genre genre, Artist artist) {
        this._id = _id;
        this.title = title;
        this.date = date;
        this.address = address;
        this.genre = genre;
        this.artist = artist;
    }
    public String get_id () {
        return _id;
    }
    public void set_id (String _id) {
        this._id = _id;
    }
    public String getTitle () {
        return title;
    }
    public void setTitle (String title) {
        this.title = title;
    }
    public String getDate () {
        return date;
    }
    public void setDate (String date) {
        this.date = date;
    }
    public String getAddress () {
        return address;
    }
    public void setAddress (String address) {
        this.address = address;
    }
    public Genre getGenre () {
        return genre;
    }
    public void setGenre (Genre genre) {
        this.genre = genre;
    }
    public Artist getArtist () {
        return artist;
    }
    public void setArtist (Artist artist) {
        this.artist = artist;
    }
    @Override public String toString () {
        return "Concert{" + "_id='" + _id + '\'' + ", title='" + title + '\'' + ", date='" + date + '\'' + ", address='" + address + '\'' + ", genre=" + genre + ", artist=" + artist + '}';
    }
}