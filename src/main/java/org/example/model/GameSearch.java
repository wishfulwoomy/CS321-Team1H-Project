package main.java.org.example.model;

import java.util.ArrayList;
import java.util.List;

public class GameSearch {
    /**
     * ArrayList to hold the current search results-- this is all games that meet the current criteria.
     */
    private static List<Game> SearchResults = new ArrayList<Game>();

    /**
     * Function to search through the full game catalog and return results matching the user's search criteria.
     * @param games The full list of games to search through. NOTE: will likely be replaced with games list from GameParse.
     * @param query The text query, taken from user input and used to search GameCollection.
     * @return A filtered list of games.
     */
    public static List<Game> searchGames(List<Game> games, String query) {
        SearchResults.clear();
        for (Game game : games) {
            if (query != null) {
                if (!matchQuery(game, query))
                    continue;
            }
            SearchResults.add(game); // if game matches query, add to list
        }
        return SearchResults;
    }

    /**
     * Function to check if a certain game matches a user-given search query.
     * @param g The game to check against the query.
     * @param q A String search query
     * @return A boolean true/false. True if the game matches the query, false otherwise.
     */
    public static boolean matchQuery(Game g, String q) {
        // clean up input. game title and query are now lowercase strings
        String game = g.getTitle().toLowerCase();
        String query = q.toLowerCase();
        return game.contains(query);
    }

    /**
     * Function to check if the game falls within the specified player range.
     * @param game The game to check against the range.
     * @param min The minimum desired amount of players.
     * @param max The maximum desired amount of players.
     * @return A boolean true/false. True if the game falls within the range, false otherwise.
     */
    public boolean matchPlayers(Game game, int min, int max) {
        if (min >= game.getMinPlayers()) {
            return (max <= game.getMaxPlayers());
        }
        return false;
    }

    /**
     * Function to check if the game falls within the specified playtime range.
     * @param game The game to check against the range.
     * @param min The minimum desired playtime.
     * @param max The maximum desired playtime.
     * @return A boolean true/false. True if the game falls within the range, false otherwise.
     */
    public boolean matchPlaytime(Game game, int min, int max) {
        if (game.getPlayTimeMinutes() >= min) {
            return (game.getPlayTimeMinutes() <= max);
        }
        return false;
    }

    /**
     * Function to check if the game matches a certain range of ratings, using a game's average rating.
     * @param game The game to check against the range.
     * @param min The minimum desired rating.
     * @param max The maximum desired rating.
     * @return A boolean true/false. True if the game falls within the range, false otherwise.
     */
    public boolean matchRating(Game game, float min, float max) {
        if (game.getAvgRating() >= min) {
            return (game.getAvgRating() <= max);
        }
        return false;
    }

    /**
     * Getter function to return the list of search results.
     * @return A List of games matching the current search criteria.
     */
    public static List<Game> getSearchResults() { return SearchResults; }
}