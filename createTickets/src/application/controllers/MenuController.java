package application.controllers;

import application.Launch;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class MenuController implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private AnchorPane parent;

    @FXML
    private JFXButton btn_dashboard;

    @FXML
    private JFXButton btn_transactions;

    @FXML
    private JFXButton btn_sellTicket;

    @FXML
    private JFXButton btn_tickets;

    @FXML
    private JFXButton btn_wallet;

    @FXML
    private JFXButton btn_signOut;

    @FXML
    private Pane content;
    @FXML
    private Text txt_firstLetter;
    @FXML
    private Label lbl_emailMenu;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            changeConent("/application/views/Tickets.fxml");
            //lbl_emailMenu.setText(LoginController.getEmail());
            //txt_firstLetter.setText(String.valueOf(lbl_emailMenu.getText().charAt(0)));
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void alert(String msg) throws IOException {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        JFXDialogLayout dl = new JFXDialogLayout();

        JFXDialog dialog = new JFXDialog(rootPane, dl, JFXDialog.DialogTransition.TOP);
        Parent view = FXMLLoader.load(getClass().getResource("/application/views/ChangeUserPassword.fxml"));

        dialog.getChildren().add(view);

        dialog.show();
        dialog.setOnDialogClosed((JFXDialogEvent e) -> {
            parent.setEffect(null);
        });
        parent.setEffect(blur);

    }

    @FXML
    void handle_logo(ActionEvent event) {

        try {
            alert("bruder muss los");
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void handle_logout(ActionEvent event) throws IOException {
        if (event.getSource().equals(btn_transactions)) {
            changeConent("/application/views/Transactions.fxml");
        } else if (event.getSource().equals(btn_dashboard)) {
            changeConent("/application/views/Dashboard.fxml");
        } else if (event.getSource().equals(btn_sellTicket)) {
            changeConent("/application/views/SellTickets.fxml");
        } else if (event.getSource().equals(btn_tickets)) {
            changeConent("/application/views/Tickets.fxml");
        } else if (event.getSource().equals(btn_wallet)) {
            changeConent("/application/views/Wallet_1.fxml");
        } else if (event.getSource().equals(btn_signOut)) {
            // delete all data etc..
            Parent menu = FXMLLoader.load(getClass().getResource("/application/views/Login.fxml"));
            parent.getChildren().removeAll();
            parent.getChildren().setAll(menu);
        }
    }

    private void changeConent(String url) throws IOException {
        Parent menu = FXMLLoader.load(getClass().getResource(url));
        content.getChildren().removeAll();
        content.getChildren().setAll(menu);
    }

    @FXML
    private void minimize_stage(MouseEvent event) {
        Launch.stage.setIconified(true);
    }

    @FXML
    private void close_app(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    void handleClicks(ActionEvent event) {

    }

}
