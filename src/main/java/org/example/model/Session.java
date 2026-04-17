package main.java.org.example.model;

import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class keeps track of the current User and whether the User is logged in.
 * It ensures settings are shared globally across the app.
 */
public class Session {
    private static Session instance;

    private User currentUser;
    private boolean highContrast;
    private int textSize;
    private boolean loggedIn;
    private ArrayList<Wishlist> currentWishlists;

    /**
     * Private constructor prevents other classes from making duplicate sessions.
     */
    private Session() 
    {
        loggedIn = false;
        textSize = 12;
        highContrast = false;
        // currentWishlists = new ArrayList<Wishlist>();
        // myWishlist = new Wishlist(myWishlist);
        // currentWishlists.add(m)
    }

    /**
     * The global access point. All controllers use this to get the Session.
     * 
     * @return The single Session instance
     */
    public static Session getInstance() 
    {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public int getTextSize() 
    {
        return textSize;
    }

    public boolean getContrast() 
    {
        return highContrast;
    }

    public void setTextSize(int textSize) 
    {
        this.textSize = textSize;
    }

    public void setContrast(boolean highContrast) 
    {
        this.highContrast = highContrast;
    }

    public void logIn(User user) 
    {
        currentUser = user;
        loggedIn = true;
        // If guest, keep settings from log in page
        // If user, change settings to saved
    }

    public void logOut() 
    {
        loggedIn = false;
        currentUser = null;
    }

    /**
     * Applies the high contrast CSS to a given scene.
     * 
     * @param scene The scene to apply the styling to
     */
    public void applyTheme(Scene scene) 
    {
        if (scene == null || scene.getRoot() == null) {
            return;
        }

        Parent root = scene.getRoot();
        String cssUrl = Objects.requireNonNull(getClass().getResource("/org.openjfx/highContrast.css"))
                .toExternalForm();

        // Remove it first to prevent accidentally adding duplicates
        root.getStylesheets().remove(cssUrl);

        // Add it back only if the user has it enabled in this session
        if (highContrast) {
            root.getStylesheets().add(cssUrl);
        }
    }

   /**
     * Applies the dynamic font size to any loaded FXML root.
     * @param root The root node of the screen
     */
    public void applyTextSize(Scene scene) {
        if (scene == null || scene.getRoot() == null) {
            return;
        }
        // Apply the size directly to the newly loaded root
        scene.getRoot().setStyle("-fx-font-size: " + textSize + "px;");
    }

    public void applyGlobalSettings(Scene scene)
    {
        applyTheme(scene);
        applyTextSize(scene);
    }
}