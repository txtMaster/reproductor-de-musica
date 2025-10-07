package components;

import datas.SongData;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import utils.ComponentUtils;
import utils.FormatUtils;
import utils.ThumbCache;
import utils.ViewUtils;

public class SongCard extends HBox {
    static final String fxmlPath = ComponentUtils.fxmlPath(SongCard.class);

    private SongData data = null;
    private ThumbCache imgCache;

    public void setImgCache(ThumbCache imgCache) {
        this.imgCache = imgCache;
    }

    public SongCard(){
        super();
        ComponentUtils.init(this,fxmlPath);
    }

    public String getTitle(){return title.getText();}
    public void setTitle(String name){title.setText(name);}
    public StringProperty titleProperty(){return title.textProperty();}

    public String getArtist(){return artist.getText();}
    public void setArtist(String name){artist.setText(name);}
    public StringProperty artistProperty(){return artist.textProperty();}

    private final IntegerProperty duration = new SimpleIntegerProperty(0);

    public int getDuration(){return duration.get();}
    public void setDuration(int ms){duration.set(ms);}
    public IntegerProperty durationProperty(){return duration;}

    public void setData(SongData songData){
        this.data = songData;
        this.setArtist(data.getArtist());
        this.setTitle(data.getTitle());
        this.setDuration(data.getDuration() * 1000);
        if(imgCache == null) return;
        Image img = imgCache.get(songData.getPath());
        if(img != null) setImg(img);
    }
    public void setImg(Image img){
        ViewUtils.setOnlyBgImage(cover,img);
    }
    public void calcImg(){
        Image img = ViewUtils.processThumbPath(this.data.getPath());
        if(img == null) return;
        setImg(img);
    }
    public SongData getData(){return this.data;}

    @FXML
    void initialize(){
        durationLb.textProperty().bind(
                Bindings.createStringBinding(
                        ()-> FormatUtils.msToString(getDuration()),durationProperty()
                )
        );
    }

    @FXML
    private Label title;
    @FXML
    private Label artist;
    @FXML
    private Label durationLb;

    @FXML
    private SquareStackPane cover;
}
