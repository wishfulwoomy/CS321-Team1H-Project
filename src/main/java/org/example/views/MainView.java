package main.java.org.example.views;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import main.java.org.example.model.Game;

public class MainView implements Initializable {
    private String search;
    private Button favoriteButton;
    private Label selectedGame;
    private Label gameName;
    /**
     * Opens and closes the list of filters. Sits to the right of the search bar.
     */
    @FXML
    private ToggleButton toggleFilters;
    /**
     * Container bar for all the individual filters.
     * Starts invisible, toggled by toggleFilters.
     */
    @FXML
    private ButtonBar filterBar;

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Put anything not done in scene builder here
    }

    public void searchView(String query) {

    }

    @FXML
    public void openFilters(ActionEvent event) {
        boolean isSelected = toggleFilters.isSelected();
        filterBar.setVisible(isSelected);
        filterBar.setManaged(isSelected);
    }
    public void openWishlists() {

    }
    public void openGameView(Game game) {

    }
}
