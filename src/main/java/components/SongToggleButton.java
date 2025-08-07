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

    public String getSong(){return song.getText();}
    public void setSong(String name){song.setText(name);}
    public StringProperty songProperty(){return song.textProperty();}

    public String getArtist(){return artist.getText();}
    public void setArtist(String name){artist.setText(name);}
    public StringProperty artistProperty(){return artist.textProperty();}

    private final IntegerProperty duration = new SimpleIntegerProperty(0,"duration");

    public int getDuration(){return duration.get();}
    public void setDuration(int ms){duration.set(ms);}
    public IntegerProperty durationProperty(){return duration;}

    public SongToggleButton(){
        super();
        Component.init(this,fxmlPath);
    }

    @FXML
    void initialize(){
        durationLb.textProperty().bind(
                Bindings.createStringBinding(
                        ()->FormatUtils.msToString(getDuration()),durationProperty()
                )
        );
    }

    @FXML
    private Label song;
    @FXML
    private Label artist;
    @FXML
    private Label durationLb;



}
