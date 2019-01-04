/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import java.sql.Date;

/**
 *
 * @author Julian
 */
public class Concert {
    private int id;
    private String title;
    private Date date;
    private String genre;
    private String address;
    private Artist artist;

    public Concert(int id, String title, Date date, String genre, String address, Artist artist) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.genre = genre;
        this.address = address;
        this.artist = artist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "Concert{" + "id=" + id + ", title=" + title + ", date=" + date + ", genre=" + genre + ", address=" + address + ", artist=" + artist + '}';
    }
    
}
