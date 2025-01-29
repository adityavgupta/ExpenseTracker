package com.expensetracker.controllers;

import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import com.expensetracker.Expense;
import com.expensetracker.ExpenseMap;
import com.expensetracker.Main;
import com.expensetracker.Expense.expenseType;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.PopupWindow;
import javafx.stage.PopupWindow.AnchorLocation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

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
    @FXML
    private Button refreshButton;
    @FXML
    private Label amtLabel;
    @FXML
    private Button warningButton;

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

        // Remove focus from the text boxes
        minAmount.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                amtLabel.requestFocus();
            }   
        });
        maxAmount.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                amtLabel.requestFocus();
            }   
        });
        minDate.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                amtLabel.requestFocus();
            }   
        });
        maxDate.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                amtLabel.requestFocus();
            }   
        });

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

        // Turn invalid boxes red
        minDate.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean onHidden, Boolean onShown)
            {
                if(onShown == true)
                {
                    unRed("date");
                }
            }
        });
        maxDate.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean onHidden, Boolean onShown)
            {
                if(onShown == true)
                {
                    unRed("date");
                }
            }
        });
        minAmount.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean onHidden, Boolean onShown)
            {
                if(onShown == true)
                {
                    unRed("amount");
                }
            }
        });
        maxAmount.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean onHidden, Boolean onShown)
            {
                if(onShown == true)
                {
                    unRed("amount");
                }
            }
        });

        warningButton.setVisible(false);
    }

    @FXML
    private void filter(ActionEvent event) throws Exception
    {
        Date dateMin = new Date(Long.MIN_VALUE);
        if(minDate.getValue() != null)
        {
            dateMin = java.sql.Date.valueOf(minDate.getValue());
        }
        Date dateMax = new Date(Long.MAX_VALUE);
        if(maxDate.getValue() != null)
        {
            dateMax = java.sql.Date.valueOf(maxDate.getValue());
        }
        Float amountMin = Float.NaN;
        if(!minAmount.getText().isEmpty())
        {
            amountMin = Float.parseFloat(minAmount.getText());
        }
        Float amountMax = Float.NaN;
        if(!maxAmount.getText().isEmpty())
        {
            amountMax = Float.parseFloat(maxAmount.getText());
        }
        String paymentMethod = paymentFilter.getText();
        String comment = commentFilter.getText();
        Boolean credit = checkCredit.isSelected();
        Boolean debit = checkDebit.isSelected();

        Boolean invalidInput = false;

        invalidInput = dateMin.after(dateMax) || (amountMin > amountMax);

        if(invalidInput)
        {
            if(dateMin.after(dateMax))
            {
                minDate.setStyle("-fx-border-color: #FF0000");
                maxDate.setStyle("-fx-border-color: #FF0000");
            }
            if(amountMin > amountMax)
            {
                minAmount.setStyle("-fx-border-color: #FF0000");
                maxAmount.setStyle("-fx-border-color: #FF0000");
            }

            warningButton.setVisible(true);
            Tooltip t = new Tooltip();
            t.setText("Invalid date or amount range");
            t.setStyle("-fx-font-size: 15");
            t.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_BOTTOM_LEFT);
            Tooltip.install(warningButton, t);
        } 
        else
        {
            warningButton.setVisible(false);
            ExpenseMap.filteredMap.clear();
            Map<Long, Expense> tempMap = ExpenseMap.getDateRange(dateMin, dateMax);
            
            for(Map.Entry<Long, Expense> entry : tempMap.entrySet())
            {
                Expense e = entry.getValue();
                double a = e.getAmount();
                String p = e.getPaymentMethod().toUpperCase();
                String c = e.getComment().toUpperCase();
                expenseType eType = e.getExpType();

                Boolean paymentFlag = true;
                Boolean commentFlag = true;
                Boolean minBoundary = true;
                Boolean maxBoundary = true;
                Boolean typeFlag = (credit && eType == expenseType.Credit) || (debit && eType == expenseType.Debit);

                if(!paymentMethod.isEmpty())
                {
                    paymentFlag = Pattern.matches(String.format(".*%s.*",paymentMethod.toUpperCase()), p);
                }
                if(!comment.isEmpty())
                {
                    commentFlag = Pattern.matches(String.format(".*%s.*",comment.toUpperCase()), c);
                }
                if(!amountMin.isNaN())
                {
                    minBoundary = a >= amountMin;
                }
                if(!amountMax.isNaN())
                {
                    maxBoundary = a <= amountMax;
                }

                if(minBoundary && maxBoundary && paymentFlag && commentFlag && typeFlag)
                {
                    ExpenseMap.filteredMap.put(entry.getKey(),entry.getValue());
                }
            }
            ControllerMediator.getInstance().updateData();
            ControllerMediator.getInstance().filterTable();
            if(ExpenseMap.filteredMap.isEmpty()){
                warningButton.setVisible(true);
                Tooltip t = new Tooltip();
                t.setText("No matching expenses");
                t.setStyle("-fx-font-size: 15");
                t.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_BOTTOM_LEFT);
                Tooltip.install(warningButton, t);
            }
        }
        ControllerMediator.getInstance().calculateAll();
    }

    //Remove red borders from input fields
    private void unRed(String option)
    {
        switch(option)
        {
            case "date":
                minDate.setStyle("-fx-border-color: transparent");
                maxDate.setStyle("-fx-border-color: transparent");
                break;
            case "amount":
                minAmount.setStyle("-fx-border-color: transparent");
                maxAmount.setStyle("-fx-border-color: transparent");
                break;
        }
    }

    //Remove Filters
    @FXML
    private void unfilter()
    {
        Date dMin = new Date(Long.MIN_VALUE);
        Date dMax = new Date(Long.MAX_VALUE);
        warningButton.setVisible(false);
        minAmount.clear();
        maxAmount.clear();
        minDate.setValue(null);
        maxDate.setValue(null);
        minDate.getEditor().clear();
        maxDate.getEditor().clear();
        checkDebit.setSelected(true);
        checkCredit.setSelected(true);
        paymentFilter.clear();
        commentFilter.clear();
        ExpenseMap.filteredMap = ExpenseMap.getDateRange(dMin, dMax);
        ControllerMediator.getInstance().updateData();
        ControllerMediator.getInstance().filterTable();
        unRed("date");
        unRed("amount");
    }

    // Filter Controller methods
    public void setEditablePropertyMinAndMaxAmount(boolean value) throws Exception
    {
        if (!value)
        {
            minAmount.clear();
            maxAmount.clear();

            filter(new ActionEvent());
        }
        minAmount.setEditable(value);
        maxAmount.setEditable(value);
    }

}