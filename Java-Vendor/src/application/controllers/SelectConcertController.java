/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.controllers;

import application.data.Artist;
import application.data.Concert;
import application.data.Genre;
import application.data.Ticket;
import application.helpers.SellTicketsViewsController;
import com.jfoenix.controls.JFXButton;
import database.Database;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author Marcel Judth
 */
public class SelectConcertController implements Initializable {

    @FXML
    private ListView<Node> sp_tickets;
    
    @FXML
    private JFXButton btn_NEXT;
    
    @FXML
    private Label lblMessage;
    
    Database db;
    ArrayList<Concert> allConcerts;
    
    Node[] nodes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            db = new Database();
            this.setListener();
            SellTicketsViewsController.setChildController(this);
            allConcerts = db.getConcerts();
            sp_tickets.getItems().clear();
            nodes = new Node[allConcerts.size()];
            
            for (int i = 0; i < allConcerts.size(); i++) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/Concert.fxml"));
                    ConcertController controller = new ConcertController();
                    controller.setCurrentTicket(allConcerts.get(i));
                    loader.setController(controller);
                    nodes[i] = (Node) loader.load();
                    nodes[i].setId(allConcerts.get(i).getId());
                    sp_tickets.getItems().add(nodes[i]);
                } catch (IOException ex) {
                    Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        } catch (Exception ex) {
            Logger.getLogger(SelectConcertController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    @FXML
    void handleBtn(ActionEvent event) {
        try {
            if(event.getSource() == btn_NEXT){
                this.changeView();
            }
        } catch (Exception ex) {
            this.lblMessage.setText(ex.getMessage());
            Logger.getLogger(SelectConcertController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   

    private void setListener() {
        this.sp_tickets.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Node>() {

            @Override
            public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                System.out.println("Hello");
                System.out.println(newValue.getId());
            }
        });
    }

    private void changeView() throws Exception {
        if(this.sp_tickets.getSelectionModel().getSelectedItem() == null)
            throw new Exception("Please select a concert!");
        String concertid = this.sp_tickets.getSelectionModel().getSelectedItem().getId();
        SellTicketsController parentController = SellTicketsViewsController.getParentController();
        Concert c = null;
        for(Concert concert : allConcerts)
            if(concert.getId() == concertid)
                c = concert;
        if(parentController == null)
            throw new Exception("parent controller has not been set");
        parentController.setConcert(c);
        parentController.nextView();
    }

    public void handleError(String message) {
        this.lblMessage.setText(message);
    }
    
}
