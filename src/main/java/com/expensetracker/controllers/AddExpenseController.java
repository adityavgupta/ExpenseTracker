package com.expensetracker.controllers;

import com.expensetracker.ExpenseMap;
import com.expensetracker.Expense;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.control.TextFormatter.Change;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;


public class AddExpenseController implements Initializable {

    /* Links to FXML elements and variables related to them
     * See resources\MainViewFxml for declerations and how they connect to gui
     */
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Add choices to the credit/debit dropdown box
        creditDebitDropdown.getItems().addAll(CreditDebit);

        //Remove focus from amount and date boxes on esc press
        inputAmount.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                amtLabel.requestFocus();
            }   
        });
        inputDate.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                amtLabel.requestFocus();
            }   
        });

        //Filter for monetary values
        UnaryOperator<Change> numFilter = change -> {
            String newText = change.getControlNewText();
            Boolean b = Pattern.matches("\\d*\\.?\\d{0,2}", newText);
            if(b) {
                return change;
            }
            return null;
        };

        //Filter for dates
        UnaryOperator<Change> dateFilter = change -> {
            String newText = change.getControlNewText();
            Boolean b = Pattern.matches("\\d*/*\\d*/*\\d*", newText);
            if(b) {
                return change;
            }
            return null;
        };

        //Apply monetary filter to amount box
        TextFormatter<String> numFormatter = new TextFormatter<>(numFilter);
        inputAmount.setTextFormatter(numFormatter);

        // Formatting for date
        TextFormatter<String> dateFormatter = new TextFormatter<>(dateFilter);
        inputDate.getEditor().setTextFormatter(dateFormatter);
        
        // GUI fix to unred respective categories.
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

    //Remove red borders from input fields
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

    //Submit button for adding expenses
    @FXML
    private void submit(ActionEvent event) {
        boolean executeSubmit = true;
        try{
            amtLabel.setText("Amount");
            amount = Float.parseFloat(inputAmount.getText());
            //System.out.println(amount);
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
            ExpenseMap.addExpense(expense);
            ControllerMediator.getInstance().addTableExpense(expense);
            ControllerMediator.getInstance().updateData();
        }
    }

}
