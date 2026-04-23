package main.java.org.example.model;

/** @author Asia Dezenberg, Zachary Glasgow, Jaxyn Kirian, Emma Planson, Eve Wall
 * @version 1.0
 * @since 03/23/2026 
 * This class is designed to store game reviews*/
import java.util.Date;
public class Review 
{
    /** Attributes of a review */
    private String reviewID;
    private String authorID;
    private String gameID;
    private int rating;
    private String comment;
    private Date datePosted;
    private String author;

    /** Constructor for the review class. The constructor will also set the datePosted
     * to the current date and time.
     * @param game the game to which the review belongs
     * @param author the author of the review's author
     * @param authorID the ID of the review's author
     * @param rate the rating given to the game
     * @param com the comment provided by the reviewer
     */
    public Review(String game, String author, String authorID, int rate, String com)
    {
        this.gameID = game;
        this.author = author;
        this.authorID = authorID;
        this.rating = rate;
        this.comment = com;
        this.datePosted = new Date(); // set the date
    }

    /** A method to edit the review, updating the rating and comment of the review.
     * @param newRating the new rating to be set
     * @param newComment the new comment to be set
     */
    public void editReview(int newRating, String newComment)
    {
        this.rating = newRating;
        this.comment = newComment;
        this.datePosted = new Date();
    }

    /** gets the review ID
     * @return the review ID
     */
    public String getReviewID()
    {
        return this.reviewID;
    }

    /** gets the author's ID
     * @return the author's ID
     */
    public String getAuthorID()
    {
        return this.authorID;
    }

    /** gets the game rating
     * @return the rating
     */
    public int getRating()
    {
        return this.rating;
    }

    /** gets the comment
     * @return String with the comment.
     */
    public String getComment()
    {
        return this.comment;
    }

    /** gets the date posted
     * @return date 
     */
    public Date getDatePosted()
    {
        return this.datePosted;
    }

    /** gets the game ID
     * @return String with the game ID
     */
    public String getGameID()
    {
        return this.gameID;
    }

    /** gets the author that left the review
     * @return String with the author's username
     */
    public String getAuthor() { return this.author; }
}
