package main.java.org.example.model;

import java.util.ArrayList;

public class UserSearch {
    private ArrayList<User> userList;

    /**
     * Initializes the search class with the parsed user list
     * @param userList The database of users to search
     */
    public UserSearch(ArrayList<User> userList) {
        this.userList = userList;
    }

    /**
     * If the user exists in the list, return the user
     * @param username The username being searched for
     * @param pass The password being searched for
     * @return The user
     */
    
    public User searchUser (String username, String pass)
    {
        for (User user : userList)
        {
            if (user.getName().equals(username) && user.getPassword().equals(pass))
            {
                return user;
            }
        }
        System.out.println("User not found. Please try a different username or password.");
        return null; 

    }
     

    /**
     * Check if a user object exists in the database
     * @param object A User account
     * @return True/false if the user is found/not found
     */
    public boolean userFound(User object){
        return userList.contains(object);
    }
}