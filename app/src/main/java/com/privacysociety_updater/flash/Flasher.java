package com.privacysociety_updater.flash;

import com.privacysociety_updater.Main;
import com.privacysociety_updater.controller.PasswordVerificationWindowController;
import com.privacysociety_updater.controller.SaveWindowController;
import com.privacysociety_updater.data.Variants;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Flasher {
    public static Flasher getFlasher() {
        Variants.OS os = Variants.getOS();
        switch (os) {
            case Linux:
                return new LinuxFlasher();
            case Windows:
                return new WindowsFlasher();
        }
        return null;
    }

    public String getPlatformToolsFilename() {
        return "";
    }

    public void preparePost(String fullPath) {

    }
}
