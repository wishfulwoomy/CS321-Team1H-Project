import java.io.InputStream;
import java.util.List;

public class GameParserTester 
{
    public static void main(String[] args) 
    {
        // 1. Define the path to your XML file. 
        // The leading slash "/" tells Java to look at the root of the classpath (your resources folder).
        String xmlFilePath = "/simple1.xml"; 
        
        // 2. Load the file as an InputStream
        InputStream inputStream = GameParserTester.class.getResourceAsStream(xmlFilePath);

        // 3. Safety check: Did Java actually find the file?
        if (inputStream == null) 
        {
            System.err.println("Error: Could not find the file -> " + xmlFilePath);
            System.err.println("Please ensure 'simple1.xml' is placed in the 'src/main/resources' directory.");
            return; // Exit the program early so we don't get a NullPointerException
        }

        System.out.println("Loading and parsing " + xmlFilePath + "...\n");

        // 4. Call the parser you just built
        List<Game> parsedGames = GameParser.parseGames(inputStream);

        // 5. Output the results
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
                // These will currently print '0' because the XML lacks this data, 
                // but it proves the Game objects were created successfully!
                System.out.println("Min Players: " + game.getMinPlayers());
                System.out.println("Max Players: " + game.getMaxPlayers());
                System.out.println("Play Time: " + game.getPlayTimeMinutes() + " mins");
                System.out.println("Average Rating: " + game.getAvgRating());
                System.out.println("--------------------------------------------------");
            }
        }
    }
}