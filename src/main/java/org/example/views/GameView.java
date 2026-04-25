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

public class GameView implements Initializable {
    // info about the game
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
    // UI controls
    @FXML
    private Button buttonBack;
    @FXML
    private Button buttonAddToList;
    @FXML
    private Button buttonLeaveReview;

    /**
     * Loads all the selected game's data
     * Formats the game's description and player count
     * @param url
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param rb
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Session currentSession = Session.getInstance();
        Game g = currentSession.getCurrentGame(); // gets the selected game from session

        labelGameTitle.setText(g.getTitle()); // sets title
        
        if (g.getImageUrl() != null && !g.getImageUrl().isEmpty()) { 
            Image img = new Image(g.getImageUrl(), true);
            imageGamePicture.setImage(img);
            // VoiceOver tag for the giant image!
            imageGamePicture.setAccessibleText("Cover art for " + g.getTitle());
        }

        // setting description text
        textDescription.setText(g.getDescription());
        
        // Dynamic Text Wrapping for the description
        textDescription.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                textDescription.wrappingWidthProperty().bind(newScene.widthProperty().subtract(60));
            }
        });

        // setting amt of players text
        String minPlayers = Integer.toString(g.getMinPlayers());
        String maxPlayers = Integer.toString(g.getMaxPlayers());
        // if block to format player count correctly
        if (minPlayers.equals(maxPlayers)) {
            if (minPlayers == "1") {
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
        
        // setting duration of playtime
        String playtimeString = Integer.toString(g.getPlayTimeMinutes());
        textPlaytime.setText(playtimeString + " minutes");

        loadReviews(g.getReviews()); // setting up reviews

        // initializes button to open list menu
        buttonAddToList.setOnAction(e -> {
            showCustomWishlistDialog(g);
        });
    }

    /**
     * Fills in the review section with player reviews
     * @param reviews A user's written review
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
     * Formats how a player review will fit into the reviews section
     * @param r Player review
     * @return Returns the formatted review
     */
    private VBox createReviewCard(Review r) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("review-card");
        card.setStyle("-fx-cursor: hand;");
        card.setMaxWidth(300);
        card.setCache(true);
        card.setCacheHint(javafx.scene.CacheHint.SPEED);

        // setting up review text
        String ratingString = String.valueOf(r.getRating());
        Text ratingText = new Text(ratingString);
        Text usernameText = new Text(r.getAuthor());
        Text dateText = new Text(r.getDatePosted().toString());
        Text commentText = new Text(r.getComment());
        card.getChildren().addAll(ratingText, usernameText, dateText, commentText);
        return card;
    }

    /**
     * Allows the user to enter the MainView from the GameView
     * @param event User clicks "Back" button
     * @throws IOException Input/Output exception
     */
    @FXML
    private void handleBackToMainMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/mainView.fxml"));
        stage.getScene().setRoot(root);
        Session.getInstance().applyGlobalSettings(stage.getScene());
        stage.show();
    }

    /**
     * Allows user to write a review for the selected game
     * @param event User clicks "Leave review" button
     */
    @FXML
    private void leaveReview(ActionEvent event) {
        Stage dialogStage = new Stage();
        dialogStage.initOwner(buttonLeaveReview.getScene().getWindow()); // ties back to button
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Leave a Review");

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Label headerLabel = new Label("Leave a review:");
        headerLabel.setWrapText(true);
        headerLabel.setFocusTraversable(true);

        // 2. Build the dropdown options
        List<String> listNames = new ArrayList<>();
        for (Wishlist w : Session.getInstance().getWishlists()) { // Add existing lists
            listNames.add(w.getName());
        }

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(listNames);
        comboBox.setValue(listNames.get(0));
        comboBox.setAccessibleText("Select a wishlist to add your game to");

        // 3. Build the hidden text field for new lists
        TextField newListNameField = new TextField();
        newListNameField.setPromptText("Choose rating...");
        newListNameField.setAccessibleText("Choose a rating number");

        newListNameField.setVisible(false);
        newListNameField.setManaged(false);

        /*
        comboBox.setOnAction(e -> {
            boolean isNew = comboBox.getValue().equals(createNewOption);
            newListNameField.setVisible(isNew);
            newListNameField.setManaged(isNew);
            if (isNew) {
                javafx.application.Platform.runLater(newListNameField::requestFocus);
            }
        }); */

        // 4. Build the buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Save");
        saveButton.setDefaultButton(true);

        Button cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true);
    }

    /**
     * Creates a custom, highly accessible Stage for adding games to wishlists,
     * mimicking the exact behavior we built for the MainView.
     */
    private void showCustomWishlistDialog(Game game) {
        Stage dialogStage = new Stage();
        dialogStage.initOwner(buttonAddToList.getScene().getWindow());
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Add to Wishlist");

        // 1. Set up the layout
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Label headerLabel = new Label("Add " + game.getTitle() + " to a list:");
        headerLabel.setWrapText(true);
        headerLabel.setFocusTraversable(true);

        // 2. Build the dropdown options
        List<String> listNames = new ArrayList<>();
        for (Wishlist w : Session.getInstance().getWishlists()) { // Add existing lists
            listNames.add(w.getName());
        }
        
        // Option to create new list
        String createNewOption = "➕ Create New List...";
        listNames.add(createNewOption);

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(listNames);
        comboBox.setValue(listNames.get(0));
        comboBox.setAccessibleText("Select a wishlist to add your game to");

        // 3. Build the hidden text field for new lists
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

        // 4. Build the buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Save");
        saveButton.setDefaultButton(true); 
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true); 

        // --- SAVE LOGIC ---
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
                }
            } else {
                Wishlist chosenList = Session.getInstance().getWishlistByName(selection);
                if (chosenList.getGames().stream().noneMatch(g -> g.getTitle().equals(game.getTitle()))) {
                    chosenList.add(game);
                }
            }
            dialogStage.close();
        });

        // --- CANCEL LOGIC ---
        cancelButton.setOnAction(e -> dialogStage.close());

        buttonBox.getChildren().addAll(saveButton, cancelButton);
        layout.getChildren().addAll(headerLabel, comboBox, newListNameField, buttonBox);

        // 5. Finalize the Scene and apply themes
        Scene scene = new Scene(layout, 350, 250);
        dialogStage.setScene(scene);
        
        // Apply global settings
        Session.getInstance().applyGlobalSettings(scene);

        // Force VoiceOver to announce the dropdown
        javafx.application.Platform.runLater(comboBox::requestFocus);

        dialogStage.showAndWait();
    }
}