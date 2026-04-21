package main.java.org.example.views;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import main.java.org.example.model.Game;
import main.java.org.example.model.GameParser;
import main.java.org.example.model.GameSearch;
import main.java.org.example.model.Session;

public class MainView implements Initializable {
    @FXML
    private ToggleButton toggleFilters;
    @FXML
    private TextField searchBar;
    @FXML
    private ButtonBar filterBar;
    @FXML
    private javafx.scene.layout.GridPane gameGrid;
    @FXML
    private javafx.scene.control.ScrollPane mainScrollPane;

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

        card.setCache(true);
        card.setCacheHint(javafx.scene.CacheHint.SPEED);

        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(200);

        if (game.getImageUrl() != null && !game.getImageUrl().isEmpty()) {
            Image img = new Image(game.getImageUrl(), true);
            imageView.setImage(img);
        } else {
            imageView.getStyleClass().add("game-image-placeholder");
        }

        VBox imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setMinHeight(210);
        imageContainer.setMaxHeight(210);

        imageContainer.setFocusTraversable(true);
        imageContainer.setAccessibleRole(javafx.scene.AccessibleRole.BUTTON);
        imageContainer.setAccessibleText("Open details for " + game.getTitle());

        imageContainer.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) {
                autoScrollToNode(card);
            }
        });

        imageContainer.setOnKeyPressed(e -> {
            if (e.getCode() == javafx.scene.input.KeyCode.ENTER || e.getCode() == javafx.scene.input.KeyCode.SPACE) {
                System.out.println("Opening Game View via Keyboard for: " + game.getTitle());
                try {
                    openGameView(game, e);
                } catch (IOException ex) {
                    System.out.println("Error opening game view: " + ex.getMessage());
                }
                e.consume();
            }
        });

        HBox textRow = new HBox(10);
        textRow.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(game.getTitle());
        titleLabel.getStyleClass().add("game-card-title");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(200);

        javafx.scene.control.ToggleButton favButton = new javafx.scene.control.ToggleButton("♥");
        favButton.getStyleClass().add("fav-button");
        favButton.setAccessibleText("Add " + game.getTitle() + " to favorites");

        favButton.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) {
                autoScrollToNode(card);
            }
        });

        favButton.setOnAction(e -> {
            if (favButton.isSelected()) {
                System.out.println("Favorited: " + game.getTitle());
                favButton.setAccessibleText("Remove " + game.getTitle() + " from favorites");
            } else {
                System.out.println("Unfavorited: " + game.getTitle());
                favButton.setAccessibleText("Add " + game.getTitle() + " to favorites");
            }
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
    private void searchGames(KeyEvent event) {
        // get parsed games from GameParser, cast to an ArrayList, search using GameSearch
        GameSearch.searchGames((ArrayList<Game>) GameParser.getGamesList(), searchBar.getText());
        // load games
        loadGamesIntoGrid(GameSearch.getSearchResults());
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
        Session currentSession = Session.getInstance(); // calls singleton session instance
        currentSession.setCurrentGame(game); // sets current game to pass to GameView
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/gameView.fxml"));
        stage.getScene().setRoot(root);
        Session.getInstance().applyGlobalSettings(stage.getScene());
        stage.show();
    }

    /**
     * Calculates a node's position in the grid and forces the ScrollPane to center
     * on it.
     */
    private void autoScrollToNode(Node card) {
        javafx.application.Platform.runLater(() -> {
            double nodeY = card.getBoundsInParent().getMinY();
            double nodeHeight = card.getBoundsInParent().getHeight();
            double gridHeight = gameGrid.getBoundsInLocal().getHeight();
            double viewportHeight = mainScrollPane.getViewportBounds().getHeight();

            double maxScroll = gridHeight - viewportHeight;

            if (maxScroll > 0) {
                double desiredY = nodeY - (viewportHeight / 2) + (nodeHeight / 2);
                double scrollValue = desiredY / maxScroll;

                scrollValue = Math.max(0.0, Math.min(1.0, scrollValue));

                mainScrollPane.setVvalue(scrollValue);
            }
        });
    }

}