package test.java;

import main.java.org.example.model.Game;
import main.java.org.example.model.GameSearch;
import java.util.ArrayList;
import java.util.List;

public class GameSearchTester
{
    public static void main(String[] args)
    {
        ArrayList<Game> games = new ArrayList<>();
        games.add(new Game("Catan", 1000, 3, 4, 90));
        games.add(new Game("Pandemic", 1001, 2, 4, 60));
        games.add(new Game("Ticket to Ride", 1002, 2, 5, 45));
        GameSearch gameSearch = new GameSearch();
        // Test case 1: Search for an existing game
        List<Game> foundGame1 = gameSearch.searchGames(games,"Catan");
        if (!foundGame1.isEmpty())
        {
            System.out.println("Test case 1 passed: found game - " + foundGame1.get(0).getTitle());
        }
        else
        {
            System.out.println("Test case 1 failed: game not found.");
        }
        // Test case 2: Search for a non-existent game
        List<Game> foundGame2 = gameSearch.searchGames(games, "Monopoly");
        if (foundGame2.isEmpty())
        {
            System.out.println("Test case 2 passed: non-existent game not found.");
        }
        else
        {
            System.out.println("Test case 2 failed: non-existent game shouldn't be found");
        }
    }
}