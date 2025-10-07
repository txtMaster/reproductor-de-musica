package components;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import utils.ComponentUtils;

public class ButtonIcon extends Button {
    static final String fxmlPath = ComponentUtils.fxmlPath(ButtonIcon.class);

    public String getSvgPath() {return svgUrl.getSvgPath();}
    public void setSvgPath(String svgPath) {
        this.svgUrl.setSvgPath(svgPath);
    }
    public StringProperty svgPathProperty() {
        return svgUrl.svgPathProperty();
    }

    public ButtonIcon(){
        super();
        ComponentUtils.init(this,fxmlPath);
    }

    @FXML
    public void initialize(){
    }
    @FXML
    SVGUrl svgUrl;

}
