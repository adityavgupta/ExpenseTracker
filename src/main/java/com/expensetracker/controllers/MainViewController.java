package com.expensetracker.controllers;

import com.expensetracker.ExpenseMap;
import com.expensetracker.Expense;

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.Map;

public class MainViewController implements Initializable {

    //Link to github page
    @FXML
    private void handleGitButtonClicked(ActionEvent event) {
        new Application() {
            @Override
            public void start(Stage stage) {
            }
        }.getHostServices().showDocument("https://github.com/adityavgupta/ExpenseTracker");
        event.consume();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        initializeExpenseMap();
    }

    // Load saved binary data from previous sessions into expenseMap
    public static void initializeExpenseMap() {
        ExpenseMap.loadBinary();
    }

    // //Submit button for adding expenses
    // @FXML
    // private void submit(ActionEvent event) {
    //     boolean executeSubmit = true;
    //     try{
    //         amtLabel.setText("Amount");
    //         amount = Float.parseFloat(inputAmount.getText());
    //     }
    //     catch (NumberFormatException e)
    //     {
    //         inputAmount.setStyle("-fx-border-color: #FF0000");
    //         executeSubmit = false;
    //     }
    //     catch (Exception e)
    //     {
    //         inputAmount.setStyle("-fx-border-color: #FF0000");
    //         amtLabel.setText("Error!");
    //         executeSubmit = false;
    //     }

    //     String paymentMethod = paymentMethodText.getText();
    //     String comment = commentText.getText();
    //     String creditOrDebitSelection = creditDebitDropdown.getValue();
    //     if (creditOrDebitSelection == null)
    //     {
    //         creditDebitDropdown.setStyle("-fx-border-color: #FF0000");
    //         executeSubmit = false;
    //     }

    //     Date date = null;

    //     if(inputDate.getValue() == null) {
    //         inputDate.setStyle("-fx-border-color: #FF0000");
    //         executeSubmit = false;
    //     } else {
    //         date = java.sql.Date.valueOf(inputDate.getValue());
    //     }

    //     if (executeSubmit)
    //     {
    //         Expense expense = new Expense(amount, Expense.expenseType.valueOf(creditOrDebitSelection), date, paymentMethod, comment);
    //         expenseTable.addExpense(expense);
    //         ObservableList<Expense> tableElements = mainTable.getItems();
    //         tableElements.add(expense);
    //         mainTable.setItems(tableElements);
    //         selectAndDeselectAll();
    //     }
    // }



}
