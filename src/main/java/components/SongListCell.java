package components;

import datas.SongData;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import utils.Component;
import utils.ThumbCache;

public class SongListCell extends ListCell<SongData> {

    public static String fxmlPath = Component.fxmlPath(SongListCell.class);
    private final ThumbCache imgCache;
    public SongListCell(ThumbCache imgCache){
        super();
        Component.init(this,fxmlPath);
        this.imgCache = imgCache;
    }
    public SongListCell(){
        super();
        Component.init(this,fxmlPath);
        this.imgCache = null;
    }

    @FXML
    void initialize(){
    }

    @Override
    protected void updateItem(SongData item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null) setGraphic(null);
        else {
            songCard.setImgCache(imgCache);
            songCard.setData(item);
            setGraphic(songCard);
        }
    }

    @FXML private SongCard songCard;
    public final SongCard songCard(){return this.songCard;}
    public SongData getData(){return this.songCard.getData();}
    public void setData(SongData data){this.songCard.setData(data);}
}