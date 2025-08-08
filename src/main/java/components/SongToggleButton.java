package components;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import utils.Component;
import utils.FormatUtils;

public class SongToggleButton extends ToggleButton {

    static final String fxmlPath = Component.fxmlPath(SongToggleButton.class);

    public SongToggleButton(){
        super();
        Component.init(this,fxmlPath);
    }

    public String getTitle(){return title.getText();}
    public void setTitle(String name){
        title.setText(name);}
    public StringProperty titleProperty(){return title.textProperty();}

    public String getArtist(){return artist.getText();}
    public void setArtist(String name){artist.setText(name);}
    public StringProperty artistProperty(){return artist.textProperty();}

    private final IntegerProperty duration = new SimpleIntegerProperty(0);

    public int getDuration(){return duration.get();}
    public void setDuration(int ms){duration.set(ms);}
    public IntegerProperty durationProperty(){return duration;}

    @FXML
    void initialize(){
        durationLb.textProperty().bind(
                Bindings.createStringBinding(
                        ()->FormatUtils.msToString(getDuration()),durationProperty()
                )
        );
    }

    @FXML
    private Label title;
    @FXML
    private Label artist;
    @FXML
    private Label durationLb;



}
