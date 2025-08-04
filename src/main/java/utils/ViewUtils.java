package utils;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.*;
import java.util.stream.Collectors;

public class ViewUtils {
    static final int RESIZE_MARGIN = 20;
    public static void setOnlyBgImage(Region region, Image image){
        if(image == null) return;
        Background oldBackground = region.getBackground();
        if (oldBackground != null && !oldBackground.getImages().isEmpty()) {
            BackgroundImage oldBgImg = oldBackground.getImages().getFirst();
            BackgroundImage nuevoBgImg = new BackgroundImage(
                    image,
                    oldBgImg.getRepeatX(),
                    oldBgImg.getRepeatY(),
                    oldBgImg.getPosition(),
                    oldBgImg.getSize()
            );
            region.setBackground(new Background(nuevoBgImg));
        }
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
        Map<Color,Integer> colors = new HashMap<>();

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
        return Arrays.stream(new Color[]{mostCommon,secondCommon}).toList();
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
