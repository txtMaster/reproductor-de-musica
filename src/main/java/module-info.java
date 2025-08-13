module libraries.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.media;
    requires uk.co.caprica.vlcj;
    requires jaudiotagger;
    requires java.desktop;
    requires java.management;

    opens libraries.demo to javafx.fxml;
    opens components to javafx.fxml;
    opens controllers.pages to javafx.fxml;
    opens libraries.demo.application to javafx.fxml;

    exports libraries.demo.application;
}