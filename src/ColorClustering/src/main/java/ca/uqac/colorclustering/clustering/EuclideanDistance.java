package ca.uqac.colorclustering.clustering;

import javafx.scene.paint.Color;

public class EuclideanDistance implements Distance{
    public double calculate(Color color1, Color color2) {
        double sum = (color1.getRed() - color2.getRed()) * (color1.getRed() - color2.getRed())
                + (color1.getGreen() - color2.getGreen()) * (color1.getGreen() - color2.getGreen())
                + (color1.getBlue() - color2.getBlue()) * (color1.getBlue() - color2.getBlue());

        return Math.sqrt(sum);
    }
}
