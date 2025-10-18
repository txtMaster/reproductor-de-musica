package controllers.pages;

import components.*;
import controllers.Controller;
import datas.SongData;
import enums.LoopMode;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import models.SongModel;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import uk.co.caprica.vlcj.binding.internal.libvlc_state_t;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import utils.*;
import utils.path.View;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Test extends Controller {

    @FXML public ToggleButton inicioBtn;
    @FXML public ToggleButton favoritosBtn;
    @FXML public ToggleButton misListasBtn;
    @FXML public ToggleButton artistasBtn;
    @FXML public WrapperPlaylistListView playListFavoritos;
    @FXML public WrapperPlaylistListView playListContainer;
    @FXML public WrapperPlaylistListView myPlaylists;
    @FXML public WrapperPlaylistListView myArtists;
    @FXML public WrapperPlaylistListView myAlbums;
    public ToggleButton albumsBtn;

    {
        MAIN_TITLE = "Test";
    }
    @FXML public ToggleGroup grupoDeSecciones;

    public Test(){
        super();
    }

    @FXML
    public void initialize(){
        grupoDeSecciones.selectedToggleProperty().addListener((obs, old, now) -> {
            System.out.println("Seleccionado: " + (now != null ? ((ToggleButton) now).getText() : "ninguno"));
        });
    }
}
