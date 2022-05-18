module com.example.robinhoodclinicpos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.bytedeco.javacv;
    requires java.desktop;
    requires org.bytedeco.opencv;
    requires google.cloud.firestore;
    requires com.google.api.client.auth;
    requires com.google.auth.oauth2;
    requires firebase.admin;
    requires com.google.auth;
    requires com.google.api.apicommon;
    requires google.cloud.core;
    requires webcam.capture;
    requires javafx.swing;
    requires google.cloud.storage;

    opens com.example.robinhoodclinicpos to javafx.fxml;
    exports com.example.robinhoodclinicpos;
}