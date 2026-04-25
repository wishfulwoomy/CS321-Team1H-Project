package main.java.org.example.model;

import java.util.ArrayList;

/**
 * Represents a registered user within the application.
 * Stores account credentials, unique identification, and manages the collection
 * of personal wishlists associated with the user.
 */
public class User {

    // Account Credentials
    private String name;
    private final int userID;
    private String password;

    // User Data
    ArrayList<Wishlist> allWishlists;

    /**
     * Primary constructor for the User class.
     * Initializes a new user account with their credentials and sets up an empty
     * list
     * to hold their future wishlists.
     *
     * @param username The user's chosen display name or login name.
     * @param id       The unique database ID assigned to the user.
     * @param pass     The user's account password.
     */
    public User(String username, int id, String pass) {
        // Assign the core account details
        this.name = username;
        this.userID = id;
        this.password = pass;

        // Initialize an empty collection so wishlists can be added safely later
        this.allWishlists = new ArrayList<Wishlist>();
    }

    /**
     * Retrieves the user's current username.
     *
     * @return The username string.
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the user's username.
     *
     * @param newName The new username to be set.
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Retrieves the user's unique database ID.
     *
     * @return The integer ID.
     */
    public int getID() {
        return userID;
    }

    /**
     * Retrieves the user's current password.
     *
     * @return The password string.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Updates the user's account password.
     *
     * @param newPass The new password string to be set.
     */
    public void setPassword(String newPass) {
        this.password = newPass;
    }

    /**
     * Instantiates a new wishlist, automatically binds it to this user's account,
     * and adds it to their internal collection.
     *
     * @param name The desired name for the newly created wishlist.
     * @return The fully constructed Wishlist object.
     */
    public Wishlist createWishlist(String name) {
        // Create the new wishlist object
        Wishlist list = new Wishlist(name);

        // Add it to the user's personal tracking array
        allWishlists.add(list);

        // Return it in case the UI needs to immediately interact with it
        return list;
    }

    /**
     * Retrieves the full collection of wishlists owned by the user.
     *
     * @return An ArrayList containing all of the user's Wishlist objects.
     */
    public ArrayList<Wishlist> getWishlists() {
        return allWishlists;
    }

    /**
     * Removes a specific wishlist from the user's account.
     *
     * @param listToDelete The exact Wishlist object to be removed from their
     *                     collection.
     */
    public void deleteWishlist(Wishlist listToDelete) {
        // Purge the list from the tracking array
        allWishlists.remove(listToDelete);
    }

    /**
     * Formats the user's collection of wishlists into a single, readable string
     * block.
     * Used primarily for debugging or text-based UI outputs.
     *
     * @return A multi-line string containing the names of all the user's wishlists.
     */
    @Override
    public String toString() {
        // Use a StringBuilder for efficient string concatenation inside a loop
        StringBuilder wishlistNames = new StringBuilder();

        // Loop through every wishlist the user owns
        for (Wishlist allWishlist : allWishlists) {
            // Extract the list's title
            String currentWishlist = allWishlist.getName();

            // Append the title to the running string, followed by a line break
            wishlistNames.append(currentWishlist).append("\n");
        }

        // Convert the final built string back into a standard String object and return
        // it
        return wishlistNames.toString();
    }
}