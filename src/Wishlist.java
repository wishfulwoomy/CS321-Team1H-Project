import java.util.ArrayList;

public class Wishlist {

    /**
     * Default constructor
     */
    public Wishlist(String name) {
        size = 0;
        this.name = name;
    }

    /**
     *
     * @param newGame
     */
    public void add(Game newGame) {
        games.add(newGame);
        size += 1;
    }

    /**
     *
     * @param removeGame
     */
    public void remove(Game removeGame) {
        games.remove(removeGame);
        size -= 1;
    }

    /**
     *
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param newName
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     *
     * @return
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
