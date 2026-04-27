package main.java.org.example.model;

import java.util.Date;

/**
 * Represents a user's review of a specific board game.
 * Stores the rating, written comment, author details, and the timestamp
 * of when the review was originally posted or last edited.
 */
public class Review {
    // Database Identifiers
    private String reviewID;
    private String game;
    private int gameID;
    private int authorID;

    // Review Content
    private String author;
    private int rating;
    private String comment;
    private Date datePosted;

    /**
     * Primary constructor for the Review class.
     * Initializes the review with user-provided data and automatically
     * stamps it with the current date and time.
     *
     * @param game The title of the game
     * @param gameID The unique ID of the game being reviewed.
     * @param author   The display username of the person writing the review.
     * @param authorID The unique database ID of the author.
     * @param rate     The numerical rating given to the game (e.g., 1-5).
     * @param com      The written comment or feedback provided by the user.
     */
    public Review(String game, int gameID, String author, int authorID, int rate, String com) {
        // Link the review to the correct game and user
        this.game = game;
        this.gameID = gameID;
        this.author = author;
        this.authorID = authorID;

        // Store the actual review content
        this.rating = rate;
        this.comment = com;

        // Automatically capture the exact moment the review is created
        this.datePosted = new Date();
    }

    /**
     * Updates an existing review with a new rating and comment.
     * Automatically updates the timestamp to reflect when the edit occurred.
     *
     * @param newRating  The updated numerical rating.
     * @param newComment The updated written comment.
     */
    public void editReview(int newRating, String newComment) {
        // Overwrite the old content with the new values
        this.rating = newRating;
        this.comment = newComment;

        // Refresh the timestamp so the system knows it was recently modified
        this.datePosted = new Date();
    }

    /**
     * Retrieves the unique database ID of the review.
     *
     * @return The review ID string.
     */
    public String getReviewID() {
        return this.reviewID;
    }

    /**
     * Retrieves the unique database ID of the user who wrote the review.
     *
     * @return The author's ID string.
     */
    public int getAuthorID()
    {
        return this.authorID;
    }

    /**
     * Retrieves the numerical rating given to the game.
     *
     * @return The integer rating.
     */
    public int getRating() {
        return this.rating;
    }

    /**
     * Retrieves the written feedback provided by the user.
     *
     * @return The comment string.
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * Retrieves the exact date and time the review was posted or last edited.
     *
     * @return A Date object representing the timestamp.
     */
    public Date getDatePosted() {
        return this.datePosted;
    }

    /**
     * Retrieves the unique database ID of the game this review belongs to.
     *
     * @return The game ID string.
     */
    public int getGameID() {
        return this.gameID;
    }

    /**
     * Retrieves the display username of the person who wrote the review.
     *
     * @return The author's username string.
     */
    public String getAuthor() {
        return this.author;
    }
}