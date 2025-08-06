package utils;

import javafx.scene.image.Image;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.State;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

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


    // la posicion representa el porcentaje del 0 al 100
    public static void setRelativePosition(MediaPlayer mediaPlayer,double percentage){
        //si el audio termino reiniciar y pausar
        if(mediaPlayer.status().state() == State.STOPPED){
            mediaPlayer.controls().start();
            mediaPlayer.controls().pause();
        }
        mediaPlayer.controls().setPosition((float) (percentage / 100.0));
    }
    public static void togglePlaying(MediaPlayer mediaPlayer){
        if(mediaPlayer.status().isPlaying()){
            mediaPlayer.controls().pause();
        }
        else if(
                mediaPlayer.media().info().state() == State.STOPPED && mediaPlayer.media().info().duration() == mediaPlayer.status().time()
        ) {
            mediaPlayer.controls().start();
        }else{
            mediaPlayer.controls().play();
        }
    }

    public static boolean isEmpty(MediaPlayer mediaPlayer){
        return mediaPlayer.status().state() == State.NOTHING_SPECIAL;
    }

    public static void safeAction(MediaPlayer mediaPlayer, Consumer<MediaPlayer> callback){
        if(!isEmpty(mediaPlayer)) callback.accept(mediaPlayer);
    }
    public static long getTime(MediaPlayer mediaPlayer){
        return mediaPlayer.status().time();
    }
    public static long getDuration(MediaPlayer mediaPlayer){
        return mediaPlayer.media().info().duration();
    }
}
