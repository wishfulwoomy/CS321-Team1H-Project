public class Session {

    /**
     *
     */
    public Session() {
        loggedIn = false;
        // Add in default text size and contrast
    }

    /**
     *
     * @return
     */
    public int getTextSize() {
        return textSize;
    }

    /**
     *
     * @return
     */
    public boolean getContrast() {
        return highContrast;
    }

    /**
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     *
     * @param highContrast
     */
    public void setContrast(boolean highContrast) {
        this.highContrast = highContrast;
    }

    public void logIn(User user) {
        currentUser = user;
        loggedIn = true;
    }

    /**
     *
     */
    public void logOut() {
        loggedIn = false;
    }

    private User currentUser;
    private boolean highContrast;
    private int textSize;
    private boolean loggedIn;
}
