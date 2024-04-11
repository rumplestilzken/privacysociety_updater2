package com.privacysociety_updater.controller;

import com.privacysociety_updater.data.Variants;
import com.privacysociety_updater.flash.Flash;
import com.privacysociety_updater.flash.FlashTask;
import com.privacysociety_updater.handler.VersionHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
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

    static ProgressBar sProgressBar;
    public static ProgressBar getProgressBar() {
        return sProgressBar;
    }

    public void initialize(URL location, ResourceBundle resources) {
        setVersionInformation();
        setupUpdateURLTextField();
        fillVariant();
        setupProgressBar();
    }

    private void setVersionInformation() {
        versionTextLabel.setText(versionTextLabel.getText() + " " + VersionHandler.VERSION);
    }

    @FXML private void flashButtonAction(ActionEvent e) {
        variantComboBox.setDisable(true);
        flashButton.setDisable(true);

        Variants.Variant variant =
                Arrays.stream(Variants.Variant.values()).
                filter(i -> i.toString().equals(variantComboBox.getValue().toString())).findFirst().get();
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
