package com.expensetracker.controllers;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        lineGraph.setAnimated(false);
        lineGraph.setCreateSymbols(false);
        updateData();
    }

     public void updateData() 
     {
        series.getData().clear();
        Map<Long, Expense> data = ExpenseMap.filteredMap;
        double tot = 0;
        Expense pastE= null;
        Expense e = null;
        Boolean firstDay = true;
        Long pastDay = Long.MIN_VALUE;
        String strDate = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for(Map.Entry<Long, Expense> entry: data.entrySet())
        {
            e = entry.getValue();
            Long currentDay = e.getUID()/dayInMils;
            //System.out.println(new Date(e.getDate().getTime()));
            double a = e.getAmount();
            expenseType eType = e.getExpType();

            if(!pastDay.equals(currentDay) && !firstDay) 
            {
                // Find number of days between
                int fillCount = (int)(currentDay - pastDay);
                for(int i = 0; i < fillCount; i++)
                {
                    // get the days between any two days and fill them in including the second day
                    Date d = new Date(e.getDate().getYear(),e.getDate().getMonth(),e.getDate().getDate()-(fillCount-i-1));
                    System.out.println(d.toString() + " " + tot);
                    series.getData().add(new XYChart.Data(dateFormat.format(d), tot));
                }
                //TODO: Remove extra miliseconds here
                strDate = dateFormat.format(pastE.getDate());
                series.getData().add(new XYChart.Data(strDate, tot));
                System.out.println(pastE.getDate().toString() + " " + tot);
            }
            firstDay = false;
            pastDay = e.getUID()/dayInMils;
            pastE = entry.getValue();
            tot += (eType == expenseType.Debit) ? a:-1*a;
        }
        if(e != null)
        {
            strDate = dateFormat.format(e.getDate());
            series.getData().add(new XYChart.Data(strDate, tot));
            series.getData().sort(new LineChartComparator());
            System.out.println(series.getData().toString());
        }
     }

     private class LineChartComparator implements Comparator
     {
        public int compareSeries(Data<String,Number> data1, Data<String,Number> data2) 
        {
            return data1.getXValue().compareTo(data2.getXValue());
        }

        @Override
        public int compare(Object o1, Object o2) 
        {
            
            int temp = compareSeries((Data<String, Number>) o1, (Data<String, Number>) o2);
            
            
            // the equal string case internally by java was returning the values in an order not suitable to us
            // in this case we tried returning a value that would return data in the order we expect it to be.
            return temp == 0 ? 1:temp;
        }
    }
}