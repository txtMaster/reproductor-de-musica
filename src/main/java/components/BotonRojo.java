package components;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import utils.Component;

public class BotonRojo extends Button {

    public static final String fxmlPath = Component.fxmlPath(BotonRojo.class);

    public BotonRojo() {
        super();
        Component.init(this,fxmlPath);
    }
}