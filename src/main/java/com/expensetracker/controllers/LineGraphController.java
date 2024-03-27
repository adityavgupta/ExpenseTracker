package com.expensetracker.controllers;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import com.expensetracker.DateAxis;
import com.expensetracker.Expense;
import com.expensetracker.Expense.expenseType;
import com.expensetracker.ExpenseMap;

public class LineGraphController implements Initializable {

    private ObservableList<XYChart.Series<Date, Number>> series = FXCollections.observableArrayList();

    @FXML
    private AnchorPane lineGraphAnchorPane;

    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis ;

    @FXML
    private LineChart<String,Number> lineGraph;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        updateData();
    }

     public void updateData() {
         lineGraph.setTitle("Exemplo Gráfico");
         yAxis.setLabel("Valores");
         xAxis.setLabel("Meses");

         XYChart.Series series = new XYChart.Series();
         series.setName("Ano: 2018");
         series.getData().add(new XYChart.Data("Jan", 23));
         series.getData().add(new XYChart.Data("Feb", 14));
         series.getData().add(new XYChart.Data("Mar", 15));
         //series.getData().add(new XYChart.Data("Apr", 24));
         series.getData().add(new XYChart.Data("May", 34));
         series.getData().add(new XYChart.Data("Jun", 36));
         series.getData().add(new XYChart.Data("Jul", 22));
         series.getData().add(new XYChart.Data("Aug", 45));
         series.getData().add(new XYChart.Data("Sep", 43));
         series.getData().add(new XYChart.Data("Oct", 17));
         series.getData().add(new XYChart.Data("Nov", 29));
         series.getData().add(new XYChart.Data("Dec", 25));

         XYChart.Series series2 = new XYChart.Series();
         series2.setName("Ano: 2019");
         series2.getData().add(new XYChart.Data("Jan", 28));
         series2.getData().add(new XYChart.Data("Feb", 17));
         series2.getData().add(new XYChart.Data("Mar", 19));
         //series2.getData().add(new XYChart.Data("Apr", 14));
         series2.getData().add(new XYChart.Data("May", 20));
         series2.getData().add(new XYChart.Data("Jun", 42));
         series2.getData().add(new XYChart.Data("Jul", 27));
         series2.getData().add(new XYChart.Data("Aug", 48));
         series2.getData().add(new XYChart.Data("Sep", 47));
         series2.getData().add(new XYChart.Data("Oct", 19));
         series2.getData().add(new XYChart.Data("Nov", 39));
         series2.getData().add(new XYChart.Data("Dec", 29));

         lineGraph.getData().addAll(series, series2);
     }
}
