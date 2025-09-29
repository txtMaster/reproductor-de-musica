package components;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Skin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.Component;

public class CustomListView<T>  extends HBox {
    public static final String fxmlPath = Component.fxmlPath(CustomListView.class);

    @FXML private ScrollBar scroll;
    @FXML private ListView<T> listView;

    public CustomListView(){
        super();
        Component.init(this,fxmlPath);
        scroll.setMin(0);
        scroll.setMax(1);
        scroll.setVisibleAmount(0.1);
        scroll.setUnitIncrement(0.01);
        scroll.setBlockIncrement(0.1);
        Platform.runLater(this::syncWithInternalScrollBar);
    }

    private void syncWithInternalScrollBar() {
        Skin<?> skin = listView.getSkin();
        if (skin == null) {
            listView.skinProperty().addListener((obs, oldSkin, newSkin) -> {
                if (newSkin != null) {
                    bindScrollBars();
                }
            });
        } else {
            bindScrollBars();
        }
    }

    private void bindScrollBars() {
        ScrollBar internalScrollBar = listView.lookupAll(".scroll-bar").stream()
                .filter(n -> n instanceof ScrollBar sb && sb.getOrientation() == Orientation.VERTICAL)
                .map(n -> (ScrollBar) n)
                .findFirst()
                .orElse(null);

        if (internalScrollBar == null) return;

        scroll.valueProperty().addListener((obs, oldVal, newVal) -> {
            internalScrollBar.setValue(newVal.doubleValue() * internalScrollBar.getMax());
        });

        internalScrollBar.valueProperty().addListener((obs, oldVal, newVal) -> {
            double max = internalScrollBar.getMax() == 0 ? 1 : internalScrollBar.getMax();
            scroll.setValue(newVal.doubleValue() / max);
        });

        internalScrollBar.setVisible(false);
        internalScrollBar.setOpacity(0);
        internalScrollBar.setManaged(false);
    }

    public ListView<T> getListView() {
        return listView;
    }
}
