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

    opens com.example.robinhoodclinicpos to javafx.fxml;
    exports com.example.robinhoodclinicpos;
}