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

public class WishlistView implements Initializable {

    @FXML
    private ListView<String> listNames;

    @FXML
    private ListView<String> listGames;

    @FXML
    public Label selectWishlist;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (Wishlist w : Session.getInstance().getWishlists()) {
            listNames.getItems().add(w.getName());
        }

        listNames.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            loadGamesForList(newValue);
        });

        listGames.setOnMouseClicked(this::handleGameDoubleClick);
    }

    private void loadGamesForList(String listName) {
        listGames.getItems().clear();
        if (listName == null)
            return;

        Wishlist selectedList = Session.getInstance().getWishlistByName(listName);
        if (selectedList != null) {
            for (Game g : selectedList.getGames()) {
                listGames.getItems().add(g.getTitle());
            }
        }
    }

    private void handleGameDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            String selectedGameTitle = listGames.getSelectionModel().getSelectedItem();

            if (selectedGameTitle != null) {
                Game selectedGame = null;
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

                if (selectedGame != null) {
                    try {
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/gameView.fxml"));

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

    @FXML
    private void handleCreateList(ActionEvent event) {
        Stage dialogStage = new Stage();
        dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());
        dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Create Wishlist");

        VBox layout = new VBox(15);
        layout.setAlignment(javafx.geometry.Pos.CENTER);
        layout.setPadding(new javafx.geometry.Insets(20));

        Label headerLabel = new Label("Create a new Wishlist:");
        headerLabel.setFocusTraversable(true);

        javafx.scene.control.TextField nameField = new javafx.scene.control.TextField();
        nameField.setPromptText("My New List");
        nameField.setAccessibleText("Enter the name of your new wishlist");

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

        Button createButton = new Button("Create");
        createButton.setDefaultButton(true);

        Button cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true);
        createButton.setOnAction(e -> {
            String newListName = nameField.getText().trim();
            if (!newListName.isEmpty()) {
                if (Session.getInstance().getWishlistByName(newListName) == null) {
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

        cancelButton.setOnAction(e -> dialogStage.close());

        buttonBox.getChildren().addAll(createButton, cancelButton);
        layout.getChildren().addAll(headerLabel, nameField, buttonBox);

        javafx.scene.Scene scene = new javafx.scene.Scene(layout, 300, 150);
        dialogStage.setScene(scene);

        Session.getInstance().applyGlobalSettings(scene);

        javafx.application.Platform.runLater(nameField::requestFocus);

        dialogStage.showAndWait();
    }

    @FXML
    private void handleDeleteList(ActionEvent event) {
        String selectedListName = listNames.getSelectionModel().getSelectedItem();

        if (selectedListName != null) {
            if (selectedListName.equals("Favorites")) {
                System.out.println("Cannot delete the default Favorites list.");
                return;
            }

            Wishlist listToRemove = Session.getInstance().getWishlistByName(selectedListName);
            Session.getInstance().getWishlists().remove(listToRemove);

            listNames.getItems().remove(selectedListName);
            listGames.getItems().clear();
        }
    }

    @FXML
    private void handleRemoveGame(ActionEvent event) {
        String selectedListName = listNames.getSelectionModel().getSelectedItem();
        String selectedGameTitle = listGames.getSelectionModel().getSelectedItem();

        if (selectedListName != null && selectedGameTitle != null) {
            Wishlist activeList = Session.getInstance().getWishlistByName(selectedListName);

            Game gameToRemove = null;
            for (Game g : activeList.getGames()) {
                if (g.getTitle().equals(selectedGameTitle)) {
                    gameToRemove = g;
                    break;
                }
            }
            if (gameToRemove != null) {
                activeList.remove(gameToRemove);
            }

            listGames.getItems().remove(selectedGameTitle);
        }
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
    private void handleBackToMainMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/org.openjfx/mainView.fxml"));
        stage.getScene().setRoot(root);
        Session.getInstance().applyGlobalSettings(stage.getScene());
        stage.show();
    }
}