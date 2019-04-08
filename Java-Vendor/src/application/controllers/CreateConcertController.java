/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.controllers;

import application.data.Concert;
import application.data.Seller;
import application.data.Ticket;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import database.Database;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author schueler
 */
public class CreateConcertController implements Initializable {

    @FXML
    private JFXTextField tf_price;
    @FXML
    private Label lbl_price;

    @FXML
    private Label lbl_numberOfTickets;

    @FXML
    private Label lbl_location;

    @FXML
    private Label lbl_title;

    @FXML
    private Label lbl_date_day;

    @FXML
    private Label lbl_date_month;

    @FXML
    private Label lbl_date_year;

    @FXML
    private Label lbl_genre;

    @FXML
    private JFXTextField tf_location;

    @FXML
    private JFXTextField tf_numberOfTickets;

    @FXML
    private JFXTextField tf_genre;

    @FXML
    private JFXTextField tf_title;

    @FXML
    private JFXDatePicker date;
    
    @FXML
    private Label lblMessage;

    
    private SellTicketsController parent;
    private Concert concert;
    private Database db;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.db = new Database();
        this.setLables();
        tf_price.textProperty().addListener(new ChangeListener<String>() {
            public void changed(final ObservableValue<? extends String> observableValue, final String oldValue,
                    final String newValue) {
                System.out.println(newValue);
                lbl_price.setText(newValue);

            }
        });
        
        tf_numberOfTickets.textProperty().addListener(new ChangeListener<String>() {
            public void changed(final ObservableValue<? extends String> observableValue, final String oldValue,
                    final String newValue) {
                System.out.println(newValue);
                lbl_numberOfTickets.setText(newValue);

            }
        });
        tf_genre.textProperty().addListener(new ChangeListener<String>() {
            public void changed(final ObservableValue<? extends String> observableValue, final String oldValue,
                    final String newValue) {
                System.out.println(newValue);
                lbl_genre.setText(newValue);

            }
        });

    }

    public void setParent(SellTicketsController parent) {
        this.parent = parent;
    }
    
    public void setConcert(Concert c){
        this.concert = c;
    }
    
    @FXML
    void createTicket(ActionEvent event) {
        try {
            System.out.println(this.tf_numberOfTickets.getText());
            System.out.println(Integer.parseInt(this.tf_numberOfTickets.getText()));
            Ticket t = new Ticket("", this.tf_genre.getText(), Float.parseFloat(this.tf_price.getText()), Integer.parseInt(this.lbl_numberOfTickets.getText()), concert, new Seller("", ""), null);
            db.insert(t);
            this.lblMessage.setText("sucessfully inserted!");
        } catch (Exception ex) {
            this.lblMessage.setText(ex.getMessage());
            Logger.getLogger(CreateConcertController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setLables() {
        this.lbl_title.setText(concert.getTitle());
        this.lbl_location.setText(concert.getAddress());
        String d = concert.getDate().split("T")[0];
        String[] date = d.split("-");
        if (date.length >= 3) {
            this.lbl_date_day.setText(date[1]);
            this.lbl_date_year.setText(date[0]);
            this.lbl_date_month.setText(date[2]);
        }
        this.lbl_genre.setText(concert.getGenre());
    }
}
