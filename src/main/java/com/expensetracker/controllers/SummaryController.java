package com.expensetracker.controllers;

import com.expensetracker.ExpenseMap;
import com.expensetracker.Expense;
import com.expensetracker.Expense.expenseType;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Map;

public class SummaryController implements Initializable
{

    @FXML
    private ChoiceBox<String> averageType;
    private String[] averageTypeDropdown = {"Gross", "Credit", "Debit" };

    @FXML
    private Label dailyVal;

    @FXML
    private Label monthlyVal;

    @FXML
    private Label yearlyVal;

    @FXML
    private Label netCreditVal;

    @FXML
    private Label netDebitVal;

    @FXML
    private Label netGrossVal;


    private long dayInMils = 86400000;

    private double avgAnnualGross = 0;
    private double avgMonthlyGross = 0;
    private double avgWeeklyGross = 0;
    private double avgDailyGross = 0;

    private double avgAnnualCredit = 0;
    private double avgMonthlyCredit = 0;
    private double avgWeeklyCredit = 0;
    private double avgDailyCredit = 0;

    private double avgAnnualDebit = 0;
    private double avgMonthlyDebit = 0;
    private double avgWeeklyDebit = 0;
    private double avgDailyDebit = 0;

    private double netCredit = 0;
    private double netDebit = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        averageType.getItems().addAll(averageTypeDropdown);
        averageType.setValue("Gross");
        averageType.setStyle("-fx-font-size: 1em");

        averageType.valueProperty().addListener(new ChangeListener<String>() 
        {
            @Override
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue)
            {
                if(!oldValue.equals(newValue))
                {
                    calculateAverages();
                }
            }
        });
    }

    //------- for all the functions below add Map<Long, Expense> as argument --------//

    public void calculateAll()
    {
        calculateNetExpense(ExpenseMap.filteredMap);
    }
    private void calculateNetExpense(Map<Long, Expense> data)
    {
        double creditTotal = 0;
        double debitTotal = 0;
        for(Map.Entry<Long, Expense> entry: data.entrySet())
        {
            Expense e = entry.getValue();
            double a = e.getAmount();
            expenseType eType = e.getExpType();
            if(eType == expenseType.Debit) 
            { 
                debitTotal += a;
            }
            else{
                creditTotal += a;
            }
        }
        double grossTotal = -1 * creditTotal + debitTotal;
        netGrossVal.setText(String.format(": $%.2f", grossTotal));
        netCreditVal.setText(String.format(": $%.2f", creditTotal));
        netDebitVal.setText(String.format(": $%.2f", debitTotal));
        calculateAverages();
    }

    private void calculateAverages()
    {
        if(!(ExpenseMap.filteredMap == null || ExpenseMap.filteredMap.isEmpty())){
            double avg = 0;
            double debitAvg = 0;
            double creditAvg = 0;
            int dayCnt = 0;
            Long firstDay = 0L;
            Long lastDay = 0L;
            Boolean firstFlag = true;
            for(Map.Entry<Long, Expense> entry : ExpenseMap.filteredMap.entrySet()){
                Expense e = entry.getValue();
                double a = e.getAmount();
                expenseType eType = e.getExpType();
                avg += (eType == expenseType.Debit) ? a:-1*a;
                if(eType == expenseType.Debit){
                    debitAvg += a;
                }
                else{
                    creditAvg += a;
                }

                if(firstFlag == true){
                    firstDay = e.getUID()/dayInMils;
                    firstFlag = false;
                }
                lastDay = e.getUID()/dayInMils;
            }
            dayCnt = (int)(lastDay - firstDay) + 1;

            netCredit = creditAvg;
            netDebit = debitAvg;

            avg = avg / dayCnt;
            creditAvg = creditAvg / dayCnt;
            debitAvg = debitAvg /dayCnt;

            avgAnnualGross = avg * 365;
            avgMonthlyGross = avg * 30;
            avgWeeklyGross = avg * 7;
            avgDailyGross = avg;

            avgAnnualCredit = creditAvg * 365;
            avgMonthlyCredit = creditAvg * 30;
            avgWeeklyCredit = creditAvg * 7;
            avgDailyCredit = creditAvg;

            avgAnnualDebit = debitAvg * 365;
            avgMonthlyDebit = debitAvg * 30;
            avgWeeklyDebit = debitAvg * 7;
            avgDailyDebit = debitAvg;
            setAvgLabels();
        }
        else{
            avgAnnualGross = 0;
            avgMonthlyGross = 0;
            avgWeeklyGross = 0;
            avgDailyGross = 0;

            avgAnnualCredit = 0;
            avgMonthlyCredit = 0;
            avgWeeklyCredit = 0;
            avgDailyCredit = 0;
    
            avgAnnualDebit = 0;
            avgMonthlyDebit = 0;
            avgWeeklyDebit = 0;
            avgDailyDebit = 0;

            netCredit = 0;
            netDebit = 0;
            setAvgLabels();
        }
         
    }

    private void setAvgLabels()
    {
        String type = averageType.getValue();
        if(type.equals("Gross")){
            dailyVal.setText(": $" + String.format("%.2f",avgDailyGross));
            monthlyVal.setText(": $" + String.format("%.2f",avgMonthlyGross));
            yearlyVal.setText(": $" + String.format("%.2f",avgAnnualGross));
        }
        else if(type.equals("Credit")){
            dailyVal.setText(": $" + String.format("%.2f",avgDailyCredit));
            monthlyVal.setText(": $" + String.format("%.2f",avgMonthlyCredit));
            yearlyVal.setText(": $" + String.format("%.2f",avgAnnualCredit));
        }
        else{
            dailyVal.setText(": $" + String.format("%.2f",avgDailyDebit));
            monthlyVal.setText(": $" + String.format("%.2f",avgMonthlyDebit));
            yearlyVal.setText(": $" + String.format("%.2f",avgAnnualDebit));
        }
    }

    //Calculator (interest rates)

    //Wishlist

}