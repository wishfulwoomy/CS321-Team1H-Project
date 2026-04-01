import java.util.ArrayList;
import java.util.NoSuchElementException;

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
