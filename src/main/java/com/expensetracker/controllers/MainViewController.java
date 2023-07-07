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
                if(onShown == true) {
                    unRed("date");
                }
            }
        });

        inputAmount.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean onHidden, Boolean onShown) {
                if(onShown == true) {
                    unRed("amount");
                }
            }
        });

        creditDebitDropdown.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean onHidden, Boolean onShown) {
                if(onShown == true) {
                    unRed("creditOrDebit");
                }
            }
        });
    }

    private void unRed(String option) {
        switch(option) {
            case "date":
                inputDate.setStyle("-fx-border-color: transparent");
                break;
            case "amount":
                inputAmount.setStyle("-fx-border-color: transparent");
                break;
            case "creditOrDebit":
                creditDebitDropdown.setStyle("-fx-border-color: transparent");
                break;
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
            inputAmount.setStyle("-fx-border-color: #FF0000");
            executeSubmit = false;
        }
        catch (Exception e)
        {
            inputAmount.setStyle("-fx-border-color: #FF0000");
            amtLabel.setText("Error!");
            executeSubmit = false;
        }

        String paymentMethod = paymentMethodText.getText();
        String comment = commentText.getText();
        String creditOrDebitSelection = creditDebitDropdown.getValue();
        if (creditOrDebitSelection == null)
        {
            creditDebitDropdown.setStyle("-fx-border-color: #FF0000");
            executeSubmit = false;
        }

        Date date = null;

        if(inputDate.getValue() == null) {
            inputDate.setStyle("-fx-border-color: #FF0000");
            executeSubmit = false;
        } else {
            date = java.sql.Date.valueOf(inputDate.getValue());
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


// Notes
// 1. If a correct date is input into the date box and then an incorrect date is input after it seems to use the previous correct date
// 2. Using background color to set the border around the input boxes doesn't quite seem to put them back to defualt