package models;

import datas.SongData;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import org.jaudiotagger.audio.AudioFile;
import utils.ExplorerUtils;
import utils.PlayerUtils;

import java.io.ByteArrayInputStream;

import static utils.ViewUtils.processImagePath;


public class SongModel {
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty artist = new SimpleStringProperty();
    private final IntegerProperty duration = new SimpleIntegerProperty();
    private final StringProperty path = new SimpleStringProperty();
    /*
    private final ObjectProperty<byte[]> imageData = new SimpleObjectProperty<>(null);
    */
    private final ObjectProperty<Image> artwork = new SimpleObjectProperty<>(null);
    public SongModel() {}

    public SongModel(String title, String artist, int duration, String path) {
        setTitle(title);
        setArtist(artist);
        setDuration(duration);
        setPath(path);
    }

    public String getTitle() { return title.get(); }
    public void setTitle(String value) { title.set(value); }
    public StringProperty titleProperty() { return title; }

    public String getArtist() { return artist.get(); }
    public void setArtist(String value) { artist.set(value); }
    public StringProperty artistProperty() { return artist; }

    public int getDuration() { return duration.get(); }
    public void setDuration(int value) { duration.set(value); }
    public IntegerProperty durationProperty() { return duration; }

    public String getPath() { return path.get(); }
    public void setPath(String value) { path.set(value); }
    public StringProperty pathProperty() { return path; }


    public Image getArtwork() { return artwork.get(); }
    public void setArtwork(Image image) { artwork.set(image); }
    public ObjectProperty<Image> artworkProperty() { return artwork; }

    public void set(SongData data){
        setArtist(data.getArtist());
        setTitle(data.getTitle());
        setDuration(data.getDuration());
        setPath(data.getPath());
    }
    public void calcImgPath(){
        Image img = processImagePath(getPath());
        if(img == null)return;
        artwork.set(null);
        this.setArtwork(img);
    }
}