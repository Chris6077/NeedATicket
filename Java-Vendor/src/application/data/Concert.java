package application.data;

import java.time.LocalDate;
import java.util.List;

public class Concert {
    private String _id;
    private String title;
    private String date;
    private String address;
    private Artist artist;
    private String genre;
    private String totalTickets;

    public Concert(String _id, String title, String date, String address, Artist artist, String genre, String totalTickets) {
        this.title = title;
        this.date = date;
        this.address = address;
        this.artist = artist;
        this.genre = genre;
        this.totalTickets = totalTickets;
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Artist getArtists() {
        return artist;
    }

    public void setArtists(Artist artists) {
        this.artist = artists;
    }

    public String getGenre() {
        return genre;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(String totalTickets) {
        this.totalTickets = totalTickets;
    }

    @Override
    public String toString() {
        return "Concert{"
                + "title='" + title + '\''
                + ", date=" + date
                + ", address='" + address + '\''
                + ", artist=" + artist
                + ", genre=" + genre
                + ", tickets=" + totalTickets
                + '}';
    }
}
