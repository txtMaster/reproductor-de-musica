package components;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import utils.ComponentUtils;

public class ToggleTab extends StackPane {

    public final static String fxmlPath = ComponentUtils.fxmlPath(ToggleTab.class);
    public ToggleTab(){
        super();
        ComponentUtils.init(this,fxmlPath);
        togglebutton.addListener((obs,pre,pos)->{
            if(pos != null){

            }
        });
    }

    public ChangeListener<Boolean> changeToggleButtonListener = (obs, pre, pos)->{
        if(pos) this.getStyleClass().add("selected");
        else this.getStyleClass().remove("selected");
    };

    private final SimpleObjectProperty<ToggleButton> togglebutton =
            new SimpleObjectProperty<ToggleButton>();

    public ToggleButton getTogglebutton() {
        return togglebutton.get();
    }
    public void setTogglebutton(ToggleButton togglebutton) {
        if(this.togglebutton.get() != null){
            this.togglebutton.get()
                    .selectedProperty()
                    .removeListener(changeToggleButtonListener);
        }
        togglebutton
                .selectedProperty()
                .addListener(changeToggleButtonListener);
        this.togglebutton.set(togglebutton);
    }
    public SimpleObjectProperty<ToggleButton> togglebuttonProperty() {
        return togglebutton;
    }
}
