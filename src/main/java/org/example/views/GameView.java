package main.java.org.example.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.org.example.model.Game;
import main.java.org.example.model.Review;
import main.java.org.example.model.Session;
import main.java.org.example.model.Wishlist;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller class for the detailed Game View.
 * Responsible for displaying comprehensive information about a selected game,
 * including its cover art, description, player count, playtime, and user
 * reviews.
 * Also handles user interactions such as adding the game to a wishlist or
 * initiating a review.
 */
public class GameView implements Initializable {

    // UI Fields for Game Information
    @FXML
    private ImageView imageGamePicture;
    @FXML
    private Label labelGameTitle;
    @FXML
    private Text textAverageRating;
    @FXML
    private Text textDescription;
    @FXML
    private Text textPlayerAmount;
    @FXML
    private Text textPlaytime;
    @FXML
    private ScrollPane reviewsScrollPane;
    @FXML
    private GridPane reviewsGrid;

    // UI Buttons
    @FXML
    private Button buttonBack;
    @FXML
    private Button buttonAddToList;
    @FXML
    private Button buttonLeaveReview;

    /**
     * Initializes the controller class. Automatically called after the FXML file is
     * loaded.
     * Populates the view with the selected game's details retrieved from the active
     * Session.
     *
     * @param url The location used to resolve relative paths for the root object,
     *            or null.
     * @param rb  The resources used to localize the root object, or null.
     */
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Retrieve the active session and the currently selected game
        Session currentSession = Session.getInstance();
        Game g = currentSession.getCurrentGame();

        // Set the main game title
        labelGameTitle.setText(g.getTitle());

        // Load and display the game's cover art if an image URL is provided
        if (g.getImageUrl() != null && !g.getImageUrl().isEmpty()) {
            // The 'true' flag loads the image asynchronously in the background
            Image img = new Image(g.getImageUrl(), true);
            imageGamePicture.setImage(img);
            // Assign an accessible label for screen readers
            imageGamePicture.setAccessibleText("Cover art for " + g.getTitle());
        }

        // Populate the game description
        textDescription.setText(g.getDescription());

