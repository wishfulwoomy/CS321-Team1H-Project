package test.java;

import main.java.org.example.model.User;
import main.java.org.example.model.Wishlist;
import main.java.org.example.model.Game;

public class UserTester {

    public static void main(String[] args) {

        User user1 = new User("Tater Tot", 1, "skatertot123");

        System.out.println("New main.java.org.example.model.User Created");

        System.out.println("Username: " + user1.getName());
        System.out.println("ID: " + user1.getID());
        System.out.println("Password: " + user1.getPassword());

        System.out.println("\nSetting new attributes:\n");

        user1.setName("Summit");
        user1.setPassword("chickennugget");

        System.out.println("Username: " + user1.getName());
        System.out.println("ID: " + user1.getID());
        System.out.println("Password: " + user1.getPassword());


        Game game1 = new Game("Monopoly", 2, 8, 80);
        Game game2 = new Game("Uno", 2, 10, 120);
        Game game3 = new Game("Battleship", 2, 2, 60);

        Wishlist wlist1 = user1.createWishlist("friends");
        wlist1.add(game1);
        wlist1.add(game2);
        wlist1.add(game3);


        if (wlist1.getGames().contains(game1) && wlist1.getGames().contains(game2) && wlist1.getGames().contains(game3)) {
            System.out.println("All 3 games added to wishlist");
        } else {
            System.out.println("Error! Games not added to the wishlist");
        }

        if (user1.getWishlists().contains(wlist1)) {
            System.out.println("Wishlist added successfully");
        } else {
            System.out.println("Error! Wishlist not added");
        }

        user1.deleteWishlist(wlist1);
        if (!user1.getWishlists().contains(wlist1)) {
            System.out.println("Wishlist removed successfully");
        } else {
            System.out.println("Error! Wishlist not removed");
        }
    }
}
