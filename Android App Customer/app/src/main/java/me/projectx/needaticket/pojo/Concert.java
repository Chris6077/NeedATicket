package me.projectx.needaticket.pojo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class Concert {
    private String id;
    private String title;
    private Date date;
    private Date redeemed;
    private String address;
    private ArrayList<Artist> artists;
    private Genre genre;
    private ArrayList<Ticket> tickets;
    public Concert (String id, String title, Date date, Date redeemed, String address, List<Artist> artists, Genre genre, List<Ticket> tickets) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.redeemed = redeemed;
        this.address = address;
        this.artists = (ArrayList<Artist>) artists;
        this.genre = genre;
        this.tickets = (ArrayList<Ticket>) tickets;
    }
    public String getId () {
        return id;
    }
    public void setId (String id) {
        this.id = id;
    }
    public String getTitle () {
        return title;
    }
    public void setTitle (String title) {
        this.title = title;
    }
    public Date getDate () {
        return date;
    }
    public void setDate (Date date) {
        this.date = date;
    }
    public Date getRedeemed () {
        return redeemed;
    }
    public void setRedeemed (Date redeemed) {
        this.redeemed = redeemed;
    }
    public String getAddress () {
        return address;
    }
    public void setAddress (String address) {
        this.address = address;
    }
    public List<Artist> getArtists () {
        return artists;
    }
    public void setArtists (List<Artist> artists) {
        this.artists = (ArrayList<Artist>) artists;
    }
    public Genre getGenre () {
        return genre;
    }
    public void setGenre (Genre genre) {
        this.genre = genre;
    }
    public List<Ticket> getTickets () {
        return tickets;
    }
    public void setTickets (List<Ticket> tickets) {
        this.tickets = (ArrayList<Ticket>) tickets;
    }
    @Override public String toString () {
        return "Concert{" + "id=" + id + ", title='" + title + '\'' + ", date=" + date + ", redeemed='" + redeemed + '\'' + ", address='" + address + '\'' + ", artists=" + artists + ", genre=" + genre + ", tickets=" + tickets + '}';
    }
}