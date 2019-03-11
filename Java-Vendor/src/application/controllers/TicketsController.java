/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        HashMap<String, String> concerts = new HashMap<>();
        sp_tickets.getChildren().clear();
        Node[] nodes = new Node[15];

        for (int i = 0; i < 10; i++) {
            try {
                nodes[i] = (Node) FXMLLoader.load(getClass().getResource("/application/views/Item.fxml"));
                sp_tickets.getChildren().add(nodes[i]); 

            } catch (IOException ex) {
                Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        scrollPaneTickets.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }    
    
}
