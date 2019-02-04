package application.controllers;

import application.Launch;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MenuController implements Initializable {

    @FXML
    private AnchorPane root;

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
    private VBox sp_tickets;

    @FXML
    private HBox top;
    @FXML
    private Pane content;
    @FXML
    private Text txt_firstLetter;
    @FXML
    private Label lbl_emailMenu;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sp_tickets.getChildren().clear();

        //lbl_emailMenu.setText(LoginController.getEmail());
        txt_firstLetter.setText(String.valueOf(lbl_emailMenu.getText().charAt(0)));

        Node[] nodes = new Node[15];

        for (int i = 0; i < 10; i++) {
            try {
                nodes[i] = (Node) FXMLLoader.load(getClass().getResource("/application/views/Item.fxml"));
                sp_tickets.getChildren().add(nodes[i]);

            } catch (IOException ex) {
                Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
            }

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

        } else if (event.getSource().equals(btn_wallet)) {
            changeConent("/application/views/Wallet.fxml");
        } else if (event.getSource().equals(btn_signOut)) {
            // delete all data etc..
            Parent menu = FXMLLoader.load(getClass().getResource("/application/views/Login.fxml"));
            root.getChildren().removeAll();
            root.getChildren().setAll(menu);
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
