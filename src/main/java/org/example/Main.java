package main.java.org.example;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root,1280, 720);

        stage.setFullScreen(true);

        stage.setScene(scene);

        stage.setTitle("Board main.java.org.example.model.Game App");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
