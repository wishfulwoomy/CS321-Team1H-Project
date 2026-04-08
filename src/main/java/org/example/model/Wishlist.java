package main.java.org.example.model;

import java.util.ArrayList;

/**
 * This class stores a personal game database for the main.java.org.example.model.User
 */
public class Wishlist 
{
    private String name;
    private ArrayList<Game> games = new ArrayList<Game>();

    /**
     * This method creates a wishlist. Requires a wishlist name
     */
    public Wishlist(String name) 
    {
        this.name = name;
    }

    /**
     * This method adds a game to the wishlist
     * @param newGame The game to be added
     */
    public void add(Game newGame) 
    {
        games.add(newGame);
    }

    /**
     * This method removes the game from the wishlist
     * @param removeGame The game to be removed
     */
    public void remove(Game removeGame) 
    {
        games.remove(removeGame);
    }

    /**
     * This method returns how many games are in the wishlist
     * @return The size of the wishlist in int form
     */
    public int getSize() 
    {
        return games.size();
    }

    /**
     * This method returns the name of the wishlist
     * @return The name of the wishlist in String form
     */
    public String getName() 
    {
        return name;
    }

    /**
     * This method changes the name of the wishlist
     * @param newName The name the wishlist should be changed to
     */
    public void setName(String newName) 
    {
        this.name = newName;
    }

    /**
     * This method checks whether there are games in the wishlist
     * @return true if the wishlist is empty
     */
    public boolean isEmpty() 
    {
        return games.isEmpty();
    }
}