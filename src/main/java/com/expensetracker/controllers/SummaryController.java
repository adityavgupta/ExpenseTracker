package com.expensetracker.controllers;

import com.expensetracker.ExpenseMap;
import com.expensetracker.Expense;
import com.expensetracker.Expense.expenseType;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.Map;

public class SummaryController implements Initializable
{

    @FXML
    private ChoiceBox<String> averageType;

    @FXML
    private Label dailyVal;

    @FXML
    private Label monthlyVal;

    @FXML
    private Label yearlyVal;

    @FXML
    private Label netEarningsVal;

    @FXML
    private Label netExpensesVal;

    @FXML
    private Label netProfitVal;


    private long dayInMils = 86400000;

    private double avgAnnualExpense = 0;
    private double avgMonthlyExpense = 0;
    private double avgWeeklyExpense = 0;
    private double avgDailyExpense = 0;

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
    }

    //------- for all the functions below add Map<Long, Expense> as argument --------//

    public void calculateAll()
    {
        calculateNetExpense(ExpenseMap.filteredMap);
    }
    private void calculateNetExpense(Map<Long, Expense> data)
    {
        double tot = 0;
        for(Map.Entry<Long, Expense> entry: data.entrySet())
        {
            Expense e = entry.getValue();
            double a = e.getAmount();
            expenseType eType = e.getExpType();
            tot += (eType == expenseType.Debit) ? a:-1*a;
        }
        //netExpense.setText(String.format("$%.2f", tot));
    }

    private void calculateAverages()
    {
        if(!(ExpenseMap.filteredMap == null || ExpenseMap.filteredMap.isEmpty())){
            double avg = 0;
            double debitAvg = 0;
            double creditAvg = 0;
            int dayCnt = 0;
            Long pastDay = Long.MIN_VALUE;
            for(Map.Entry<Long, Expense> entry : ExpenseMap.filteredMap.entrySet()){
                Expense e = entry.getValue();
                double a = e.getAmount();
                Long currentDay = e.getUID()/dayInMils;
                expenseType eType = e.getExpType();
                avg += (eType == expenseType.Debit) ? a:-1*a;
                if(eType == expenseType.Debit){
                    debitAvg += a;
                }
                else{
                    creditAvg += a;
                }
                dayCnt += !pastDay.equals(currentDay) ? 1:0;
            }
            double netCredit = creditAvg;
            double netDebit = debitAvg;

            avg = avg / dayCnt;
            creditAvg = creditAvg / dayCnt;
            debitAvg = debitAvg /dayCnt;

            double avgAnnualExpense = avg * 365;
            double avgMonthlyExpense = avg * 30;
            double avgWeeklyExpense = avg * 7;
            double avgDailyExpense = avg;

            double avgAnnualCredit = creditAvg * 365;
            double avgMonthlyCredit = creditAvg * 30;
            double avgWeeklyCredit = creditAvg * 7;
            double avgDailyCredit = creditAvg;

            double avgAnnualDebit = debitAvg * 365;
            double avgMonthlyDebit = debitAvg * 30;
            double avgWeeklyDebit = debitAvg * 7;
            double avgDailyDebit = debitAvg;
        }
        else{
            double avgAnnualExpense = 0;
            double avgMonthlyExpense = 0;
            double avgWeeklyExpense = 0;
            double avgDailyExpense = 0;

            double avgAnnualCredit = 0;
            double avgMonthlyCredit = 0;
            double avgWeeklyCredit = 0;
            double avgDailyCredit = 0;
    
            double avgAnnualDebit = 0;
            double avgMonthlyDebit = 0;
            double avgWeeklyDebit = 0;
            double avgDailyDebit = 0;

            double netCredit = 0;
            double netDebit = 0;
        }
         
    }

    //Calculator (interest rates)

    //Wishlist

}