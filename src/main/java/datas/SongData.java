package datas;

import org.jaudiotagger.audio.AudioFile;
import utils.PlayerUtils;

public class SongData {
    private final String title;
    private final String artist;
    private final int duration; // en milisegundos
    private final String path;

    public SongData(String title, String artist, int duration, String path) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.path = path;
    }

    public SongData(AudioFile audioFile){
        duration = PlayerUtils.getDuration(audioFile);
        artist = PlayerUtils.getArtist(audioFile);
        title = PlayerUtils.getTitle(audioFile);
        path = audioFile.getFile().getAbsolutePath();
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public int getDuration() { return duration; }
    public String getPath() { return path; }


    public static SongData fxml(String title, String artist, int duration, String path){
        return new SongData(title,artist,duration,path);
    }
}