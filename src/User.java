import java.util.ArrayList;

public class User {
    private String name;
    private int userID;
    private String password;
    ArrayList<Wishlist> AllWishlists;

    User(String username, int id, String pass){
        name = username;
        userID = id;
        password = pass;
        AllWishlists = new ArrayList<>();
    }

    /**
     *
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     *
     * @param newName
     */
    public void setName(String newName){
        name = newName;
    }

    /**
     *
     * @return
     */
    public int getID(){
        return userID;
    }

    /**
     *
     * @param newID
     */
    public void setID(int newID){
        userID = newID;
    }

    /**
     *
     * @return
     */
    public String getPassword(){
        return password;
    }

    /**
     *
     * @param newPass
     */
    public void setPassword(String newPass){
        password = newPass;
    }

    /*
    public bool isCorrect(){

    }
    */
    /*I'm assuming that this is checking to see if the pass the user types in is the same as the pass the user set.
    If so, this might be a class that we need to put in  LoginView or something. */

    /*
    public void createWishlist(){
        Wishlist list = new Wishlist(<parameters>);
        AllWishLists.add(list);
    }
    */

    /*
    public ArrayList<Wishlist> getWishlists(){
        return AllWishLists;
    }
    */

    /*
    public void deleteWishlist(Wishlist listName){
        AllWishLists.remove(listName)
    }
    */
}
