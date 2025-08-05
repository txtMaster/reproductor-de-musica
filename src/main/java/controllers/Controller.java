package controllers;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {

    public void setStage(Stage stage){
        this.stage = stage;
    }

    static final String resourcesFolderPath = "/views/components/MiComponent/";
    public Controller(){

    }
    protected Stage stage;

    public void onClose(){

    }

    public void onStageAssigned(Stage stage){

    }

    public void onSceneAssigned(Scene scene){

    }
}

