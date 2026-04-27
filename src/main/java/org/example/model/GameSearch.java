package main.java.org.example.model;

import java.util.ArrayList;
import java.util.List;

public class GameSearch {
    /**
     * ArrayList to hold the current search results-- this is all games that meet the current criteria.
     */
    private static List<Game> SearchResults = new ArrayList<Game>();
    private static int minPlayers = 0;
    private static int maxPlayers = 16;
    private static int minPlaytime = 15;
    private static int maxPlaytime = 480;

    /**
     * Function to search through the full game catalog and return results matching the user's search criteria.
     * 
     * @param games The full list of games to search through.
     * @param query The text query, taken from user input and used to search GameCollection.
     * @return A filtered list of games.
     */
    public static List<Game> searchGames(List<Game> games, String query) {
        SearchResults.clear();
        for (Game game : games) {
            if (query != null) { // If we have a query, check against
                if (!matchQuery(game, query))
                    continue;
            }
            // If filter settings have been changed from default, check against
            if ((minPlayers != 0) || (maxPlayers != 16)) {
                if (!matchPlayers(game)) {
                    continue;
                }
            }
            if ((minPlaytime != 15) || (maxPlaytime != 480)) {
                if (!matchPlaytime(game)) {
                    continue;
                }
            }
            SearchResults.add(game); // if game matches query, add to list
        }
        return SearchResults;
    }

    /**
     * Function to check if a certain game matches a user-given search query.
     * 
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
     * 
     * @param game The game to check against the range.
     * @return A boolean true/false. True if the game falls within the range, false otherwise.
     */
    public static boolean matchPlayers(Game game) {
        if (minPlayers <= game.getMaxPlayers()) {
            return (maxPlayers >= game.getMinPlayers());
        }
        return false;
    }

    /**
     * Function to check if the game falls within the specified playtime range.
     * 
     * @param game The game to check against the range.
     * @return A boolean true/false. True if the game falls within the range, false otherwise.
     */
    public static boolean matchPlaytime(Game game) {
        if (game.getPlayTimeMinutes() >= minPlaytime) {
            return (game.getPlayTimeMinutes() <= maxPlaytime);
        }
        return false;
    }

    /**
     * Function to check if the game matches a certain range of ratings, using a game's average rating.
     * 
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
     * 
     * @return A List of games matching the current search criteria.
     */
    public static List<Game> getSearchResults() { return SearchResults; }

    // Setters and getters for each filter setting

    /**
     * Getter function to return the minimum amount of players to filter by.
     * @return An integer for the current minimum amount of players to filter by.
     */
    public static int getMinPlayers() { return minPlayers; }

    /**
     * Getter function to return the maximum amount of players to filter by.
     * @return An integer for the current maximum amount of players to filter by.
     */
    public static int getMaxPlayers() { return maxPlayers; }

    /**
     * Getter function to return the minimum length of playtime to filter by.
     * @return An integer for the current minimum length of playtime to filter by.
     */
    public static int getMinPlaytime() { return minPlaytime; }

    /**
     * Getter function to return the maximum length of playtime to filter by.
     * @return An integer for the current maximum length of playtime to filter by.
     */
    public static int getMaxPlaytime() { return maxPlaytime; }

    /**
     * Setter function to set the minimum amount of players to filter by.
     * @param min The new setting for minimum amount of players.
     */
    public static void setMinPlayers(int min) { minPlayers = min; }

    /**
     * Setter function to set the maximum amount of players to filter by.
     * @param max The new setting for maximum amount of players.
     */
    public static void setMaxPlayers(int max) { maxPlayers = max; }

    /**
     * Setter function to set the minimum length of playtime to filter by.
     * @param min The new setting for minimum length of playtime.
     */
    public static void setMinPlaytime(int min) { minPlaytime = min; }

    /**
     * Setter function to set the maximum length of playtime to filter by.
     * @param max The new setting for maximum length of playtime.
     */
    public static void setMaxPlaytime(int max) { maxPlaytime = max; }
}