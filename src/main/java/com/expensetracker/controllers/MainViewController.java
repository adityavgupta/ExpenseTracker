package com.expensetracker.controllers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

import javafx.scene.control.DatePicker;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import javafx.scene.control.TextFormatter.Change;
import org.w3c.dom.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

//import javax.swing.event.ChangeListener;

public class MainViewController implements Initializable {

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
        System.out.println(inputDate.getValue());
        if(inputDate.getValue() != null) {
            dateFlag = false;
            inputDate.setStyle("-fx-background-color: #00ff00");
        }
        else {
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
        boolean executeSubmit = false;
        try{
            amtLabel.setText("Amount");
            amount = Float.parseFloat(inputAmount.getText());
            executeSubmit = true;
        }
        catch (NumberFormatException e)
        {
            amtLabel.setText("Invalid number");
        }
        catch (Exception e)
        {
            amtLabel.setText("Error!");
        }

        String paymentMethod = paymentMethodText.getText();
        String comment = commentText.getText();
        String creditOrDebitSelection = creditDebitDropdown.getValue();
        LocalDate date = inputDate.getValue();

        if (creditOrDebitSelection != null && date != null)
        {
            executeSubmit = true;
        }

        // construct the expense map and add the above values
        if (executeSubmit)
        {

        }
    }

}
