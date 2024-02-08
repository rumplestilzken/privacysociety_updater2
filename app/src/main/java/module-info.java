module com.privacysociety_updater {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.privacysociety_updater to javafx.fxml;
    opens com.privacysociety_updater.controller to javafx.fxml;
    exports com.privacysociety_updater;
    exports com.privacysociety_updater.controller;
}