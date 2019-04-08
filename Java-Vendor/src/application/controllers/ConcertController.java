/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.controllers;

import application.data.Concert;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Marcel Judth
 */
public class ConcertController implements Initializable {
    @FXML
    private Label lbl_title;

    @FXML
    private Label lbl_artist;

    @FXML
    private Label lbl_totalTickets;

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


    /**
     * Initializes the controller class.
     */
    private Concert concert;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if(this.concert != null)
            setLabels();
    }   

    void setCurrentTicket(Concert concert) {
        this.concert = concert;
    }

    private void setLabels() {
        this.lbl_artist.setText(concert.getArtist().getName());
        this.lbl_genre.setText(concert.getGenre());
        this.lbl_location.setText(concert.getAddress());
        this.lbl_title.setText(concert.getTitle());
        this.lbl_totalTickets.setText(concert.getTotalTickets());
        String d = concert.getDate().split("T")[0];
        String[] date = d.split("-");
        if (date.length >= 3) {
            this.lbl_day.setText(date[1]);
            this.lbl_year.setText(date[0]);
            this.lbl_month.setText(date[2]);
        }
        
    }
    
}
