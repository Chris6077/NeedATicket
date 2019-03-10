package application.data;

import java.time.LocalDate;
import java.util.List;

public class Concert {
    private String title;
    private LocalDate date;
    private String address;
    private List<Artist> artists;
    private Genre genre;
    private List<Ticket> tickets;

    public Concert(String title, LocalDate date, String address, List<Artist> artists, Genre genre, List<Ticket> tickets) {
        this.title = title;
        this.date = date;
        this.address = address;
        this.artists = artists;
        this.genre = genre;
        this.tickets = tickets;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "Concert{" +
                "title='" + title + '\'' +
                ", date=" + date +
                ", address='" + address + '\'' +
                ", artists=" + artists +
                ", genre=" + genre +
                ", tickets=" + tickets +
                '}';
    }
}
