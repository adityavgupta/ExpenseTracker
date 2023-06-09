package com.expensetracker;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.util.Objects;

import com.expensetracker.controllers.MainViewController;

public class ExpenseTrackerApp extends Application {

    private double xOffset;
    private double yOffset;

    @Override
    public void start(Stage primaryStage) throws Exception{
        MainViewController.initializeExpenseMap();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));

        root.setOnMousePressed(event ->
                {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                    event.consume();
                }
        );
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
            event.consume();
        });

        Scene scene = new Scene(root);
        scene.getRoot().setEffect(new DropShadow(10, Color.rgb(100, 100, 100)));
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
//        primaryStage.initStyle(StageStyle.TRANSPARENT);

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo.png")));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

