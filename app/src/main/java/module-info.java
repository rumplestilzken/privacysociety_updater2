module com.privacysociety_updater {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    requires org.json;
    requires org.apache.commons.compress;
    requires org.tukaani.xz;

    opens com.privacysociety_updater to javafx.fxml;
    opens com.privacysociety_updater.controller to javafx.fxml;
    opens com.privacysociety_updater.data to javafx.fxml;
    exports com.privacysociety_updater;
    exports com.privacysociety_updater.controller;
    exports com.privacysociety_updater.data;
}