package main.java.org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a board game within the application's data model.
 * Stores core details such as the game's title, player counts, playtime,
 * and a dynamic list of user reviews used to calculate an average rating.
 */
public class Game {
    // Core Game Data
    private String imageURL;
    private int gameID;
    private String title;
    private String description;

    // Gameplay Statistics
    private int minPlayers;
    private int maxPlayers;
    private int playTimeMinutes;

    // Review Data
    private double avgRating;
    private List<Review> reviews;

    /**
     * Primary constructor for the Game class.
     * Initializes a new game with its core mechanical details and sets up an empty
     * review list.
     *
     * @param t    The title of the game.
     * @param id   The game's numerical ID.
     * @param min  The minimum number of players required.
     * @param max  The maximum number of players allowed.
     * @param time The estimated playtime in minutes.
     */
    public Game(String t, int id, int min, int max, int time) {
        // Set basic mechanical stats
        this.title = t;
        this.gameID = id;
        this.minPlayers = min;
        this.maxPlayers = max;
        this.playTimeMinutes = time;

        // New games start with a default rating of 0.0 and an empty list for future
        // reviews
        this.avgRating = 0.0;
        this.reviews = new ArrayList<Review>();
     }

    /**
     * Overloaded fallback constructor for the Game class.
     * Creates a game with only a title, defaulting all numeric stats to zero.
     * Useful when the database is missing detailed information.
     *
     * @param t The title of the game.
     */
    public Game(String t) {
        // Hands the title off to the main constructor while passing 0 for the missing
        // stats
        this(t, 0, 0, 0, 0);
    }

    /**
     * Adds a new user review to the game and automatically recalculates the
     * average rating.
     *
     * @param r The Review object to be added.
     */
    public void addReview(Review r) {
        // Add the review to the internal list
        this.reviews.add(r);

        // Trigger a recalculation so the overall rating stays perfectly synced with the
        // new data
        calculateAverageRating();
    }

    /**
     * Attempts to remove a specific review from the game.
     * If successful, it automatically recalculates the overall average rating.
     *
     * @param r The Review object to be removed.
     * @return True if the review was found and removed, false otherwise.
     */
    public boolean removeReview(Review r) {
        // Attempt to pull the review from the list.
        // .remove() returns true if the object actually existed and was successfully
        // deleted.
        if (this.reviews.remove(r)) {
            // Re-sync the average rating now that the data has changed
            calculateAverageRating();
            return true;
        }
        return false;
    }

    /**
     * Calculates and updates the internal average rating of the game based on
     * current reviews.
     * Kept private so outside classes cannot force arbitrary recalculations out of
     * sync with data.
     */
    private void calculateAverageRating() {
        // Edge case: If there are no reviews, default the rating to 0.0 to prevent
        // division by zero errors
        if (reviews.isEmpty()) {
            this.avgRating = 0.0;
            return;
        }

        double totalRating = 0.0;

        // Sum up the numeric scores of every existing review
        for (Review r : reviews) {
            totalRating += r.getRating();
        }

        // Calculate the mean by dividing the total score by the total number of reviews
        this.avgRating = totalRating / reviews.size();
    }

    /**
     * Retrieves the database ID of the game.
     *
     * @return The unique numerical game ID.
     */
    public int getGameID() {
        return this.gameID;
    }

    /**
     * Retrieves the title of the game.
     *
     * @return The game title.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Retrieves the text description of the game.
     *
     * @return The description paragraph.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Retrieves the minimum number of players required to play.
     *
     * @return The minimum player count.
     */
    public int getMinPlayers() {
        return this.minPlayers;
    }

    /**
     * Retrieves the maximum number of players allowed.
     *
     * @return The maximum player count.
     */
    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    /**
     * Retrieves the estimated time it takes to play the game.
     *
     * @return The playtime in minutes.
     */
    public int getPlayTimeMinutes() {
        return this.playTimeMinutes;
    }

    /**
     * Retrieves the current mathematical average rating of the game.
     *
     * @return The average rating out of 5.
     */
    public double getAvgRating() {
        return this.avgRating;
    }

    /**
     * Retrieves a read-only list of all user reviews associated with the game.
     *
     * @return An unmodifiable list of Review objects.
     */
    public List<Review> getReviews() {
        // Wraps the internal list in an unmodifiable layer.
        // This prevents external classes from bypassing addReview()/removeReview()
        // and breaking the average rating calculation!
        return Collections.unmodifiableList(this.reviews);
    }

    /**
     * Sets the unique database ID for the game.
     *
     * @param id The numerical game ID to be assigned.
     */
    public void setGameID(int id) {
        this.gameID = id;
    }

    /**
     * Sets the text description for the game.
     *
     * @param desc The description paragraph to be assigned.
     */
    public void setDescription(String desc) {
        this.description = desc;
    }

    /**
     * Retrieves the web URL pointing to the game's cover art image.
     *
     * @return The image URL string.
     */
    public String getImageUrl() {
        return this.imageURL;
    }

    /**
     * Sets the web URL pointing to the game's cover art image.
     *
     * @param url The image URL string to be assigned.
     */
    public void setImageUrl(String url) {
        this.imageURL = url;
    }
}