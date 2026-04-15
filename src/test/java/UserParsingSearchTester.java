package test.java;

import main.java.org.example.model.User;
import main.java.org.example.model.UserParser;
import main.java.org.example.model.UserSearch;
import java.util.ArrayList;

public class UserParsingSearchTester 
{
    public static void main(String[] args)
    {
        UserParser parser = new UserParser();
        parser.loadUsers("src/main/resources/sampleUser.xml");
        ArrayList<User> users = parser.getUserList();
        UserSearch search = new UserSearch(users);
        
        // Test case 1: Search for existing users
        User foundUser1 = search.searchUser("CasualSteve", "stevepass");
        if (foundUser1 != null)
        {
            System.out.println("Test case 1 passed: found user - " + foundUser1.getName());
        }
        else
        {
            System.out.println("Test case 1 failed: user not found.");
        }

        // Test Case 2: Search for non-existent user
        User foundUser2 = search.searchUser("Allice", "pass");
        if (foundUser2 == null)
        {
            System.out.println("Test case 2 passed: non-existent user not found.");
        }
        else
        {
            System.out.println("Test case 2 failed: non-existent user shouldn't be found");
        }
    }
}
