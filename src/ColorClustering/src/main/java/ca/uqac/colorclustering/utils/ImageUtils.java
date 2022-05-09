package ca.uqac.colorclustering.utils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Map;

public class ImageUtils {
    public static Image getImageFromClustering(Image image,
                                        Map<Color, List<Color>> clusters) {
        PixelReader pixelReader = image.getPixelReader();

        WritableImage wImage = new WritableImage(
                (int)image.getWidth(),
                (int)image.getHeight());
        PixelWriter pixelWriter = wImage.getPixelWriter();

        Color currentColor;
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                currentColor = pixelReader.getColor(i, j);
                System.out.println(i + " - " + j);
                pixelWriter.setColor(i, j, getClusterOfColor(currentColor, clusters));

            }
        }

        return wImage;
    }

    private static Color getClusterOfColor(Color current,
                                    Map<Color, List<Color>> clusters) {
        Color cluster = Color.YELLOW;
        for (Map.Entry<Color, List<Color>> entry : clusters.entrySet()) {
            if (entry.getValue().contains(current)) {
                return entry.getKey();
            }
        }

        return cluster;
    }
}
