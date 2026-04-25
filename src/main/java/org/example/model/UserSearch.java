package main.java.org.example.model;

import java.util.ArrayList;

/**
 * Utility class responsible for searching through a collection of user
 * accounts.
 * Provides authentication functionality by matching usernames and passwords
 * against the loaded database of registered users.
 */
public class UserSearch {

    // Internal reference to the master list of parsed users
    private ArrayList<User> userList;

    /**
     * Primary constructor for the UserSearch class.
     * Initializes the search engine with a specific list of user accounts to query
     * against.
     *
     * @param userList The database (ArrayList) of User objects to search.
     */
    public UserSearch(ArrayList<User> userList) {
        // Store the provided master list internally for future search operations
        this.userList = userList;
    }

    /**
     * Searches the user database for an exact match of both username and password.
     * Used primarily during the login process to authenticate credentials.
     *
     * @param username The username string being searched for.
     * @param pass     The password string being searched for.
     * @return The authenticated User object if a match is found, or null if no
     *         match exists.
     */
    public User searchUser(String username, String pass) {
        // Iterate through every registered user in the database list
        for (User user : userList) {
            // Verify both the username and password match exactly (case-sensitive)
            if (user.getName().equals(username) && user.getPassword().equals(pass)) {
                // Exact match found! Return the authenticated user object
                return user;
            }
        }

        // Loop finished without finding a valid match; return null to indicate
        // authentication failure
        return null;
    }
}