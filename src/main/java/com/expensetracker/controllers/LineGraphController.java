package com.expensetracker.controllers;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
private LineChart<String,Number> lineGraph;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {           
        series = new XYChart.Series();
        updateData();
        if(!(series.getData() == null)) {
            lineGraph.getData().add(series);
            lineGraph.setLegendVisible(false);
        }
    }
 
    public void updateData() {
        series.getData().clear();
        Map<Long, Expense> data = ExpenseMap.filteredMap;
        double tot = 0;
        for(Map.Entry<Long, Expense> entry: data.entrySet())
        {
            Expense e = entry.getValue();
            double a = e.getAmount();
            expenseType eType = e.getExpType();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String strDate = dateFormat.format(e.getDate());
            tot += (eType == expenseType.Debit) ? a:-1*a;
            series.getData().add(new XYChart.Data(strDate, tot));
        }

        //lineGraph.getData().add(series);
    }
}
