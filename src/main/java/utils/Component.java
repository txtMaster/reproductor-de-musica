package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;

public class Component {
    public static void init(Node node, String fxmlPath){
        try {
            URL path = node.getClass().getResource(fxmlPath);
            FXMLLoader loader = new FXMLLoader(path);
            loader.setRoot(node);
            loader.setController(node);
            loader.setClassLoader(node.getClass().getClassLoader());
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar el FXML", e);
        }
    }
    public static String fxmlPath(Class<?> nodeClass){
        return nodeClass.getSimpleName()+"/index.fxml";
    }
}

