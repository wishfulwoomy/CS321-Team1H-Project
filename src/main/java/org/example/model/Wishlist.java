package main.java.org.example.model;

import java.util.ArrayList;

/**
 * Represents a custom collection of board games created by a User.
 * Allows users to categorize, track, and manage games they are interested in
 * saving for later viewing.
 */
public class Wishlist 
{
    // Core Wishlist Data
    private String name;
    private ArrayList<Game> games = new ArrayList<Game>();

    /**
     * Primary constructor for the Wishlist class.
     * Initializes an empty wishlist with the designated display name.
     *
     * @param name The desired string name for the wishlist
     */
    public Wishlist(String name) 
    {
        // Assign the provided name to this list
        this.name = name;
    }

    /**
     * Appends a new game to the end of this wishlist.
     *
     * @param newGame The Game object to be added to the collection.
     */
    public void add(Game newGame) 
    {
        // Add the specified game into the tracking array
        games.add(newGame);
    }

    /**
     * Removes a specific game from the wishlist if it currently exists inside it.
     *
     * @param removeGame The Game object to be removed from the collection.
     */
    public void remove(Game removeGame) 
    {
        // Purge the specified game from the tracking array
        games.remove(removeGame);
    }

    /**
     * Retrieves the total number of games currently stored inside this wishlist.
     *
     * @return The size of the wishlist as an integer.
     */
    public int getSize() 
    {
        return games.size();
    }

    /**
     * Retrieves the display name of the wishlist.
     *
     * @return The string name of the wishlist.
     */
    public String getName() 
    {
        return name;
    }

    /**
     * Updates the display name of the wishlist.
     *
     * @param newName The new string name to be assigned to the wishlist.
     */
    public void setName(String newName) 
    {
        this.name = newName;
    }

    /**
     * Checks whether the wishlist is currently empty.
     *
     * @return True if the wishlist contains zero games, false otherwise.
     */
    public boolean isEmpty() 
    {
        // Evaluate if the tracking array has any contents
        return games.isEmpty();
    }

    /**
     * Retrieves the underlying collection of games stored in this wishlist.
     *
     * @return An ArrayList containing all the Game objects currently in the list.
     */
    public ArrayList<Game> getGames()
    {
        return games;
    }
}