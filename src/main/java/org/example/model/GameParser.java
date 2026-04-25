package main.java.org.example.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class responsible for parsing board game data from an XML database.
 * Uses Java's built-in DOM (Document Object Model) parser to read the file,
 * extract specific tags (like title, player counts, and descriptions),
 * and map them directly into Game objects.
 */
public class GameParser {

    // Stores a static cache of the parsed games so the application doesn't have
    // to re-read the massive XML file every time it needs the master list.
    private static List<Game> fullGamesList;

    /**
     * Parses an XML file containing board game data into a List of Game objects.
     * 
     * @param inputStream The input stream of the XML database file.
     * @return A fully populated list of Game objects extracted from the XML.
     */
    public static List<Game> parseGames(InputStream inputStream) {
        List<Game> gamesList = new ArrayList<>();

        try {
            // Set up the DOM parser factory and builder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Load the XML file into a readable Document object
            Document document = builder.parse(inputStream);

            // Normalize the XML structure to prevent weird text-node formatting issues
            document.getDocumentElement().normalize();

            // Grab every individual game entry (BoardGameGeek uses the <item> tag for
            // games)
            NodeList nodeList = document.getElementsByTagName("item");

            // Loop through every game in the database
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                // Ensure the node is actually an element containing data, not just whitespace
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // EXTRACT GAME DATA

                    // Extract the primary game ID (stored as an attribute on the <item> tag itself)
                    String gameId = element.getAttribute("id");

                    // Extract the Title
                    String title = "Unknown Title";
                    NodeList nameList = element.getElementsByTagName("name");
                    if (nameList.getLength() > 0) {
                        Element nameElement = (Element) nameList.item(0);
                        // BoardGameGeek stores the title in a 'value' attribute, not as text inside the
                        // tag
                        title = nameElement.getAttribute("value");
                    }

                    // Extract the minimum player count
                    int minPlayers = 0;
                    NodeList minList = element.getElementsByTagName("minplayers");
                    if (minList.getLength() > 0) {
                        Element minElement = (Element) minList.item(0);
                        String minString = minElement.getAttribute("value");
                        if (!minString.isEmpty()) {
                            minPlayers = Integer.parseInt(minString);
                        }
                    }

                    // Extract the maximum player count
                    int maxPlayers = 0;
                    NodeList maxList = element.getElementsByTagName("maxplayers");
                    if (maxList.getLength() > 0) {
                        Element maxElement = (Element) maxList.item(0);
                        String maxString = maxElement.getAttribute("value");
                        if (!maxString.isEmpty()) {
                            maxPlayers = Integer.parseInt(maxString);
                        }
                    }

                    // Extract the estimated play time
                    int playTime = 0;
                    NodeList timeList = element.getElementsByTagName("playingtime");
                    if (timeList.getLength() > 0) {
                        Element timeElement = (Element) timeList.item(0);
                        String timeString = timeElement.getAttribute("value");
                        if (!timeString.isEmpty()) {
                            playTime = Integer.parseInt(timeString);
                        }
                    }

                    // Extract the cover art image URL
                    String imageURL = "";
                    NodeList imageList = element.getElementsByTagName("image");
                    if (imageList.getLength() > 0) {
                        Element imageElement = (Element) imageList.item(0);
                        // Unlike the other stats, the URL is stored as direct text inside the tag
                        imageURL = imageElement.getTextContent();
                    }

                    // Extract the text description
                    String description = "No description available.";
                    NodeList descList = element.getElementsByTagName("description");
                    if (descList.getLength() > 0) {
                        Element descElement = (Element) descList.item(0);

                        // Strip out any invisible formatting or whitespace
                        String extractedText = descElement.getTextContent().trim();

                        // Only override the default message if the tag actually had words in it
                        if (!extractedText.isEmpty()) {
                            description = extractedText.replace("&#10;", "\n");
                        }
                    }

                    // CONSTRUCT THE GAME OBJECT

                    // Create the base game using the required constructor arguments
                    Game game = new Game(title, minPlayers, maxPlayers, playTime);

                    // Inject the supplemental data using setters
                    game.setImageUrl(imageURL);
                    game.setDescription(description);
                    game.setGameID(gameId);

                    // Add the fully constructed game to our running list
                    gamesList.add(game);
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing the XML file: " + e.getMessage());
            e.printStackTrace();
        }

        // Cache the parsed list in memory for instant retrieval later
        fullGamesList = gamesList;
        return gamesList;
    }

    /**
     * Retrieves the statically cached list of fully parsed games.
     * Prevents the application from having to run the expensive parseGames() method
     * multiple times during a single session.
     * 
     * @return The cached list of Game objects.
     */
    public static List<Game> getGamesList() {
        return fullGamesList;
    }
}