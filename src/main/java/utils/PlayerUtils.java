package utils;

import javafx.scene.image.Image;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PlayerUtils {
    static public Image getArtwork(AudioFile audio){
        Tag tag = audio.getTagOrCreateDefault();
        if (tag.getFirstArtwork() == null) return null;
        Artwork artwork = tag.getFirstArtwork();
        byte[] imageData = artwork.getBinaryData();
        return new Image(new ByteArrayInputStream(imageData));
    }
    static final List<String> EXTENSIONS = Arrays.asList(".mp3", ".flac", ".wav", ".aac", ".ogg", ".m4a");

    static public String getTitle(AudioFile audio){
        Tag tag = audio.getTagOrCreateDefault();
        String name = tag.getFirst("TITLE");
        if(name.isEmpty()) {
            name = audio.getFile().getName();
            name = name.substring(0, name.lastIndexOf('.'));
        }
        if(name.isEmpty()) name = "Sin Nombre";
        return name;
    }
    static public String getArtist(AudioFile audio){
        Tag tag = audio.getTagOrCreateDefault();
        String name = tag.getFirst("ARTIST");
        if(name.isEmpty()) name = "Artista Desconocido";
        return name;
    }

    private PlayerUtils(){}

    public static int getDuration(AudioFile audio) {
        return audio.getAudioHeader().getTrackLength();
    }
}
