package Game;

import java.awt.Color;
import java.awt.Graphics;

public class Brick {
    private double brickX, brickY;
    private double brickWidth, brickHeight;
    private Color color;
    private boolean isHit;

    public Brick(double x, double y, Color c)
    {
        brickX = x;
        brickY = y;
        brickWidth = 60;
        brickHeight = 20;
        color = c;
        isHit = false;
    }

    public void draw(Graphics g)
    {
        if (this.isHit == true)
        {
            g.setColor(Color.black);
            g.fill3DRect((int) brickX, (int) brickY, (int) brickWidth, (int) brickHeight, true);
        }
        else
        {
            g.setColor(color);
            g.fill3DRect((int) brickX, (int) brickY, (int) brickWidth, (int) brickHeight, true);
        }
    }

    public double findBrickX() { return this.brickX; }
    public double findBrickY() { return this.brickY; }
    public Color findBrickColor() { return this.color; }
    public boolean isHit() { return this.isHit; }
    public void deleteBrick() { this.isHit = true; }
}
