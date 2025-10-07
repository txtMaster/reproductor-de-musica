package components;

import datas.SongData;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import utils.ComponentUtils;

public class SongToggleButton extends ToggleButton {

    static final String fxmlPath = ComponentUtils.fxmlPath(SongToggleButton.class);
    public SongToggleButton(){
        super();
        ComponentUtils.init(this,fxmlPath);
    }
    public SongData getData(){return this.songCard.getData();}
    public void setData(SongData data){this.songCard.setData(data);}
    public SongCard getSongCard(){return this.songCard;}

    @FXML
    private SongCard songCard;
}
