package main.java.org.example.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class SettingsViewController {
    @FXML
    private CheckBox highContrastBox;

    @FXML
    private void toggleHighContrast(ActionEvent event) {
        boolean isEnabled = highContrastBox.isSelected();

        System.out.println("High Contrast Mode Active: " + isEnabled);

        String cssFile = getClass().getResource("/org.openjfx/highContrast.css").toExternalForm();
        Parent settingsRoot = highContrastBox.getScene().getRoot();
        if (isEnabled) {
            settingsRoot.getStylesheets().add(cssFile);
        } else {
            settingsRoot.getStylesheets().remove(cssFile);
        }

        Stage settingsStage = (Stage) highContrastBox.getScene().getWindow();
        Stage mainStage = (Stage) settingsStage.getOwner();

        if (mainStage != null && mainStage.getScene() != null) {
            Parent mainRoot = mainStage.getScene().getRoot();
            if (isEnabled) {
                mainRoot.getStylesheets().add(cssFile);
            } else {
                mainRoot.getStylesheets().remove(cssFile);
            }
        }
    }

    @FXML
    private void closeSettings(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}