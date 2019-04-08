/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.controllers;

import application.data.Ticket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 *
 * @author schueler
 */
public class ItemController implements Initializable {

    @FXML
    private Label lbl_title;

    @FXML
    private Label lbl_Seller;

    @FXML
    private Label lbl_numberOfTickets;

    @FXML
    private Label lbl_location;

    @FXML
    private Label lbl_day;

    @FXML
    private Label lbl_month;

    @FXML
    private Label lbl_year;

    @FXML
    private Label lbl_genre;

    @FXML
    private Label lbl_artist;
    
    @FXML
    private Label lbl_price;
    private Ticket currentTicket;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (this.currentTicket != null) {
            this.setLabels();
        }
    }

    public Ticket getCurrentTicket() {
        return currentTicket;
    }

    public void setCurrentTicket(Ticket currentTicket) {
        this.currentTicket = currentTicket;
    }

    private void setLabels() {
        String d = currentTicket.getConcert().getDate().split("T")[0];
        String[] date = d.split("-");
        this.lbl_Seller.setText(currentTicket.getSeller().getUsername());
        this.lbl_price.setText("" + currentTicket.getPrice());
        if (currentTicket.getConcert().getArtists() != null) {
            this.lbl_artist.setText(currentTicket.getConcert().getArtists().getName());
        }
        this.lbl_title.setText(currentTicket.getConcert().getTitle());
        this.lbl_numberOfTickets.setText(currentTicket.getConcert().getTotalTickets());
        this.lbl_location.setText(currentTicket.getConcert().getAddress());
        this.lbl_genre.setText(currentTicket.getConcert().getGenre());
        if (date.length >= 3) {
            this.lbl_day.setText(date[1]);
            this.lbl_year.setText(date[0]);
            this.lbl_month.setText(date[2]);
        }
    }

}
