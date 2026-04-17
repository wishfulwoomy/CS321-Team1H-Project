package main.java.org.example.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import main.java.org.example.model.Game;

public class WishlistView implements Initializable{

    @FXML
    private ListView<String> listNames;

    @FXML
    private ListView<String> listGames;

    @FXML
    public Label selectWishlist;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] testWishlists = {"Favorites", "Two-Player", "Card Games", "Party Games"};
        listNames.getItems().addAll(testWishlists);

        String[] testGames = {"Monopoly", "Clue", "Sorry", "Life"};
        listGames.getItems().addAll(testGames);

    }
}
