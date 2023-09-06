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
import javafx.collections.ObservableList;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.Map;

public class FilterController implements Initializable
{
    @FXML
    private TextField minAmount;
    @FXML
    private TextField maxAmount;
    @FXML
    private DatePicker minDate;
    @FXML
    private DatePicker maxDate;
    @FXML
    private CheckBox checkCredit;
    @FXML
    private CheckBox checkDebit;
    @FXML
    private TextField paymentFilter;
    @FXML
    private TextField commentFilter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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

        //Apply monetary filter to amount filters
        TextFormatter<String> numFormatter1 = new TextFormatter<>(numFilter);
        minAmount.setTextFormatter(numFormatter1);
        TextFormatter<String> numFormatter2 = new TextFormatter<>(numFilter);
        maxAmount.setTextFormatter(numFormatter2);

        // Formatting for date
        TextFormatter<String> dateFormatter1 = new TextFormatter<>(dateFilter);
        minDate.getEditor().setTextFormatter(dateFormatter1);
        TextFormatter<String> dateFormatter2 = new TextFormatter<>(dateFilter);
        maxDate.getEditor().setTextFormatter(dateFormatter2);
    }

    @FXML
    private void filter(){
        System.out.println("button pressed");
    }

}