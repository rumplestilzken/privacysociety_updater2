package com.privacysociety_updater;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    protected static Parent rootObject = null;
    protected static Scene scene = null;
    protected static Stage stage = null;

    public static Parent getRootObject() {
        return rootObject;
    }

    public static void setRootObject(Parent rootObject) {
        Main.rootObject = rootObject;
    }

    public static Scene getScene() {
        return scene;
    }

    public static void setScene(Scene scene) {
        Main.scene = scene;
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        Main.stage = stage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Main.stage = stage;

        Parent root = FXMLLoader.load(getClass().getResource("/com/privacysociety_updater/fxml/MainWindow.fxml"));

        Main.rootObject = root;

        setupUI(root);

        Scene scene = new Scene(root, 430, 200);
        Main.scene = scene;

        stage.setTitle("Privacy Society Updater");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/privacysociety_updater/icons/icon.png")));
        stage.show();
    }

    private void setupUI(Parent root) {
        VBox vbox = (VBox) root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
