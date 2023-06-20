package com.expensetracker.controllers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class MainViewController implements Initializable {

    @FXML
    private ChoiceBox<String> creditDebitDropdown;

    private String[] CreditDebit = {"Credit","Debit"};

    @FXML
    private TextField inputAmount;

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

        TextFormatter<String> numFormatter = new TextFormatter<>(numFilter);
        inputAmount.setTextFormatter(numFormatter);
        
        inputDate.getEditor().setOnAction(this::dateCheck);
    }

    private void dateCheck(ActionEvent event) {
        System.out.println("testing");
        try {
            inputDate.getValue();
            dateFlag = false;
            inputDate.setStyle("-fx-background-color: #00ff00");
        }
        catch(Exception e) {
            dateFlag = true;
            inputDate.setStyle("-fx-background-color: #ff0000");
        }
    }

    public Boolean getDateFlag() {
        return dateFlag;
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
        System.out.println("Button Clicked");
    }

}
