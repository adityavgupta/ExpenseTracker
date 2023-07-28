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

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.Map;

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

    @FXML
    private TableView<Expense> mainTable;

    @FXML
    private TableColumn<Expense, Date> dateColumn;

    @FXML
    private TableColumn<Expense, Double> amountColumn;

    @FXML
    private TableColumn<Expense, String> commentsColumn;

    @FXML
    private TableColumn<Expense, String> paymentMethodColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();
        mainTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        mainTable.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.DELETE){
                ObservableList<Expense> selectedRows = mainTable.getSelectionModel().getSelectedItems();

                for(Expense e : selectedRows) {
                    expenseTable.removeExpense(e.getUID());
                }
                mainTable.getItems().removeAll(selectedRows);
            }
            if (event.getCode() == KeyCode.ESCAPE){
                mainTable.getSelectionModel().clearSelection();
            }
        });

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


        mainTable.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean onHidden, Boolean onShown) {
                if(onHidden == true) {
                    mainTable.getSelectionModel().select(-1);
                }
            }
        });

        dateColumn.setCellValueFactory(new PropertyValueFactory<Expense, Date>("date"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<Expense, Double>("amount"));
        commentsColumn.setCellValueFactory(new PropertyValueFactory<Expense, String>("comment"));
        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<Expense, String>("paymentMethod"));

        mainTable.setRowFactory(row->new TableRow<Expense>() {
            @Override
            public void updateItem(Expense item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item == null || empty)
                {
                    for (int i = 0; i<getChildren().size(); i++)
                    {
                        ((Labeled)getChildren().get(i)).setStyle("-fx-background-color: transparent");
                    }
                }
                else {
                    if (item.getExpType().equals(Expense.expenseType.Credit))
                    {
                        for (int i = 0; i<getChildren().size(); i++)
                        {
                            ((Labeled)getChildren().get(i)).setStyle("-fx-background-color: #FF9090");
                        }
                    }
                    else if (item.getExpType().equals(Expense.expenseType.Debit)){
                        for (int i = 0; i<getChildren().size(); i++)
                        {
                            ((Labeled)getChildren().get(i)).setStyle("-fx-background-color: #B5E981");

                        }
                    }
                    else
                    {
                        for (int i = 0; i<getChildren().size(); i++)
                        {
                            ((Labeled)getChildren().get(i)).setStyle("-fx-background-color: transparent");

                        }
                    }
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
            ObservableList<Expense> tableElements = mainTable.getItems();
            tableElements.add(expense);
            mainTable.setItems(tableElements);
        }
    }

    public static void initializeExpenseMap() {
        ExpenseMap.loadBinary();
    }

    private void initializeTable() {
        if(!(ExpenseMap.expenseMap == null || ExpenseMap.expenseMap.isEmpty())){
            for(Map.Entry<Long,Expense> entry : ExpenseMap.expenseMap.entrySet()) {
                ObservableList<Expense> tableElements = mainTable.getItems();
                tableElements.add(entry.getValue());
                mainTable.setItems(tableElements);
            }
        }
    }

}


// Notes
// 1. add save (button, ctrl+s, on exit)
// 2. on submit immediately select and deselect item to get table into focus and for color to update