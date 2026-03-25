import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GameParser 
{
    /**
     * Parses an XML file containing board game data into a List of Game objects.
     * * @param inputStream The input stream of the XML file (e.g., from resources)
     * @return A list of Game objects parsed from the XML
     */
    public static List<Game> parseGames(InputStream inputStream) 
    {
        List<Game> gamesList = new ArrayList<>();

        try 
        {
            // Set up the standard Java XML Document Builder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            
            // Normalize the XML structure (good practice for standardizing text formatting)
            document.getDocumentElement().normalize();

            // Get all <item> tags from the XML
            NodeList nodeList = document.getElementsByTagName("item");

            // Loop through every <item> found
            for (int i = 0; i < nodeList.getLength(); i++) 
            {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) 
                {
                    Element element = (Element) node;

                    // 1. Extract the ID from the <item id="..."> attribute
                    String gameId = element.getAttribute("id");

                    // 2. Extract the Title from the <name value="..."> tag
                    String title = "Unknown Title";
                    NodeList nameList = element.getElementsByTagName("name");
                    if (nameList.getLength() > 0) 
                    {
                        Element nameElement = (Element) nameList.item(0);
                        title = nameElement.getAttribute("value");
                    }

                    // 3. Create the Game object. 
                    // NOTE: Passing 0 for min/max/time because the XML doesn't provide them yet.
                    Game game = new Game(title, 0, 0, 0);
                    
                    // NOTE: Your Game class currently lacks a setGameID() method, 
                    // so we cannot assign the gameId we just extracted.
                    // game.setGameID(gameId); // Uncomment this once the setter is added to Game.java

                    gamesList.add(game);
                }
            }
        } 
        catch (Exception e) 
        {
            System.err.println("Error parsing the XML file: " + e.getMessage());
            e.printStackTrace();
        }

        return gamesList;
    }
}