package utils;

import javafx.scene.paint.Color;

public class ColorUtils {
    public static float getHue(Color input){
        Color output;

        float[] hsb1 = java.awt.Color.RGBtoHSB(
                (int) (input.getRed() * 255),
                (int) (input.getGreen() * 255),
                (int) (input.getBlue() * 255),
                null
        );
        return hsb1[0] * 360;
    }
}
