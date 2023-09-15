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

public class MainViewController implements Initializable {

    private ExpenseMap expenseTable = new ExpenseMap();

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
    @FXML
    private VBox baseWindow;
    private Boolean cntrlPressed = false;
    private Boolean sPressed = false;
    private Boolean iniTableFlag = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Save the table values when cntrl + s is pressed anywhere in the app
        baseWindow.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.S){
                sPressed = true;
            }      
            if(event.getCode() == KeyCode.CONTROL){
                cntrlPressed = true;
            }         
            if(cntrlPressed&&sPressed){
                ExpenseMap.saveBinary();
            }
        });

        /* Fix for when all elements in the table are selected on application start
         * On mouse move clear selections and set flag
         */
        
        mainTable.setOnMouseMoved(event -> {
            if(iniTableFlag){
                mainTable.getSelectionModel().clearSelection();
                iniTableFlag = false;
            }
        });

        //Remove focus from other elements when base window is clicked
        baseWindow.setOnMouseClicked(event -> {
            baseWindow.requestFocus();
        });

        //Remove focus from most elements when esc is pressed
        baseWindow.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                baseWindow.requestFocus();
            }      
        });

        //Supporting logic for cntrl + s table save
        baseWindow.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.S){
                sPressed = false;
            }      
            if(event.getCode() == KeyCode.CONTROL){
                cntrlPressed = false;
            }         
        });

        //Delete selected elements from the table and expenseMap when delete is pressed while the table has focus
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

        //Add choices to the credit/debit dropdown box
        creditDebitDropdown.getItems().addAll(CreditDebit);

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
        mainTable.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean onHidden, Boolean onShown) {
                if(onHidden == true) {
                    mainTable.getSelectionModel().select(-1);
                }
            }
        });

        /* Coloring for table
         * Color the table rows red or green based on credit or debit entry.
         */
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
                        // Credit is red
                        for (int i = 0; i<getChildren().size(); i++)
                        {
                            ((Labeled)getChildren().get(i)).setStyle("-fx-background-color: #FF9090");
                        }
                    }
                    else if (item.getExpType().equals(Expense.expenseType.Debit))
                    {
                        // Debit is green
                        for (int i = 0; i<getChildren().size(); i++)
                        {
                            ((Labeled)getChildren().get(i)).setStyle("-fx-background-color: #B5E981");
                        }
                    }
                    else
                    {
                        // Transparent for all other cases.
                        for (int i = 0; i<getChildren().size(); i++)
                        {
                            ((Labeled)getChildren().get(i)).setStyle("-fx-background-color: transparent");
                        }
                    }
                }
            }
        });
        initializeTable();
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

    //Submit button for adding expenses
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
            selectAndDeselectAll();
        }
    }

    //Load saved binary data from previous sessions into expenseMap
    public static void initializeExpenseMap() {
        ExpenseMap.loadBinary();
    }

    //Load the table with the data from expenseMap and select all rows to apply coloring
    private void initializeTable() {
        mainTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        if(!(ExpenseMap.expenseMap == null || ExpenseMap.expenseMap.isEmpty())){
            mainTable.getItems();
            ObservableList<Expense> tableElements = mainTable.getItems();
            for(Map.Entry<Long,Expense> entry : ExpenseMap.expenseMap.entrySet()) {
                tableElements.add(entry.getValue());
            }
            mainTable.setItems(tableElements);
            mainTable.getSelectionModel().selectAll();
        }
    }

    public void filterTable() {
        if(!(ExpenseMap.filteredMap == null || ExpenseMap.filteredMap.isEmpty())){
            mainTable.getItems().removeAll();
            ObservableList<Expense> tableElements = mainTable.getItems();
            for(Map.Entry<Long,Expense> entry : ExpenseMap.filteredMap.entrySet()) {
                tableElements.add(entry.getValue());
            }
            mainTable.setItems(tableElements);
            selectAndDeselectAll();
        }
    }

    //Select and deselect all the table rows to fix coloring
    private void selectAndDeselectAll() {
        mainTable.getSelectionModel().selectAll();
        mainTable.getSelectionModel().clearSelection();
    }

}
