package com.privacysociety_updater.controller;

import com.privacysociety_updater.Main;
import com.privacysociety_updater.data.Variants;
import com.privacysociety_updater.flash.Flash;
import com.privacysociety_updater.flash.FlashTask;
import com.privacysociety_updater.handler.VariantHandler;
import com.privacysociety_updater.handler.VersionHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class MainWindowController implements Initializable {
    @FXML
    ComboBox variantComboBox;
    @FXML
    TextField updateURLTextField;
    @FXML
    ProgressBar progressBar;
    @FXML
    TextArea flashMessageTextArea;
    @FXML
    Button flashButton;
    @FXML
    Label versionTextLabel;
    @FXML
    GridPane gridPane;
    @FXML
    Label updateURLLabel;

    static ProgressBar sProgressBar;
    public static ProgressBar getProgressBar() {
        return sProgressBar;
    }

    public void initialize(URL location, ResourceBundle resources) {
        setVersionInformation();
        setupUpdateURLTextField();
        fillVariant();
        setupProgressBar();
        hideVariantURL();
    }

    private void hideVariantURL() {
        updateURLTextField.setVisible(false);
        updateURLLabel.setVisible(false);
    }

    private void setVersionInformation() {
        versionTextLabel.setText(versionTextLabel.getText() + " " + VersionHandler.VERSION);
    }

    @FXML private void flashButtonAction(ActionEvent e) {
        Parent root = null;
        Variants.Variant variant =
                Arrays.stream(Variants.Variant.values()).
                filter(i -> i.toString().equals(variantComboBox.getValue().toString())).findFirst().get();

        VariantHandler.setVariant(variant);

        try {
            Stage dialog = new Stage();
            SaveWindowController.setModalStage(dialog);

            root = FXMLLoader.load(ClassLoader.getSystemClassLoader().getResource("fxml/SaveWindow.fxml"));

            Scene scene = new Scene(root, 600, 106);

            dialog.initOwner(Main.getStage());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Flash Device Verification");
            dialog.setScene(scene);
            dialog.setResizable(false);
            dialog.getIcons().add(new Image(ClassLoader.getSystemClassLoader().getResourceAsStream("com/privacysociety_updater/icons/icon.png")));
            dialog.showAndWait();

            if(SaveWindowController.getState() != SaveWindowController.State.Accepted) {
                return;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        variantComboBox.setDisable(true);
        flashButton.setDisable(true);

        Flash flash = new Flash(variant, updateURLTextField.getText());
        FlashTask<Void>  flashTask = new FlashTask<Void>() {
            @Override
            protected Void call() throws Exception {
                flash.setTask(this);
                boolean result = flash.flash();
                return null;
            }
        };

        //TODO: Logging
        flashTask.setOnFailed( fail -> {
            flashTask.setProgressMessage("Flash Failed. See logs for details.");
            fail.getSource().getException().printStackTrace();
        });
        flashTask.setOnSucceeded(suc -> { flashTask.setProgressMessage("Flashing Complete."); });

        MainWindowController.getProgressBar().progressProperty().bind(flashTask.progressProperty());
        flashMessageTextArea.textProperty().bind(flashTask.messageProperty());

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                flashTask.run();
                return null;
            }
        };

        task.setOnFailed(fail -> {
            variantComboBox.setDisable(false);
            flashButton.setDisable(false);
            flashTask.setProgressBarProgress(1);
        });

        task.setOnSucceeded(suc -> {
            variantComboBox.setDisable(false);
            flashButton.setDisable(false);
            flashTask.setProgressBarProgress(1);
        });

        ForkJoinPool.commonPool().execute(task);
    }

    private void setupUpdateURLTextField() {
        updateURLTextField.setText("http://20.29.42.174/ota.json");
        updateURLTextField.setEditable(false);
    }

    private void fillVariant() {
        List<String> list = new ArrayList<String>();
        list.addAll(Arrays.stream(Variants.Variant.values()).map(Enum::toString).collect(Collectors.toList()));
        ObservableList obList = FXCollections.observableList(list);
        variantComboBox.getItems().clear();
        variantComboBox.setItems(obList);
        variantComboBox.setValue(variantComboBox.getItems().get(0));
    }

    private void setupProgressBar() {
        sProgressBar = progressBar;
    }

}
