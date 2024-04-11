package com.privacysociety_updater;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(ClassLoader.getSystemClassLoader().getResource("fxml/MainWindow.fxml"));

        setupUI(root);

        Scene scene = new Scene(root, 430, 200);
        stage.setTitle("Privacy Society Updater");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("icons/icon.png")));
        stage.show();
    }

    private void setupUI(Parent root) {
        VBox vbox = (VBox) root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
