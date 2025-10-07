package libraries.demo.application;

import ch.qos.logback.classic.Logger;
import javafx.application.Platform;
import org.slf4j.LoggerFactory;
import utils.FontUtils;
import utils.path.View;
import utils.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class App extends Application {


    public static SceneManager sceneManager;
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setOnCloseRequest(e->{
            Platform.exit();
            System.exit(0);
        });
        sceneManager = new SceneManager(stage,App.class);
        FontUtils.load("Inter-Regular.ttf","Inter-Bold.ttf","Inter-Light.ttf","Inter-Medium.ttf","Inter-Thin.ttf");
        sceneManager.show(View.HOME);
        //new MemoryMonitor().start(stage);
    }

}