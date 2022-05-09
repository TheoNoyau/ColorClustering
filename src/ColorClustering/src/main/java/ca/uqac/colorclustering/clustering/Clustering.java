package ca.uqac.colorclustering.clustering;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Map;

public interface Clustering {
    Map<Color, List<Color>> applyClustering(Image image,
                                            int k,
                                            Distance distance,
                                            int maxIterations);
}
