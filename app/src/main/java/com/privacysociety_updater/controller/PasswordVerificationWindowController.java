package com.privacysociety_updater.controller;

import com.privacysociety_updater.handler.ProcessHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class PasswordVerificationWindowController implements Initializable {

    @FXML
    PasswordField passwordOne;
    @FXML
    PasswordField passwordTwo;
    @FXML
    TextField textField;

    public static Stage getModalStage() {
        return stage;
    }

    public static  void setModalStage(Stage stage) {
        PasswordVerificationWindowController.stage = stage;
    }

    static Stage stage = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void confirmButtonAction(ActionEvent e) {
        System.out.println("Confirm");
        if(!passwordOne.getText().equals(passwordTwo.getText())){
            //TODO: Password not equal
        }

        String password = passwordOne.getText();
        System.out.println("Password:'" + password + "'");

        try {
            String[] cmd1 = {"/bin/bash","-c","echo \"" + password + "\"| sudo -S ls -l /root"};
            String output = ProcessHandler.processCommands(cmd1);

            if(output.toLowerCase().contains("incorrect")) {
                textField.setText("P/W Incorrect");
                return;
            }

            textField.setText("Installing...");

            String[] cmd2 = {"/bin/bash","-c","echo \"" + password + "\"| sudo -S apt install android-sdk-platform-tools-common"};
            output = ProcessHandler.processCommands(cmd2);

            String[] cmd3 = {"/bin/bash","-c","echo \"" + password + "\"| sudo -S udevadm control --reload-rules"};
            output = ProcessHandler.processCommands(cmd3);

            String[] cmd4 = {"/bin/bash","-c","echo \"" + password + "\"| sudo -S udevadm trigger"};
            output = ProcessHandler.processCommands(cmd4);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        stage.close();
    }


}
