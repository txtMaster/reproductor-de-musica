package utils;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;

public class ViewUtils {
    static final int RESIZE_MARGIN = 20;
    public static void setOnlyBgImage(Region region, Image image){
        if(image == null) return;
        Background oldBackground = region.getBackground();

        BackgroundImage nuevoBgImg = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        BackgroundSize.AUTO,
                        100,
                        false,
                        true,
                        false,
                        true
                )
        );
        region.setBackground(new Background(nuevoBgImg));
    }
    public static void changeToDraggable(
            Stage stage
    ){
        Scene scene = stage.getScene();
        Node root = scene.getRoot();
        Delta dragDelta = new Delta();

        scene.setOnMousePressed(e -> {
            dragDelta.clicked = e.getPickResult().getIntersectedNode();
            dragDelta.x = stage.getX() - e.getScreenX();
            dragDelta.y = stage.getY() - e.getScreenY();
            boolean toRigth = e.getSceneX() > stage.getWidth() - RESIZE_MARGIN;
            boolean toBottom = e.getSceneY() > stage.getHeight() - RESIZE_MARGIN;
            if (toRigth && toBottom) {
                dragDelta.resize = Delta.COOR.XY.get();
                scene.setCursor(Cursor.SE_RESIZE);
            } else if (toRigth) {
                dragDelta.resize = Delta.COOR.X.get();
                scene.setCursor(Cursor.E_RESIZE);
            } else if (toBottom) {
                dragDelta.resize = Delta.COOR.Y.get();
                scene.setCursor(Cursor.S_RESIZE);
            } else {
                dragDelta.resize = Delta.COOR.NONE.get();
                scene.setCursor(Cursor.DEFAULT);
            }

        });

        scene.setOnMouseDragged(e -> {
            if(dragDelta.clicked != root) return;
            if(dragDelta.resize.equals(Delta.COOR.NONE.get())){
                stage.setX(e.getScreenX() + dragDelta.x);
                stage.setY(e.getScreenY() + dragDelta.y);
            }else{
                if (dragDelta.resize.equals(Delta.COOR.X.get())) {
                    stage.setWidth(e.getSceneX());
                } else if (dragDelta.resize.equals(Delta.COOR.Y.get())) {
                    stage.setHeight(e.getSceneY());
                } else if (dragDelta.resize.equals(Delta.COOR.XY.get())) {
                    stage.setWidth(e.getSceneX());
                    stage.setHeight(e.getSceneY());
                }
            }
        });
        root.setOnMouseReleased(e->{
            scene.setCursor(Cursor.DEFAULT);
            dragDelta.clicked = null;
            dragDelta.resize = Delta.COOR.NONE.get();
        });
    }
    public static void hideSystemBar(Stage stage){
        Scene scene = stage.getScene();
        Node root = scene.getRoot();
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);

        Rectangle clip = new Rectangle(0,0);
        clip.widthProperty().bind(stage.widthProperty());
        clip.heightProperty().bind(stage.heightProperty());
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        root.setClip(clip);
    }
    public static List<Color> getDominantColors(Image image){
        PixelReader reader = image.getPixelReader();

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        Map<Color, Integer> colorCount = new HashMap<>();
        for (int y = 0; y < height; y += 4) {
            for (int x = 0; x < width; x += 4) {
                Color color = reader.getColor(x, y);

                // Opcional: redondear colores para agrupar tonos similares
                color = new Color(
                        Math.round(color.getRed() * 10) / 10.0,
                        Math.round(color.getGreen() * 10) / 10.0,
                        Math.round(color.getBlue() * 10) / 10.0,
                        1.0
                );

                colorCount.put(color, colorCount.getOrDefault(color, 0) + 1);
            }
        }
        List<Map.Entry<Color, Integer>> sorted = colorCount.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(2)
                .toList();



        Color mostCommon = sorted.get(0).getKey();
        Color secondCommon = sorted.size() > 1 ? sorted.get(1).getKey() : mostCommon;
        reader = null;
        return Arrays.stream(
                mostCommon.getSaturation() >= secondCommon.getSaturation() ?
                        new Color[]{mostCommon,secondCommon}
                        :
                        new Color[]{secondCommon,mostCommon}
        ).toList();
    }

    public static Node getPartner(Node node, KeyCode key){
        if(key != KeyCode.UP && key != KeyCode.DOWN)
            return null;
       return getPartner(node,key != KeyCode.UP);
    }

    public static Node getPartner(Node node, boolean toNext){
        Parent parent = node.getParent();
        if(!(parent instanceof Pane)) return null;

        ObservableList<Node> partners = ((Pane) parent).getChildren();

        int size = partners.size();

        if(size == 1) return null;
        int index = partners.indexOf(node);
        if(index == -1) return null;

        if(toNext){
            index = (index + 1) % size;
        }else {
            index = (index - 1 + size) % size;
        }
        //index = ((toNext) ? (index + 1) : ((index - 1) + size)) % size;
        return partners.get(index);
    }
    public static Image processImagePath(String path){
        return processImagePath(path,500);
    }
    public static Image processThumbPath(String path){
        return processImagePath(path, 50);
    }
    public static Image processImagePath(String path,int size){
        try {
            byte[] a = PlayerUtils.getImageData(ExplorerUtils.getAudioFile(path));
            if(a == null) return null;
            return new Image(new ByteArrayInputStream(a), size, size, true, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void safeMoveScroll(ListView<?> listView, int index) {
        Platform.runLater(()->{
            listView.scrollTo(
                    Math.clamp(index, 0,listView.getItems().size()-1)
            );
        });
    }
}

class Delta {
    enum COOR{
        X("X"),Y("Y"),XY("XY"),NONE("NONE");

        private final String coor;
        COOR(String coor) {this.coor = coor;}
        public String get() {
            return coor;
        }

    }
    double x, y;
    Node clicked;
    String resize = COOR.NONE.get();
}
