/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
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
    private JFXButton mf_createArtist;

    @FXML
    private JFXButton mf_createTickets;

    @FXML
    private JFXButton mf_selectArtists;

    @FXML
    private Pane multiFormRootPane;

    //top bar btn's
    @FXML
    private JFXButton bar_left;

    @FXML
    private JFXButton bar_middle_left;

    @FXML
    private JFXButton bar_middle_right;

    @FXML
    private JFXButton bar_right;
//#3a1c9d
    private final String FINISHED_COLOR = "#00008C";
    private final String UNFINISHED_COLOR = "lightgrey";
    private final String CURRENT_TAP_COLOR = "#00008C"; //00008C
    private final String TAP_TEXT_COLOR = "#000028";
    private final String WHITE = "#fff";

    private final String NORMAL_CSS = "-fx-background-color:" + WHITE + ";"
            + "-fx-text-fill:" + TAP_TEXT_COLOR + ";"
            + "-fx-background-radius: 100;";
    private final String CURRENT_CSS = "-fx-background-color:" + CURRENT_TAP_COLOR + ";"
            + "-fx-text-fill:" + WHITE + ";"
            + "-fx-background-radius: 100;";

    private final HashMap<String, Parent> view_map = new HashMap();
    private final String SELECT_ARTIST_VIEW = "/application/views/SelectArtist.fxml";
    private final String CREATE_CONCERT_VIEW = "/application/views/CreateConcert.fxml";
    private final String CREATE_ARTIST_VIEW = "/application/views/CreateArtist.fxml";

    @FXML
    void handle_btn(ActionEvent event) throws IOException {
        if (event.getSource().equals(mf_createArtist)) {
            //changeConent("/application/views/CreateArtist.fxml");
            System.out.println("hallo");
            if (!view_map.containsKey(CREATE_ARTIST_VIEW)) {
                // play load spinner then change content
                Parent newView = getANDchangeConent(CREATE_ARTIST_VIEW);
                view_map.put(CREATE_ARTIST_VIEW, newView);
            } else {
                Parent view = view_map.get(CREATE_ARTIST_VIEW);
                changeContent(view);
            }

            this.mf_createArtist.setStyle(CURRENT_CSS);
            this.mf_selectArtists.setStyle(NORMAL_CSS);
            this.mf_createTickets.setStyle(NORMAL_CSS);

            this.bar_left.setStyle("-fx-background-color:" + FINISHED_COLOR);
            this.bar_middle_left.setStyle("-fx-background-color:" + UNFINISHED_COLOR);
            this.bar_middle_right.setStyle("-fx-background-color:" + UNFINISHED_COLOR);
            this.bar_right.setStyle("-fx-background-color:" + UNFINISHED_COLOR);

        } else if (event.getSource().equals(mf_selectArtists)) {
            //changeConent("/application/views/SellTickets.fxml");

            if (!view_map.containsKey(SELECT_ARTIST_VIEW)) {
                // play load spinner then change content
                Parent newView = getANDchangeConent(SELECT_ARTIST_VIEW);
                view_map.put(SELECT_ARTIST_VIEW, newView);
            } else {
                Parent view = view_map.get(SELECT_ARTIST_VIEW);
                changeContent(view);
            }

            this.mf_createArtist.setStyle(NORMAL_CSS);
            this.mf_selectArtists.setStyle(CURRENT_CSS);
            this.mf_createTickets.setStyle(NORMAL_CSS);

            this.bar_middle_left.setStyle("-fx-background-color:" + FINISHED_COLOR);
            this.bar_middle_right.setStyle("-fx-background-color:" + FINISHED_COLOR);
            this.bar_right.setStyle("-fx-background-color:" + UNFINISHED_COLOR);
            this.bar_left.setStyle("-fx-background-color:" + FINISHED_COLOR);

        } else if (event.getSource().equals(mf_createTickets)) {
            if (!view_map.containsKey(CREATE_CONCERT_VIEW)) {
                // play load spinner then change content
                Parent newView = getANDchangeConent(CREATE_CONCERT_VIEW);
                view_map.put(CREATE_CONCERT_VIEW, newView);
            } else {
                Parent view = view_map.get(CREATE_CONCERT_VIEW);
                changeContent(view);
            }

            this.mf_createArtist.setStyle(NORMAL_CSS);
            this.mf_selectArtists.setStyle(NORMAL_CSS);
            this.mf_createTickets.setStyle(CURRENT_CSS);

            this.bar_middle_left.setStyle("-fx-background-color:" + FINISHED_COLOR);
            this.bar_middle_right.setStyle("-fx-background-color:" + FINISHED_COLOR);
            this.bar_right.setStyle("-fx-background-color:" + FINISHED_COLOR);
            this.bar_left.setStyle("-fx-background-color:" + FINISHED_COLOR);

        }

//        try {
//            alert("hehe");
//        } catch (IOException ex) {
//            Logger.getLogger(SellTicketsController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> data = FXCollections.observableArrayList();
        if (!view_map.containsKey(CREATE_ARTIST_VIEW)) {
            try {
                // play load spinner then change content
                Parent newView = getANDchangeConent(CREATE_ARTIST_VIEW);
                view_map.put(CREATE_ARTIST_VIEW, newView);
            } catch (IOException ex) {
                Logger.getLogger(SellTicketsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Parent view = view_map.get(CREATE_ARTIST_VIEW);
            changeContent(view);
        }

        this.mf_createArtist.setStyle(CURRENT_CSS);
        this.mf_selectArtists.setStyle(NORMAL_CSS);
        this.mf_createTickets.setStyle(NORMAL_CSS);

        this.bar_left.setStyle("-fx-background-color:" + FINISHED_COLOR);
        this.bar_middle_left.setStyle("-fx-background-color:" + UNFINISHED_COLOR);
        this.bar_middle_right.setStyle("-fx-background-color:" + UNFINISHED_COLOR);
        this.bar_right.setStyle("-fx-background-color:" + UNFINISHED_COLOR);
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
        SelectArtistController.msg = msg2;
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

    private Parent getANDchangeConent(String url) throws IOException {
        Parent menu = FXMLLoader.load(getClass().getResource(url));
        multiFormRootPane.getChildren().removeAll();
        multiFormRootPane.getChildren().setAll(menu);
        return menu;
    }

    private void changeContent(Parent view) {
        multiFormRootPane.getChildren().removeAll();
        multiFormRootPane.getChildren().setAll(view);
    }
}
