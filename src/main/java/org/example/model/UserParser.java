package main.java.org.example.model;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utility class responsible for parsing user account data from an XML database.
 * Uses Java's built-in DOM (Document Object Model) parser to read the file,
 * extract user credentials and their associated wishlists, and map them
 * directly into User objects.
 */
public class UserParser {

    // Internal cache of all parsed users
    private ArrayList<User> userList;

    /**
     * Primary constructor for the UserParser class.
     * Initializes an empty array to store all user accounts read from the database.
     */
    public UserParser() {
        userList = new ArrayList<>();
    }

    /**
     * Appends a newly parsed or created user account to the internal database list.
     *
     * @param newUser The fully constructed User object to be added.
     */
    public void addUser(User newUser) {
        userList.add(newUser);
    }

    /**
     * Parses the designated XML file to load user credentials and their saved
     * wishlists.
     * 
     * @param inputPath The relative file path to the XML database (e.g.,
     *                  "src/main/resources/sampleUser.xml").
     */
    public void loadUsers(String inputPath) {
        try {
            // Set up the DOM parser factory and builder
            File inputFile = new File(inputPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            // Load the XML file into a readable Document object
            Document doc = dBuilder.parse(inputFile);

            // Normalize the XML structure to prevent text-node formatting issues
            doc.getDocumentElement().normalize();

            // Grab every individual User entry in the database
            NodeList userNodes = doc.getElementsByTagName("User");

            // Loop through every user node found
            for (int i = 0; i < userNodes.getLength(); i++) {
                Node userNode = userNodes.item(i);

                // Ensure the node is an actual XML element containing data
                if (userNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element userElement = (Element) userNode;

                    // EXTRACT CORE ACCOUNT DATA

                    // Utilize the helper method to safely grab the text inside the tags
                    String name = getTagValue("name", userElement);
                    String userIDString = getTagValue("userID", userElement);
                    String password = getTagValue("password", userElement);

                    // Convert the string ID into a usable integer
                    int userID = 0;
                    try {
                        userID = Integer.parseInt(userIDString);
                    } catch (NumberFormatException e) {
                        // Fallback gracefully if the XML has corrupted or missing ID data
                        System.err.println("Warning: Invalid user ID format for user " + name + ". Defaulting to 0.");
                    }

                    // Create the base User object
                    User parsedUser = new User(name, userID, password);

                    // EXTRACT ASSOCIATED WISHLISTS

                    // Find all Wishlist tags nested specifically inside this User's tag
                    NodeList wishlistNodes = userElement.getElementsByTagName("Wishlist");

                    for (int j = 0; j < wishlistNodes.getLength(); j++) {
                        Node wishNode = wishlistNodes.item(j);

                        if (wishNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element wishElement = (Element) wishNode;

                            // Get the string name of the wishlist
                            String wishlistName = getTagValue("name", wishElement);

                            // Bind the new wishlist to the user's account utilizing their own internal
                            // method
                            parsedUser.createWishlist(wishlistName);
                        }
                    }

                    // Add the fully constructed and populated user to the parser's running list
                    addUser(parsedUser);
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing user XML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * A safe helper method to extract text content from a specific XML tag.
     * Prevents NullPointerExceptions if a tag is missing or entirely empty.
     * 
     * @param tag     The exact string name of the XML tag to look for.
     * @param element The parent XML element to search inside.
     * @return The extracted text content, stripped of whitespace, or an empty
     *         string if not found.
     */
    private String getTagValue(String tag, Element element) {
        // Look for the specific tag
        NodeList nodeList = element.getElementsByTagName(tag);

        // Ensure the tag actually exists within this element
        if (nodeList != null && nodeList.getLength() > 0) {
            Node node = nodeList.item(0);

            // Ensure the node isn't fundamentally broken
            if (node != null) {
                // Return the text, stripping away any invisible leading/trailing whitespace or
                // line breaks
                return node.getTextContent().trim();
            }
        }
        // Return a safe empty string instead of null to prevent application crashes
        return "";
    }

    /**
     * Retrieves the fully parsed list of users from the database.
     * 
     * @return An ArrayList containing all loaded User objects.
     */
    public ArrayList<User> getUserList() {
        return userList;
    }
}