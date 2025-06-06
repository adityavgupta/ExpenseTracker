package com.expensetracker.controllers;

import com.expensetracker.ExpenseMap;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.File;

public class MainViewController implements Initializable {

    @FXML
    private VBox baseWindow;
    private Boolean cntrlPressed = false;
    private Boolean sPressed = false;

    @FXML
    private AnchorPane addExpenseAnchorPane;
    @FXML
    private AddExpenseController addExpenseAnchorPaneController;

    @FXML
    private AnchorPane tableAnchorPane;
    @FXML
    private TableController tableAnchorPaneController;

    @FXML
    private AnchorPane filterAnchorPane;
    @FXML
    private FilterController filterAnchorPaneController;

    @FXML
    private AnchorPane lineGraphAnchorPane;
    @FXML
    private LineGraphController lineGraphAnchorPaneController;

    @FXML
    private AnchorPane summaryAnchorPane;
    @FXML
    private SummaryController summaryAnchorPaneController;

    @FXML
    private TabPane MainTabPane;

    @FXML
    private Tab RetroTab;

    @FXML
    private Tab YahooTab;

    @FXML
    private MenuItem saveAs;

    private static Stage primaryStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //initializeExpenseMap();
        ControllerMediator.getInstance().registerAddExpenseController(addExpenseAnchorPaneController);
        ControllerMediator.getInstance().registerTableController(tableAnchorPaneController);
        ControllerMediator.getInstance().registerLineGraphController(lineGraphAnchorPaneController);
        ControllerMediator.getInstance().registerFilterController(filterAnchorPaneController);
        ControllerMediator.getInstance().registerSummaryController(summaryAnchorPaneController);
        
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
            
            //Remove focus from most elements when esc is pressed
            if(event.getCode() == KeyCode.ESCAPE){
                baseWindow.requestFocus();
            }   
        });

        //Supporting logic for ctrl + s table save
        baseWindow.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.S){
                sPressed = false;
            }      
            if(event.getCode() == KeyCode.CONTROL){
                cntrlPressed = false;
            }         
        });

        //Remove focus from other elements when base window is clicked
        baseWindow.setOnMouseClicked(event -> {
            baseWindow.requestFocus();
        });

        // Updates when tab is changed
        MainTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
                if(newTab.equals(YahooTab)) {
                    try {
                        ControllerMediator.getInstance().setEditablePropertyMinAndMaxAmount(false);
                        ControllerMediator.getInstance().calculateAll();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    ControllerMediator.getInstance().updateData();
                }
                if(newTab.equals(RetroTab))
                {
                    try {
                        ControllerMediator.getInstance().setEditablePropertyMinAndMaxAmount(true);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

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

    //Handler for the save as button
    @FXML
    private void saveAsButtonClicked(ActionEvent event)
    {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Save Directory");
        directoryChooser.setInitialDirectory(new File("."));
        File selectedDirectory = directoryChooser.showDialog(this.primaryStage); // 'window' is the parent window
        if (selectedDirectory != null) {
                String directoryPath = selectedDirectory.getAbsolutePath();
                System.out.println("Selected directory: " + directoryPath);
        }
    }

    // Load saved binary data from previous sessions into expenseMap
    public static void initializeExpenseMap() {
        ExpenseMap.loadBinary();
    }

    public void setStage(Stage stage) throws Exception{
        this.primaryStage = stage;
    }

}
