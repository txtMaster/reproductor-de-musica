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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import utils.*;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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

    public Runnable loaderImage = ()->{};
    public Rectangle coverRectangleClip;
    public Button btnPrevious;
    public Slider timeStatus;

    @FXML
    void initialize(){
        timeStatus.valueProperty().addListener((o,pre,pos)->{
            double percent = (pos.doubleValue() - timeStatus.getMin()) / (timeStatus.getMax() - timeStatus.getMin());
            timeStatus.lookup(".track").setStyle(
                    "-fx-background-color: linear-gradient(to right, main-color " + (percent * 100) + "%, bg-color " + (percent * 100) + "%);"
            );
        });
        mediaPlayerFactory = new MediaPlayerFactory();
        currentSong = mediaPlayerFactory.mediaPlayers().newMediaPlayer();
        coverRectangleClip.widthProperty().bind(cover.widthProperty());
        coverRectangleClip.heightProperty().bind(cover.heightProperty());
        cover.heightProperty().addListener((obs,prev,next)->{
            if(artwork != null) loaderImage.run();
        });
        AtomicBoolean moviendoSlider = new AtomicBoolean(false);
        AtomicReference<Double> progress = new AtomicReference<>((double) 0);
        Timeline progressUpdater = new Timeline(
                new KeyFrame(Duration.millis(500), e -> {
                    if(moviendoSlider.get()) return;
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
            moviendoSlider.set(true);
        });
        timeStatus.setOnMouseReleased(e-> {
            moviendoSlider.set(false);
            if(progress.get() == timeStatus.getValue()) return;
            float newPosition = (float) (timeStatus.getValue() / 100.0);
            currentSong.controls().setPosition(newPosition);
        });



        timeStatus.valueChangingProperty().addListener((e,was,is)->{
            System.out.println("changing!!!");
            System.out.println(was);
            System.out.println(is);
        });

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
