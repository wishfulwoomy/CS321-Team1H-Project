import java.util.ArrayList;

/**
 * This class stores a personal game database for the User
 */
public class Wishlist {

    /**
     * This method creates a wishlist. Requires a wishlist name
     */
    public Wishlist(String name) {
        size = 0;
        this.name = name;
    }

    /**
     * This method adds a game to the wishlist
     * @param newGame The game to be added
     */
    public void add(Game newGame) {
        games.add(newGame);
        size += 1;
    }

    /**
     * This method removes the game from the wishlist
     * @param removeGame The game to be removed
     */
    public void remove(Game removeGame) {
        games.remove(removeGame);
        size -= 1;
    }

    /**
     * This methods returns how many games are in the wishlist
     * @return The size of the wishlist in int form
     */
    public int getSize() {
        return size;
    }

    /**
     * This method returns the name of the wishlist
     * @return The name of the wishlist in String form
     */
    public String getName() {
        return name;
    }

    /**
     * This method changes the name of the wishlist
     * @param newName The name the wishlist should be changed to
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * This method checks whether there are games in the wishlist
     * @return true if the wishlist is empty
     */
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        else return false;
    }

    private int size;
    private String name;
    private ArrayList<Game> games = new ArrayList<Game>();
}
