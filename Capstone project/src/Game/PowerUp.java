package Game;

import utilities.TimerManager;


public class PowerUp {
    private double xMin, xMax;
    private int typeOfPowerUps;
    private double x, y;
    private double dx, dy;
    private double speed;
    private double radius;
    private boolean isHit;

    private int timerDelay;
    static TimerManager timerManager;

    public PowerUp(double left, double right, int powerUpType)
    {
        xMin = left;
        xMax = right;
        typeOfPowerUps = powerUpType;
        speed = 0.035;
        dx = 0 * speed;
        dy = 1 * speed;
        isHit = false;
    }

    public void setLocation(double x, double y) { this.x = x; this.y = y; }
    public void setRadius(double radius) { this.radius = radius; }
    public void setIsHit(boolean hit) { this.isHit = hit; }


    public double findPowerUpRadius() { return this.radius; }
    public double findPowerUpX()
    {
        return this.x;
    }
    public double findPowerUpY()
    {
        return this.y;
    }
    public int findPowerUpType() { return this.typeOfPowerUps; }
    public boolean findisHit() { return this.isHit; }



    public void movePowerUp()
    {
        timerManager = new TimerManager();
        timerDelay = timerManager.getTimeDelay();
        boolean isPaused = timerManager.getIsPaused();
        if(!isPaused)
        {
            x += dx * timerDelay;
            y += dy * timerDelay;
        }
        else { }
    }
}
