package main.java.org.example.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import main.java.org.example.model.Session;

public class SettingsViewController 
{
    @FXML
    private CheckBox highContrastBox;

    @FXML
    private javafx.scene.control.Slider sizeSlider;

    @FXML
    public void initialize() 
    {
        // When the settings menu opens, make sure the checkbox matches the current session state
        highContrastBox.setSelected(Session.getInstance().getContrast());

        sizeSlider.setValue(Session.getInstance().getTextSize());

        sizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Session.getInstance().setTextSize(newValue.intValue());
            Session.getInstance().applyTextSize(sizeSlider.getScene().getRoot());

            Stage settingsStage = (Stage) sizeSlider.getScene().getWindow();
            Stage mainStage = (Stage) settingsStage.getOwner();
            if (mainStage != null && mainStage.getScene() != null)
            {
                Session.getInstance().applyTextSize(mainStage.getScene().getRoot());
            }
        });
    }

    @FXML
    private void toggleHighContrast(ActionEvent event) 
    {
        boolean isEnabled = highContrastBox.isSelected();
        
        // Terminal feedback for debugging
        System.out.println("High Contrast Mode Active: " + isEnabled);

        //Tell the global Session to remember the new setting
        Session.getInstance().setContrast(isEnabled);

        //Apply the updated Session theme to the Settings window
        Session.getInstance().applyTheme(highContrastBox.getScene());

        //Apply the updated Session theme to the Main window underneath it
        Stage settingsStage = (Stage) highContrastBox.getScene().getWindow();
        Stage mainStage = (Stage) settingsStage.getOwner();
        
        if (mainStage != null && mainStage.getScene() != null) 
        {
            Session.getInstance().applyTheme(mainStage.getScene());
        }
    }

    @FXML
    private void closeSettings(ActionEvent event) 
    {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}