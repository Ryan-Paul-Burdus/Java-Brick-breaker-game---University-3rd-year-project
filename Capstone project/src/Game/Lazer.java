package Game;

import utilities.TimerManager;

import java.awt.*;

public class Lazer {
    private double x, y;
    private double xLength, yLength;
    private double dx, dy;
    private double speed;
    private boolean isDestroyed;

    private int timerDelay;
    static TimerManager timerManager;

    public Lazer()
    {
        xLength = 7.5;
        yLength = 20;
        speed = 0.6;
        dx = 0 * speed;
        dy = -1 * speed;
        isDestroyed = false;
    }

    public void setIsDestroyed(boolean isDead) { this.isDestroyed = isDead; }

    public boolean findIsDestroyed() { return this.isDestroyed; }
    public double findLazerX() { return this.x; }
    public double findLazerY() { return this.y; }

    public void setLocation(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g)
    {
        g.setColor(Color.red);
        g.fillOval((int)(x - this.xLength/2), (int)(y - this.yLength/2), (int)xLength, (int)yLength);
    }

    public void moveLazer()
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
