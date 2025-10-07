package controllers.pages;
import controllers.Controller;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.ConfigurationManager;
import utils.PlayerManager;
import utils.PlayerUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class Configuration extends Controller {

    public VBox root;

    {
        MAIN_TITLE = "Configuration";
    }

    @FXML public Button searchDirBtn;
    @FXML public TextField dirField;
    public Button checkVLC;
    public Label messageLbl;

    public StringProperty dirPath = new SimpleStringProperty("");


    public Configuration() {
        super();
    }

    @Override
    public void onStageAssigned(Stage stage) {
        super.onStageAssigned(stage);
        System.out.println("stage");
        System.out.println(stage);
    }

    @FXML
    public void initialize(){
        dirField.textProperty().bindBidirectional(dirPath);
    }

    @FXML
    public void onCheckVLCAction(ActionEvent actionEvent) {
        checkVLC.setDisable(true);
        checkVLC.setText("Verificando ...");
        boolean isLoeaded = PlayerManager.tryLoadVLC(dirPath.get());
        if(isLoeaded){
            checkVLC.setText("cargando...");
            root.getStyleClass().remove("error");
            root.getStyleClass().add("success");
            messageLbl.setText("liberia cargado correctamente");

            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(event -> {
                close();
            });
            pause.play();
        }else{
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> {
                checkVLC.setDisable(false);

                checkVLC.setText("Validar carpeta");
                root.getStyleClass().add("error");
                messageLbl.setText("No se pudo cargar VLC, porfavor seleccione una carpeta válida.");
            });
            pause.play();
        }
    }

    public void onOpenDirBtn(ActionEvent actionEvent) {
        DirectoryChooser dir = new DirectoryChooser();
        dir.setTitle("seleccione su carpeta de instalacion de VLC");
        File selectedDir = dir.showDialog(stage);
        if(selectedDir == null) return;
        dirPath.set(selectedDir.getPath());
    }
}
