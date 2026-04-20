package main.java.org.example.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.org.example.model.Game;
import main.java.org.example.model.Session;

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

    @FXML
    //@Override
    public void initialize(URL url, ResourceBundle rb)
    {
        System.out.println("gameview trying to initialize");  // debug statement, delete later
        // receiving current game from MainView using Session
        Session currentSession = Session.getInstance();
        Game g = currentSession.getCurrentGame();
        System.out.println("gameview loaded" + g.getTitle()); // debug statement, delete later

        // now we can load game info
        labelGameTitle.setText(g.getTitle());
        Image img = new Image(g.getImageUrl());
        imageGamePicture.setImage(img); //HAVE GAME CLASS RETURN IMAGE
        textDescription.setText(g.getDescription());
        // textReview.setText(game.) MAKE THIS A LOOP TO LOAD ALL REVIEWS
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
