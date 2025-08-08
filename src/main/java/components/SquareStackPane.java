package components;

import javafx.scene.layout.StackPane;
import utils.Component;

public class SquareStackPane extends StackPane {

    static final String fxmlPath = Component.fxmlPath(SquareStackPane.class);

    public SquareStackPane() {
        super();
        Component.init(this,fxmlPath);
        prefWidthProperty().bind(heightProperty());
    }
}

