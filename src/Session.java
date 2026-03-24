/**
 * This class keeps track of the current User and whether the User is logged in
 */
public class Session {

    /**
     * This method creates the session and sets the default settings
     */
    public Session() {
        loggedIn = false;
        textSize = 12;
        highContrast = false;
        // Add in default text size and contrast
    }

    /**
     * This method returns the text size as an int
     * @return Text size as an int
     */
    public int getTextSize() {
        return textSize;
    }

    /**
     * This method returns the high contrast setting
     * @return true if high contrast is on
     */
    public boolean getContrast() {
        return highContrast;
    }

    /**
     * This method sets the text size for the session
     * @param textSize Text size
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * This method sets the session to either high or low contrast
     * @param highContrast True if high contrast is on
     */
    public void setContrast(boolean highContrast) {
        this.highContrast = highContrast;
    }

    public void logIn(User user) {
        currentUser = user;
        loggedIn = true;
        // If guest, keep settings from log in page
        // If user, change settings to saved
    }

    /**
     * This method is called when the user clicks log out.
     */
    public void logOut() {
        loggedIn = false;
    }

    private User currentUser;
    private boolean highContrast;
    private int textSize;
    private boolean loggedIn;
}
