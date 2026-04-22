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
    private Game currentGame;
    private final String FILE_PATH = "src/main/resources/userWishlists.xml";

    private Session() 
    {
        loggedIn = false;
        textSize = 12;
        highContrast = false;
        
        currentWishlists = loadFromXML();

        Runtime.getRuntime().addShutdownHook(new Thread(this::saveToXML));
    }

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
    }

    public void logOut() 
    {
        saveToXML();
        loggedIn = false;
        currentUser = null;
    }

    public void setCurrentGame(Game g) { this.currentGame = g; }

    public Game getCurrentGame() { return this.currentGame; }

    public ArrayList<Wishlist> getWishlists() {
        return currentWishlists;
    }

    public Wishlist getWishlistByName(String name) {
        for (Wishlist w : currentWishlists) {
            if (w.getName().equals(name)) {
                return w;
            }
        }
        return null;
    }

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

    public void applyTextSize(Scene scene) {
        if (scene == null || scene.getRoot() == null) {
            return;
        }
        scene.getRoot().setStyle("-fx-font-size: " + textSize + "px;");
    }

    public void applyGlobalSettings(Scene scene)
    {
        applyTheme(scene);
        applyTextSize(scene);
    }

    private void saveToXML() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

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

        } catch (Exception e) {
            System.out.println("Error saving XML: " + e.getMessage());
        }
    }

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