        // Bind the text description's wrapping width to the window size to ensure it
        // scales responsively.
        // Subtracting 60 pixels accounts for padding and prevents text from being cut
        // off.
        textDescription.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                textDescription.wrappingWidthProperty().bind(newScene.widthProperty().subtract(60));
            }
        });

        // Format and display the required player count
        String minPlayers = Integer.toString(g.getMinPlayers());
        String maxPlayers = Integer.toString(g.getMaxPlayers());

        if (minPlayers.equals(maxPlayers)) {
            // String comparison to properly format single-player games
            if (minPlayers.equals("1")) {
                String playersString = minPlayers + " player";
                textPlayerAmount.setText(playersString);
            } else {
                String playersString = minPlayers + " players";
                textPlayerAmount.setText(playersString);
            }
        } else {
            String playersString = minPlayers + " - " + maxPlayers + " players";
            textPlayerAmount.setText(playersString);
        }

        // Format and display the expected playtime
        String playtimeString = Integer.toString(g.getPlayTimeMinutes());
        textPlaytime.setText(playtimeString + " minutes");

        // Populate the review section with the game's existing reviews
        loadReviews(g.getReviews());

        // Attach the custom wishlist dialog to the "Add to list" button
        buttonAddToList.setOnAction(e -> {
            showCustomWishlistDialog(g);
        });
    }

    /**
     * Clears the current review grid and populates it with the provided list of
     * reviews.
     *
     * @param reviews The list of Review objects associated with the current game.
     */
    private void loadReviews(List<Review> reviews) {
        reviewsGrid.getChildren().clear();
        int row = 0;

        for (Review r : reviews) {
            VBox card = createReviewCard(r);
            reviewsGrid.add(card, 0, row);
            row++;
        }
    }

    /**
     * Constructs a UI card to display a single user review.
     *
     * @param r The Review object containing the rating, author, date, and comment.
     * @return A styled VBox containing the review's details.
     */
    private VBox createReviewCard(Review r) {
        // Build the main outer container
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("review-card");
        card.setStyle("-fx-cursor: hand;");
        card.setMaxWidth(300);

        // Enable caching for smoother scrolling performance
        card.setCache(true);
        card.setCacheHint(javafx.scene.CacheHint.SPEED);

        // Extract and format review data into Text nodes
        String ratingString = String.valueOf(r.getRating());
        Text ratingText = new Text(ratingString);
        Text usernameText = new Text(r.getAuthor());
        Text dateText = new Text(r.getDatePosted().toString());
        Text commentText = new Text(r.getComment());

        // Assemble the card components
        card.getChildren().addAll(ratingText, usernameText, dateText, commentText);
        return card;
    }

    /**
     * Navigates the user back to the main dashboard view.
     *
     * @param event The action event triggered by clicking the "Back" button.
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

    /**
     * Opens a modal dialog allowing the user to submit a new review for the current
     * game.
     *
     * @param event The action event triggered by clicking the "Leave review"
     *              button.
     */
    @FXML
    private void leaveReview(ActionEvent event) {
        Stage dialogStage = new Stage();
        // Tie the dialog to the current window
        dialogStage.initOwner(buttonLeaveReview.getScene().getWindow());
        // Block interactions with the main window while open
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Leave a Review");

        // Define the layout container with padding and spacing
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Label headerLabel = new Label("Leave a review:");
        headerLabel.setWrapText(true);
        headerLabel.setFocusTraversable(true);

        // (TODO: Review-specific fields like rating and comment input should go here)
        // Currently repurposing wishlist logic as a placeholder structure

        List<String> listNames = new ArrayList<>();
        for (Wishlist w : Session.getInstance().getWishlists()) {
            listNames.add(w.getName());
        }

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(listNames);
        comboBox.setValue(listNames.get(0));
        comboBox.setAccessibleText("Select a wishlist to add your game to");

        TextField newListNameField = new TextField();
        newListNameField.setPromptText("Choose rating...");
        newListNameField.setAccessibleText("Choose a rating number");

        newListNameField.setVisible(false);
        newListNameField.setManaged(false);

        // Container for Save/Cancel buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Save");
        saveButton.setDefaultButton(true);

        Button cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true);

        // (TODO: Add action handlers and assemble the final scene layout)
    }

    /**
     * Displays a custom modal dialog allowing the user to add the current game to
     * an existing
     * wishlist or create a new one. Bypasses native JavaFX dialogs for better
     * styling
     * and accessibility.
     *
     * @param game The Game object being added to the wishlist.
     */
    private void showCustomWishlistDialog(Game game) {
        Stage dialogStage = new Stage();
        dialogStage.initOwner(buttonAddToList.getScene().getWindow());
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Add to Wishlist");

        // Set up the vertical layout structure
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Label headerLabel = new Label("Add " + game.getTitle() + " to a list:");
        headerLabel.setWrapText(true);
        headerLabel.setFocusTraversable(true);

        // Populate the dropdown with existing wishlist names
        List<String> listNames = new ArrayList<>();
        for (Wishlist w : Session.getInstance().getWishlists()) {
            listNames.add(w.getName());
        }

        // Add the option to create an entirely new list
        String createNewOption = "➕ Create New List...";
        listNames.add(createNewOption);

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(listNames);
        comboBox.setValue(listNames.get(0));
        comboBox.setAccessibleText("Select a wishlist to add your game to");

        // Prepare a hidden text field for naming a newly created list
        TextField newListNameField = new TextField();
        newListNameField.setPromptText("Enter new list name...");
        newListNameField.setAccessibleText("Type the name of your new wishlist");

        newListNameField.setVisible(false);
        newListNameField.setManaged(false);

        // Reveal the text field dynamically if the user chooses to create a new list
        comboBox.setOnAction(e -> {
            boolean isNew = comboBox.getValue().equals(createNewOption);
            newListNameField.setVisible(isNew);
            newListNameField.setManaged(isNew); // managed=true allows it to take up physical space
            if (isNew) {
                javafx.application.Platform.runLater(newListNameField::requestFocus);
            }
        });

        // Set up the action buttons container
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Save");
        saveButton.setDefaultButton(true); // Triggers via the Enter key

        Button cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true); // Triggers via the Escape key

        // Logic for saving the game to the selected wishlist
        saveButton.setOnAction(e -> {
            String selection = comboBox.getValue();

            if (selection.equals(createNewOption)) {
                // Logic for creating a new list
                String newListName = newListNameField.getText().trim();

                if (!newListName.isEmpty()) {
                    Wishlist existingList = Session.getInstance().getWishlistByName(newListName);
                    if (existingList == null) {
                        // Create the new list and add the game if no list with the same name exists
                        Wishlist newList = new Wishlist(newListName);
                        newList.add(game);
                        Session.getInstance().getWishlists().add(newList);
                    } else {
                        // Check for duplicates before adding
                        if (existingList.getGames().stream().noneMatch(g -> g.getTitle().equals(game.getTitle()))) {
                            existingList.add(game);
                        }
                    }
                }
            } else {
                // Logic for adding to an already existing list
                Wishlist chosenList = Session.getInstance().getWishlistByName(selection);
                // Prevent duplicate additions to the same list
                if (chosenList.getGames().stream().noneMatch(g -> g.getTitle().equals(game.getTitle()))) {
                    chosenList.add(game);
                }
            }
            dialogStage.close();
        });

        // Logic for dismissing the dialog without saving
        cancelButton.setOnAction(e -> dialogStage.close());

        // Assemble the layout components
        buttonBox.getChildren().addAll(saveButton, cancelButton);
        layout.getChildren().addAll(headerLabel, comboBox, newListNameField, buttonBox);

        // Finalize the Scene and link it to the dialog stage
        Scene scene = new Scene(layout, 350, 250);
        dialogStage.setScene(scene);

        // Apply global user settings
        Session.getInstance().applyGlobalSettings(scene);

        // Force screen reader to announce the dropdown immediately when opened
        javafx.application.Platform.runLater(comboBox::requestFocus);

        dialogStage.showAndWait();
    }
}