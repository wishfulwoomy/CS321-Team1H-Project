package main.java.org.example.views;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.java.org.example.model.Game;
import main.java.org.example.model.Session;
import main.java.org.example.model.Wishlist;

/**
 * Controller class for the Wishlist View.
 * Manages the display and interaction of user-created wishlists.
 * Handles selecting wishlists, viewing games within those lists,
 * creating/deleting lists, and removing individual games.
 */
public class WishlistView implements Initializable {

    // UI Fields
    @FXML
    private ListView<String> listNames;

    @FXML
    private ListView<String> listGames;

    @FXML
    public Label selectWishlist;

    /**
     * Initializes the controller class. Automatically called after the FXML file is
     * loaded.
     * Populates the list of wishlists and sets up listeners for user interactions.
     *
     * @param location  The location used to resolve relative paths for the root
     *                  object, or null.
     * @param resources The resources used to localize the root object, or null.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate the left-hand list with all existing wishlist names from the Session
        for (Wishlist w : Session.getInstance().getWishlists()) {
            listNames.getItems().add(w.getName());
        }

        // Listen for when the user clicks a different wishlist name
        listNames.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            loadGamesForList(newValue);
        });

        // Listen for double-clicks on specific games to open their details
        listGames.setOnMouseClicked(this::handleGameDoubleClick);
    }

    /**
     * Updates the secondary ListView to display the games contained in the selected
     * wishlist.
     *
     * @param listName The name of the wishlist currently selected by the user.
     */
    private void loadGamesForList(String listName) {
        // Clear out any games currently showing from a previously selected list
        listGames.getItems().clear();

        // Safety check to prevent errors if the selection is somehow null
        if (listName == null)
            return;

        // Retrieve the actual Wishlist object from the database/session
        Wishlist selectedList = Session.getInstance().getWishlistByName(listName);

        // Extract the titles of every game in that list and push them to the UI
        if (selectedList != null) {
            for (Game g : selectedList.getGames()) {
                listGames.getItems().add(g.getTitle());
            }
        }
    }

