package com.slinky.jellysmash;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(new StackPane(new Label("Under Construction")), 300, 100));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}