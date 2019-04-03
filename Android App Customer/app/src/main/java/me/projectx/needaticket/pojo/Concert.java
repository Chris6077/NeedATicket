package me.projectx.needaticket.pojo;
public class Concert {
    private String _id;
    private ConcertType type;
    private String title;
    private String date;
    private String address;
    private Genre genre;
    private Artist artist;
    public Concert (String _id, ConcertType type, String title, String date, String address, Genre genre, Artist artist) {
        this._id = _id;
        this.type = type;
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
    public ConcertType getType () {
        return type;
    }
    public void setType (ConcertType type) {
        this.type = type;
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
        return "Concert{" + "_id='" + _id + '\'' + ", type=" + type + ", title='" + title + '\'' + ", date='" + date + '\'' + ", address='" + address + '\'' + ", genre=" + genre + ", artist=" + artist + '}';
    }
}