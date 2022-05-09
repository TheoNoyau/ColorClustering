package ca.uqac.colorclustering.clustering;

import javafx.scene.paint.Color;

public interface Distance {
    double calculate(Color color1, Color color2);
}
