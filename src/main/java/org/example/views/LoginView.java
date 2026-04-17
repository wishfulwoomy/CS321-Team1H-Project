package main.java.org.example.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.input.KeyCode;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginView {
    @FXML
    private Hyperlink loginAsGuest;

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // Put anything not done in scene builder here
    }

    @FXML
    private void guestLogin(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/mainView.fxml"));
        stage.getScene().setRoot(root);
        stage.show();
    }

    @FXML
    private void openSettings(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/settingsView.fxml"));

        Stage settingsStage = new Stage();
        settingsStage.initOwner(((Node)event.getSource()).getScene().getWindow());
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        settingsStage.setTitle("Settings");

        Scene scene = new Scene(root, 300, 250);

        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                settingsStage.close();
            }
        });

        settingsStage.setScene(scene);
        settingsStage.showAndWait();
    }

}