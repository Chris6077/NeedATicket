package me.projectx.needaticket.pojo;

import java.util.ArrayList;
import java.util.Date;

public class Concert {
    private String id;
    private String title;
    private Date date;
    private String address;
    private ArrayList<Artist> artists;
    private Genre genre;
    private ArrayList<Ticket> tickets;

    public Concert(String id, String title, Date date, String address, ArrayList<Artist> artists, Genre genre, ArrayList<Ticket> tickets) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.address = address;
        this.artists = artists;
        this.genre = genre;
        this.tickets = tickets;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<Artist> artists) {
        this.artists = artists;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "Concert{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", address='" + address + '\'' +
                ", artists=" + artists +
                ", genre=" + genre +
                ", tickets=" + tickets +
                '}';
    }
}