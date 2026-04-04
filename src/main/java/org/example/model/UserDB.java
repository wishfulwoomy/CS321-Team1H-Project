package main.java.org.example.model;

import java.util.ArrayList;

public class UserDB {
    private ArrayList<User> userList;

    /**
     *  Creates an array to store all user accounts
     */
    public UserDB(){
        userList = new ArrayList();
    }

    /**
     * Adds a user account to the database
     * @param newUser new account object
     */
    public void addUser(User newUser){
        userList.add(newUser);
    }

    //INCOMPLETE
    private void loadUsers(String inputPath){

    }

    /**
     * If the user exists in the list, return the user
     * @param searchUser The user being searched for
     * @return The user
     */
    /*
    public main.java.org.example.model.User searchUsers(main.java.org.example.model.User searchUser){

        if(userFound(searchUser)){

            for (int i = 0; i < userList.size(); i++){
                if(searchUser.equals(userList.get(i))){
                    return userList.get(i);
                }
            }
        }
        else{
            throw new NoSuchElementException("This user does not exist.");
        }
    }
     */

    /**
     * Check if a user object exists in the database
     * @param object A main.java.org.example.model.User account
     * @return True/false if the user is found/not found
     */
    public boolean userFound(User object){
        return userList.contains(object);
    }
}
