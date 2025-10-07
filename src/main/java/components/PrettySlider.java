package components;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import utils.ComponentUtils;

import static java.lang.Double.NaN;

public class PrettySlider extends Slider {
    public static String fxmlPath = ComponentUtils.fxmlPath(PrettySlider.class);

    public PrettySlider(){
        super();
        ComponentUtils.init(this,fxmlPath);
        initTrackBinding();
    }

    private void initTrackBinding(){
        Node track = lookup(".track");
        if(track == null){
            Platform.runLater(this::initTrackBinding);
            return;
        }

        updateTrackStyle();

        valueProperty().addListener((obs,pre,pos)->updateTrackStyle(pos.doubleValue()));
    }

    private void updateTrackStyle(double value) {
        Node track = lookup(".track");
        if (track == null) return;

        double percent = (value - getMin()) / (getMax() - getMin());
        double clamped = Math.max(0, Math.min(1, percent)) * 100;
        if(Double.isNaN(clamped))return;

        track.setStyle(
                "-fx-background-color: linear-gradient(to right, main-color " + clamped + "%, bg-color " + clamped + "%);"
        );
    }

    private void updateTrackStyle(){
        updateTrackStyle(getValue());
    }
}
