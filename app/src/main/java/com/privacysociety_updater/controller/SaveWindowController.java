package com.privacysociety_updater.controller;

import com.privacysociety_updater.data.Variants;
import com.privacysociety_updater.handler.VariantHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SaveWindowController implements Initializable {

    @FXML
    VBox vbox;
    @FXML
    Label confirmationLabel;


    public static Stage getModalStage() {
        return stage;
    }

    public static  void setModalStage(Stage stage) {
        SaveWindowController.stage = stage;
    }

    static Stage stage = null;

    public static State getState() {
        return state;
    }

    public void setState(State state) {
        SaveWindowController.state = state;
    }

    private static State state = State.NotSet;

    public enum State {
        NotSet,
        Cancelled,
        Accepted
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        confirmationLabel.setText(confirmationLabel.getText().replace("{device}", VariantHandler.getVariant().toString()));
    }

    @FXML
    private void cancelButtonAction(ActionEvent e) {
        setState(State.Cancelled);
        stage.close();
    }

    @FXML
    private void flashButtonAction(ActionEvent e) {
        setState(State.Accepted);
        stage.close();
    }

}
