package main.java.org.example.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import main.java.org.example.model.Session;

/**
 * Controller class for the application's Settings menu.
 * Manages global accessibility preferences, such as High Contrast mode
 * and dynamic text scaling, and ensures those preferences are instantly
 * applied across all active application windows.
 */
public class SettingsViewController {
    // UI Fields
    @FXML
    private CheckBox highContrastBox;

    @FXML
    private javafx.scene.control.Slider sizeSlider;

    @FXML
    public void initialize() {
        // Sync the UI with the backend Session data so the controls reflect current
        // settings
        highContrastBox.setSelected(Session.getInstance().getContrast());
        sizeSlider.setValue(Session.getInstance().getTextSize());

        // Add a dynamic listener to the slider so text resizes in real-time as the user
        // drags it
        sizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {

            // Update the global Session manager with the new integer value
            Session.getInstance().setTextSize(newValue.intValue());

            // Instantly apply the new text size to the currently open Settings window
            Session.getInstance().applyGlobalSettings(sizeSlider.getScene());

            // Retrieve the main application window sitting behind the Settings popup
            Stage settingsStage = (Stage) sizeSlider.getScene().getWindow();
            Stage mainStage = (Stage) settingsStage.getOwner();

            // Safely apply the new text size to the main window so the user sees the
            // changes live
            if (mainStage != null && mainStage.getScene() != null) {
                Session.getInstance().applyGlobalSettings(mainStage.getScene());
            }
        });
    }

    @FXML
    private void toggleHighContrast(ActionEvent event) {
        // Check if the box was just checked or unchecked
        boolean isEnabled = highContrastBox.isSelected();

        // Terminal feedback for debugging
        System.out.println("High Contrast Mode Active: " + isEnabled);

        // Tell the global Session to remember the new setting
        Session.getInstance().setContrast(isEnabled);

        // Apply the updated Session theme to the Settings window immediately
        Session.getInstance().applyGlobalSettings(highContrastBox.getScene());

        // Retrieve the main application window sitting behind the Settings popup
        Stage settingsStage = (Stage) highContrastBox.getScene().getWindow();
        Stage mainStage = (Stage) settingsStage.getOwner();

        // Safely apply the updated Session theme to the Main window underneath it
        if (mainStage != null && mainStage.getScene() != null) {
            Session.getInstance().applyGlobalSettings(mainStage.getScene());
        }
    }

    @FXML
    private void closeSettings(ActionEvent event) {
        // Extract the specific window that the clicked button belongs to
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Close the window
        stage.close();
    }
}