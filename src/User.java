import java.util.ArrayList;

public class User {
    private String name;
    private int userID;
    private String password;
    ArrayList<Wishlist> AllWishlists;

    /**
     * Class constructor to initialize user attributes
     * @param username user's username
     * @param id user's ID
     * @param pass user's password
     */
    public User(String username, int id, String pass){
        name = username;
        userID = id;
        password = pass;
        AllWishlists = new ArrayList<>();
    }

    /**
     * Returns the user's username
     * @return the user's username
     */
    public String getName(){
        return name;
    }

    /**
     * Change the user's username
     * @param newName the new username
     */
    public void setName(String newName){
        name = newName;
    }

    /**
     * Return the user's ID
     * @return the user account's ID
     */
    public int getID(){
        return userID;
    }


    /**
     * Return the user's current password
     * @return the user's password
     */
    public String getPassword(){
        return password;
    }

    /**
     * Change the user's password
     * @param newPass the new password
     */
    public void setPassword(String newPass){
        password = newPass;
    }

    /**
     * Creates a new instance of a wishlist and appends it to the list of all wishlists
     */
    public void createWishlist(){
        Wishlist list = new Wishlist(name);
        AllWishlists.add(list);
    }

    /**
     *
     * @return the list of all the user's wishlists
     */
    public ArrayList<Wishlist> getWishlists(){
        return AllWishlists;
    }

    /**
     * Deletes a wishlist
     * @param listName the name of the wishlist the user will delete
     */
    public void deleteWishlist(Wishlist listName){
        AllWishlists.remove(listName);
    }
}
