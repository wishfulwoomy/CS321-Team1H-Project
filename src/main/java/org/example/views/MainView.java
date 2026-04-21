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
    private javafx.scene.layout.GridPane gameGrid;

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

        int col = 0;
        int row = 0;

        for (Game game : games) {
            VBox card = createGameCard(game);

            // Add to the grid at the specific column and row
            gameGrid.add(card, col, row);

            // Move to the next column
            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
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
        card.setStyle("-fx-cursor: hand;");
        
        card.setMaxWidth(300); 

        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true); 
        
        imageView.setFitHeight(200); 

        if (game.getImageUrl() != null && !game.getImageUrl().isEmpty()) {
            Image img = new Image(game.getImageUrl(), true);
            imageView.setImage(img);
        } else {
            imageView.getStyleClass().add("game-image-placeholder");
        }
        imageView.setAccessibleText("Cover art for " + game.getTitle());

        VBox imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setMinHeight(210);
        imageContainer.setMaxHeight(210);

        HBox textRow = new HBox(10);
        textRow.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(game.getTitle());
        titleLabel.getStyleClass().add("game-card-title");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(200); 

        Button favButton = new Button("♥");
        favButton.getStyleClass().add("fav-button");
        favButton.setAccessibleText("Add " + game.getTitle() + " to favorites");
        favButton.setOnAction(e -> {
            System.out.println("Favorited: " + game.getTitle());
            e.consume(); 
        });

        textRow.getChildren().addAll(titleLabel, favButton);
        
        card.getChildren().addAll(imageContainer, textRow);

        card.setOnMouseClicked(e -> {
            System.out.println("Opening Game View for: " + game.getTitle());
            try {
                openGameView(game, e);
            } catch (IOException ex) {
                System.out.println("Error opening game view: " + ex.getMessage());
            }
        });

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
    private void navToGames(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/mainView.fxml"));
        stage.getScene().setRoot(root);
        Session.getInstance().applyGlobalSettings(stage.getScene());
        stage.show();
    }

    @FXML
    private void navToLists(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/wishlistView.fxml"));
        stage.getScene().setRoot(root);
        Session.getInstance().applyGlobalSettings(stage.getScene());
        stage.show();
    }

    @FXML
    private void openSettings(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/settingsView.fxml"));

        Stage settingsStage = new Stage();
        settingsStage.initOwner(((Node) event.getSource()).getScene().getWindow());
        settingsStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        settingsStage.setTitle("Settings");

        javafx.scene.Scene scene = new javafx.scene.Scene(root, 300, 250);

        settingsStage.setScene(scene);
        Session.getInstance().applyGlobalSettings(scene);

        settingsStage.showAndWait();
    }

    @FXML
    private void logOut(ActionEvent event) throws IOException {
        Session.getInstance().logOut();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/loginView.fxml"));
        stage.getScene().setRoot(root);
        Session.getInstance().applyGlobalSettings(stage.getScene());
        stage.show();
    }

    @FXML
    private void openGameView(Game game, javafx.event.Event action) throws IOException {
        Stage stage = (Stage) ((Node) action.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/gameView.fxml"));

        Session currentSession = Session.getInstance();
        currentSession.setCurrentGame(game);

        stage.getScene().setRoot(root);
        Session.getInstance().applyGlobalSettings(stage.getScene());
        stage.show();
    }
}