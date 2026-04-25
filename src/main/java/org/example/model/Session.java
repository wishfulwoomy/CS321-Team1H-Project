package main.java.org.example.model;

import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Singleton class that manages the global state of the application.
 * Handles the currently logged-in user, global UI preferences (like text size
 * and contrast),
 * the currently viewed game, and the saving/loading of user wishlists to an XML
 * database.
 */
public class Session {
    // Single static instance of the class (Singleton pattern)
    private static Session instance;

    // User & State Data
    private User currentUser;
    private boolean loggedIn;
    private ArrayList<Wishlist> currentWishlists;
    private Game currentGame;

    // UI Preferences
    private boolean highContrast;
    private int textSize;

    // Database Configuration
    private final String FILE_PATH = "src/main/resources/userWishlists.xml";

    /**
     * Private constructor for the Session class.
     * Initializes default application settings and establishes a logged-out, guest
     * state.
     * Prevents other classes from instantiating their own separate Session objects.
     */
    private Session() {
        // Default UI and login states
        loggedIn = false;
        textSize = 12;
        highContrast = false;

        // Guests start with a clean slate and a default wishlist
        currentWishlists = new ArrayList<>();
        currentWishlists.add(new Wishlist("Favorites"));

        // Add a background hook to automatically save user data when the application is
        // closed
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Only execute the save function if an actual user is logged in
            if (loggedIn) {
                saveToXML();
            }
        }));
    }

    /**
     * Retrieves the single, global instance of the Session.
     * If the session does not exist yet, it creates it.
     *
     * @return The active Session instance.
     */
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    /**
     * Checks if a user is currently authenticated and logged into the application.
     *
     * @return True if a user is logged in, false if operating as a guest.
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Retrieves the current global text size preference.
     *
     * @return The integer font size (in pixels).
     */
    public int getTextSize() {
        return textSize;
    }

    /**
     * Checks if the global High Contrast mode is active.
     *
     * @return True if high contrast is active, false otherwise.
     */
    public boolean getContrast() {
        return highContrast;
    }

    /**
     * Updates the global text size preference.
     *
     * @param textSize The new integer font size to apply.
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * Toggles the global High Contrast preference.
     *
     * @param highContrast The boolean state representing whether the mode should be
     *                     on or off.
     */
    public void setContrast(boolean highContrast) {
        this.highContrast = highContrast;
    }

    /**
     * Authenticates a user into the session and loads their saved data from the XML
     * database.
     *
     * @param user The User object representing the person logging in.
     */
    public void logIn(User user) {
        currentUser = user;
        loggedIn = true;

        // Load the XML data only when an actual account is authenticated
        currentWishlists = loadFromXML();
    }

    /**
     * Safely logs out the current user, saves their data to the XML database,
     * and resets the session back to a clean guest state.
     */
    public void logOut() {
        // Save the user's wishlists to disk before wiping the data from active memory
        if (loggedIn) {
            saveToXML();
        }

        // Clear authentication status
        loggedIn = false;
        currentUser = null;

        // Reset the active data back to a clean slate for the next login or guest user
        currentWishlists = new ArrayList<>();
        currentWishlists.add(new Wishlist("Favorites"));
    }

    /**
     * Stores a reference to the specific game the user is currently interacting
     * with.
     * Used primarily to pass data between the Main View and the detailed Game View.
     *
     * @param g The Game object currently selected.
     */
    public void setCurrentGame(Game g) {
        this.currentGame = g;
    }

    /**
     * Retrieves the game the user most recently selected or interacted with.
     *
     * @return The active Game object.
     */
    public Game getCurrentGame() {
        return this.currentGame;
    }

    /**
     * Retrieves the full list of wishlists belonging to the current user or guest.
     *
     * @return An ArrayList containing all active Wishlist objects.
     */
    public ArrayList<Wishlist> getWishlists() {
        return currentWishlists;
    }

    /**
     * Searches the active session for a wishlist matching a specific name.
     *
     * @param name The exact string name of the desired wishlist.
     * @return The matching Wishlist object, or null if no match is found.
     */
    public Wishlist getWishlistByName(String name) {
        for (Wishlist w : currentWishlists) {
            if (w.getName().equals(name)) {
                return w;
            }
        }
        return null; // Not found
    }

    /**
     * Applies the appropriate CSS stylesheet to a given JavaFX scene based on the
     * user's current High Contrast preference.
     *
     * @param scene The active JavaFX scene to style.
     */
    public void applyTheme(Scene scene) {
        if (scene == null || scene.getRoot() == null) {
            return;
        }

        Parent root = scene.getRoot();

        // Locate the external high contrast CSS file
        String cssUrl = Objects.requireNonNull(getClass().getResource("/org.openjfx/highContrast.css"))
                .toExternalForm();

        // Always remove the stylesheet first to prevent duplicate stacking errors
        root.getStylesheets().remove(cssUrl);

        // If the user has toggled the setting on, inject the CSS back into the scene
        if (highContrast) {
            root.getStylesheets().add(cssUrl);
        }
    }

    /**
     * Dynamically injects an inline CSS style to alter the base font size
     * of all elements within a given JavaFX scene.
     *
     * @param scene The active JavaFX scene to resize.
     */
    public void applyTextSize(Scene scene) {
        if (scene == null || scene.getRoot() == null) {
            return;
        }
        // Apply the integer preference as a direct CSS pixel style rule
        scene.getRoot().setStyle("-fx-font-size: " + textSize + "px;");
    }

    /**
     * A utility wrapper method that applies all global visual preferences
     * (both CSS themes and text size scaling) to a newly loaded scene at once.
     *
     * @param scene The JavaFX scene to configure.
     */
    public void applyGlobalSettings(Scene scene) {
        applyTheme(scene);
        applyTextSize(scene);
    }

    /**
     * Serializes the user's current wishlists and their associated games
     * into an XML document and saves it to the local file system.
     */
    private void saveToXML() {
        try {
            // Initialize the DOM document builder
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Create the master root element
            Element rootElement = doc.createElement("UserData");
            doc.appendChild(rootElement);

            // Iterate over every list in active memory
            for (Wishlist w : currentWishlists) {
                // Create an XML tag for the list and attach its name as an attribute
                Element listElement = doc.createElement("Wishlist");
                String wName = w.getName() != null ? w.getName() : "Unnamed List";
                listElement.setAttribute("name", wName);
                rootElement.appendChild(listElement);

                // Iterate over every game inside that list
                for (Game g : w.getGames()) {
                    // Create an XML tag for the game and attach its title as an attribute
                    Element gameElement = doc.createElement("Game");
                    gameElement.setAttribute("title", g.getTitle());
                    listElement.appendChild(gameElement);
                }
            }

            // Prepare the transformer to write the DOM object to an actual file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // Format the XML with nice indentation for readability
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // Define the source (the DOM) and the target destination (the file path)
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(FILE_PATH));

            // Execute the save operation
            transformer.transform(source, result);
            System.out.println("Wishlists successfully saved to XML!");

        } catch (Exception e) {
            System.out.println("Error saving XML: " + e.getMessage());
        }
    }

    /**
     * Reads the local XML database, reconstructs the user's saved wishlists and
     * games,
     * and loads them into active memory.
     *
     * @return An ArrayList containing the parsed Wishlist objects.
     */
    private ArrayList<Wishlist> loadFromXML() {
        ArrayList<Wishlist> loadedLists = new ArrayList<>();
        File xmlFile = new File(FILE_PATH);

        // Fallback: If no file exists yet, give the user a default starting list
        if (!xmlFile.exists()) {
            System.out.println("No saved data found. Starting fresh!");
            loadedLists.add(new Wishlist("Favorites"));
            return loadedLists;
        }

        try {
            // Initialize the DOM parser and load the file
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            // Normalize the document to handle weird whitespace or formatting
            doc.getDocumentElement().normalize();

            // Extract every Wishlist tag
            NodeList wishlistNodes = doc.getElementsByTagName("Wishlist");

            for (int i = 0; i < wishlistNodes.getLength(); i++) {
                Element listElement = (Element) wishlistNodes.item(i);

                // Reconstruct the wishlist object using its stored name attribute
                String listName = listElement.getAttribute("name");
                Wishlist newList = new Wishlist(listName);

                // Extract every Game tag nested inside this specific Wishlist tag
                NodeList gameNodes = listElement.getElementsByTagName("Game");
                for (int j = 0; j < gameNodes.getLength(); j++) {
                    Element gameElement = (Element) gameNodes.item(j);

                    // Reconstruct a lightweight Game object using just the title
                    String gameTitle = gameElement.getAttribute("title");
                    Game loadedGame = new Game(gameTitle);

                    newList.add(loadedGame);
                }

                // Add the fully populated list to our array
                loadedLists.add(newList);
            }
            System.out.println("Wishlists successfully loaded from XML!");
            return loadedLists;

        } catch (Exception e) {
            // If the file is corrupted, print the error and provide a safe fallback list
            System.out.println("Error loading XML: " + e.getMessage());
            loadedLists.add(new Wishlist("Favorites"));
            return loadedLists;
        }
    }
}