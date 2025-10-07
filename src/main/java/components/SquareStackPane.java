package components;

import javafx.scene.layout.StackPane;
import utils.ComponentUtils;

public class SquareStackPane extends StackPane {

    static final String fxmlPath = ComponentUtils.fxmlPath(SquareStackPane.class);

    public SquareStackPane() {
        super();
        ComponentUtils.init(this,fxmlPath);
        prefWidthProperty().bind(heightProperty());
    }
}

