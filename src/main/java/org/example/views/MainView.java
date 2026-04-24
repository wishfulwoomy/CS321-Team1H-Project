package main.java.org.example.views;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

import java.util.HashMap;
import java.util.Map;

import main.java.org.example.model.Game;
import main.java.org.example.model.GameParser;
import main.java.org.example.model.GameSearch;
import main.java.org.example.model.Session;
import main.java.org.example.model.Wishlist;

public class MainView implements Initializable {
    @FXML
    private ToggleButton toggleFilters;
    @FXML
    private HBox filterBar;
    @FXML
    private TextField searchBar;
    @FXML
    private javafx.scene.layout.GridPane gameGrid;
    @FXML
    private javafx.scene.control.ScrollPane mainScrollPane;
    private Map<String, Image> imageCache = new HashMap<>();

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String dbPath = "src/main/resources/bgg90Games.xml";

        try {
            InputStream inputStream = new FileInputStream(dbPath);
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

    private void loadGamesIntoGrid(List<Game> games) {
        gameGrid.getChildren().clear();

        int col = 0;
        int row = 0;

        for (Game game : games) {
            VBox card = createGameCard(game);

            gameGrid.add(card, col, row);

            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }
    }

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
            // --- THE FIX: Check the cache before downloading! ---
            Image img = imageCache.get(game.getImageUrl());

            if (img == null) {
                img = new Image(game.getImageUrl(), true);
                imageCache.put(game.getImageUrl(), img);
            }

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

        boolean isFavorited = false;
        for (Wishlist w : Session.getInstance().getWishlists()) {
            if (w.getGames().stream().anyMatch(g -> g.getTitle().equals(game.getTitle()))) {
                isFavorited = true;
                break;
            }
        }
        favButton.setSelected(isFavorited);

        if (isFavorited) {
            favButton.setAccessibleText("Remove " + game.getTitle() + " from favorites");
        } else {
            favButton.setAccessibleText("Add " + game.getTitle() + " to favorites");
        }

        favButton.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) {
                autoScrollToNode(card);
            }
        });

        favButton.setOnAction(e -> {
            // --- SECURITY CHECK: Block Guests from Favoriting! ---
            if (!Session.getInstance().isLoggedIn()) {
                showGuestAlert("You must be logged in to save games to a wishlist!");
                favButton.setSelected(false);
                e.consume();
                return;
            }
            // -----------------------------------------------------

            if (favButton.isSelected()) {
                showCustomWishlistDialog(game, favButton);
            } else {
                System.out.println("Unfavorited: " + game.getTitle());
                for (Wishlist w : Session.getInstance().getWishlists()) {
                    w.getGames().removeIf(g -> g.getTitle().equals(game.getTitle()));
                }
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
        // --- SECURITY CHECK: Block Guests from Viewing Lists! ---
        if (!Session.getInstance().isLoggedIn()) {
            showGuestAlert("You must be logged in to view wishlists!");
            return;
        }
        // --------------------------------------------------------

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

        // User settings are not applied once logged out
        stage.show();
    }

    /**
     * When a card is clicked, switches scene from MainView to GameView.
     * 
     * @param game   The game to be opened. Gets saved as the current game to
     *               Session.
     * @param action The action triggering the function. Takes a general javafx
     *               Event,
     *               so GameView can open via mouse click or the enter key.
     * @throws IOException Throws exception if the fxml file fails to load.
     */
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
     * Function to search all games in database and display to screen.
     * 
     * @param event The event triggering the search. Currently, the search runs
     *              immediately as you type into the search bar.
     */
    @FXML
    private void searchGames(KeyEvent event) {
        // get parsed games from GameParser, cast to an ArrayList, search using
        // GameSearch
        GameSearch.searchGames((ArrayList<Game>) GameParser.getGamesList(), searchBar.getText());
        // load games
        loadGamesIntoGrid(GameSearch.getSearchResults());
    }

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

    private void showCustomWishlistDialog(Game game, javafx.scene.control.ToggleButton favButton) {
        Stage dialogStage = new Stage();
        dialogStage.initOwner(mainScrollPane.getScene().getWindow());
        dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Wishlist Menu");

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new javafx.geometry.Insets(20));

        Label headerLabel = new Label("Add " + game.getTitle() + " to a list:");
        headerLabel.setWrapText(true);
        headerLabel.setFocusTraversable(true);

        List<String> listNames = new ArrayList<>();
        for (Wishlist w : Session.getInstance().getWishlists()) {
            listNames.add(w.getName());
        }
        String createNewOption = "➕ Create New List...";
        listNames.add(createNewOption);

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(listNames);
        comboBox.setValue(listNames.get(0));
        comboBox.setAccessibleText("Select a wishlist to add your game to");

        TextField newListNameField = new TextField();
        newListNameField.setPromptText("Enter new list name...");
        newListNameField.setAccessibleText("Type the name of your new wishlist");

        newListNameField.setVisible(false);
        newListNameField.setManaged(false);

        comboBox.setOnAction(e -> {
            boolean isNew = comboBox.getValue().equals(createNewOption);
            newListNameField.setVisible(isNew);
            newListNameField.setManaged(isNew);
            if (isNew) {
                javafx.application.Platform.runLater(newListNameField::requestFocus);
            }
        });

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Save");
        saveButton.setDefaultButton(true);

        Button cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true);

        saveButton.setOnAction(e -> {
            String selection = comboBox.getValue();

            if (selection.equals(createNewOption)) {
                String newListName = newListNameField.getText().trim();
                if (!newListName.isEmpty()) {
                    Wishlist existingList = Session.getInstance().getWishlistByName(newListName);
                    if (existingList == null) {
                        Wishlist newList = new Wishlist(newListName);
                        newList.add(game);
                        Session.getInstance().getWishlists().add(newList);
                    } else {
                        if (existingList.getGames().stream().noneMatch(g -> g.getTitle().equals(game.getTitle()))) {
                            existingList.add(game);
                        }
                    }
                    favButton.setAccessibleText("Remove " + game.getTitle() + " from favorites");
                    favButton.setSelected(true);
                } else {
                    favButton.setSelected(false);
                }
            } else {
                Wishlist chosenList = Session.getInstance().getWishlistByName(selection);
                if (chosenList.getGames().stream().noneMatch(g -> g.getTitle().equals(game.getTitle()))) {
                    chosenList.add(game);
                }
                favButton.setAccessibleText("Remove " + game.getTitle() + " from favorites");
                favButton.setSelected(true);
            }
            dialogStage.close();
        });

        cancelButton.setOnAction(e -> {
            favButton.setSelected(false);
            dialogStage.close();
        });

        dialogStage.setOnCloseRequest(e -> favButton.setSelected(false));

        buttonBox.getChildren().addAll(saveButton, cancelButton);
        layout.getChildren().addAll(headerLabel, comboBox, newListNameField, buttonBox);

        javafx.scene.Scene scene = new javafx.scene.Scene(layout, 350, 250);
        dialogStage.setScene(scene);

        Session.getInstance().applyGlobalSettings(scene);

        javafx.application.Platform.runLater(comboBox::requestFocus);

        dialogStage.showAndWait();
    }

    private void showGuestAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Guest Mode");
        alert.setHeaderText("Feature Unavailable");
        alert.setContentText(message);
        alert.showAndWait();
    }
}