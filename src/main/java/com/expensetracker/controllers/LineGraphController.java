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
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

import javax.sound.midi.SysexMessage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import com.expensetracker.Expense;
import com.expensetracker.Expense.expenseType;
import com.expensetracker.ExpenseMap;

public class LineGraphController implements Initializable {

    private XYChart.Series<String,Number> series;
    private long dayInMils = 86400000;
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
        Long pastDay = Long.MIN_VALUE;
        Boolean firstDay = true;
        String strDate = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for(Map.Entry<Long, Expense> entry: data.entrySet())
        {
            if(firstDay)
            {
                firstDay = false;
                Expense e = entry.getValue();
                expenseType eType = e.getExpType();
                double a = e.getAmount();
                strDate = dateFormat.format(e.getDate());
                tot += (eType == expenseType.Debit) ? a:-1*a;
                series.getData().add(new XYChart.Data(strDate, tot));
                pastDay = e.getUID()/dayInMils;
            }
            else
            {
                Expense e = entry.getValue();
                Long currentDay = e.getUID()/dayInMils;
                double a = e.getAmount();
                expenseType eType = e.getExpType();

                if(pastDay != currentDay) 
                {
                    // Find number of days between
                    Long fillCount = currentDay - pastDay;
                    for(int i = 0; i < fillCount; i++)
                    {
                        // get the days between any two days and fill them in including the second day
                        Date d = new Date(e.getDate().getTime()-(fillCount-i-1)*dayInMils);
                        series.getData().add(new XYChart.Data(dateFormat.format(d), tot));
                    }
                    strDate = dateFormat.format(e.getDate());
                    tot += (eType == expenseType.Debit) ? a:-1*a;
                    series.getData().add(new XYChart.Data(strDate, tot));
                }
                pastDay = e.getUID()/dayInMils;
            }
        }
     }
}