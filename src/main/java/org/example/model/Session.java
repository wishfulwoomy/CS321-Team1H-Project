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

public class Session {
    private static Session instance;

    private User currentUser;
    private boolean highContrast;
    private int textSize;
    private boolean loggedIn;
    private ArrayList<Wishlist> currentWishlists;
    private ArrayList<Review> currentReviews;
    private Game currentGame;
    private final String FILE_PATH = "src/main/resources/userWishlists.xml";
    private final String REVIEWS_FILE_PATH = "src/main/resources/gameReviews.xml";

    /**
     * Class constructor that initializes default settings and a logged-out state
     */
    private Session() 
    {
        loggedIn = false;
        textSize = 12;
        highContrast = false;
        
        // Guests start with a clean slate
        currentWishlists = new ArrayList<>();
        currentWishlists.add(new Wishlist("Favorites"));
        currentReviews = new ArrayList<>();

        // Only save on shutdown if an actual user is logged in
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (loggedIn) {
                saveToXML();
            }
        }));
    }

    /**
     * Return the instance of the current session
     * If there is no instance, create one
     * @return the current instance
     */
    public static Session getInstance() 
    {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    /**
     * Returns true or false depending on if the user is logged in
     * @return True if the user is logged in, false if not
     */
    public boolean isLoggedIn() 
    {
        return loggedIn;
    }

    /**
     * Getter to return the currently logged-in user
     * @return The current User.
     */
    public User getUser()
    {
        return currentUser;
    }

    /**
     * Returns the current text size
     * @return the number size of the text
     */
    public int getTextSize() 
    {
        return textSize;
    }

    /**
     * Returns true or false depending on whether the user has activated high contrast settings
     * @return true if high contrast is active, false if not
     */
    public boolean getContrast() 
    {
        return highContrast;
    }

    /**
     * Change and set the text size
     * @param textSize The new size of the text
     */
    public void setTextSize(int textSize) 
    {
        this.textSize = textSize;
    }

    /**
     * Change the high contrast settings to on or off
     * @param highContrast boolean value of the high contrast settings
     */
    public void setContrast(boolean highContrast) 
    {
        this.highContrast = highContrast;
    }

    /**
     * Allow the user to log into their account
     * @param user The current user attempting to log in
     */
    public void logIn(User user) 
    {
        currentUser = user;
        loggedIn = true;
        // Load the XML data only when someone logs in
        currentWishlists = loadFromXML();
    }

    /**
     * Allow the user to log out by saving their data before resetting the session attributes
     */
    public void logOut() 
    {
        // Save before wiping memory if they were a real user
        if (loggedIn) {
            saveToXML();
        }
        
        loggedIn = false;
        currentUser = null;
        
        // Reset back to the clean slate for the next person/guest
        currentWishlists = new ArrayList<>();
        currentWishlists.add(new Wishlist("Favorites"));
        currentReviews = new ArrayList<>();
    }

    /**
     * Sets currentGame to the game the user has selected
     * @param g the selected game
     */
    public void setCurrentGame(Game g) { this.currentGame = g; }

    /**
     * Return the game the user has selected
     * @return the selected game
     */
    public Game getCurrentGame() { return this.currentGame; }

    /**
     * Returns the current user's wishlists
     * @return the user's wishlists
     */
    public ArrayList<Wishlist> getWishlists() {
        return currentWishlists;
    }

    /**
     * Returns the current list of reviews added this session
     * @return the current list of reviews
     */
    public ArrayList<Review> getReviews() {
        return currentReviews;
    }

    /**
     * Find the wishlist with the chosen name and return it
     * @param name The name of the desired wishlist
     * @return The wishlist that matches the given name
     */
    public Wishlist getWishlistByName(String name) {
        for (Wishlist w : currentWishlists) {
            if (w.getName().equals(name)) {
                return w;
            }
        }
        return null;
    }

    /**
     * Applies the current .css theme whenever a new scene loads; either default or high-contrast mode.
     * @param scene The scene to apply the theme to.
     */
    public void applyTheme(Scene scene) 
    {
        if (scene == null || scene.getRoot() == null) {
            return;
        }

        Parent root = scene.getRoot();
        String cssUrl = Objects.requireNonNull(getClass().getResource("/org.openjfx/highContrast.css"))
                .toExternalForm();

        root.getStylesheets().remove(cssUrl);

        if (highContrast) {
            root.getStylesheets().add(cssUrl);
        }
    }

    /**
     * Apply the text size to the screen
     * @param scene the current window
     */
    public void applyTextSize(Scene scene) {
        if (scene == null || scene.getRoot() == null) {
            return;
        }
        scene.getRoot().setStyle("-fx-font-size: " + textSize + "px;");
    }

    /**
     * Apply the current settings [both .css theme and text size] whenever a new scene loads.
     * @param scene The scene to apply the theme to.
     */
    public void applyGlobalSettings(Scene scene)
    {
        applyTheme(scene);
        applyTextSize(scene);
    }

    /**
     * Saves any changes the user made in a session to the .xml file
     */
    private void saveToXML() {
        try {
            // Create DocumentBuilder for both Wishlists and Reviews
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Saving Wishlists to XML file
            Document doc = docBuilder.newDocument(); // New document in memory

            Element rootElement = doc.createElement("UserData");
            doc.appendChild(rootElement);

            for (Wishlist w : currentWishlists) {
                Element listElement = doc.createElement("Wishlist");
                String wName = w.getName() != null ? w.getName() : "Unnamed List";
                listElement.setAttribute("name", wName);
                rootElement.appendChild(listElement);

                for (Game g : w.getGames()) {
                    Element gameElement = doc.createElement("Game");
                    gameElement.setAttribute("title", g.getTitle()); 
                    listElement.appendChild(gameElement);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(FILE_PATH));

            transformer.transform(source, result);
            System.out.println("Wishlists successfully saved to XML!");

            // Saving Reviews to XML file
            Document reviewDoc = docBuilder.newDocument(); // New document in memory

            Element reviewRootElement = reviewDoc.createElement("ReviewData");
            reviewDoc.appendChild(reviewRootElement);

            for (Review r : currentReviews) {
                Element revElement = reviewDoc.createElement("Review");
                reviewRootElement.appendChild(revElement);
                // Set up review info
                Element gameTitle = reviewDoc.createElement("Game");
                gameTitle.appendChild(reviewDoc.createTextNode(r.getGameTitle()));
                Element gameID = reviewDoc.createElement("GameID");
                gameID.appendChild(reviewDoc.createTextNode(Integer.toString(r.getGameID())));
                Element author = reviewDoc.createElement("Author");
                author.appendChild(reviewDoc.createTextNode(r.getAuthor()));
                Element authorID = reviewDoc.createElement("AuthorID");
                authorID.appendChild(reviewDoc.createTextNode(Integer.toString(r.getAuthorID())));
                Element rating = reviewDoc.createElement("Rating");
                rating.appendChild(reviewDoc.createTextNode(Integer.toString(r.getRating())));
                Element comment = reviewDoc.createElement("Comment");
                comment.appendChild(reviewDoc.createTextNode(r.getComment()));
                // Append review info to the review element
                revElement.appendChild(gameTitle);
                revElement.appendChild(gameID);
                revElement.appendChild(author);
                revElement.appendChild(authorID);
                revElement.appendChild(rating);
                revElement.appendChild(comment);
            }

            // Writing reviews to review XML
            DOMSource reviewSource = new DOMSource(reviewDoc);
            StreamResult reviewResult = new StreamResult(new File(REVIEWS_FILE_PATH));

            transformer.transform(reviewSource, reviewResult);
            System.out.println("Reviews successfully saved to XML!");

        } catch (Exception e) {
            System.out.println("Error saving XML: " + e.getMessage());
        }
    }

    /**
     * Loads the saved user data from the .xml file
     * @return
     */
    private ArrayList<Wishlist> loadFromXML() {
        ArrayList<Wishlist> loadedLists = new ArrayList<>();
        File xmlFile = new File(FILE_PATH);

        if (!xmlFile.exists()) {
            System.out.println("No saved data found. Starting fresh!");
            loadedLists.add(new Wishlist("Favorites"));
            return loadedLists;
        }

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList wishlistNodes = doc.getElementsByTagName("Wishlist");

            for (int i = 0; i < wishlistNodes.getLength(); i++) {
                Element listElement = (Element) wishlistNodes.item(i);
                String listName = listElement.getAttribute("name");
                Wishlist newList = new Wishlist(listName);

                NodeList gameNodes = listElement.getElementsByTagName("Game");
                for (int j = 0; j < gameNodes.getLength(); j++) {
                    Element gameElement = (Element) gameNodes.item(j);
                    String gameTitle = gameElement.getAttribute("title");
                    
                    Game loadedGame = new Game(gameTitle); 
                    newList.add(loadedGame);
                }
                loadedLists.add(newList);
            }
            System.out.println("Wishlists successfully loaded from XML!");
            return loadedLists;

        } catch (Exception e) {
            System.out.println("Error loading XML: " + e.getMessage());
            loadedLists.add(new Wishlist("Favorites"));
            return loadedLists;
        }
    }
}