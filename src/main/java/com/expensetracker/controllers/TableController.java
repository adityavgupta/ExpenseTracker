package com.expensetracker.controllers;

import com.expensetracker.ExpenseMap;

import com.expensetracker.Expense;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Map;


public class TableController implements Initializable{

    private Boolean iniTableFlag = true;
    
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

        // /* Fix for when all elements in the table are selected on application start
        //  * On mouse move clear selections and set flag
        //  */
        
        mainTable.setOnMouseMoved(event -> {
            if(iniTableFlag){
                mainTable.getSelectionModel().clearSelection();
                iniTableFlag = false;
            }
        });

        //Delete selected elements from the table and expenseMap when delete is pressed while the table has focus
        mainTable.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.DELETE){
                ObservableList<Expense> selectedRows = mainTable.getSelectionModel().getSelectedItems();

                for(Expense e : selectedRows) {
                    ExpenseMap.removeExpense(e.getUID());
                }
                mainTable.getItems().removeAll(selectedRows);
            }
            if (event.getCode() == KeyCode.ESCAPE){
                mainTable.getSelectionModel().clearSelection();
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

        // Format floats in the table
        amountColumn.setCellFactory(tc -> new TableCell<Expense, Double>() {
            @Override
            protected void updateItem(Double amountColumn, boolean empty) {
                super.updateItem(amountColumn, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", amountColumn.doubleValue()));
                }
            }
        });

        commentsColumn.setCellFactory(tc -> new TableCell<Expense, String>() {
        @Override
        protected void updateItem(String comment, boolean empty) {
            super.updateItem(comment, empty);
            setWrapText(true);
            if (empty) {
                //setText(null);
                setGraphic(null);
            } else {
                Text text = new Text(comment);
                text.wrappingWidthProperty().bind(commentsColumn.widthProperty());
                setGraphic(text);
            }
        }
        });

        mainTable.setRowFactory(row->new TableRow<Expense>() {
            @Override
            public void updateItem(Expense item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item == null || empty)
                {
                    // restore the default row style
                    setStyle("");
                }
                else {
                    if (item.getExpType().equals(Expense.expenseType.Credit))
                    {
                        // Credit is red
                        setStyle("-fx-background-color: #FF9090");
                    }
                    else if (item.getExpType().equals(Expense.expenseType.Debit))
                    {
                        // Debit is green
                        setStyle("-fx-background-color: #B5E981");
                    }
                    else
                    {
                        // use the default row style
                        setStyle("");
                    }
                }
            }
        });
        initializeTable();
    }

    //Load the table with the data from expenseMap and select all rows to apply coloring
    private void initializeTable() {
        mainTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        if(!(ExpenseMap.expenseMap == null || ExpenseMap.expenseMap.isEmpty())){
            ObservableList<Expense> tableElements = mainTable.getItems();
            for(Map.Entry<Long,Expense> entry : ExpenseMap.expenseMap.entrySet()) {
                tableElements.add(entry.getValue());
            }
            mainTable.setItems(tableElements);
            mainTable.getSelectionModel().selectAll();
        }
    }

    public void addTableExpense(Expense e) {
        ObservableList<Expense> tableElements = mainTable.getItems();
        tableElements.add(e);
        mainTable.setItems(tableElements);
        selectAndDeselectAll();
    }

    public void filterTable() {
        if(!(ExpenseMap.filteredMap == null || ExpenseMap.filteredMap.isEmpty())){
            mainTable.getItems().clear();
            ObservableList<Expense> tableElements = FXCollections.observableArrayList();
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
