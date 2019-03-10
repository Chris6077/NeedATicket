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
import com.jfoenix.controls.JFXButton;
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
import javafx.scene.Parent;
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
    
    Node[] nodes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.setListener();
        HashMap<String, String> concerts = new HashMap<>();
        sp_tickets.getItems().clear();
        nodes = new Node[15];

        for (int i = 0; i < 10; i++) {
            try {
                nodes[i] = (Node) FXMLLoader.load(getClass().getResource("/application/views/Concert.fxml"));
                nodes[i].setId("concertID");
                sp_tickets.getItems().add(nodes[i]); 
            } catch (IOException ex) {
                Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
    
    @FXML
    void handleBtn(ActionEvent event) {
        try {
            if(event.getSource() == btn_NEXT){
                    String concertid = this.sp_tickets.getSelectionModel().getSelectedItem().getId();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("SellTickets.fxml"));
                    
                    
                    SellTicketsController controller = loader.getController();
                    
                    Concert selectedConcert = new Concert("Concert 1", LocalDate.now(), "", new ArrayList<Artist> (), new Genre("POP"), new ArrayList<Ticket> ());
                    controller.setConcert(selectedConcert);
                    controller.nextView();
            }
        } catch (Exception ex) {
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
    
}
