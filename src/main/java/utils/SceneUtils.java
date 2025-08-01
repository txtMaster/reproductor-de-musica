package utils;

import application.App;
import controllers.pages.PopUpController;
import utils.path.View;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SceneUtils {
    public static void showBlockedPopUp(String message){
        Stage stage = new Stage();
        App.sceneManager.<PopUpController>setSceneInStage(stage,View.POPUP,c->{
            c.setMessage(message);
        });
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
    }
}
