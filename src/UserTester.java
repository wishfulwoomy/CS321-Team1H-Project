public class UserTester {

    public static void main(){

        User user1 = new User("Tater Tot", 001, "skatertot123");

        System.out.println("New User Created");

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
