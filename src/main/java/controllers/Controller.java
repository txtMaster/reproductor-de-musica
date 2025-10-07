package controllers;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Controller {

    public String MAIN_TITLE = "ventana";

    public void setStage(Stage stage){
        this.stage = stage;
        this.onStageAssigned(stage);
    }

    static String resourcesFolderPath = "/views/components/MiComponent/";
    public Controller(){

    }
    protected Stage stage;

    public void onClose(WindowEvent e){
        System.out.println("onClose controller reaction");
    }

    public void onStageAssigned(Stage stage){

    }

    public void onSceneAssigned(Scene scene){

    }
    public void close(){
        System.out.println("disparanto event de cierre");
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}

