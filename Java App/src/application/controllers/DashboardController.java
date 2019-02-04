/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author schueler
 */
public class DashboardController implements Initializable {

    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private LineChart<Number, Number> myChart;

    @FXML
    private Label lbl_totalTrans;

    @FXML
    private Label lbl_totalTickets;

    @FXML
    private Label lbl_totalConcerts;

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        myChart.setTitle("Balance Monitoring, 2018");

        XYChart.Series series = new XYChart.Series();
        series.setName("Average Earned Amount Of Money Per Day");

        series.getData().add(new XYChart.Data("24.02.2018", 23));
        series.getData().add(new XYChart.Data("24.03.2018", 14));
        series.getData().add(new XYChart.Data("24.04.2018", 15));
        series.getData().add(new XYChart.Data("24.05.2018", 24));
        series.getData().add(new XYChart.Data("24.06.2018", 34));
        series.getData().add(new XYChart.Data("24.07.2018", 36));
        series.getData().add(new XYChart.Data("24.03.2018", 22));

        myChart.getData().add(series);
    }

}
