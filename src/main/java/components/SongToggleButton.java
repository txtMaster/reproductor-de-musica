package components;

import datas.SongData;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import utils.Component;

public class SongToggleButton extends ToggleButton {

    static final String fxmlPath = Component.fxmlPath(SongToggleButton.class);
    public SongToggleButton(){
        super();
        Component.init(this,fxmlPath);
    }
    public SongData getData(){return this.songCard.getData();}
    public void setData(SongData data){this.songCard.setData(data);}
    public SongCard getSongCard(){return this.songCard;}

    @FXML
    private SongCard songCard;
}
