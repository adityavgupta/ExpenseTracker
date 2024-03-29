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
    /* 
    @FXML
    private Label netExpense;

    @FXML
    private Label avgAnnualExpense;

    @FXML
    private Label avgMonthlyExpense;

    @FXML
    private Label avgWeeklyExpense;

    @FXML
    private Label avgDailyExpense;

    @FXML
    private Label netEarning;

    @FXML
    private Label totalProfit;*/

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

    private void calculateAvgAnnualExpense()
    {
        double avg = 0;
         
    }

    //Projected expense for the year

    private void calculateAvgMonthlyExpense() 
    {

    }

    private void calculateAvgWeeklyExpense()
    {

    }

    private void calculateAvgDailyExpense() 
    {

    }

    private void calculateNetEarning()
    {

    }

    private void calculateTotalProfit()
    {

    }
}