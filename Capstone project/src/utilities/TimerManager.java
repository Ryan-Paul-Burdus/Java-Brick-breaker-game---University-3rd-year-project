package utilities;

public class TimerManager {
    public static volatile int timeDelay;//The delay between each timer tick
    public static volatile boolean isPaused;
    public static volatile boolean backToMenu;

    public void updateTimeDelay(int newTimeDelay) { timeDelay = newTimeDelay; }
    public int getTimeDelay() { return timeDelay; }
    public void updateIsPaused(boolean newIsPaused) { isPaused = newIsPaused; }
    public boolean getIsPaused() { return isPaused; }
    public void updateBackToMenu(boolean newBackToMenu) { backToMenu = newBackToMenu; }
    public boolean getBackTOMenu() { return backToMenu; }
}
