package utils;

import controllers.Controller;
import javafx.stage.WindowEvent;
import libraries.demo.application.App;
import utils.path.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.function.BiConsumer;

public class SceneManager {

    public static void openModal(View viewPath, BiConsumer<WindowEvent, Controller> onClose){
        try{
            Stage stage = new Stage();
            FXMLLoader loader = createLoader(viewPath);
            Parent rootView = loader.load();
            Controller controller = loader.getController();
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            bind(stage,controller,onClose);
            setScene(stage, new Scene(rootView),controller);
            stage.show();

        }catch (Exception e){
            System.out.println(e);
        }

    }
    public static void openModal(View viewPath){
        openModal(viewPath,(event,contoller)->{});
    }

    private final Stage baseStage;
    public final Class<?> baseClass;

    public SceneManager(Stage stage, Class<?> baseClass) {
        this.baseStage = stage;
        this.baseClass = baseClass;
    }

    public static void setScene(Stage stage, Scene scene, Controller controller) {
        stage.setScene(scene);
        ViewUtils.changeToDraggable(stage);
        ViewUtils.hideSystemBar(stage);
        controller.onSceneAssigned(scene);
    }

    public static FXMLLoader createLoader(View path) throws IllegalArgumentException, IOException {
        URL url = App.class.getResource(path.getPath());
        if(url == null)throw new IllegalArgumentException("path no found: "+path.getPath());
        return new FXMLLoader(url);
    }
    public static void bind(
            Stage stage,
            Controller controller
    ) throws IOException {
        bind(stage,controller,(e, c)->{});
    }
    public static void bind(
            Stage stage,
            Controller controller,
            BiConsumer<WindowEvent,Controller> onClose
    ) throws IOException {
        controller.setStage(stage);
        controller.onStageAssigned(stage);
        stage.setTitle(controller.MAIN_TITLE);
        System.out.println("class: "+controller.getClass());
        System.out.println("title: "+controller.MAIN_TITLE);
        stage.setOnCloseRequest(event->{
            System.out.println("stage close request");
            onClose.accept(event,controller);
            controller.onClose(event);
        });
    }
    public static void show(SceneManager manager, View viewPath) throws IOException {
        FXMLLoader loader = createLoader(viewPath);
        Parent viewRoot = loader.load();
        Controller controller = loader.getController();
        Scene scene = new Scene(viewRoot);
        bind(manager.baseStage,controller);
        setScene(manager.baseStage,scene,controller);
        manager.baseStage.show();
    }
    public void show(View viewPath) throws IOException {
        FXMLLoader loader = createLoader(viewPath);
        Parent viewRoot = loader.load();
        Controller controller = loader.getController();
        Scene scene = new Scene(viewRoot);
        bind(this.baseStage,controller);
        setScene(this.baseStage,scene,controller);
        this.baseStage.show();
    }
}
