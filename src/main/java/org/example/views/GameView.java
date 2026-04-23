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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.org.example.model.Game;
import main.java.org.example.model.Session;
import main.java.org.example.model.Wishlist;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameView implements Initializable {
    
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
    private Button buttonBack;
    @FXML
    private Button buttonAddToList;
    @FXML
    private Button buttonLeaveReview;

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
        if (minPlayers.equals(maxPlayers)) {
            textPlayerAmount.setText(minPlayers);
        } else {
            String playersString = minPlayers + " - " + maxPlayers;
            textPlayerAmount.setText(playersString);
        }
        
        // setting duration of playtime
        String playtimeString = Integer.toString(g.getPlayTimeMinutes());
        textPlaytime.setText(playtimeString + " minutes");

        // --- NEW: Triggers the custom dialog window ---
        buttonAddToList.setOnAction(e -> {
            showCustomWishlistDialog(g);
        });
    }

    @FXML
    private void handleBackToMainMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/mainView.fxml"));
        stage.getScene().setRoot(root);
        Session.getInstance().applyGlobalSettings(stage.getScene());
        stage.show();
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
        for (Wishlist w : Session.getInstance().getWishlists()) {
            listNames.add(w.getName());
        }
        
        // Even if they have no lists, they can still create one!
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
        
        // This guarantees High Contrast Mode works perfectly!
        Session.getInstance().applyGlobalSettings(scene);

        // Force VoiceOver to announce the dropdown
        javafx.application.Platform.runLater(comboBox::requestFocus);

        dialogStage.showAndWait();
    }
}