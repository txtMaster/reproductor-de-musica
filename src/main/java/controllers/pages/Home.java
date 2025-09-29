package controllers.pages;

import components.*;
import controllers.Controller;
import datas.SongData;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import models.SongModel;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.base.State;
import utils.*;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Home extends Controller {

    public MediaPlayer currentSong;
    public MediaPlayerFactory mediaPlayerFactory;
    public WrapperPlaylistListView playListContainer;

    Shortcuts shortcuts;

    @FXML public ScrollPane scrollPlaylist;
    @FXML public PrettySlider volumeSlider;
    @FXML public VBox options;

    @FXML public SongToggleButton lastBtn;
    @FXML public Button closeBtn;
    @FXML public RelativeHBox player;
    @FXML public SquareStackPane cover;
    @FXML public Label artistName;
    @FXML public Label songName;
    @FXML public Label currentTime;
    @FXML public Label totalTime;
    @FXML public VBox folders;
    @FXML public ToggleButton inicio;
    @FXML public ToggleGroup grupoDeSecciones;
    @FXML public ScrollPane secciones;
    @FXML public ImageView testImage;
    @FXML public VBox pestanas;
    @FXML public ToggleButton abrirCarpeta;
    @FXML public Button pause;
    @FXML public ListView<SongData> playlist;
    @FXML public SquareStackPane squareCoverWrapper;
    @FXML public Rectangle coverRectangleClip;
    @FXML public Button btnPrevious;
    @FXML public PrettySlider timeStatus;

    public SongModel currentSongModel = new SongModel();

    ThumbCache imageCache = new ThumbCache();

    //para el slider
    long skipTime = 0L;
    AtomicBoolean interactuandoConElSlider = new AtomicBoolean(false);

    public void playSong(SongData data){
        currentSong.controls().stop();
        currentSongModel.set(data);
        currentSongModel.calcImgPath();
        currentSong.media().play(currentSongModel.getPath());
    }

    @FXML
    void initialize(){
        

        mediaPlayerFactory = new MediaPlayerFactory("--no-video", "--no-xlib");;
        currentSong = mediaPlayerFactory.mediaPlayers().newMediaPlayer();

        songName.textProperty().bind(currentSongModel.titleProperty());
        artistName.textProperty().bind(currentSongModel.artistProperty());
        totalTime.textProperty().bind(
                Bindings.createStringBinding(
                        ()->FormatUtils.msToString(currentSongModel.getDuration()),
                        currentSongModel.durationProperty()
                )
        );
        currentSongModel.artworkProperty().addListener((e,pre,pos)->{
            if(pos == null){
                songName.setStyle(null);
                artistName.setStyle(null);
                timeStatus.setStyle(null);
            }else{
                ViewUtils.setOnlyBgImage(cover,pos);

                List<Color> colors = ViewUtils.getDominantColors(pos);

                if(colors.size() != 2) return;

                Color
                        primaryColor = colors.get(0), secondaryColor = colors.get(1);

                double
                        saturation1 = primaryColor.getSaturation(),
                        saturation2 = secondaryColor.getSaturation();

                //si la saturacion es 0 (sin tono), no editarlo. Caso contrario limitar saturacion a un rango
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
        });


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

            @Override
            public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
                super.lengthChanged(mediaPlayer, newLength);
                Platform.runLater(()->{
                    currentSongModel.setDuration((int)newLength);
                });
            }
        });


        coverRectangleClip.widthProperty().bind(cover.widthProperty());
        coverRectangleClip.heightProperty().bind(cover.heightProperty());
        cover.heightProperty().addListener((obs,prev,next)->{
        });
        AtomicReference<Double> progress = new AtomicReference<>((double) 0);
        Timeline progressUpdater = new Timeline(
                new KeyFrame(Duration.millis(500), e -> {

                    if(interactuandoConElSlider.get()) return;

                    PlayerUtils.safeAction(currentSong,ms->{
                        long currentsMs = PlayerUtils.getTime(currentSong);
                        long totalMs = PlayerUtils.getDuration(currentSong);

                        progress.set((double) currentsMs / totalMs * 100);
                        timeStatus.setValue(progress.get());
                        currentTime.setText(
                                FormatUtils.msToString(currentsMs)
                        );
                    });
                })
        );
        progressUpdater.setCycleCount(Animation.INDEFINITE);
        progressUpdater.play();

        timeStatus.setOnMousePressed(e-> {
            interactuandoConElSlider.set(true);
            //al clickear el slider actualizar el label de tiempo
            PlayerUtils.safeAction(currentSong,mp->{
                long currentMs = (long) (timeStatus.getValue() / 100 * PlayerUtils.getDuration(currentSong));
                currentTime.setText(FormatUtils.msToString(currentMs));
            });
        });
        timeStatus.setOnMouseReleased(e-> {
            interactuandoConElSlider.set(false);
            PlayerUtils.safeAction(currentSong,mp->{
                if(
                        progress.get() == timeStatus.getValue()
                                ||
                                currentSong.status().state() == State.NOTHING_SPECIAL
                ) return;
                PlayerUtils.setRelativePosition(currentSong,timeStatus.getValue());

                //al soltar el slider actualizar el label de tiempo
                long currentMs = (long) (timeStatus.getValue() / 100 * PlayerUtils.getDuration(currentSong));
                currentTime.setText(FormatUtils.msToString(currentMs));
            });
        });
        timeStatus.valueProperty().addListener((obs,pre,pos)->{
            if(!interactuandoConElSlider.get())return;
            PlayerUtils.safeAction(currentSong,mp->{
                long currentMs = (long) (pos.doubleValue() / 100 * PlayerUtils.getDuration(currentSong));
                currentTime.setText(
                        FormatUtils.msToString(currentMs)
                );
            });
        });
        
        playlist = playListContainer.getListView();

        /*
        button.setMaxWidth(Double.MAX_VALUE);
        button.prefWidthProperty().bind(listView.widthProperty());
        */
        playlist.setCellFactory(listView -> new SongListCell(imageCache));

        playlist.getSelectionModel().selectedItemProperty().addListener((obs,pre,pos)->{

            if(pos != null) playSong(pos);
        });
        playlist.addEventFilter(KeyEvent.KEY_PRESSED, e->{
            EventTarget songBtn = e.getTarget();
            Node nextNode = ViewUtils.getPartner(
                    (Node)songBtn,
                    e.getCode()
            );
            if(nextNode == null) return;
            nextNode.requestFocus();
            e.consume();
        });

    }

    @Override
    public void onSceneAssigned(Scene scene){
        shortcuts = new Shortcuts();
        shortcuts.actions.put(KeyEvent.KEY_PRESSED,(e)->{
            System.out.println(e.getCode().toString());
            boolean toConsume = true;
            long skipTime = 0;
            switch (e.getCode()){
                case LEFT-> skipTime = -5000;
                case RIGHT-> skipTime = 5000;
                case SPACE -> PlayerUtils.safeAction(currentSong, PlayerUtils::togglePlaying);
                case HOME -> PlayerUtils.safeAction(currentSong,PlayerUtils::timeToInit);
                case END -> PlayerUtils.safeAction(currentSong,PlayerUtils::timeToEnd);
                default -> toConsume = false;
            };
            if(toConsume)e.consume();
            if (skipTime == 0 || !currentSong.status().isPlayable())return;

            interactuandoConElSlider.set(true);

            this.skipTime += skipTime;
            long duration = PlayerUtils.getDuration(currentSong);
            double newPercentage = Math.clamp((double) skipTime / duration * 100 + timeStatus.getValue(),0,100);

            timeStatus.setValue(newPercentage);
        });
        shortcuts.actions.put(KeyEvent.KEY_RELEASED,(e)->{
            switch (e.getCode()){
                case LEFT,RIGHT -> {
                    currentSong.controls().skipTime(skipTime);
                    currentTime.setText(
                            FormatUtils.msToString(
                                    PlayerUtils.getTime(currentSong)
                            )
                    );
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

        Platform.runLater(()-> {
            imageCache.clear();
            playlist.getItems().clear();
            /*
            if(btn instanceof SongToggleButton songBtn){
                songBtn.setToggleGroup(songButtonsGroup);
            }
             */
        });

        List<File> fileAudios = ExplorerUtils.getAudios(selectedDirectory);

        carpeta = null;
        selectedDirectory = null;

        Task<Void> loadSongsTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                AudioFile audio;
                for (File fileAudio : fileAudios){
                    try{
                        audio = AudioFileIO.read(new java.io.File(fileAudio.getAbsolutePath()));
                        SongData songData = new SongData(audio);
                        imageCache.computeIfAbsent(
                                songData.getPath(),
                                ViewUtils::processThumbPath
                        );
                        Platform.runLater(()-> {
                            playlist.getItems().add(songData);
                        });
                    } catch (Exception _) {}
                }
                return null;
            }
        };

        loadSongsTask.setOnFailed(e -> {
            playlist.getItems().clear();
            //Label error = new Label("Error al cargar audios");
            //playList.getChildren().add(error);
            loadSongsTask.getException().printStackTrace();
        });

        Thread thread = new Thread(loadSongsTask);
        thread.setDaemon(true);
        thread.start();
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
        Platform.exit();
        System.exit(0);
    }

    @FXML
    public void onPauseAction(ActionEvent actionEvent) {
        PlayerUtils.safeAction(currentSong, PlayerUtils::togglePlaying);
    }

    @FXML
    public void onCloseBtnAction(ActionEvent actionEvent) {
        stage.close();
    }
}
