package ca.uqac.colorclustering;

import ca.uqac.colorclustering.clustering.*;
import ca.uqac.colorclustering.utils.ImageUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

public class ColorClusteringController {
    @FXML
    private ImageView mainImageView;
    private Image mainImage;

    @FXML
    private ChoiceBox algorithmChoiceBox;

    @FXML
    private TextField kInput;

    @FXML
    private TextField maxIterInput;

    @FXML
    private TextField mInput;

    @FXML
    private TextField epsInput;

    @FXML
    private ChoiceBox distanceChoiceBox;

    public void onPickImageClicked(ActionEvent actionEvent) throws MalformedURLException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choisir une image");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files",
                        "*.bmp", "*.png", "*.jpg", "*.jpeg"));
        File file = chooser.showOpenDialog(new Stage());
        if (file != null) {
            String imagePath = file.toURI().toURL().toString();
            mainImage = new Image(imagePath);
            mainImageView.setImage(mainImage);
        }
    }

    private Distance getSelectedDistance() {
        String choosedDistance = distanceChoiceBox.getSelectionModel().getSelectedItem().toString();
        if (choosedDistance.equals("Manhattan")) {
            return new ManhattanDistance();
        }

        return new EuclideanDistance();
    }

    public void onApplyClicked(ActionEvent actionEvent) {
        Map<Color, List<Color>> clusters;

        if (algorithmChoiceBox.getSelectionModel().getSelectedItem().equals("K-Means")) {
            final int k = Integer.parseInt(kInput.getText());
            final int maxIter = Integer.parseInt(maxIterInput.getText());

            Distance distance = getSelectedDistance();

            KMeansClustering clustering = new KMeansClustering();
            clusters = clustering.applyClustering(mainImage, k, distance, maxIter);
        } else {
            final int min = Integer.parseInt(mInput.getText());
            final double eps = Double.parseDouble(epsInput.getText());

            Distance distance = getSelectedDistance();

            DBScanClustering clustering = new DBScanClustering(mainImage, distance, min, eps);
            clusters = clustering.applyClustering();
        }

        mainImageView.setImage(ImageUtils.getImageFromClustering(mainImage, clusters));
    }

    public void onSaveClicked(ActionEvent actionEvent) throws MalformedURLException {
        FileChooser chooser = new FileChooser();
        File file = chooser.showSaveDialog(new Stage());

        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(mainImageView.getImage(),
                        null), "png", file);
            } catch (IOException ex) {
               System.out.println(ex);
            }
        }
    }

    public void onAlgorithmChanged(ActionEvent actionEvent) {
        if (algorithmChoiceBox.getSelectionModel().getSelectedItem().equals("K-Means")) {
            kInput.setVisible(true);
            maxIterInput.setVisible(true);

            mInput.setVisible(false);
            epsInput.setVisible(false);
        } else {
            kInput.setVisible(false);
            maxIterInput.setVisible(false);

            mInput.setVisible(true);
            epsInput.setVisible(true);
        }
    }
}