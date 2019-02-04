/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.controllers;

import application.helpers.DecreaseButtonListener;
import application.helpers.IncreaseButtonListener;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author schueler
 */
public class WalletController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Pane content;
    @FXML
    private AnchorPane parent;
    @FXML
    private StackPane rootPane;
    @FXML
    private Label lbl_title;
    @FXML
    private Label lbl_balanceOld;
    @FXML
    private Label lbl_cashChangeText;
    @FXML
    private Label lbl_changeCash;
    @FXML
    private Label lbl_newBalance;
    @FXML
    private JFXToggleButton toggleSwitch;
    @FXML
    private JFXButton btn_checkOutMax;
    @FXML
    private JFXButton btn_check;

    // since boolean is immutable when passing arround I needed a mutable type
    private final AtomicBoolean cashOutMode = new AtomicBoolean(true);

    // buttons + -  and amount - textfields
    @FXML
    private JFXButton subtract_10;
    @FXML
    private JFXTextField tft_amount_10;
    @FXML
    private JFXButton add_10;

    @FXML
    private JFXButton subtract_25;
    @FXML
    private JFXTextField tft_amount_25;
    @FXML
    private JFXButton add_25;

    @FXML
    private JFXButton subtract_50;
    @FXML
    private JFXTextField tft_amount_50;
    @FXML
    private JFXButton add_50;

    @FXML
    private JFXButton subtract_100;
    @FXML
    private JFXTextField tft_amount_100;
    @FXML
    private JFXButton add_100;

    @FXML
    private JFXButton subtract_250;
    @FXML
    private JFXTextField tft_amount_250;
    @FXML
    private JFXButton add_250;

    @FXML
    private JFXButton subtract_500;
    @FXML
    private JFXTextField tft_amount_500;
    @FXML
    private JFXButton add_500;

    @FXML
    private JFXButton subtract_1000;
    @FXML
    private JFXTextField tft_amount_1000;
    @FXML
    private JFXButton add_1000;

    @FXML
    private JFXButton subtract_1500;
    @FXML
    private JFXTextField tft_amount_1500;
    @FXML
    private JFXButton add_1500;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initAmountBttns();

    }

    @FXML
    void handle_bttn(ActionEvent event) {
        if (event.getSource().equals(this.toggleSwitch)) {
            int oldBalance = Integer.parseInt(lbl_balanceOld.getText());
            int changeCash = Integer.parseInt(lbl_changeCash.getText());

            if (cashOutMode.get()) {
                lbl_title.setText("CASH-IN");
                lbl_cashChangeText.setText("+MONEY TO CASH-IN");
                toggleSwitch.setText("switch to Cash-OUT");
                content.setStyle("-fx-background-color: #631A86");
                toggleSwitch.setText("switch to Cash-OUT");
                btn_checkOutMax.setOpacity(0);
                btn_checkOutMax.setDisable(true);
                cashOutMode.set(false);

                int result = oldBalance + changeCash;
                lbl_newBalance.setText("" + result);

            } else {
                lbl_title.setText("CASH-OUT");
                lbl_cashChangeText.setText("-MONEY TO CASH OUT");
                toggleSwitch.setText("switch to Cash-IN");
                content.setStyle("-fx-background-color: #333734");
                btn_checkOutMax.setOpacity(1);
                btn_checkOutMax.setDisable(false);
                cashOutMode.set(true);
                int result = oldBalance - changeCash;
                lbl_newBalance.setText("" + result);
            }
        }
    }

    @FXML
    void handle_checks(ActionEvent event) {
        int money;
        if (event.getSource().equals(this.btn_checkOutMax)) {
            money = Integer.parseInt(this.lbl_balanceOld.getText());
            // do webService call
        } else if (event.getSource().equals(this.btn_check)) {
            money = Integer.parseInt(this.lbl_newBalance.getText());
            
            // do webService call
            int newBalance = 0;
            money = Integer.parseInt(this.lbl_newBalance.getText());
        }
        alert("Will take a while. \nTransfer - Process - Simulation");
        resetFields();
    }

    private void initAmountBttns() {

        add_10.setOnAction(new IncreaseButtonListener(10, lbl_changeCash, tft_amount_10, lbl_newBalance, lbl_balanceOld, cashOutMode));
        subtract_10.setOnAction(new DecreaseButtonListener(10, lbl_changeCash, tft_amount_10, lbl_newBalance, lbl_balanceOld, cashOutMode));

        add_25.setOnAction(new IncreaseButtonListener(25, lbl_changeCash, tft_amount_25, lbl_newBalance, lbl_balanceOld, cashOutMode));
        subtract_25.setOnAction(new DecreaseButtonListener(25, lbl_changeCash, tft_amount_25, lbl_newBalance, lbl_balanceOld, cashOutMode));

        add_50.setOnAction(new IncreaseButtonListener(50, lbl_changeCash, tft_amount_50, lbl_newBalance, lbl_balanceOld, cashOutMode));
        subtract_50.setOnAction(new DecreaseButtonListener(50, lbl_changeCash, tft_amount_50, lbl_newBalance, lbl_balanceOld, cashOutMode));

        add_100.setOnAction(new IncreaseButtonListener(100, lbl_changeCash, tft_amount_100, lbl_newBalance, lbl_balanceOld, cashOutMode));
        subtract_100.setOnAction(new DecreaseButtonListener(100, lbl_changeCash, tft_amount_100, lbl_newBalance, lbl_balanceOld, cashOutMode));

        add_250.setOnAction(new IncreaseButtonListener(250, lbl_changeCash, tft_amount_250, lbl_newBalance, lbl_balanceOld, cashOutMode));
        subtract_250.setOnAction(new DecreaseButtonListener(250, lbl_changeCash, tft_amount_250, lbl_newBalance, lbl_balanceOld, cashOutMode));

        add_500.setOnAction(new IncreaseButtonListener(500, lbl_changeCash, tft_amount_500, lbl_newBalance, lbl_balanceOld, cashOutMode));
        subtract_500.setOnAction(new DecreaseButtonListener(500, lbl_changeCash, tft_amount_500, lbl_newBalance, lbl_balanceOld, cashOutMode));

        add_1000.setOnAction(new IncreaseButtonListener(1000, lbl_changeCash, tft_amount_1000, lbl_newBalance, lbl_balanceOld, cashOutMode));
        subtract_1000.setOnAction(new DecreaseButtonListener(1000, lbl_changeCash, tft_amount_1000, lbl_newBalance, lbl_balanceOld, cashOutMode));

        add_1500.setOnAction(new IncreaseButtonListener(1500, lbl_changeCash, tft_amount_1500, lbl_newBalance, lbl_balanceOld, cashOutMode));
        subtract_1500.setOnAction(new DecreaseButtonListener(1500, lbl_changeCash, tft_amount_1500, lbl_newBalance, lbl_balanceOld, cashOutMode));
    }

    public void alert(String msg) {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        JFXDialogLayout dl = new JFXDialogLayout();
        JFXButton bttn = new JFXButton("OK");
        JFXDialog dialog = new JFXDialog(rootPane, dl, JFXDialog.DialogTransition.TOP);
        bttn.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            dialog.close();
        });
        dl.setHeading(new Text(msg));

        JFXSpinner s = new JFXSpinner();
        s.setScaleX(1.5);
        s.setScaleY(1.5);
        dl.setBody(s);
        dl.setActions(bttn);
        dialog.show();
        dialog.setOnDialogClosed((JFXDialogEvent e) -> {
            parent.setEffect(null);
        });

        PauseTransition wait = new PauseTransition(Duration.seconds(1.5));
        wait.setOnFinished((e) -> {
            /*YOUR METHOD*/
            dialog.close();
            wait.playFromStart();
        });
        wait.play();
        parent.setEffect(blur);
    }

    private void resetFields() {
        int oldBalance = Integer.parseInt(this.lbl_newBalance.getText());
        this.lbl_balanceOld.setText("" + oldBalance);
        this.lbl_changeCash.setText("" + 0);
        
        
        this.tft_amount_10.setText("" + 0);
        this.tft_amount_25.setText("" + 0);
        this.tft_amount_50.setText("" + 0);
        this.tft_amount_100.setText("" + 0);
        this.tft_amount_250.setText("" + 0);
        this.tft_amount_500.setText("" + 0);
        this.tft_amount_1000.setText("" + 0);
        this.tft_amount_1500.setText("" + 0);
    }
}
