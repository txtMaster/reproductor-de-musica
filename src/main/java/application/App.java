package application;

import javafx.scene.text.Font;
import javafx.stage.StageStyle;
import utils.FontUtils;
import utils.path.View;
import utils.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class App extends Application {


    public static SceneManager sceneManager;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        sceneManager = new SceneManager(stage,App.class);
        FontUtils.load("Inter-Regular.ttf","Inter-Bold.ttf","Inter-Light.ttf","Inter-Medium.ttf","Inter-Thin.ttf");
        sceneManager.showScene(View.HOME);
    }

}