package ca.uqac.colorclustering.clustering;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.util.*;

public class KMeansClustering implements Clustering {
    private double[] min;
    private double[] max;

    public KMeansClustering() {

    }

    public Map<Color, List<Color>> applyClustering(Image image,
                                                   int k,
                                                   Distance distance,
                                                   int maxIterations) {
        List<Color> centroids = initCentroids(image, k);
        Map<Color, List<Color>> clusters = new HashMap<>();
        Map<Color, List<Color>> lastClusters;

        PixelReader pixelReader = image.getPixelReader();
        Color currentColor;
        int step = 0;
        do {
            System.out.println("step = " + step);
            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    currentColor = pixelReader.getColor(i, j);
                    getAndUpdateNearestCentroid(currentColor, centroids, distance, clusters);
                }
            }

            updateCentroids(clusters);
            lastClusters = clusters;
            clusters = new HashMap<>();
            step++;
        } while (step < maxIterations);

        System.out.println("Result of clustering:");
        System.out.println("Color hex code - number of colors in cluster");
        for (Color c : lastClusters.keySet()) {
            System.out.println(c.toString() + " - " + lastClusters.get(c).size());
        }

        return centerClusters(lastClusters);
    }

    private Map<Color, List<Color>> centerClusters(Map<Color, List<Color>> clusters) {
        Map<Color, List<Color>> centeredClusters = new HashMap<>();

        for (Color c : clusters.keySet()) {
            final Color centeredColor = getMeanOfCluster(clusters.get(c));
            centeredClusters.put(centeredColor, clusters.get(c));
        }

        return centeredClusters;
    }

    private void calculateColorRanges(Image image) {
        // Min and max values for each RGB value.
        // Red will be at index 0,
        // Green will be at index 1,
        // Blue will be at index 2.
        min = new double[3];
        max = new double[3];

        PixelReader pixelReader = image.getPixelReader();
        Color currentColor;

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                currentColor = pixelReader.getColor(i, j);

                min[0] = Math.min(min[0], currentColor.getRed());
                min[1] = Math.min(min[1], currentColor.getGreen());
                min[2] = Math.min(min[2], currentColor.getBlue());

                max[0] = Math.max(max[0], currentColor.getRed());
                max[1] = Math.max(max[1], currentColor.getGreen());
                max[2] = Math.max(max[2], currentColor.getBlue());
            }
        }
    }

    private List<Color> initCentroids(Image image, int k) {
        calculateColorRanges(image);

        List<Color> centroids = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            centroids.add(new Color(getRandomDoubleBetween(min[0], max[0]),
                    getRandomDoubleBetween(min[1], max[1]),
                    getRandomDoubleBetween(min[2], max[2]),
                    1));
        }

        return centroids;
    }

    private void getAndUpdateNearestCentroid(Color current,
                                             List<Color> centroids,
                                             Distance distance,
                                             Map<Color, List<Color>> clusters) {
        double minDistance = Double.MAX_VALUE;
        double currDistance = minDistance;
        Color nearestCentroid = null;

        // Calculate nearest centroid.
        for (Color centroid : centroids) {
            currDistance = distance.calculate(current, centroid);
            if (currDistance < minDistance) {
                minDistance = currDistance;
                nearestCentroid = centroid;
            }
        }

        // Assigning new color to its nearest centroid cluster.
        clusters.compute(nearestCentroid, (key, list) -> {
            if (list == null) {
                list = new ArrayList<>();
            }

            list.add(current);
            return list;
        });
    }

    private List<Color> updateCentroids(Map<Color, List<Color>> clusters) {
        List<Color> newCentroids = new ArrayList<>();

        for (List<Color> cluster : clusters.values()) {
            newCentroids.add(getMeanOfCluster(cluster));
        }

        return  newCentroids;
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

    private double getRandomDoubleBetween(double a, double b) {
        return a + new Random().nextDouble() * (b - a);
    }
}
