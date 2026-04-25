package main.java.org.example.views;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Map;

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
import main.java.org.example.model.Wishlist;

/**
 * Controller class for the main view of the application.
 * Handles the display of the game grid, search functionality, navigation,
 * and user interactions with individual game cards such as favoriting.
 */
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

    // Stores downloaded images and prevents lag during searches
    private Map<String, Image> imageCache = new HashMap<>();

    /**
     * Initializes the controller class. Automatically called after the FXML file
     * has been loaded.
     * Parses the game database from the XML file and loads the initial game grid.
     *
     * @param url The location used to resolve relative paths for the root object,
     * or null.
     * @param rb  The resources used to localize the root object, or null.
     */
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Point to the local XML database
        String dbPath = "src/main/resources/bgg90Games.xml";

        try {
            // Open the file and hand it to the parser
            InputStream inputStream = new FileInputStream(dbPath);
            List<Game> parsedGames = GameParser.parseGames(inputStream);

            // Verify the parser actually found data
            if (parsedGames.isEmpty()) {
                System.out.println("Warning: No games loaded. Check your XML file path!");
            } else {
                System.out.println("Successfully loaded " + parsedGames.size() + " games from the database.");
                // Populate the game grid
                loadGamesIntoGrid(parsedGames);
            }

            inputStream.close();

        } catch (Exception e) {
            System.out.println("Error loading game database: " + e.getMessage());
        }
    }

    /**
     * Clears the current grid and populates it with the provided list of games.
     * Maps the list into a 3-column layout.
     *
     * @param games The list of Game objects to display in the grid.
     */
    private void loadGamesIntoGrid(List<Game> games) {
        // Wipe any existing cards before drawing new ones (important for search filtering)
        gameGrid.getChildren().clear();

        // Track grid coordinates
        int col = 0;
        int row = 0;

        for (Game game : games) {
            // Generate the UI component for the game card
            VBox card = createGameCard(game);

            // Place  the card in the grid at the current coordinates
            gameGrid.add(card, col, row);

            // Move one column to the right
            col++;
            
            // At three columns, wrap around to the next row
            if (col == 3) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * Constructs a stylized VBox card for a single game, containing its cover art,
     * title,
     * and a favorite toggle button. Also implements caching and accessibility
     * features.
     *
     * @param game The Game object to extract data from.
     * @return A fully constructed VBox representing the game card.
     */
    private VBox createGameCard(Game game) {
        // Build the main outer container
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("game-card");
        card.setStyle("-fx-cursor: hand;");
        card.setMaxWidth(300);

        // Reduce scroll lag
        card.setCache(true);
        card.setCacheHint(javafx.scene.CacheHint.SPEED);

        // Prepare the image view
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(200);

        if (game.getImageUrl() != null && !game.getImageUrl().isEmpty()) {
            // Check the RAM cache for the image to prevent duplicate network requests
            Image img = imageCache.get(game.getImageUrl());

            if (img == null) {
                // If the image is not found in cache; download it in the background
                img = new Image(game.getImageUrl(), true);
                imageCache.put(game.getImageUrl(), img); // Save it for later
            }

            imageView.setImage(img);
        } else {
            // Apply fallback styling if no image URL exists
            imageView.getStyleClass().add("game-image-placeholder");
        }

        // Wrap the image in a container to normalize heights across the grid
        VBox imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setMinHeight(210);
        imageContainer.setMaxHeight(210);

        // Assign screen reader accessibility properties to the image container
        imageContainer.setFocusTraversable(true);
        imageContainer.setAccessibleRole(javafx.scene.AccessibleRole.BUTTON);
        imageContainer.setAccessibleText("Open details for " + game.getTitle());

        // Auto-scroll the window if screen reader tabs onto this element
        imageContainer.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) {
                autoScrollToNode(card);
            }
        });

        // Trigger Game View if a keyboard user presses Space or Enter on the image
        imageContainer.setOnKeyPressed(e -> {
            if (e.getCode() == javafx.scene.input.KeyCode.ENTER || e.getCode() == javafx.scene.input.KeyCode.SPACE) {
                System.out.println("Opening Game View via Keyboard for: " + game.getTitle());
                try {
                    openGameView(game, e);
                } catch (IOException ex) {
                    System.out.println("Error opening game view: " + ex.getMessage());
                }
                e.consume(); // Prevent the event from bubbling up
            }
        });

        // Setup the bottom text row
        HBox textRow = new HBox(10);
        textRow.setAlignment(Pos.CENTER);

        // Setup the title label
        Label titleLabel = new Label(game.getTitle());
        titleLabel.getStyleClass().add("game-card-title");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(200);

        // Setup the favorite heart button
        javafx.scene.control.ToggleButton favButton = new javafx.scene.control.ToggleButton("♥");
        favButton.getStyleClass().add("fav-button");

        // Check if this game is already in any of the user's wishlists
        boolean isFavorited = false;
        for (Wishlist w : Session.getInstance().getWishlists()) {
            if (w.getGames().stream().anyMatch(g -> g.getTitle().equals(game.getTitle()))) {
                isFavorited = true;
                break;
            }
        }
        
        // Sync the button state with the database
        favButton.setSelected(isFavorited);

        // Set dynamic screen reader text based on the initial state
        if (isFavorited) {
            favButton.setAccessibleText("Remove " + game.getTitle() + " from favorites");
        } else {
            favButton.setAccessibleText("Add " + game.getTitle() + " to favorites");
        }

        // Auto-scroll the window if screen reader tabs onto the heart button
        favButton.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) {
                autoScrollToNode(card);
            }
        });

        // Handle clicks on the favorite button
        favButton.setOnAction(e -> {
            // Block guests from modifying wishlists
            if (!Session.getInstance().isLoggedIn()) {
                showGuestAlert("You must be logged in to save games to a wishlist!");
                favButton.setSelected(false); // Revert the button toggle
                e.consume();
                return;
            }

            if (favButton.isSelected()) {
                // If the user selects the heart, launch the custom wishlist assignment dialog
                showCustomWishlistDialog(game, favButton);
            } else {
                // If the user deselects the heart, wipe the game from all lists
                System.out.println("Unfavorited: " + game.getTitle());
                for (Wishlist w : Session.getInstance().getWishlists()) {
                    w.getGames().removeIf(g -> g.getTitle().equals(game.getTitle()));
                }
                // Update screen reader text for next interaction
                favButton.setAccessibleText("Add " + game.getTitle() + " to favorites");
            }
            e.consume(); // Prevent the click from opening the Game View behind it
        });

        // Assemble the final card
        textRow.getChildren().addAll(titleLabel, favButton);
        card.getChildren().addAll(imageContainer, textRow);

        // Allow standard mouse users to click anywhere on the background card to open the game
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

    /**
     * Toggles the visibility of the advanced search filter bar.
     *
     * @param event The action event triggered by clicking the filter toggle button.
     */
    @FXML
    public void openFilters(ActionEvent event) {
        // Read the toggle state and apply it directly to the UI elements
        boolean isSelected = toggleFilters.isSelected();
        filterBar.setVisible(isSelected);
        filterBar.setManaged(isSelected);
        System.out.println("Filters Menu " + (isSelected ? "Expanded" : "Collapsed"));
    }

    /**
     * Navigates the user back to the main game grid view by reloading the FXML.
     *
     * @param event The action event triggered by clicking the Games menu bar button.
     * @throws IOException If the target FXML file cannot be loaded.
     */
    @FXML
    private void navToGames(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/mainView.fxml"));
        stage.getScene().setRoot(root);
        Session.getInstance().applyGlobalSettings(stage.getScene());
        stage.show();
    }

    /**
     * Navigates the user to their wishlists view.
     * Blocks access and shows a warning alert if the current user is a guest.
     *
     * @param event The action event triggered by clicking the wishlist menu bar
     * button.
     * @throws IOException If the target FXML file cannot be loaded.
     */
    @FXML
    private void navToLists(ActionEvent event) throws IOException {
        // Verify user privileges before allowing navigation
        if (!Session.getInstance().isLoggedIn()) {
            showGuestAlert("You must be logged in to view wishlists!");
            return;
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/wishlistView.fxml"));
        stage.getScene().setRoot(root);
        Session.getInstance().applyGlobalSettings(stage.getScene());
        stage.show();
    }

    /**
     * Opens the application settings menu in a modal dialog window.
     *
     * @param event The action event triggered by clicking the settings menu bar button.
     * @throws IOException If the settings FXML file cannot be loaded.
     */
    @FXML
    private void openSettings(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/settingsView.fxml"));

        // Create a separate window so it floats over the main UI
        Stage settingsStage = new Stage();
        settingsStage.initOwner(((Node) event.getSource()).getScene().getWindow());
        settingsStage.initModality(javafx.stage.Modality.APPLICATION_MODAL); // Blocks interaction with main window
        settingsStage.setTitle("Settings");

        javafx.scene.Scene scene = new javafx.scene.Scene(root, 300, 250);

        settingsStage.setScene(scene);
        
        // Ensure theme and text size settings apply to the popup
        Session.getInstance().applyGlobalSettings(scene);

        settingsStage.showAndWait();
    }

    /**
     * Logs the current user out, clears session data, and returns to the login
     * screen.
     *
     * @param event The action event triggered by clicking the logout menu bar button.
     * @throws IOException If the login FXML file cannot be loaded.
     */
    @FXML
    private void logOut(ActionEvent event) throws IOException {
        // Clear active session data
        Session.getInstance().logOut();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/loginView.fxml"));
        stage.getScene().setRoot(root);

        // Global settings will reset to defaults on the new login screen
        stage.show();
    }

    /**
     * Switches the scene from the main dashboard to the detailed individual Game View.
     * * @param game The game to be opened, which gets saved to the current Session.
     * * @param action The event triggering the navigation (supports mouse clicks or
     * keyboard events).
     * @throws IOException If the Game View FXML file fails to load.
     */
    @FXML
    private void openGameView(Game game, javafx.event.Event action) throws IOException {
        Stage stage = (Stage) ((Node) action.getSource()).getScene().getWindow();
        
        // Hand the target game to the Session so the GameView controller can retrieve it
        Session currentSession = Session.getInstance();
        currentSession.setCurrentGame(game);
        
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/gameView.fxml"));
        stage.getScene().setRoot(root);
        Session.getInstance().applyGlobalSettings(stage.getScene());
        stage.show();
    }

    /**
     * Filters the displayed games based on the text entered in the search bar.
     * Triggers automatically as the user types.
     * * @param event The key event triggered by typing in the search bar.
     */
    @FXML
    private void searchGames(KeyEvent event) {
        // Pass the master list to the search utility
        GameSearch.searchGames((ArrayList<Game>) GameParser.getGamesList(), searchBar.getText());
        
        // Refresh the grid with the resulting filtered list
        loadGamesIntoGrid(GameSearch.getSearchResults());
    }

    /**
     * Calculates a node's position within the grid and forces the ScrollPane to
     * center on it. Used primarily to keep elements in view during keyboard and
     * screen reader navigation.
     *
     * @param card The UI node to scroll into the center of the viewport.
     */
    private void autoScrollToNode(Node card) {
        // Platform.runLater ensures the math calculates only after JavaFX finishes drawing the layout
        javafx.application.Platform.runLater(() -> {
            // Calculate absolute positions
            double nodeY = card.getBoundsInParent().getMinY();
            double nodeHeight = card.getBoundsInParent().getHeight();
            double gridHeight = gameGrid.getBoundsInLocal().getHeight();
            double viewportHeight = mainScrollPane.getViewportBounds().getHeight();

            // Total scrollable area
            double maxScroll = gridHeight - viewportHeight;

            if (maxScroll > 0) {
                // Find the percentage needed to place the node in the exact center of the screen
                double desiredY = nodeY - (viewportHeight / 2) + (nodeHeight / 2);
                double scrollValue = desiredY / maxScroll;

                // Clamp the value between 0.0 and 1.0 to prevent crashing the scrollbar
                scrollValue = Math.max(0.0, Math.min(1.0, scrollValue));

                mainScrollPane.setVvalue(scrollValue);
            }
        });
    }

    /**
     * Displays a custom modal dialog allowing the user to add a game to an existing
     * wishlist or create a new one. Bypasses native JavaFX dialogs for better styling
     * and accessibility.
     *
     * @param game      The game being added to a wishlist.
     * @param favButton The toggle button associated with the game, used to update
     * its visual state.
     */
    private void showCustomWishlistDialog(Game game, javafx.scene.control.ToggleButton favButton) {
        // Create the custom window
        Stage dialogStage = new Stage();
        dialogStage.initOwner(mainScrollPane.getScene().getWindow());
        dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Wishlist Menu");

        // Layout container
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new javafx.geometry.Insets(20));

        Label headerLabel = new Label("Add " + game.getTitle() + " to a list:");
        headerLabel.setWrapText(true);
        headerLabel.setFocusTraversable(true);

        // Gather existing list names from the Session
        List<String> listNames = new ArrayList<>();
        for (Wishlist w : Session.getInstance().getWishlists()) {
            listNames.add(w.getName());
        }
        
        // Add the "Create New List" option to the end of the data list
        String createNewOption = "➕ Create New List...";
        listNames.add(createNewOption);

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(listNames);
        comboBox.setValue(listNames.get(0));
        comboBox.setAccessibleText("Select a wishlist to add your game to");

        // Prepare the secondary text field for creating entirely new lists
        TextField newListNameField = new TextField();
        newListNameField.setPromptText("Enter new list name...");
        newListNameField.setAccessibleText("Type the name of your new wishlist");

        // Keep it hidden unless "Create New List" is selected
        newListNameField.setVisible(false);
        newListNameField.setManaged(false);

        // Listener to dynamically reveal the text field
        comboBox.setOnAction(e -> {
            boolean isNew = comboBox.getValue().equals(createNewOption);
            newListNameField.setVisible(isNew);
            newListNameField.setManaged(isNew); // managed=true allows it to take up physical space
            if (isNew) {
                // Instantly hand screen reader focus to the text field when it appears
                javafx.application.Platform.runLater(newListNameField::requestFocus);
            }
        });

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Save");
        saveButton.setDefaultButton(true); // Maps to Enter key

        Button cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true); // Maps to Esc key

        // Save Button Logic
        saveButton.setOnAction(e -> {
            String selection = comboBox.getValue();

            if (selection.equals(createNewOption)) {
                // Handle entirely new list creation
                String newListName = newListNameField.getText().trim();
                
                if (!newListName.isEmpty()) {
                    Wishlist existingList = Session.getInstance().getWishlistByName(newListName);
                    
                    if (existingList == null) {
                        // Safe to create new list
                        Wishlist newList = new Wishlist(newListName);
                        newList.add(game);
                        Session.getInstance().getWishlists().add(newList);
                    } else {
                        // Check for duplicates before adding
                        if (existingList.getGames().stream().noneMatch(g -> g.getTitle().equals(game.getTitle()))) {
                            existingList.add(game);
                        }
                    }
                    favButton.setAccessibleText("Remove " + game.getTitle() + " from favorites");
                    favButton.setSelected(true); // Ensure heart stays red
                } else {
                    favButton.setSelected(false); // User left it blank, revert heart
                }
            } else {
                // Handle adding to an existing list
                Wishlist chosenList = Session.getInstance().getWishlistByName(selection);
                
                // Prevent duplicate insertions
                if (chosenList.getGames().stream().noneMatch(g -> g.getTitle().equals(game.getTitle()))) {
                    chosenList.add(game);
                }
                favButton.setAccessibleText("Remove " + game.getTitle() + " from favorites");
                favButton.setSelected(true);
            }
            dialogStage.close();
        });

        // Cancel Button Logic
        cancelButton.setOnAction(e -> {
            favButton.setSelected(false); // Revert heart toggle on abort
            dialogStage.close();
        });

        // Revert heart toggle if the user clicks the red 'X' window control
        dialogStage.setOnCloseRequest(e -> favButton.setSelected(false));

        buttonBox.getChildren().addAll(saveButton, cancelButton);
        layout.getChildren().addAll(headerLabel, comboBox, newListNameField, buttonBox);

        javafx.scene.Scene scene = new javafx.scene.Scene(layout, 350, 250);
        dialogStage.setScene(scene);

        // Pass standard/high-contrast CSS into the new window
        Session.getInstance().applyGlobalSettings(scene);

        // Force screen reader to focus the combobox immediately upon window open
        javafx.application.Platform.runLater(comboBox::requestFocus);

        dialogStage.showAndWait();
    }

    /**
     * Displays a warning alert to users operating in Guest Mode when they
     * attempt to access restricted features.
     *
     * @param message The specific warning message to display in the alert.
     */
    private void showGuestAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Guest Mode");
        alert.setHeaderText("Feature Unavailable");
        alert.setContentText(message);
        alert.showAndWait();
    }
}