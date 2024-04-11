package com.privacysociety_updater.controller;

import com.privacysociety_updater.data.Variants;
import com.privacysociety_updater.flash.Flash;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainWindowController implements Initializable {
    @FXML
    ComboBox variantComboBox;
    @FXML
    TextField updateURLTextField;
    @FXML
    ProgressBar progressBar;
    static ProgressBar sProgressBar;
    public static ProgressBar getProgressBar() {
        return sProgressBar;
    }

    public void initialize(URL location, ResourceBundle resources) {
        setupUpdateURLTextField();
        fillVariant();
        setupProgressBar();
    }

    @FXML private void flashButtonAction(ActionEvent e) {
        Variants.Variant variant =
                Arrays.stream(Variants.Variant.values()).
                filter(i -> i.toString().equals(variantComboBox.getValue().toString())).findFirst().get();
        Flash flash = new Flash(variant, updateURLTextField.getText());
        boolean result = flash.flash();
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
