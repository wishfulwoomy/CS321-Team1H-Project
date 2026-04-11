package main.java.org.example.model;

import java.util.ArrayList;
import java.util.List;

public class GameSearch {
    private List<Game> SearchResults = new ArrayList<Game>();

    /**
     * Function to search through the full game catalog and return results matching the user's search criteria.
     * @param games The full list of games to search through. NOTE: will likely be replaced with games list from GameParse.
     * @param query The text query, taken from user input and used to search GameCollection.
     * @return A filtered list of games.
     */
    public List<Game> searchGames(ArrayList<Game> games, String query) {
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
     * @param game The game to check against the query.
     * @param query A String search query
     * @return A boolean true/false. True if the game matches the query, false otherwise.
     */
    public boolean matchQuery(Game game, String query) {
        return game.getTitle().toLowerCase().contains(query.toLowerCase());
    }

    public boolean matchPlayers(Game game, int min, int max) {
        if (min >= game.getMinPlayers()) {
            return (max <= game.getMaxPlayers());
        }
        return false;
    }

    public boolean matchPlaytime(Game game, int min, int max) {
        if (game.getPlayTimeMinutes() >= min) {
            return (game.getPlayTimeMinutes() <= max);
        }
        return false;
    }

    public boolean matchRating(Game game, float min, float max) {
        if (game.getAvgRating() >= min) {
            return (game.getAvgRating() <= max);
        }
        return false;
    }
}