package ca.uqac.colorclustering.clustering;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.util.*;

public class DBScanClustering {
    private Distance distance;
    private Image image;
    private int m;
    private double eps;

    public DBScanClustering(Image image, Distance distance, int m, double eps) {
        this.distance = distance;
        this.image = image;
        this.m = m;
        this.eps = eps;
    }

    public Map<Color, List<Color>> applyClustering() {
        List<Color> dataPoints = getDataPoints();
        Map<Color, List<Color>> result = new HashMap<>();
        Set<Color> visited = new HashSet<>();

        List<Color> neighbors;

        int step = 0;
        for (Color c : dataPoints) {
            System.out.println("step = " + step);
            if (!visited.contains(c)) {
                visited.add(c);
                neighbors = getNeighbors(c, dataPoints);

                if (neighbors.size() >= m) {
                    result.put(c, expandCluster(c, neighbors, dataPoints, visited));
                }
            }
            step++;
        }

        System.out.println("Result of clustering:");
        System.out.println("Color hex code - number of colors in cluster");
        for (Color c : result.keySet()) {
            System.out.println(c.toString() + " - " + result.get(c).size());
        }

        return centerClusters(result);
    }

    private Color getMeanOfCluster(List<Color> cluster) {
        double red = 0;
        double green = 0;
        double blue = 0;
        double n = cluster.size();

        for (Color color : cluster) {
            red += color.getRed();
            green += color.getGreen();
            blue += color.getBlue();
        }

        return new Color(red / n, green / n, blue / n, 1);
    }

    private Map<Color, List<Color>> centerClusters(Map<Color, List<Color>> clusters) {
        Map<Color, List<Color>> centeredClusters = new HashMap<>();

        for (Color c : clusters.keySet()) {
            final Color centeredColor = getMeanOfCluster(clusters.get(c));
            centeredClusters.put(centeredColor, clusters.get(c));
        }

        return centeredClusters;
    }

    private List<Color> expandCluster(Color point,
                                      List<Color> neighbors,
                                      List<Color> dataPoints,
                                      Set<Color> visited) {
        visited.add(point);

        List<Color> copyNeighbors = new ArrayList<>(neighbors);
        List<Color> currentNeighbors;
        for (Color c : copyNeighbors) {
            if (!visited.contains(c)) {
                visited.add(c);
                currentNeighbors = getNeighbors(c, dataPoints);
                if (currentNeighbors.size() >= m) {
                    copyNeighbors = merge(copyNeighbors, currentNeighbors);
                }
            }
        }

        return copyNeighbors;
    }

    private List<Color> merge(List<Color> listOne, List<Color> listTwo) {
        Set<Color> set = new LinkedHashSet<>(listOne);
        set.addAll(listTwo);
        List<Color> combinedList = new ArrayList<>(set);

        return combinedList;
    }

    private List<Color> getDataPoints() {
        List<Color> dataPoints = new ArrayList<>();

        PixelReader pixelReader = image.getPixelReader();
        Color currentColor;

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                currentColor = pixelReader.getColor(i, j);
                dataPoints.add(currentColor);
            }
        }

        return dataPoints;
    }


    private List<Color> getNeighbors(Color point, List<Color> points) {
        List<Color> neighbors = new ArrayList<>();

        for (Color c : points) {
            if (point != c && distance.calculate(point, c) <= eps) {
                neighbors.add(c);
            }
        }

        return neighbors;
    }
}
