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

import com.expensetracker.Expense;
import com.expensetracker.Expense.expenseType;
import com.expensetracker.ExpenseMap;

public class LineGraphController implements Initializable {

    private XYChart.Series<String,Number> series;

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
        //lineGraph.setTitle("Title");
        lineGraph.setLegendVisible(false);
        series = new XYChart.Series();
        yAxis.setLabel("$");
        xAxis.setLabel("Date");
        lineGraph.getData().addAll(series);
        updateData();
    }

     public void updateData() {

        series.getData().clear();
        Map<Long, Expense> data = ExpenseMap.filteredMap;
        double tot = 0;
        Long pastUID = Long.MIN_VALUE;
        Boolean flag = true;
        String strDate = "";
        for(Map.Entry<Long, Expense> entry: data.entrySet())
        {
            Expense e = entry.getValue();
            if(!flag && pastUID/1000 != e.getUID()/1000) {
                series.getData().add(new XYChart.Data(strDate, tot));
            }
            flag = false;
            pastUID = e.getUID();
            double a = e.getAmount();
            expenseType eType = e.getExpType();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            strDate = dateFormat.format(e.getDate());
            tot += (eType == expenseType.Debit) ? a:-1*a;
        }
        if(!flag) {
            series.getData().add(new XYChart.Data(strDate, tot));
        }
     }
}