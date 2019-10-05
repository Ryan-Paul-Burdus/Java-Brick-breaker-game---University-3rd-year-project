package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import utilities.Constants;
import utilities.TimerManager;

public class Ball {
    private double xMin, xMax, yMin, yMax;
    private double x, y;
    private double dx, dy;
    private double speed;
    private double radius;
    private Color color;
    private boolean ballIsFree;
    private boolean isDead;

    private int timerDelay;
    static TimerManager timerManager;

    public Ball(double left, double right, double top, double bottom)
    {
        xMin = left;
        xMax = right;
        yMin = top;
        yMax = bottom;
        radius = 10;
        color = Constants.BALL_COLOR;
        Random r = new Random();
        speed = 0.5;
        dx = (-0.75 + (0.75 - (-0.75)) * r.nextDouble()) * speed;
        dy = (-0.75 + (-0.25 - (-0.75)) * r.nextDouble()) * speed;
    }

    public void draw(Graphics g)
    {
        g.setColor(color);
        g.fillOval((int)(x - radius), (int)(y - radius), (int)(2 * radius), (int)(2 * radius));
    }

    public void setSpeed(double newSpeed)
    {
        this.speed = speed + (newSpeed);
    }
    public void setRadius(double radius) { this.radius = radius; }
    public void setBallIsFree(boolean free) { this.ballIsFree = free; }
    public void setIsDead(boolean dead) { this.isDead = dead; }

    public double findBallX()
    {
        return this.x;
    }
    public double findBallY()
    {
        return this.y;
    }
    public double findBallRadius() { return this.radius; }
    public boolean findBallIsFree() { return this.ballIsFree; }
    public boolean findIsDead() { return this.isDead; }

    public void setLocation(double x, double y)
    {
        this.x = x;
        this.y = y;
    }


    public void moveBall()
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

    public void moveBallLeft()
    {
        this.x -= 20;
    }
    public void moveBallRight() { this.x += 20; }

    public void collideWithSides()
    {
        //these change the speed depending on the angle
        this.dx = -this.dx;
    }
    public void collideWithTopOrBottom()
    {
        //these change the speed depending on the angle
        this.dy = -this.dy;
    }


    public double findBalldx() { return this.dx; }
    public double findBalldy() { return this.dy; }
}
