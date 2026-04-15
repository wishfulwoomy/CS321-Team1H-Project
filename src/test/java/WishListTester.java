package test.java;

import main.java.org.example.model.Wishlist;
import main.java.org.example.model.Game;
import java.util.ArrayList;

public class WishListTester 
{
    public static void main(String[] args)
    {
        Wishlist wishList = new Wishlist("My Wish List");
        // Test case 1: Add a game to the wish list
        Game game1 = new Game("Catan", 3, 4, 90);
        wishList.add(game1);
        if (wishList.getGames().contains(game1))
        {
            System.out.println("Test case 1 passed: game added");
        }
        else
        {
            System.out.println("Test case 1 failed: game not added");
        }

        // Test case 2: remove a game from the wish list
        wishList.remove(game1);
        if (!wishList.getGames().contains(game1))
        {
            System.out.println("Test case 2 passed: game removed");
        }
        else
        {
            System.out.println("Test case 2 failed: game not removed");
        }
    }
}
