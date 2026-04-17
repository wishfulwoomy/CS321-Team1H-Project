package main.java.org.example.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.org.example.model.Game;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameView 
{
    // UI elements that display game info
    @FXML
    private ImageView imageGamePicture;
    @FXML
    private Label labelGameTitle;
    @FXML
    private Text textAverageRating;
    @FXML
    private Text textDescription;
    @FXML
    private Text textReview;

    // UI controls
    @FXML
    private Button buttonAddToList;
    @FXML
    private Button buttonLeaveReview;

    // the game itself
    private Game game;

    @FXML
    //@Override
    public void initialize(URL url, ResourceBundle rb)
    {

        labelGameTitle.setText(game.getTitle());
        // imageGamePicture.setImage(game.); HAVE GAME CLASS RETURN IMAGE
        textDescription.setText(game.getDescription());
        // textReview.setText(game.) MAKE THIS A LOOP TO LOAD ALL REVIEWS
    }

    public void setGame(Game game) { // WORK ON LATER
        this.game = game;
    }

    @FXML
    private void handleBackToMainMenu(MouseEvent event) throws IOException
    {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/mainView.fxml"));
        stage.getScene().setRoot(root);
        stage.show();
    }
}
