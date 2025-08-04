package utils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.stream.Collectors;

public class DominantColors {

    public static List<Color> getTopColors(Image image, int topN, int tolerance) {
        PixelReader reader = image.getPixelReader();
        if (reader == null) return Collections.emptyList();

        Map<ColorKey, Integer> colorCount = new HashMap<>();

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c = reader.getColor(x, y);

                // Convert to reduced-key color
                ColorKey key = new ColorKey(c, tolerance);

                colorCount.merge(key, 1, Integer::sum);
            }
        }

        return colorCount.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(topN)
                .map(e -> e.getKey().toColor())
                .collect(Collectors.toList());
    }

    // Helper class to reduce color granularity for grouping similar colors
    private static class ColorKey {
        int r, g, b;

        public ColorKey(Color color, int tolerance) {
            this.r = ((int) (color.getRed() * 255) / tolerance) * tolerance;
            this.g = ((int) (color.getGreen() * 255) / tolerance) * tolerance;
            this.b = ((int) (color.getBlue() * 255) / tolerance) * tolerance;
        }

        public Color toColor() {
            return Color.rgb(r, g, b);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ColorKey)) return false;
            ColorKey that = (ColorKey) o;
            return r == that.r && g == that.g && b == that.b;
        }

        @Override
        public int hashCode() {
            return Objects.hash(r, g, b);
        }
    }
}