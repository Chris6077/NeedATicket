/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author schueler
 */
public class CreateArtistController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public static String msg;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private JFXTextField tf_name;

    @FXML
    private JFXButton btn_CreateArtist;

    @FXML
    void handleBtn(ActionEvent event) {
        if (event.getSource().equals(btn_CreateArtist)) {
            System.out.println("heleleleleo");
            System.out.println("mgs : " + msg);
            msg = tf_name.getText();
            System.out.println("mgs : " + msg);
        }

    }

}
