package models;

import datas.SongData;
import javafx.beans.property.*;
import org.jaudiotagger.audio.AudioFile;
import utils.PlayerUtils;


public class SongModel {
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty artist = new SimpleStringProperty();
    private final IntegerProperty duration = new SimpleIntegerProperty();
    private final StringProperty path = new SimpleStringProperty();
    private final ObjectProperty<byte[]> imageData = new SimpleObjectProperty<>(null);

    public SongModel() {}

    public SongModel(String title, String artist, int duration, String path, byte[] imageData) {
        setTitle(title);
        setArtist(artist);
        setDuration(duration);
        setPath(path);
        setImageData(imageData);
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

    public byte[] getImageData() { return imageData.get(); }
    public void setImageData(byte[] data) { imageData.set(data); }
    public ObjectProperty<byte[]> imageDataProperty() { return imageData; }

    public void set(SongData data){
        setArtist(data.getArtist());
        setTitle(data.getTitle());
        setDuration(data.getDuration());
        setImageData(data.getImageData());
        setPath(data.getPath());
    }
}