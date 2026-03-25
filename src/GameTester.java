public class GameTester 
{
    public static void main(String[] args)
    {
        Game game1 = new Game("Catan", 3, 4, 90);
        Review review1 = new Review("Catan", "user1", 5, "Great game!");
        Review review2 = new Review("Catan", "user2", 4, "Fun but can be long.");

        game1.setGameID("game123");
        game1.setDescription("A popular board game where players collect resources and build settlements.");

        game1.addReview(review1);
        game1.addReview(review2);

        System.out.println("Game: " + game1.getTitle());
        System.out.println("Description: " + game1.getDescription());
        System.out.println("Min Players: " + game1.getMinPlayers());
        System.out.println("Max Players: " + game1.getMaxPlayers());
        System.out.println("Play Time (minutes): " + game1.getPlayTimeMinutes());
        System.out.println("Average Rating: " + game1.getAvgRating());

        System.out.println("\nReviews:");
        for (Review review : game1.getReviews()) {
            System.out.println("- GameID: " + review.getGameID() + 
                               ", Author: " + review.getAuthorID() + 
                               ", Rating: " + review.getRating() + 
                               ", Date: " + review.getDatePosted() + 
                               ", Comment: " + review.getComment());
        }
    }
}
