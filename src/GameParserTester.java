import java.io.InputStream;
import java.util.List;

public class GameParserTester 
{
    public static void main(String[] args) 
    {
        // 1. Point to the new XML file with 90 games
        String xmlFilePath = "/bgg90Games.xml"; 
        
        InputStream inputStream = GameParserTester.class.getResourceAsStream(xmlFilePath);

        if (inputStream == null) 
        {
            System.err.println("Error: Could not find the file -> " + xmlFilePath);
            System.err.println("Please ensure 'bgg90Games.xml' is in your src directory.");
            return; 
        }

        System.out.println("Loading and parsing " + xmlFilePath + "...\n");

        List<Game> parsedGames = GameParser.parseGames(inputStream);

        if (parsedGames.isEmpty()) 
        {
            System.out.println("No games were found or parsed from the XML file.");
        } 
        else 
        {
            System.out.println("Successfully parsed " + parsedGames.size() + " games!\n");
            
            for (Game game : parsedGames) 
            {
                System.out.println("Title: " + game.getTitle());
                System.out.println("Min Players: " + game.getMinPlayers());
                System.out.println("Max Players: " + game.getMaxPlayers());
                System.out.println("Play Time: " + game.getPlayTimeMinutes() + " mins");
                System.out.println("Average Rating: " + game.getAvgRating());
                System.out.println("--------------------------------------------------");
            }
        }
    }
}