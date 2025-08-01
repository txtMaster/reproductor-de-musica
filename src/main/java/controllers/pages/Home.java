package controllers.pages;

import components.RelativeHBox;
import components.SquareStackPane;
import controllers.Controller;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.Tag;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import utils.ExplorerUtils;
import utils.PlayerUtils;
import utils.ViewUtils;

import java.io.File;
import java.util.List;

public class Home extends Controller {

    public MediaPlayer currentSong;
    public MediaPlayerFactory mediaPlayerFactory;

    public RelativeHBox player;
    public SquareStackPane cover;
    public Label artistName;
    public Label songName;
    public Image artwork = null;
    public Label currentTime;
    public Label totalTime;
    public VBox folders;
    public ToggleButton inicio;
    public ToggleGroup grupoDeSecciones;
    public ScrollPane secciones;
    public ImageView testImage;
    public VBox pestanas;
    public ToggleButton abrirCarpeta;
    public Button pause;
    public VBox playList;
    public SquareStackPane squareCoverWrapper;

    public Runnable loadImage = ()->{};
    public Rectangle coverRectangleClip;
    public Button btnPrevious;
    public Slider timeStatus;

    @FXML
    void initialize(){
        mediaPlayerFactory = new MediaPlayerFactory();
        currentSong = mediaPlayerFactory.mediaPlayers().newMediaPlayer();
        coverRectangleClip.widthProperty().bind(cover.widthProperty());
        coverRectangleClip.heightProperty().bind(cover.heightProperty());
        cover.heightProperty().addListener((obs,prev,next)->{
            if(artwork != null) loadImage.run();
        });
        Timeline progressUpdater = new Timeline(
                new KeyFrame(Duration.millis(500), e -> {
                    if (currentSong.status().isPlaying()) {
                        long time = currentSong.status().time();         // en ms
                        long length = currentSong.media().info().duration(); // en ms
                        if (length > 0) {
                            double progress = (double) time / length * 100;
                            timeStatus.setValue(progress);
                        }
                    }
                })
        );
        progressUpdater.setCycleCount(Animation.INDEFINITE);
        progressUpdater.play();
    }

    @FXML
    public void onActionAbrirCarpeta(ActionEvent actionEvent) {
        if(!abrirCarpeta.isSelected()) return;
        DirectoryChooser carpeta = new DirectoryChooser();
        carpeta.setTitle("seleccione la carpeta con archivos de audio");

        File selectedDirectory = carpeta.showDialog(stage);

        if(selectedDirectory == null || !selectedDirectory.isDirectory())return;

        playList.getChildren().clear();

        List<File> fileAudios = ExplorerUtils.getAudios(selectedDirectory);
        for (File fileAudio : fileAudios){
            Button btn = new Button(fileAudio.getName());
            AudioFile audio;
            try{
                audio = AudioFileIO.read(new java.io.File(fileAudio.getAbsolutePath()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            btn.setOnAction((ActionEvent event)->{
                currentSong.controls().stop();

                totalTime.setText(Integer.toString(PlayerUtils.getDuration(audio)));
                artistName.setText(PlayerUtils.getArtist(audio));
                songName.setText(PlayerUtils.getTitle(audio));
                this.artwork = PlayerUtils.getArtwork(audio);

                loadImage = ()-> ViewUtils.setOnlyBgImage(cover,this.artwork);
                loadImage.run();

                currentSong.media().play(fileAudio.getAbsolutePath());
            });
            playList.getChildren().add(btn);
        }
    }

    @Override
    public void onClose(){
        if (currentSong != null) {
            currentSong.controls().stop();
            currentSong.release();
        }
        if (mediaPlayerFactory != null) {
            mediaPlayerFactory.release();
        }
    }

    @FXML
    public void onPauseAction(ActionEvent actionEvent) {
        if(!currentSong.status().isPlayable()) return;
        if(currentSong.status().isPlaying()){
            currentSong.controls().pause();
            pause.getStyleClass().remove("playing");
        }else{
            currentSong.controls().play();
            pause.getStyleClass().add("playing");
        }
    }
}
