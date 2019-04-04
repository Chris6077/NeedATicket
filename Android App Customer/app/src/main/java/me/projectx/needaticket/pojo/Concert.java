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
    public ConcertType getType () {
        return type;
    }
    public String getTitle () {
        return title;
    }
    public String getDate () {
        return date;
    }
    public String getAddress () {
        return address;
    }
    public Genre getGenre () {
        return genre;
    }
    public Artist getArtist () {
        return artist;
    }
    @Override public String toString () {
        return "Concert{" + "_id='" + _id + '\'' + ", type=" + type + ", title='" + title + '\'' + ", date='" + date + '\'' + ", address='" + address + '\'' + ", genre=" + genre + ", artist=" + artist + '}';
    }
}