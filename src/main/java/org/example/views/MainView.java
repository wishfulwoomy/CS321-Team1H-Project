package main.java.org.example.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import main.java.org.example.model.Game;

public class MainView implements Initializable {

    @FXML

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Put anything not done in scene builder here
    }

    private String search;
    private Button favoriteButton;
    private Label selectedGame;
    private Label gameName;

    public void searchView(String query) {

    }
    public void openFilters() {

    }
    public void openWishlists() {

    }
    public void openGameView(Game game) {

    }
}
