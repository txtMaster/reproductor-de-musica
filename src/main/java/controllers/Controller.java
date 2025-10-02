package controllers;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Controller {

    public void setStage(Stage stage){
        this.stage = stage;
    }

    static final String resourcesFolderPath = "/views/components/MiComponent/";
    public Controller(){

    }
    protected Stage stage;

    public void onClose(WindowEvent e){
        System.out.println("onClose");
    }

    public void onStageAssigned(Stage stage){

    }

    public void onSceneAssigned(Scene scene){

    }
}

