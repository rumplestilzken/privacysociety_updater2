package com.privacysociety_updater;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(ClassLoader.getSystemClassLoader().getResource("fxml/MainWindow.fxml"));

        Scene scene = new Scene(root, 339, 90);
        stage.setTitle("Privacy Society Updater");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
