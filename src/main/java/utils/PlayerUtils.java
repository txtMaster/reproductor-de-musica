package utils;

import javafx.scene.image.Image;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;
import uk.co.caprica.vlcj.binding.internal.libvlc_state_t;
import uk.co.caprica.vlcj.player.MediaPlayer;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class PlayerUtils {

    static public byte[] getImageData(AudioFile audioFile){
        Tag tag = audioFile.getTagOrCreateDefault();
        if (tag.getFirstArtwork() == null) return null;
        return tag.getFirstArtwork().getBinaryData();
    }
    static public Image getArtwork(AudioFile audio){
        byte[] imageData = getImageData(audio);
        if(imageData == null) return null;
        return new Image(new ByteArrayInputStream(imageData));
    }

    static public Image getArtwork(byte[] imageData){
        if(imageData == null) return null;
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
    public static void setRelativePosition(MediaPlayer mediaPlayer, double percentage){
        //si el audio termino reiniciar y pausar
        if(mediaPlayer.getMediaPlayerState() == libvlc_state_t.libvlc_Stopped){
            mediaPlayer.start();
            mediaPlayer.pause();
        }
        mediaPlayer.setPosition((float) (percentage / 100.0));
    }
    public static void togglePlaying(MediaPlayer mediaPlayer){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        else if(
                mediaPlayer.getMediaPlayerState() == libvlc_state_t.libvlc_Stopped &&
                        mediaPlayer.getLength() == mediaPlayer.getTime()
        ) {
            mediaPlayer.start();
        }else{
            mediaPlayer.play();
        }
    }
    public static void timeToInit(MediaPlayer mediaPlayer){
        if(notEmpty(mediaPlayer))mediaPlayer.setTime(0);
    }
    public static void timeToEnd(MediaPlayer mediaPlayer){
        if(notEmpty(mediaPlayer))
            mediaPlayer.setTime(
                    //enviar casi al ultimo para que no salga un error de buffer al intentar cargar el ultimo momento del archivo de golpe
                    PlayerUtils.getDuration(mediaPlayer) - 500
            );
    }

    public static boolean notEmpty(MediaPlayer mediaPlayer){
        return mediaPlayer.getMediaPlayerState() != libvlc_state_t.libvlc_NothingSpecial;
    }

    public static void safeAction(MediaPlayer mediaPlayer, Consumer<MediaPlayer> callback){
        if(notEmpty(mediaPlayer)) callback.accept(mediaPlayer);
    }
    public static void safeAction(MediaPlayer mediaPlayer, Consumer<MediaPlayer> callback,Consumer<MediaPlayer> ifEmptyCallback){
        if(notEmpty(mediaPlayer)) callback.accept(mediaPlayer);
        else ifEmptyCallback.accept(mediaPlayer);
    }
    public static long getTime(MediaPlayer mediaPlayer){
        return mediaPlayer.getTime();
    }
    public static long getDuration(MediaPlayer mediaPlayer){
        return mediaPlayer.getLength();
    }

    public static boolean isPlayable(MediaPlayer mp){
        final libvlc_state_t state = mp.getMediaPlayerState();
        return state != libvlc_state_t.libvlc_NothingSpecial
                && state != libvlc_state_t.libvlc_Error
                && state != libvlc_state_t.libvlc_Ended;
    }
}
