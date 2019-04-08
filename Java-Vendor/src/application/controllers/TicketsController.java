/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.controllers;

import application.data.Concert;
import application.data.GenreTyp;
import application.data.Seller;
import application.data.Ticket;
import application.data.User;
import application.data.Wallet;
import database.Database;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Marcel Judth
 */
public class TicketsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private ScrollPane scrollPaneTickets;

    @FXML
    private VBox sp_tickets;
    
    @FXML
    private Label lbl_totalTickets;

    @FXML
    private Label lbl_totalConcerts;
    
    Database db;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            db = new Database();
            ArrayList<Ticket> allTickets = db.getTickets();
            sp_tickets.getChildren().clear();
            Node[] nodes = new Node[allTickets.size()];
            this.lbl_totalTickets.setText("" + allTickets.size());
            ArrayList<Concert> allConcerts = new ArrayList<>();
            
            
            for (int i = 0; i < allTickets.size(); i++) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/Item.fxml"));
                    ItemController controller = new ItemController();
                    controller.setCurrentTicket(allTickets.get(i));
                    loader.setController(controller);
                    nodes[i] = (Node) loader.load();
                    sp_tickets.getChildren().add(nodes[i]);
                    if(!allConcerts.contains(allTickets.get(i).getConcert()))
                        allConcerts.add(allTickets.get(i).getConcert());
                } catch (IOException ex) {
                    Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.lbl_totalConcerts.setText("" + allConcerts.size());
            }
            scrollPaneTickets.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        } catch (Exception ex) {
            Logger.getLogger(TicketsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
