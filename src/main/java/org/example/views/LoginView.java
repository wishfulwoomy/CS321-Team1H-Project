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

/**
 * Controller class for the Login View.
 * Handles user authentication, guest access, and initial application
 * configuration.
 */
public class LoginView {

    // UI Fields
    @FXML
    private Hyperlink loginAsGuest;
    @FXML
    private Label invalidLabel;
    @FXML
    private javafx.scene.control.TextField usernameField;
    @FXML
    private javafx.scene.control.PasswordField passwordField;

    /**
     * Initializes the controller class. Automatically called after the FXML file is
     * loaded.
     *
     * @param url The location used to resolve relative paths for the root object,
     *            or null.
     * @param rb  The resources used to localize the root object, or null.
     */
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // Currently empty
    }

    /**
     * Handles the login process when the user clicks the login button.
     * Authenticates the entered credentials against the local XML user database.
     *
     * @param event The action event triggered by clicking the login button.
     * @throws IOException If the Main View FXML file fails to load upon successful
     *                     login.
     */
    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        // Retrieve the entered credentials
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        // Load the user database
        main.java.org.example.model.UserParser parser = new main.java.org.example.model.UserParser();
        parser.loadUsers("src/main/resources/SampleUser.xml");

        boolean loginSuccessful = false;

        // Verify the database is not empty and search for a matching user
        if (parser.getUserList() != null && !parser.getUserList().isEmpty()) {
            for (main.java.org.example.model.User user : parser.getUserList()) {
                // Check if both username and password match a record exactly
                if (user.getName().equals(enteredUsername) && user.getPassword().equals(enteredPassword)) {
                    // Pass the authenticated user to the global Session manager
                    Session.getInstance().logIn(user);
                    loginSuccessful = true;
                    break; // Stop searching once a match is found
                }
            }
        }

        // Handle login result
        if (loginSuccessful) {
            // Transition to the Main Dashboard
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/mainView.fxml"));
            stage.getScene().setRoot(root);

            // Apply the user's preferred visual theme
            Session.getInstance().applyGlobalSettings(stage.getScene());
            stage.show();
        } else {
            // Display error message and reset the form
            invalidLabel.setVisible(true);
            passwordField.clear();
            usernameField.requestFocus(); // Snap the cursor back to the username box
        }
    }

    /**
     * Handles the login process when the user presses the ENTER key.
     * Mirrors the exact authentication logic found in handleLogin().
     *
     * @param event The key event triggered by typing in the input fields.
     * @throws IOException If the Main View FXML file fails to load upon successful
     *                     login.
     */
    @FXML
    private void handleLogin2(KeyEvent event) throws IOException {
        // Only trigger the authentication flow if the specific key pressed was Enter
        if (event.getCode() == KeyCode.ENTER) {

            // Retrieve the entered credentials
            String enteredUsername = usernameField.getText();
            String enteredPassword = passwordField.getText();

            // Load the user database
            main.java.org.example.model.UserParser parser = new main.java.org.example.model.UserParser();
            parser.loadUsers("src/main/resources/SampleUser.xml");

            boolean loginSuccessful = false;

            // Search for a matching user
            if (parser.getUserList() != null && !parser.getUserList().isEmpty()) {
                for (main.java.org.example.model.User user : parser.getUserList()) {
                    if (user.getName().equals(enteredUsername) && user.getPassword().equals(enteredPassword)) {
                        Session.getInstance().logIn(user);
                        loginSuccessful = true;
                        break;
                    }
                }
            }

            // Handle login result
            if (loginSuccessful) {
                // Transition to the Main Dashboard
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/mainView.fxml"));
                stage.getScene().setRoot(root);

                Session.getInstance().applyGlobalSettings(stage.getScene());
                stage.show();
            } else {
                // Display error message and reset the form
                invalidLabel.setVisible(true);
                passwordField.clear();
                usernameField.requestFocus();
            }
        }
    }

    /**
     * Bypasses the authentication flow and logs the user in as a Guest.
     * Navigates directly to the main dashboard without establishing a user profile
     * in the Session.
     *
     * @param event The action event triggered by clicking the guest login link.
     * @throws IOException If the Main View FXML file fails to load.
     */
    @FXML
    private void guestLogin(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/mainView.fxml"));
        stage.getScene().setRoot(root);

        // Apply default global settings (since guests have no saved preferences)
        Session.getInstance().applyGlobalSettings(stage.getScene());
        stage.show();
    }

    /**
     * Opens the application settings menu in a modal dialog window directly from
     * the login screen.
     *
     * @param event The action event triggered by clicking the settings button.
     * @throws IOException If the Settings View FXML file fails to load.
     */
    @FXML
    private void openSettings(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/settingsView.fxml"));

        // Create a separate window so it floats over the login screen
        Stage settingsStage = new Stage();
        settingsStage.initOwner(((Node) event.getSource()).getScene().getWindow());
        settingsStage.initModality(Modality.APPLICATION_MODAL); // Blocks interaction with the login window
        settingsStage.setTitle("Settings");

        Scene scene = new Scene(root, 300, 250);

        // Allow users to close the settings window quickly using the Escape key
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