package test.java;

import java.util.ArrayList;
import main.java.org.example.model.User;
import main.java.org.example.model.UserSearch; 

public class UserSearchTester 
{
    public static void main(String[] args)
    {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Alice", 1, "password123"));
        users.add(new User("Bob", 2, "securePass"));
        UserSearch userSearch = new UserSearch(users);

        // Test case 1: Valid user
        User foundUser1 = userSearch.searchUser("Alice", "password123");
        if (foundUser1 != null) 
        {
            System.out.println("Test case 1 passed: Found user - " + foundUser1.getName());
        } 
        else 
        {
            System.out.println("Test case 1 failed: User not found.");
        }
        // Test case 2: Invalid password
        User foundUser2 = userSearch.searchUser("Alice", "wrongPassword");
        if (foundUser2 == null)
        {
            System.out.println("Test case 2 passed: User not found with wrong password.");
        }
        else
        {
            System.out.println("Test case 2 failed: User should not be used with wrong password.");
        }
        // Test case 3: Non-existent user
        User foundUser3 = userSearch.searchUser("Charlie", "somePassword");
        if (foundUser3 == null)
        {
            System.out.println("Test case 3 passed: non-existent user not found.");
        }
        else
        {
            System.out.println("Test case 3 failed: non-existent user should not be found.");
        }
    }
}
