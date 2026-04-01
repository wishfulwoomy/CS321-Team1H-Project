import java.util.ArrayList;

public class UserParser {
    private ArrayList<User> userList;

    /**
     * Creates an array to store all user accounts
     */
    public UserParser(){
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
     * Returns the populated list of users to be searched
     * @return the user list
     */
    public ArrayList<User> getUserList() {
        return userList;
    }
}