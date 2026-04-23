package main.java.org.example.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.org.example.model.Game;
import main.java.org.example.model.Session;
import main.java.org.example.model.Wishlist;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        }

        textDescription.setText(g.getDescription());
        System.out.println(g.getDescription());

        textDescription.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                textDescription.wrappingWidthProperty().bind(newScene.widthProperty().subtract(60));
            }
        });

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
        // Added a space before 'minutes' so it reads correctly
        textPlaytime.setText(playtimeString + " minutes");

        buttonAddToList.setOnAction(e -> {
            List<Wishlist> allLists = Session.getInstance().getWishlists();

            if (allLists.isEmpty()) {
                System.out.println("No wishlists exist! Please create one first.");
                return;
            }

            List<String> listNames = new ArrayList<>();
            for (Wishlist w : allLists) {
                listNames.add(w.getName());
            }

            ChoiceDialog<String> dialog = new ChoiceDialog<>(listNames.get(0), listNames);
            dialog.setTitle("Add to Wishlist");
            dialog.setHeaderText("Add " + g.getTitle() + " to a wishlist:");
            dialog.setContentText("Select list:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                Wishlist chosenList = Session.getInstance().getWishlistByName(result.get());
                if (!chosenList.getGames().contains(g)) {
                    chosenList.add(g);
                    System.out.println("Added " + g.getTitle() + " to " + result.get());
                }
            }
        });
    }

    @FXML
    private void handleBackToMainMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/mainView.fxml"));
        stage.getScene().setRoot(root);
        Session.getInstance().applyGlobalSettings(stage.getScene());
        stage.show();
    }
}