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
     * @param index
     */
    public void remove(int index) {

    }

    /**
     *
     * @return
     */
    public int getSize() {

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

    }

    private int size;
    private String name;
    private ArrayList<Game> games = new ArrayList<Game>();
}
