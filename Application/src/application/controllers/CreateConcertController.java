/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author schueler
 */
public class CreateConcertController implements Initializable {

    @FXML
    private JFXTextField tf_price;
    @FXML
    private Label lbl_price;

    @FXML
    private Label lbl_numberOfTickets;

    @FXML
    private Label lbl_location;

    @FXML
    private Label lbl_title;

    @FXML
    private Label lbl_date_day;

    @FXML
    private Label lbl_date_month;

    @FXML
    private Label lbl_date_year;

    @FXML
    private Label lbl_genre;

    @FXML
    private JFXTextField tf_location;

    @FXML
    private JFXTextField tf_numberOfTickets;

    @FXML
    private JFXTextField tf_genre;

    @FXML
    private JFXTextField tf_title;

    @FXML
    private JFXDatePicker date;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tf_price.textProperty().addListener(new ChangeListener<String>() {
            public void changed(final ObservableValue<? extends String> observableValue, final String oldValue,
                    final String newValue) {
                System.out.println(newValue);
                lbl_price.setText(newValue);

            }
        });
        tf_location.textProperty().addListener(new ChangeListener<String>() {
            public void changed(final ObservableValue<? extends String> observableValue, final String oldValue,
                    final String newValue) {
                System.out.println(newValue);
                lbl_location.setText(newValue);

            }
        });
        tf_numberOfTickets.textProperty().addListener(new ChangeListener<String>() {
            public void changed(final ObservableValue<? extends String> observableValue, final String oldValue,
                    final String newValue) {
                System.out.println(newValue);
                lbl_numberOfTickets.setText(newValue);

            }
        });
        tf_genre.textProperty().addListener(new ChangeListener<String>() {
            public void changed(final ObservableValue<? extends String> observableValue, final String oldValue,
                    final String newValue) {
                System.out.println(newValue);
                lbl_genre.setText(newValue);

            }
        });
        tf_title.textProperty().addListener(new ChangeListener<String>() {
            public void changed(final ObservableValue<? extends String> observableValue, final String oldValue,
                    final String newValue) {
                System.out.println(newValue);
                lbl_title.setText(newValue);

            }
        });
        date.valueProperty().addListener((ov, oldValue, newValue) -> {
            LocalDate localDate = date.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date _date = Date.from(instant);

            this.lbl_date_day.setText("" + _date.getDay());
            this.lbl_date_month.setText("" + _date.getMonth());
            this.lbl_date_year.setText("" + _date.getYear());

            System.out.println(localDate + "\n" + instant + "\n" + date);
        });

    }

}
