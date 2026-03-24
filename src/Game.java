/** @author Asia Dezenberg, Zachary Glasgow, Jaxyn Kirian, Emma Planson, Eve Wall
 * @version 1.0
 * @since 03/23/2026
 * This class represents a game, with its title, description, and various other attributes.
 */
import java.util.ArrayList;
import java.util.List;
public class Game 
{
    /** Attributes of a game */
    private String gameID;
    private String title;
    private String description;
    private int minPlayers;
    private int maxPlayers;
    private int playTimeMinutes;
    private double avgRating;
    private List<Review> reviews;

     /** Constuctor for the Game class
      * @param t The title of the game
      * @param min The minimum players needed.
      * @param max The maximum players allowed.
      * @param time How long the game could take
      * Will also make an empty list and set the ragtings to zero.
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



     /** method to add a review 
      * @param r the review to be added
      * Also update the average rating.
      * @return void
      */
     public void addReview(Review r)
     {
        this.reviews.add(r);
        calculateAverageRating();
     }

     /** This method removes a review and updates the average rating.
      * @param r the review to be removed
      * @return boolean indicating whether the review was successfully removed
      */
     public boolean removeReview(Review r)
     {
        boolean removedItem = this.reviews.remove(r);
        if (removedItem)
        {
            calculateAverageRating();
            return true;
        }
        else
        {
            return false;
        }
     }

     /** Calculate the average rating
      * @return the average rating of the game based on the reviews
      * This method iterates through the list, sums up all of the review ratings, and then divides by the number of reviews to get the average.
      * If there are no reviews, it returns 0.0 to avoid dividing by zero.
      * This method is called whenever a new review is added.
      */
     public double calculateAverageRating()
     {
        double totalRating = 0.0;
        for (Review r : reviews) 
        {
            totalRating += r.getRating();
        }
        this.avgRating = reviews.size() > 0 ? totalRating / reviews.size() : 0.0;
        return this.avgRating;
     }

     /** geth the game id
      * @return the game id
      */
     public String getGameID()
     {
         return this.gameID;
     }

     /** get the title of the game
      * @return the title
      */
     public String getTitle()
     {
         return this.title;
     }

     /** get the description of the game
      * @return description
      */
     public String getDescription()
     {
         return this.description;
     }

     /** get the minimum number of players
      * @return the minimum number of players
      */
     public int getMinPlayers()
     {
         return this.minPlayers;
     }

     /** get the maximum number of players
      * @return in with the max number of players
      */
     public int getMaxPlayers()
     {
         return this.maxPlayers;
     }

     /** gives us the estimated play time in minutes 
      * @return int with the estimated play time.
      * */ 
     public int getPlayTimeMinutes()
     {
         return this.playTimeMinutes;
     }

     /** get the average rating of the game
      * @return double with the average rating
      */
     public double getAvgRating()
     {
         return this.avgRating;
     }

     /** get the list of reviews for the game
      * @return list of reviews
      */
     public List<Review> getReviews()
     {
         return this.reviews;
     }
}
