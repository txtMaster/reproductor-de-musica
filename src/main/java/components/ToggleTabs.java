package components;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;

public class ToggleTabs extends StackPane {

    public ToggleTabs(){
        super();
        this.getStyleClass().add("toggle-tabs");
    }

    private final SimpleObjectProperty<ToggleGroup> togglegroup =
            new SimpleObjectProperty<ToggleGroup>(){

    };

    public ToggleGroup getTogglegroup() {
        return togglegroup.get();
    }

    private final ChangeListener<Toggle> changeToggleGroupListener = (obs, pre, pos)->{
        for (Node child : this.getChildren()){
            if(pos == null){
                child.setVisible(false);
                child.setManaged(false);
            }else if(child instanceof ToggleTab tTab){
                boolean visible = tTab.getTogglebutton() == pos;
                child.setVisible(visible);
                child.setManaged(visible);
            }
        }
    };

    public void setTogglegroup(ToggleGroup togglegroup) {
        if(this.togglegroup.get() != null){
            this.togglegroup.get()
                    .selectedToggleProperty()
                    .removeListener(changeToggleGroupListener);
        }
        togglegroup
                .selectedToggleProperty()
                .addListener(changeToggleGroupListener);

        this.togglegroup.set(togglegroup);
    }
    public SimpleObjectProperty<ToggleGroup> togglegroupProperty() {
        return togglegroup;
    }
    @FXML
    public void initialize(){

    }
}
