package utils;

import application.App;
import javafx.scene.text.Font;

public class FontUtils {
    public static void load(String fileName){
        Font.loadFont(
                App.class.getResourceAsStream("/fonts/"+fileName),
                12
        );
    }
    public static void load(String... fileNames){
        for (String fileName : fileNames){
            load(fileName);
        }
    }
}
