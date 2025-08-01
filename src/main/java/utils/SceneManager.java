package utils;

import controllers.Controller;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import utils.path.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

public class SceneManager {

    private final Stage stage;
    public final Class<?> baseClass;

    public SceneManager(Stage stage, Class<?> baseClass) {
        this.stage = stage;
        this.baseClass = baseClass;
    }

    public void showScene(Stage stage, View path) {
        try {
            FXMLLoader loader = createLoader(path);
            Scene scene = createScene(stage,loader);
            stage.setScene(scene);
            ViewUtils.changeToDraggable(stage);
            ViewUtils.hideSystemBar(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void  showScene(View path){
        showScene(stage, path);
    }

    public FXMLLoader createLoader(View path) throws IllegalArgumentException, IOException {
        URL url = baseClass.getResource(path.getPath());
        if(url == null)throw new IllegalArgumentException("path no found: "+path.getPath());
        return new FXMLLoader(url);
    }
    public Scene createScene(Stage stage,FXMLLoader loader) throws IOException {
        Parent rootView = loader.load();
        Controller controller = loader.getController();
        controller.stage = stage;
        stage.setOnCloseRequest(event->controller.onClose());
        return new Scene(rootView);

    }
    public <C extends Controller> void setSceneInStage(Stage stage, View view, Consumer<C> callback){
        try{
            FXMLLoader loader = createLoader(view);
            Scene scene = createScene(stage, loader);
            callback.accept(loader.getController());
            stage.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