    /**
     * Handles mouse interactions on the game list. Specifically looks for
     * double-clicks
     * to navigate the user directly to the detailed Game View for that specific
     * game.
     *
     * @param event The mouse event triggered by clicking on the list of games.
     */
    private void handleGameDoubleClick(MouseEvent event) {
        // Only trigger navigation if it was a definitive double-click
        if (event.getClickCount() == 2) {
            String selectedGameTitle = listGames.getSelectionModel().getSelectedItem();

            if (selectedGameTitle != null) {
                Game selectedGame = null;

                // Search through the session data to find the actual Game object matching this
                // title
                for (Wishlist w : Session.getInstance().getWishlists()) {
                    for (Game g : w.getGames()) {
                        if (g.getTitle().equals(selectedGameTitle)) {
                            selectedGame = g;
                            break;
                        }
                    }
                    if (selectedGame != null)
                        break;
                }

                // If the game was successfully found, transition to the Game View
                if (selectedGame != null) {
                    try {
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/gameView.fxml"));

                        // Save the selected game to the session so GameView knows what to load
                        Session.getInstance().setCurrentGame(selectedGame);

                        stage.getScene().setRoot(root);
                        Session.getInstance().applyGlobalSettings(stage.getScene());
                        stage.show();
                    } catch (Exception e) {
                        System.out.println("CRITICAL Error opening GameView:");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Opens a custom modal dialog allowing the user to create an entirely new
     * wishlist.
     * Updates both the UI and the underlying Session data upon successful creation.
     *
     * @param event The action event triggered by clicking the "Create List" button.
     */
    @FXML
    private void handleCreateList(ActionEvent event) {
        // Create the custom window
        Stage dialogStage = new Stage();
        dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());
        dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL); // Blocks main window interaction
        dialogStage.setTitle("Create Wishlist");

        // Set up layout and padding
        VBox layout = new VBox(15);
        layout.setAlignment(javafx.geometry.Pos.CENTER);
        layout.setPadding(new javafx.geometry.Insets(20));

        Label headerLabel = new Label("Create a new Wishlist:");
        headerLabel.setFocusTraversable(true);

        // Input field for the new list's name
        javafx.scene.control.TextField nameField = new javafx.scene.control.TextField();
        nameField.setPromptText("My New List");
        nameField.setAccessibleText("Enter the name of your new wishlist");

        // Container for action buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

        Button createButton = new Button("Create");
        createButton.setDefaultButton(true); // Maps to Enter key

        Button cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true); // Maps to Escape key

        // CREATE LOGIC
        createButton.setOnAction(e -> {
            String newListName = nameField.getText().trim();

            if (!newListName.isEmpty()) {
                // Ensure no existing list already shares this name
                if (Session.getInstance().getWishlistByName(newListName) == null) {
                    // Create it, save it, and update the UI
                    Wishlist newList = new Wishlist(newListName);
                    Session.getInstance().getWishlists().add(newList);
                    listNames.getItems().add(newListName);
                    System.out.println("Created new list: " + newListName);
                } else {
                    System.out.println("A list with that name already exists!");
                }
            }
            dialogStage.close();
        });

        // CANCEL LOGIC
        cancelButton.setOnAction(e -> dialogStage.close());

        // Assemble layout
        buttonBox.getChildren().addAll(createButton, cancelButton);
        layout.getChildren().addAll(headerLabel, nameField, buttonBox);

        // Apply scene and themes
        javafx.scene.Scene scene = new javafx.scene.Scene(layout, 300, 150);
        dialogStage.setScene(scene);
        Session.getInstance().applyGlobalSettings(scene);

        // Hand screen reader/keyboard focus directly to the text field
        javafx.application.Platform.runLater(nameField::requestFocus);

        dialogStage.showAndWait();
    }

    /**
     * Deletes the currently selected wishlist from the user's profile.
     * Contains safeguards to prevent deleting critical default lists like
     * "Favorites".
     *
     * @param event The action event triggered by clicking the "Delete List" button.
     */
    @FXML
    private void handleDeleteList(ActionEvent event) {
        String selectedListName = listNames.getSelectionModel().getSelectedItem();

        if (selectedListName != null) {
            // Hardcoded protection to prevent breaking core app features
            if (selectedListName.equals("Favorites")) {
                System.out.println("Cannot delete the default Favorites list.");
                return;
            }

            // Find the list in the database and remove it
            Wishlist listToRemove = Session.getInstance().getWishlistByName(selectedListName);
            Session.getInstance().getWishlists().remove(listToRemove);

            // Wipe it from the UI and clear the games viewing pane
            listNames.getItems().remove(selectedListName);
            listGames.getItems().clear();
        }
    }

    /**
     * Removes the currently selected game from the currently active wishlist.
     *
     * @param event The action event triggered by clicking the "Remove Game" button.
     */
    @FXML
    private void handleRemoveGame(ActionEvent event) {
        String selectedListName = listNames.getSelectionModel().getSelectedItem();
        String selectedGameTitle = listGames.getSelectionModel().getSelectedItem();

        // Ensure both a list and a specific game are actually selected
        if (selectedListName != null && selectedGameTitle != null) {
            Wishlist activeList = Session.getInstance().getWishlistByName(selectedListName);

            Game gameToRemove = null;

            // Find the actual Game object inside the list
            for (Game g : activeList.getGames()) {
                if (g.getTitle().equals(selectedGameTitle)) {
                    gameToRemove = g;
                    break;
                }
            }

            // Purge the game from the database
            if (gameToRemove != null) {
                activeList.remove(gameToRemove);
            }

            // Purge the game from the UI
            listGames.getItems().remove(selectedGameTitle);
        }
    }

    /**
     * Logs the current user out, clears session data, and returns to the login
     * screen.
     *
     * @param event The action event triggered by clicking the logout menu option.
     * @throws IOException If the login FXML file fails to load.
     */
    @FXML
    private void logOut(ActionEvent event) throws IOException {
        Session.getInstance().logOut();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/loginView.fxml"));
        stage.getScene().setRoot(root);
        Session.getInstance().applyGlobalSettings(stage.getScene());
        stage.show();
    }

    /**
     * Opens the application settings menu in a modal dialog window.
     *
     * @param event The action event triggered by clicking the settings menu option.
     * @throws IOException If the settings FXML file fails to load.
     */
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

    /**
     * Navigates the user back to the main dashboard view.
     *
     * @param event The action event triggered by clicking the Games menu option.
     * @throws IOException If the mainView FXML file fails to load.
     */
    @FXML
    private void handleBackToMainMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/mainView.fxml"));
        stage.getScene().setRoot(root);
        Session.getInstance().applyGlobalSettings(stage.getScene());
        stage.show();
    }
}