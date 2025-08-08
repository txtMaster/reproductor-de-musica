package datas;

import org.jaudiotagger.audio.AudioFile;
import utils.PlayerUtils;

public class SongData {
    private final String title;
    private final String artist;
    private final int duration; // en milisegundos
    private final String path;
    private final byte[] imageData;

    public SongData(String title, String artist, int duration, String path, byte[] imageData) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.path = path;
        this.imageData = imageData;
    }

    public SongData(AudioFile audioFile){
        duration = PlayerUtils.getDuration(audioFile);
        artist = PlayerUtils.getArtist(audioFile);
        title = PlayerUtils.getTitle(audioFile);
        path = audioFile.getFile().getAbsolutePath();
        this.imageData = PlayerUtils.getImageData(audioFile);
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public int getDuration() { return duration; }
    public String getPath() { return path; }
    public byte[] getImageData() { return imageData; }
}