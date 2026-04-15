package test.java;

import main.java.org.example.model.User;
import main.java.org.example.model.Wishlist;


public class UserTester {

    public static void main(String[] args){

        User user1 = new User("Tater Tot", 1, "skatertot123");

        System.out.println("New main.java.org.example.model.User Created");

        System.out.println("Username: " + user1.getName());
        System.out.println("ID: " + user1.getID());
        System.out.println("Password: " + user1.getPassword());

        System.out.println("\nSetting new attributes:\n");

        user1.setName("Summit");
        user1.setPassword("donuts");

        System.out.println("Username: " + user1.getName());
        System.out.println("ID: " + user1.getID());
        System.out.println("Password: " + user1.getPassword());

        //Fix userDB and test for wishlists
    }
}
