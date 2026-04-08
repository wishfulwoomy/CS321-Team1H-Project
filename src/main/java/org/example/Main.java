package main.java.org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/mainView.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/org.openjfx/styles.css").toExternalForm());

        Group root = new Group();
        Scene scene = new Scene(root,1280, 720);

        stage.setFullScreen(true);

        stage.setScene(scene);

        stage.setTitle("Board main.java.org.example.model.Game App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
