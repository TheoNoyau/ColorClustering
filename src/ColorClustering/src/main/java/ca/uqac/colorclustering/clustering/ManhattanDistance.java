package ca.uqac.colorclustering.clustering;

import javafx.scene.paint.Color;

public class ManhattanDistance implements Distance {
    public double calculate(Color color1, Color color2) {
        double sum = Math.abs(color1.getRed() - color2.getRed())
                + Math.abs(color1.getGreen() - color2.getGreen())
                + Math.abs(color1.getBlue() - color2.getBlue());

        return sum;
    }
}
