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

public class GameParser 
{

    /**
     * Parses an XML file containing board game data into a List of main.java.org.example.model.Game objects.
     * @param inputStream The input stream of the XML file
     * @return A list of main.java.org.example.model.Game objects parsed from the XML
     */
    public static List<Game> parseGames(InputStream inputStream) 
    {
        List<Game> gamesList = new ArrayList<>();

        try 
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("item");

            for (int i = 0; i < nodeList.getLength(); i++) 
            {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) 
                {
                    Element element = (Element) node;

                    // 1. Extract the ID 
                    String gameId = element.getAttribute("id");

                    // 2. Extract the Title 
                    String title = "Unknown Title";
                    NodeList nameList = element.getElementsByTagName("name");
                    if (nameList.getLength() > 0) 
                    {
                        Element nameElement = (Element) nameList.item(0);
                        title = nameElement.getAttribute("value");
                    }

                    // 3. Extract Min Players
                    int minPlayers = 0;
                    NodeList minList = element.getElementsByTagName("minplayers");
                    if (minList.getLength() > 0) 
                    {
                        Element minElement = (Element) minList.item(0);
                        String minString = minElement.getAttribute("value");
                        if (!minString.isEmpty()) 
                        {
                            minPlayers = Integer.parseInt(minString);
                        }
                    }

                    // 4. Extract Max Players
                    int maxPlayers = 0;
                    NodeList maxList = element.getElementsByTagName("maxplayers");
                    if (maxList.getLength() > 0) 
                    {
                        Element maxElement = (Element) maxList.item(0);
                        String maxString = maxElement.getAttribute("value");
                        if (!maxString.isEmpty()) 
                        {
                            maxPlayers = Integer.parseInt(maxString);
                        }
                    }

                    // 5. Extract Play Time
                    int playTime = 0;
                    NodeList timeList = element.getElementsByTagName("playingtime");
                    if (timeList.getLength() > 0) 
                    {
                        Element timeElement = (Element) timeList.item(0);
                        String timeString = timeElement.getAttribute("value");
                        if (!timeString.isEmpty()) 
                        {
                            playTime = Integer.parseInt(timeString);
                        }
                    }

                    // 5.5 Extract Image URL
                    String imageURL = "";
                    NodeList imageList = element.getElementsByTagName("image");
                    if (imageList.getLength() > 0)
                    {
                        Element imageElement = (Element) imageList.item(0);
                        imageURL = imageElement.getTextContent();
                    }

                    // 6. Create the main.java.org.example.model.Game object using the real data!
                    Game game = new Game(title, minPlayers, maxPlayers, playTime);

                    game.setImageUrl(imageURL);
                    
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