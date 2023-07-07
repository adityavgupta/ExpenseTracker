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
import javafx.stage.Stage;

import javafx.scene.control.DatePicker;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import javafx.scene.control.TextFormatter.Change;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class MainViewController implements Initializable {

    private ExpenseMap expenseTable = new ExpenseMap();

    @FXML
    private ChoiceBox<String> creditDebitDropdown;
    private String[] CreditDebit = {"Credit","Debit"};

    @FXML
    private TextField inputAmount;
    @FXML
    private Label amtLabel;
    private Float amount;

    @FXML
    private TextField paymentMethodText;

    @FXML
    private TextField commentText;

    @FXML
    private DatePicker inputDate;

    private Boolean dateFlag = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        creditDebitDropdown.getItems().addAll(CreditDebit);

        UnaryOperator<Change> numFilter = change -> {
            String newText = change.getControlNewText();
            Boolean b = Pattern.matches("\\d*\\.?\\d{0,2}", newText);
            if(b) {
                return change;
            }
            return null;
        };

        UnaryOperator<Change> dateFilter = change -> {
            String newText = change.getControlNewText();
            Boolean b = Pattern.matches("\\d*/*\\d*/*\\d*", newText);
            if(b) {
                return change;
            }
            return null;
        };

        TextFormatter<String> numFormatter = new TextFormatter<>(numFilter);
        inputAmount.setTextFormatter(numFormatter);

        TextFormatter<String> dateFormatter = new TextFormatter<>(dateFilter);
        inputDate.getEditor().setTextFormatter(dateFormatter);
        
        inputDate.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean onHidden, Boolean onShown) {
                if(onShown == false) {
                    dateCheck();
                }
            }
        });
    }

    private void dateCheck() {
        //System.out.println(inputDate.getValue());
        if(inputDate.getValue() != null) {
            dateFlag = false;
            inputDate.setStyle("-fx-background-color: #00ff00");
        }
        else {
            dateFlag = true;
            inputDate.setStyle("-fx-background-color: #ff0000");
        }
    }

    @FXML
    private void handleGitButtonClicked(ActionEvent event) {
        new Application() {
            @Override
            public void start(Stage stage) {
            }
        }.getHostServices().showDocument("https://github.com/adityavgupta/ExpenseTracker");
        event.consume();
    }

    @FXML
    private void submit(ActionEvent event) {
        boolean executeSubmit = true;
        try{
            amtLabel.setText("Amount");
            amount = Float.parseFloat(inputAmount.getText());

        }
        catch (NumberFormatException e)
        {
            // maybe replace this with amount box becoming red
            amtLabel.setText("Invalid number");
            executeSubmit = false;
        }
        catch (Exception e)
        {
            // maybe replace this with amount box becoming red
            amtLabel.setText("Error!");
            executeSubmit = false;
        }

        String paymentMethod = paymentMethodText.getText();
        String comment = commentText.getText();
        String creditOrDebitSelection = creditDebitDropdown.getValue();
        if (creditOrDebitSelection == null)
        {
            // maybe replace this with credit box becoming red
            executeSubmit = false;
        }

        Date date = java.sql.Date.valueOf(inputDate.getValue());

        if (dateFlag)
        {
            // maybe replace this with date box becoming red
            executeSubmit = false;
        }

        if (executeSubmit)
        {
            Expense expense = new Expense(amount, Expense.expenseType.valueOf(creditOrDebitSelection), date, paymentMethod, comment);
            expenseTable.addExpense(expense);
            Expense e = expenseTable.expenseMap.get(date.getTime());
        }
    }

    public static void initializeExpenseMap() {
        ExpenseMap.loadBinary();
    }

}
