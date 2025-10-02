module libraries.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires jaudiotagger;
    requires java.desktop;
    requires java.management;
    requires javafx.base;
    requires javafx.graphics;
    requires vlcj;

    opens libraries.demo to javafx.fxml;
    opens components to javafx.fxml;
    opens controllers.pages to javafx.fxml;
    opens libraries.demo.application to javafx.fxml;

    exports libraries.demo.application;
    opens enums to javafx.fxml;
}