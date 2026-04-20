package main.java.org.example.views;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import main.java.org.example.model.Game;
import main.java.org.example.model.GameParser;
import main.java.org.example.model.Session;

public class MainView implements Initializable {
    @FXML
    private ToggleButton toggleFilters;
    @FXML
    private ButtonBar filterBar;
    @FXML
    private TilePane gameGrid;

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Path to XML boardgame database
        String dbPath = "src/main/resources/bgg90Games.xml";

        try {
            // Convert file path into InputStream for parser
            InputStream inputStream = new FileInputStream(dbPath);

            // Call GameParser class!
            List<Game> parsedGames = GameParser.parseGames(inputStream);

            if (parsedGames.isEmpty()) {
                System.out.println("Warning: No games loaded. Check your XML file path!");
            } else {
                System.out.println("Successfully loaded " + parsedGames.size() + " games from the database.");
                loadGamesIntoGrid(parsedGames);
            }

            inputStream.close();

        } catch (Exception e) {
            System.out.println("Error loading game database: " + e.getMessage());
        }
    }

    /**
     * Loops through a list of Game objects and adds them to the FXML TilePane.
     */
    private void loadGamesIntoGrid(List<Game> games) {
        gameGrid.getChildren().clear();

        for (Game game : games) {
            VBox card = createGameCard(game);
            gameGrid.getChildren().add(card);
        }
    }

    /**
     * Builds the visual UI node for a single Game, complete with its downloaded
     * image.
     */
    private VBox createGameCard(Game game) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("game-card");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        // Load the image from the URL!
        if (game.getImageUrl() != null && !game.getImageUrl().isEmpty()) {
            Image img = new Image(game.getImageUrl(), true);
            imageView.setImage(img);
        } else {
            imageView.getStyleClass().add("game-image-placeholder");
        }

        imageView.setAccessibleText("Cover art for " + game.getTitle());

        HBox textRow = new HBox(10);
        textRow.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(game.getTitle());
        titleLabel.getStyleClass().add("game-card-title");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(100); // Keeps long titles from stretching the card

        Button favButton = new Button("♥");
        favButton.getStyleClass().add("fav-button");
        favButton.setAccessibleText("Add " + game.getTitle() + " to favorites");
        favButton.setOnAction(e -> {
            System.out.println("Favorited: " + game.getTitle());
            // Wishlist logic goes here
        });

        textRow.getChildren().addAll(titleLabel, favButton);
        card.getChildren().addAll(imageView, textRow);

        return card;
    }

    @FXML
    public void openFilters(ActionEvent event) {
        boolean isSelected = toggleFilters.isSelected();
        filterBar.setVisible(isSelected);
        filterBar.setManaged(isSelected);
        System.out.println("Filters Menu " + (isSelected ? "Expanded" : "Collapsed"));
    }

    @FXML
    private void openWishlistView(ActionEvent action) throws IOException {
        Stage stage = (Stage) ((Node) action.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/wishlistView.fxml"));
        stage.getScene().setRoot(root);
        Session.getInstance().applyGlobalSettings(stage.getScene());
        stage.show();
    }
}