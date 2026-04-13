import main.java.org.example.model.User;
import main.java.org.example.model.UserParser;
import java.util.ArrayList;

public class UserParserTester 
{
    public static void main(String[] args) 
    {
        UserParser parser = new UserParser();

        String xmlPath = "src/main/resources/sampleUser.xml";
        System.out.println("Loading and parsing " + xmlPath + "...\n");

        parser.loadUsers(xmlPath);

        ArrayList<User> parsedUsers = parser.getUserList();

        if (parsedUsers.isEmpty()) 
        {
            System.out.println("No users were found or parsed from the XML file.");
        } 
        else 
        {
            System.out.println("Successfully parsed " + parsedUsers.size() + " users!\n");
            
            for (User user : parsedUsers) 
            {
                System.out.println("Name: " + user.getName());
                System.out.println("User ID: " + user.getID()); 
                System.out.println("Password: " + user.getPassword());
                System.out.println("--------------------------------------------------");
            }
        }
    }
}