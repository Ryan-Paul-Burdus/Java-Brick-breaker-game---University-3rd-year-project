package utilities;

public class LivesManager {
    public static volatile int lives;
    public static volatile String livesString;

    public void updateLives(int newLives) { lives = newLives; }
    public void minusLives(int livesLost) { lives = lives - livesLost; }
    public void addLives(int livesToAdd) { lives = lives + livesToAdd; }
    public int getLives() { return lives; }
    public void updateLivesText() { livesString = "Lives: " + getLives(); }
    public String getLivesText() { return livesString; }
}
