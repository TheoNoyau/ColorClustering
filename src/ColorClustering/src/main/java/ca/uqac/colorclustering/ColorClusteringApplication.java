package ca.uqac.colorclustering;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ColorClusteringApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ColorClusteringApplication.class.getResource("color-clustering.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("ColorClustering - by Theo Kerneves");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}