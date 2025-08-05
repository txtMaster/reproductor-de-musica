package controllers.pages;

import components.PrettySlider;
import components.RelativeHBox;
import components.SquareStackPane;
import controllers.Controller;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.TrackType;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.base.State;
import utils.*;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class Home extends Controller {

    public PrettySlider volumeSlider;
    Shortcuts shortcuts;

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

    public Runnable loaderImage = ()->{};
    public Rectangle coverRectangleClip;
    public Button btnPrevious;
    public PrettySlider timeStatus;

    //para el slider
    long skipTime = 0L;
    AtomicBoolean interactuandoConElSlider = new AtomicBoolean(false);

    @FXML
    void initialize(){

        mediaPlayerFactory = new MediaPlayerFactory();
        currentSong = mediaPlayerFactory.mediaPlayers().newMediaPlayer();


        volumeSlider.valueProperty().addListener((obs,pre,pos)->{
            currentSong.audio().setVolume(pos.intValue());
        });

        currentSong.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter(){

            @Override
            public void audioDeviceChanged(MediaPlayer mediaPlayer, String audioDevice) {
                super.audioDeviceChanged(mediaPlayer, audioDevice);
                mediaPlayer.audio().setVolume((int)volumeSlider.getValue());
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                super.error(mediaPlayer);
                Platform.runLater(()->{
                    pause.getStyleClass().remove("playing");
                });
            }

            @Override
            public void paused(MediaPlayer mediaPlayer) {
                super.paused(mediaPlayer);
                Platform.runLater(()->{
                    pause.getStyleClass().remove("playing");
                });
            }

            @Override
            public void stopped(MediaPlayer mediaPlayer) {
                super.stopped(mediaPlayer);
                Platform.runLater(()->{
                    pause.getStyleClass().remove("playing");
                });
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                super.finished(mediaPlayer);
                Platform.runLater(()->{
                    pause.getStyleClass().remove("playing");
                });
            }

            @Override
            public void playing(MediaPlayer mediaPlayer) {
                super.playing(mediaPlayer);
                Platform.runLater(()->{
                    pause.getStyleClass().add("playing");
                });
            }
        });


        coverRectangleClip.widthProperty().bind(cover.widthProperty());
        coverRectangleClip.heightProperty().bind(cover.heightProperty());
        cover.heightProperty().addListener((obs,prev,next)->{
            if(artwork != null) loaderImage.run();
        });
        AtomicReference<Double> progress = new AtomicReference<>((double) 0);
        Timeline progressUpdater = new Timeline(
                new KeyFrame(Duration.millis(500), e -> {
                    if(interactuandoConElSlider.get()) return;
                    if (currentSong.status().isPlaying()) {
                        long time = currentSong.status().time();         // en ms
                        long length = currentSong.media().info().duration(); // en ms
                        if (length > 0) {
                            progress.set((double) time / length * 100);
                            timeStatus.setValue(progress.get());
                        }
                    }
                })
        );
        timeStatus.setOnMousePressed(e-> {
            interactuandoConElSlider.set(true);
        });
        timeStatus.setOnMouseReleased(e-> {
            interactuandoConElSlider.set(false);
            if(
                    progress.get() == timeStatus.getValue()
                    ||
                    currentSong.status().state() == State.NOTHING_SPECIAL
            ) return;
            PlayerUtils.setRelativePosition(currentSong,timeStatus.getValue());
        });

        progressUpdater.setCycleCount(Animation.INDEFINITE);
        progressUpdater.play();
    }

    @Override
    public void onSceneAssigned(Scene scene){
        shortcuts = new Shortcuts();
        shortcuts.actions.put(KeyEvent.KEY_PRESSED,(e)->{
            boolean toConsume = true;
            long skipTime = 0;
            switch (e.getCode()){
                case LEFT-> skipTime = -5000;
                case RIGHT-> skipTime = 5000;
                case SPACE -> PlayerUtils.safeAction(currentSong, PlayerUtils::togglePlaying);
                default -> toConsume = false;
            };
            if(toConsume)e.consume();
            if (skipTime == 0 || !currentSong.status().isPlayable())return;

            interactuandoConElSlider.set(true);

            this.skipTime += skipTime;
            double length = currentSong.media().info().duration();
            double newPercentage = ((double) skipTime / length) * 100;
            timeStatus.setValue(timeStatus.getValue()+newPercentage);
        });
        shortcuts.actions.put(KeyEvent.KEY_RELEASED,(e)->{
            switch (e.getCode()){
                case LEFT,RIGHT -> {
                    currentSong.controls().skipTime(skipTime);
                    interactuandoConElSlider.set(false);
                    skipTime = 0;
                }
                default -> {}
            }
        });

        scene.addEventFilter(KeyEvent.KEY_PRESSED,shortcuts.run);
        scene.addEventFilter(KeyEvent.KEY_RELEASED,shortcuts.run);

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
                if(this.artwork == null){
                    loaderImage = ()->{};
                    songName.setStyle(null);
                    artistName.setStyle(null);
                    timeStatus.setStyle(null);
                    cover.setBackground(null);
                    cover.applyCss();
                    cover.layout();
                }else{
                    loaderImage = ()-> {
                    ViewUtils.setOnlyBgImage(cover, this.artwork);
                    };
                    List<Color> colors = ViewUtils.getDominantColors(this.artwork);

                    if(colors.size() != 2) return;

                    Color primaryColor, secondaryColor;

                    if(colors.getFirst().getSaturation() >= colors.get(1).getSaturation()){
                        primaryColor = colors.getFirst();
                        secondaryColor = colors.get(1);
                    }else{
                        primaryColor = colors.get(1);
                        secondaryColor = colors.getFirst();
                    }

                    double
                            saturation1 = primaryColor.getSaturation(),
                            saturation2 = secondaryColor.getSaturation();

                    saturation1 = saturation1 == 0 ?
                            0.0 : Math.max(0.15,Math.min(saturation1,0.3));
                    saturation2 = saturation2 == 0 ?
                            0.0 : Math.max(0.02,Math.min(saturation2,0.10));

                    songName.setStyle(
                            "-fx-text-fill:hsb("+
                                    primaryColor.getHue()+","+
                                    (saturation1 * 100) + "%,"+
                                    "86%);"
                    );
                    artistName.setStyle(
                            "-fx-text-fill:hsb("+
                                    secondaryColor.getHue()+","+
                                    (saturation2 * 100) + "%,"+
                                    "74%);"
                    );
                    timeStatus.setStyle(
                            "main-color:hsb("+
                                    primaryColor.getHue()+","+
                                    ((saturation1 * 1.6) * 100) + "%,"+
                                    "86%);"
                    );
                }

                loaderImage.run();

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
        PlayerUtils.safeAction(currentSong, PlayerUtils::togglePlaying);
    }
}
