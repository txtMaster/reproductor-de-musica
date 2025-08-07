package utils;

import application.App;
import javafx.scene.text.Font;

public class FontUtils {
    public static void load(String fileName){
        var font = Font.loadFont(
                App.class.getResourceAsStream("/fonts/"+fileName),
                12
        );
        System.out.println(font.getName());
    }
    public static void load(String... fileNames){
        for (String fileName : fileNames){
            load(fileName);
        }
    }
}
