package components;

import javafx.scene.layout.StackPane;

public class SquareStackPane extends StackPane {
    public SquareStackPane() {
        super();
        prefWidthProperty().bind(heightProperty());
    }
}

