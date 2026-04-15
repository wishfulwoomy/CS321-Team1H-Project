package main.java.org.example.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginView {
    @FXML
    private Hyperlink loginAsGuest;

    @FXML
    //@Override
    public void initialize(URL url, ResourceBundle rb) {
        // Put anything not done in scene builder here
    }

    @FXML
    private void guestLogin(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/mainView.fxml"));
        stage.setScene(new Scene(root));
    }
}
