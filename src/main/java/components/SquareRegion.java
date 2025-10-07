package components;

import javafx.scene.layout.Region;

public class SquareRegion extends Region {
    public SquareRegion() {
        super();
        prefWidthProperty().bind(heightProperty());
    }
}

