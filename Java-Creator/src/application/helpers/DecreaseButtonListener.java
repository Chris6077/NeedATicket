/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.helpers;

import com.jfoenix.controls.JFXTextField;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;

/**
 *
 * @author Valon
 */
public class DecreaseButtonListener implements EventHandler {

    int amount;
    Label lbl_changeCash, lbl_balanceOld, lbl_newBalance;
    JFXTextField tft_times;
    AtomicBoolean cashOutMode;

    public DecreaseButtonListener(int amount, Label lbl_changeCash, JFXTextField tft_times, Label lbl_newBalance, Label lbl_balanceOld, AtomicBoolean cashOutMode) {
        this.amount = amount;
        this.lbl_changeCash = lbl_changeCash;
        this.tft_times = tft_times;
        this.lbl_balanceOld = lbl_balanceOld;
        this.lbl_newBalance = lbl_newBalance;
        this.cashOutMode = cashOutMode;
    }

    @Override
    public void handle(Event event) {
        try {
            int oldBalance = Integer.parseInt(lbl_balanceOld.getText());
            int changeCash = Integer.parseInt(lbl_changeCash.getText());

            if (cashOutMode.get()) {
                changeCash -= amount;
                int times = Integer.parseInt(tft_times.getText());
                if (changeCash >= 0 && times > 0) {
                    lbl_changeCash.setText(String.valueOf(changeCash));
                    tft_times.setText("" + --times);
                    lbl_newBalance.setText("" + (oldBalance + changeCash));
                }
            }else{
                changeCash -= amount;
                int times = Integer.parseInt(tft_times.getText());
                if (changeCash >= 0 && times > 0) {
                    lbl_changeCash.setText(String.valueOf(changeCash));
                    tft_times.setText("" + --times);
                    lbl_newBalance.setText("" + (oldBalance - changeCash));
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
