package main.java.org.example.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.input.KeyCode;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import main.java.org.example.model.Session;

public class LoginView {
    @FXML
    private Hyperlink loginAsGuest;
    @FXML
    private Label invalidLabel;
    @FXML
    private javafx.scene.control.TextField usernameField;
    @FXML
    private javafx.scene.control.PasswordField passwordField;

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException 
    {
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        main.java.org.example.model.UserParser parser = new main.java.org.example.model.UserParser();
        parser.loadUsers("src/main/resources/SampleUser.xml"); 

        boolean loginSuccessful = false;

        if (parser.getUserList() != null && !parser.getUserList().isEmpty()) {
            for (main.java.org.example.model.User user : parser.getUserList()) 
            {
                if (user.getName().equals(enteredUsername) && user.getPassword().equals(enteredPassword)) 
                {
                    Session.getInstance().logIn(user);
                    loginSuccessful = true;
                    break;
                }
            }
        }

        if (loginSuccessful) 
        {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/mainView.fxml"));
            stage.getScene().setRoot(root);
            
            Session.getInstance().applyGlobalSettings(stage.getScene());
            stage.show();
        } 
        else 
        {
            invalidLabel.setVisible(true);
            passwordField.clear();
            usernameField.requestFocus();
        }
    }

    @FXML
    private void handleLogin2(KeyEvent event) throws IOException
    {
        if (event.getCode() == KeyCode.ENTER) {
            String enteredUsername = usernameField.getText();
            String enteredPassword = passwordField.getText();

            main.java.org.example.model.UserParser parser = new main.java.org.example.model.UserParser();
            parser.loadUsers("src/main/resources/SampleUser.xml");

            boolean loginSuccessful = false;

            if (parser.getUserList() != null && !parser.getUserList().isEmpty()) {
                for (main.java.org.example.model.User user : parser.getUserList())
                {
                    if (user.getName().equals(enteredUsername) && user.getPassword().equals(enteredPassword))
                    {
                        Session.getInstance().logIn(user);
                        loginSuccessful = true;
                        break;
                    }
                }
            }

            if (loginSuccessful)
            {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/mainView.fxml"));
                stage.getScene().setRoot(root);

                Session.getInstance().applyGlobalSettings(stage.getScene());
                stage.show();
            }
            else {
                invalidLabel.setVisible(true);
                passwordField.clear();
                usernameField.requestFocus();
            }
        }
    }

    @FXML
    private void guestLogin(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/mainView.fxml"));
        stage.getScene().setRoot(root);
        Session.getInstance().applyGlobalSettings(stage.getScene());
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
        Session.getInstance().applyGlobalSettings(scene);
        settingsStage.showAndWait();
    }
}