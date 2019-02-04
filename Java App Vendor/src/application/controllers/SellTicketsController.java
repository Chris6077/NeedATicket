/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author schueler
 */
public class SellTicketsController implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private AnchorPane parent;

    @FXML
    private JFXButton btn_createArtist;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        ObservableList<String> options = 
//    FXCollections.observableArrayList(
//        "Option 1",
//        "Option 2",
//        "Option 3"
//    );
//        cmbx.getItems().addAll(options);
    }

    @FXML
    void handleBtn(ActionEvent event) {
        try {
            alert("hehe");
        } catch (IOException ex) {
            Logger.getLogger(SellTicketsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void alert(String msg) throws IOException {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        JFXDialogLayout dl = new JFXDialogLayout();
        JFXButton bttn = new JFXButton("Ok");
        JFXDialog dialog = new JFXDialog(rootPane, dl, JFXDialog.DialogTransition.TOP);
        
        bttn.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            dialog.close();
        });
        String msg2 = msg;
        dl.setHeading(new Text(msg2));
        Pane content = new Pane();
        CreateArtistController.msg = msg2;
        Parent menu = FXMLLoader.load(getClass().getResource("/application/views/CreateArtist.fxml"));
        content.getChildren().removeAll();
        content.getChildren().setAll(menu);
//
        dl.setBody(content);
        dl.setActions(bttn);
        dialog.show();
        dialog.setOnDialogClosed((JFXDialogEvent e) -> {
            parent.setEffect(null);
        });
        parent.setEffect(blur);
    }

}
