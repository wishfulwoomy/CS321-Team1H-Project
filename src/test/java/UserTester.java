package test.java;

import main.java.org.example.model.User;
import main.java.org.example.model.Wishlist;
import main.java.org.example.model.Game;
import java.util.ArrayList;

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
        Wishlist wlist1 = user1.createWishlist("Games with Friends");
        wlist1.add(game1);
        wlist1.add(game2);
        wlist1.add(game3);


        if (user1.getWishlists().contains(wlist1)) {
            System.out.println("Wishlist added successfully");
            System.out.println(wlist1);
        } else {
            System.out.println("Error! Wishlist not added");
        }

        Game game4 = new Game("Checkers", 2, 2, 80);
        Game game5 = new Game("Chess", 2, 2, 120);
        Wishlist wlist2 = user1.createWishlist("Strategy Games");
        wlist2.add(game4);
        wlist2.add(game5);

        System.out.println(user1.toString());

        user1.deleteWishlist(wlist1);
        if (!user1.getWishlists().contains(wlist1)) {
            System.out.println("Wishlist removed successfully");
        } else {
            System.out.println("Error! Wishlist not removed");
        }
    }
}
