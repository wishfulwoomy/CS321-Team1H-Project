package org.example.model;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UserParser {
    private ArrayList<User> userList;

    /**
     * Creates an array to store all user accounts
     */
    public UserParser(){
        userList = new ArrayList<>();
    }

    /**
     * Adds a user account to the database
     * @param newUser new account object
     */
    public void addUser(User newUser){
        userList.add(newUser);
    }

    /**
     * Parses the sampleUser.xml file to load users and their wishlists.
     * @param inputPath Path to the XML file (e.g., "src/main/resources/sampleUser.xml")
     */
    public void loadUsers(String inputPath){
        try {
            File inputFile = new File(inputPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            
            doc.getDocumentElement().normalize();

            // Fetch all <User> elements from the XML
            NodeList userNodes = doc.getElementsByTagName("User");

            for (int i = 0; i < userNodes.getLength(); i++) {
                Node userNode = userNodes.item(i);

                if (userNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element userElement = (Element) userNode;

                    // Extract user data
                    String name = getTagValue("name", userElement);
                    String userIDString = getTagValue("userID", userElement);
                    String password = getTagValue("password", userElement);
                    
                    // Convert the ID to an int
                    int userID = 0;
                    try {
                        userID = Integer.parseInt(userIDString);
                    } catch (NumberFormatException e) {
                        System.err.println("Warning: Invalid user ID format for user " + name + ". Defaulting to 0.");
                    }

                    User parsedUser = new User(name, userID, password);
                    
                    // Extract all Wishlists for this specific user
                    NodeList wishlistNodes = userElement.getElementsByTagName("Wishlist");
                    
                    for (int j = 0; j < wishlistNodes.getLength(); j++) {
                        Node wishNode = wishlistNodes.item(j);
                        if (wishNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element wishElement = (Element) wishNode;
                            
                            // Get the wishlist name
                            String wishlistName = getTagValue("name", wishElement);
                            
                            // Utilize the createWishlist method in the User class
                            parsedUser.createWishlist(wishlistName);
                        }
                    }

                    // Add the completed user to the ArrayList
                    addUser(parsedUser);
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing user XML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method to safely extract text content from an XML tag.
     */
    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList != null && nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node != null) {
                return node.getTextContent().trim();
            }
        }
        return "";
    }

    /**
     * Returns the populated list of users to be searched
     * @return the user list
     */
    public ArrayList<User> getUserList() {
        return userList;
    }
}