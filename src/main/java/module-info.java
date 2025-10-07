module libraries.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires jaudiotagger;
    requires java.desktop;
    requires java.management;
    requires javafx.base;
    requires javafx.graphics;
    requires org.kordamp.ikonli.core;
    requires ch.qos.logback.core;
    requires ch.qos.logback.classic;
    requires org.kordamp.ikonli.material2;
    requires vlcj;
    requires jna;
    requires java.prefs;
    requires com.sun.jna.platform;
    requires org.slf4j;

    opens libraries.demo to javafx.fxml;
    opens components to javafx.fxml;
    opens controllers.pages to javafx.fxml;
    opens libraries.demo.application to javafx.fxml;

    exports libraries.demo.application;
    opens enums to javafx.fxml;
}