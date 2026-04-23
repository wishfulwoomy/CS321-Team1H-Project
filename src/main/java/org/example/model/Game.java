package main.java.org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** @author Asia Dezenberg, Zachary Glasgow, Jaxyn Kirian, Emma Planson, Eve Wall
 * @version 1.0
 * @since 03/23/2026
 * This class represents a game, with its title, description, and various other attributes.
 */

public class Game 
{
    private String imageURL;
    private String gameID;
    private String title;
    private String description;
    private int minPlayers;
    private int maxPlayers;
    private int playTimeMinutes;
    private double avgRating;
    private List<Review> reviews;

     /** Constructor for the Game class
      * @param t The title of the game
      * @param min The minimum players needed
      * @param max The maximum players allowed
      * @param time How long the game could take
      */
     public Game(String t, int min, int max, int time)
     {
        this.title = t;
        this.minPlayers = min;
        this.maxPlayers = max;
        this.playTimeMinutes = time;
        this.avgRating = 0.0;
        this.reviews = new ArrayList<Review>();
    }

    /** * Overloaded constructor for the Game class
     * @param t The title of the game
     */
    public Game(String t)
    {
        this(t, 0, 0, 0);
    }

    /** * Method to add a review and update the average rating
     * @param r the review to be added
     */
    public void addReview(Review r)
    {
        this.reviews.add(r);
        calculateAverageRating();
    }

    /** * This method removes a review and updates the average rating.
     * @param r the review to be removed
     * @return boolean indicating whether the review was successfully removed
     */
    public boolean removeReview(Review r)
    {
        if (this.reviews.remove(r))
        {
            calculateAverageRating();
            return true;
        }
        return false;
    }

    /** * Calculates and updates the internal average rating of the game.
     * Kept private so outside classes cannot force arbitrary recalculations.
     */
    private void calculateAverageRating()
    {
        if (reviews.isEmpty())
        {
            this.avgRating = 0.0;
            return;
        }

        double totalRating = 0.0;
        for (Review r : reviews) 
        {
            totalRating += r.getRating();
        }

        this.avgRating = totalRating / reviews.size();
    }

    /** * Gets the game ID
     * @return the game ID
     */
    public String getGameID()
    {
        return this.gameID;
    }

    /** * Gets the title of the game
     * @return the title
     */
    public String getTitle()
    {
        return this.title;
    }

    /** * Gets the description of the game
     * @return description
     */
    public String getDescription()
    {
        return this.description;
    }

    /** * Gets the minimum number of players
     * @return the minimum number of players
     */
    public int getMinPlayers()
    {
        return this.minPlayers;
    }

    /** * Gets the maximum number of players
     * @return the maximum number of players
     */
    public int getMaxPlayers()
    {
        return this.maxPlayers;
    }

    /** * Gets the estimated play time in minutes
     * @return the estimated play time
     */
    public int getPlayTimeMinutes()
    {
        return this.playTimeMinutes;
    }

    /** * Gets the average rating of the game
     * @return the average rating
     */
    public double getAvgRating()
    {
        return this.avgRating;
    }

    /** * Gets a read-only list of reviews for the game
     * @return unmodifiable list of reviews
     */
    public List<Review> getReviews()
    {
        // Returns an unmodifiable list to prevent external modification of the array
        return Collections.unmodifiableList(this.reviews);
    }

    /** * Sets the game ID
     * @param id the game ID to be set
     */
    public void setGameID(String id)
    {
        this.gameID = id;
    }

    /** * Sets the description of the game
     * @param desc the description
     */
    public void setDescription(String desc)
    {
        this.description = desc;
    }

    /**
     * Gets the url of the associated game's image
     * @return url of the image
     */
    public String getImageUrl()
    {
        return this.imageURL;
    }

    /**
     * Sets the url for the game's image
     * @param url the image's url
     */
    public void setImageUrl(String url)
    {
        this.imageURL = url;
    }
}
