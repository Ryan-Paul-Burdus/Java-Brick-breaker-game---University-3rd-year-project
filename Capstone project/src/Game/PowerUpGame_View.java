package Game;

import utilities.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PowerUpGame_View extends JComponent implements ActionListener {
    static int w = 37, h = 40;//The width and height for the game area
    static int[][] a = new int[h][w];//an array for the game area
    static int size = 20;//The size of each game space

    private Timer timer;//Timer for the game to repaint between
    static TimerManager timerManager;
    public boolean isPaused = false;

    private boolean gameRunning = true;//Bool to check if the game is running/being played

    private boolean left = false;//Bool for the left movement
    private boolean right = false;//Bool for the right movement

    //Values for the paddles position
    private int paddleY = 34;
    private final int paddleX[] = new int[37];

    private int paddleXNum = 16;
    private int paddleLength = 7;//The amount of array spaces for the paddle

    Ball ball;
    public List<Ball> balls;
    double ballRadius;
    double updatedBallRadius;
    double ballX;
    double ballY;

    Brick brick;
    public List<Brick> bricks;
    private int bricksPerRow = 11;
    private int bricksTouched;


    PowerUp powerUp;
    public List<PowerUp> powerUps;
    double powerUpRadius;
    int powerUpType;
    double pX;
    double pY;

    Lazer lazer;
    public List<Lazer> lazers;
    double timeSinceActive;
    double timeBetweenShots;
    double lX;
    double lY;

    static ScoreManager scoremanager;
    static LivesManager livesmanager;
    static SoundsManager soundsmanager;

    static JLabel livesArea = new JLabel();
    static JLabel scoreArea = new JLabel();
    static JFrame gameFrame;
    static JPanel bottomPanel2;

    boolean isMouseControl = false;
    boolean canShoot = false;
    boolean ableToShoot = true;

    File brickHitSound;
    File ballBounceSound;
    File loseLifeSound;
    File backgroundMusic;
    File powerUpSound;
    File lazerShotSound;

    public static void main(String[] args)
    {
        gameFrame = new JFrame("Ryan Burdus: Breakout game");

        scoremanager = new ScoreManager();
        livesmanager = new LivesManager();
        timerManager = new TimerManager();
        soundsmanager = new SoundsManager();

        scoremanager.updateScore(0);//Starting score
        scoremanager.updateScoreText();
        livesmanager.updateLives(3);//Starting lives
        livesmanager.updateLivesText();
        timerManager.updateIsPaused(false);
        timerManager.updateBackToMenu(false);

        //This creates the top area for the score to be shown
        JPanel topPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 200, 5));
        topPanel2.setBorder(BorderFactory.createLineBorder(Constants.BG_COLOR, 2));

        livesArea = new JLabel(livesmanager.getLivesText());
        topPanel2.add(livesArea);
        scoreArea = new JLabel(scoremanager.getScoreText());
        topPanel2.add(scoreArea);
        gameFrame.add(BorderLayout.NORTH, topPanel2);

        //This creates the bottom area for the game to be shown
        bottomPanel2 = new JPanel();
        bottomPanel2.add(new PowerUpGame_View(a));
        gameFrame.add(BorderLayout.SOUTH, bottomPanel2);

        //This creates a custom icon for the window using an image
        ImageIcon logoIcon = new ImageIcon(("breakout logo.png"));
        Image logo = logoIcon.getImage();
        gameFrame.setIconImage(logo);

        //These are the settings for the frame to have
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setBackground(Constants.BG_COLOR);
        gameFrame.setVisible(true);
        gameFrame.pack();
        gameFrame.setResizable(false);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.repaint();
    }


    public PowerUpGame_View(int[][] a)
    {
        //This makes the mouse and keys all focused on the programs window
        if(!isMouseControl) { addKeyListener(new PressingKeys()); }
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        //This makes sure the array is set to the correct width and height values
        this.a = a;
        w = a[0].length;
        h = a.length;


        startGame();//Starts the game
    }

    public void startGame()
    {
        //Starting location for the paddle
        for(int i = 0; i < paddleLength; i++)
        {
            paddleX[i] = paddleXNum;
            paddleXNum++;
        }

        brickHitSound = new File("brickHitSound.wav");
        ballBounceSound = new File("ballHitSound.wav");
        loseLifeSound = new File("LoseLife.wav");
        backgroundMusic = new File("backgroundMusic.wav");
        powerUpSound = new File("powerUpSound.wav");
        lazerShotSound = new File("LazerShot.wav");

        newGame();
        if (gameRunning)
        {
            PlaySound(backgroundMusic, true, false);
        }

        //Creates and starts a new timer for the game to be run on
        timerManager.updateTimeDelay(30);
        timer = new Timer(timerManager.getTimeDelay(), this);
        timer.start();


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                newPowerUp();
            }
        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        isPaused = timerManager.getIsPaused();
        if (!isPaused)
        {
            service.scheduleAtFixedRate(runnable, 0, 3, TimeUnit.SECONDS);
        }
        else { service.scheduleAtFixedRate(runnable, 3, 999, TimeUnit.DAYS);}
    }

    private void newGame()
    {
        powerUps = new ArrayList<PowerUp>();
        lazers = new ArrayList<Lazer>();
        balls = new ArrayList<Ball>();
        updatedBallRadius = 10;
        newBall();
        setUpBricks();
        bricksTouched = 0;
        timeBetweenShots = 0;
    }

    public void PlaySound(File Sound, boolean isLooping, boolean stopPlaying)
    {
        try
        {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(Sound));
            if (!stopPlaying)
            {
                clip.start();
                if (isLooping)
                {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
                for (int i = 0; i < clip.getMicrosecondLength()/1000000; i++) { }
            }
            else
            {
                clip.stop();
            }
        }
        catch (Exception e)
        {

        }
    }



    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(gameRunning)
        {
            collisionCheck();
            movePaddle();
            moveBall();
            movePowerUp();
            if (!lazers.isEmpty())
            {
                moveLazer();
            }

            if (canShoot)
            {
                int durationOfShooting = timer.getDelay();
                timeSinceActive++;
                if (timeSinceActive >= (durationOfShooting * 3))
                {
                    System.out.println("Cant shoot");
                    canShoot = false;
                }
            }
            if (!ableToShoot)
            {
                int durationOfShooting = timer.getDelay();
                timeBetweenShots++;
                if (timeBetweenShots >= (durationOfShooting*0.55))
                {
                    timeBetweenShots = 0;
                    ableToShoot = true;
                }
            }


            if(timerManager.getBackTOMenu())
            {
                gameOver();
            }

            if(livesmanager.getLives() == 0)
            {
                gameOver();
            }
        }
        repaint();
    }


    private void setUpBricks()
    {
        bricks = new ArrayList<Brick>();
        for (int y = 3; y < 13; y++)
        {
            int x = 40;
            if(y>=3 && y<7)
            {
                for(int i = 0; i < bricksPerRow; i++)
                {
                    brick = new Brick(x, (y*20), Color.red);
                    bricks.add(brick);
                    x+=60;
                }
            }
            if(y>=7 && y<10)
            {
                for(int i = 0; i < bricksPerRow; i++)
                {
                    brick = new Brick(x, (y*20), Color.orange);
                    bricks.add(brick);
                    x+=60;
                }
            }
            if(y>=10 && y<12)
            {
                for(int i = 0; i < bricksPerRow; i++)
                {
                    brick = new Brick(x, (y*20), Color.yellow);
                    bricks.add(brick);
                    x+=60;
                }
            }
            if(y>=12)
            {
                for(int i = 0; i < bricksPerRow; i++)
                {
                    brick = new Brick(x, (y*20), Color.green);
                    bricks.add(brick);
                    x+=60;
                }
            }
        }
    }

    private void drawBricks(Graphics g)
    {
        if(gameRunning)
        {
            for (Brick b : bricks)
            {
                b.draw(g);
            }
        }
    }

    private void drawPaddle(Graphics g)
    {
        if(gameRunning)
        {
            g.setColor(Constants.PADDLE_COLOR);
            for(int i = 0; i < paddleLength; i++)
            {
                g.fillRect(paddleX[i] * size,paddleY * size, size, size);
            }
        }
    }

    public void movePaddle()
    {
        if(isMouseControl)
        {
            Point b = bottomPanel2.getMousePosition();
            if(b != null)
            {
                int mouseX = (int) b.getX();
                if(((mouseX/20) < paddleX[2] && (paddleX[0] >= 1)))
                {
                    for (int i = 0; i < paddleLength; i++)
                    {
                        paddleX[i] -= 1;
                    }
                    paddleXNum -= 1;
                }
                if((mouseX/20) > paddleX[2] && (paddleX[paddleLength-1] <= (w-2)))
                {
                    for (int i = 0; i < paddleLength; i++)
                    {
                        paddleX[i] += 1;
                    }
                    paddleXNum += 1;
                }
            }
        }
        //These two move the player to the left or right by one each timer tick
        if(left)
        {
            for(int i = 0; i < paddleLength; i++)
            {
                paddleX[i] -= 1;
            }
            paddleXNum -= 1;
        }
        if(right)
        {
            for(int i = 0; i < paddleLength; i++)
            {
                paddleX[i] += 1;
            }
            paddleXNum += 1;
        }
    }

    private Object newBall()
    {
        ball = new Ball(0, 740, 0, 810);//Creates an instance of the ball with the boundaries of the game area
        ballRadius = ball.findBallRadius();//Gets the radius of the ball
        ball.setBallIsFree(false);
        ball.setIsDead(false);
        ball.setLocation((paddleX[paddleLength/2] * size) + ballRadius, (paddleY * size)-ballRadius*2.5);//Starting location for the ball
        balls.add(ball);
        return ball;
    }

    private Object newPowerUpBall()
    {
        ball = new Ball(0, 740, 0, 810);//Creates an instance of the ball with the boundaries of the game area
        ballRadius = updatedBallRadius;//Gets the radius of the ball
        ball.setBallIsFree(true);
        ball.setIsDead(false);
        ball.setLocation((paddleX[paddleLength/2] * size) + ballRadius, (paddleY * size)-ballRadius*2.5);//Starting location for the ball
        balls.add(ball);
        return ball;
    }

    private void drawBall(Graphics g)
    {
        if (gameRunning)
        {
            for (Ball b : balls)
            {
                if (!b.findIsDead())
                {
                    b.draw(g);
                }
            }
        }
    }

    public void moveBall()
    {
        for (Ball b : balls)
        {
            if (b.findBallIsFree())
            {
                b.moveBall();//move the ball freely
            }
            else
            {
                if(isMouseControl)
                {
                    ballY = b.findBallY();
                    Point bP = bottomPanel2.getMousePosition();
                    if(bP != null)
                    {
                        int mouseX = (int) bP.getX();
                        if(((mouseX/20) < (paddleX[paddleLength/2]+10) && (paddleX[0] >= 1)))
                        {
                            b.setLocation((paddleX[paddleLength/2]*size) + ballRadius, ballY);
                        }
                        if((mouseX/20) > (paddleX[paddleLength/2]+10) && (paddleX[4] <= (w-2)))
                        {
                            b.setLocation((paddleX[paddleLength/2]*size) + ballRadius, ballY);
                        }
                    }
                }
                //Moves the ball when attached to the paddle
                if(left)
                {
                    b.moveBallLeft();
                }
                if(right)
                {
                    b.moveBallRight();
                }
            }
        }

    }

    private class PressingKeys extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            int action = e.getKeyCode();

            if (!isMouseControl)
            {
                //This checks if the player is holding down a movement key and moves the paddle
                if(action == KeyEvent.VK_LEFT)
                {
                    left = true;
                    right = false;
                }
                if(action == KeyEvent.VK_RIGHT)
                {
                    right = true;
                    left = false;
                }
            }

            if (canShoot)
            {
                if(action == KeyEvent.VK_S && ableToShoot)
                {
                    PlaySound(lazerShotSound, false, false);
                    newLazer();
                    ableToShoot = false;
                }
            }

            //This checks to see if the player presses the space button to release the ball
            if(action == KeyEvent.VK_SPACE)
            {
                for (Ball b : balls)
                {
                    if (!b.findBallIsFree())
                    {
                        b.setBallIsFree(true);
                    }
                }
            }

            if(action == KeyEvent.VK_C)
            {
                if(isMouseControl) { isMouseControl = false; }
                else { isMouseControl = true; }
            }

            if(action == KeyEvent.VK_ESCAPE)
            {
                left = false;
                right = false;
                timerManager.updateIsPaused(true);
                timerManager.updateTimeDelay(999999999);
                new PauseMenu("Paused game!");
            }
        }

        @Override
        public void keyReleased(KeyEvent e)
        {
            int action = e.getKeyCode();

            //This checks if the player stops holding down a movement key and stops the paddle
            if((action == KeyEvent.VK_LEFT))
            {
                left = false;
            }
            if((action == KeyEvent.VK_RIGHT))
            {
                right = false;
            }
        }

    }

    private void collisionCheck()
    {
        //This makes the paddle stay within the boundaries of the game
        if(paddleX[paddleLength-1] >= (w - 1))
        {
            right = false;
        }
        if(paddleX[0] <= 0)
        {
            left = false;
        }

        OUTER_LOOP2:
        for (Ball ba : balls)
        {
            boolean ballDead = ba.findIsDead();
            if (!ballDead)
            {
                ballX = ba.findBallX();
                ballY = ba.findBallY();
                ballRadius = ba.findBallRadius();
                //This makes the ball bounce of the walls
                if((ballX-ballRadius) <= 15 || (ballX+ballRadius) >= w * size)
                {
                    PlaySound(ballBounceSound, false, false);
                    ba.collideWithSides();
                }
                if(ballY <= 15)
                {
                    PlaySound(ballBounceSound, false, false);
                    ba.collideWithTopOrBottom();
                }
                if(ballY >= ((h * size)+ballRadius))
                {
                    //lose a life
                    if (balls.size() == 1)
                    {
                        PlaySound(loseLifeSound, false, false);
                        ba.setIsDead(true);
                        balls.remove(balls.indexOf(ba));
                        livesmanager.minusLives(1);
                        livesmanager.updateLivesText();
                        livesArea.setText(livesmanager.getLivesText());
                        newBall();
                        break OUTER_LOOP2;
                    }
                    else
                    {
                        ba.setIsDead(true);
                        balls.remove(balls.indexOf(ba));
                        break OUTER_LOOP2;
                    }
                }


                //This makes the ball bounce off the top and bottom of the paddle
                if(ballX+ballRadius >= ((paddleX[0]) * size) - 1 && ballX-ballRadius <= (paddleX[paddleLength-1]+1) * size)
                {
                    if((ballY + ballRadius) >= (paddleY)*size - 1 && (ballY - ballRadius) <= (paddleY+1)*size)
                    {
                        PlaySound(ballBounceSound, false, false);
                        ba.collideWithTopOrBottom();
                    }
                }

                //This makes the ball bounce of the sides of the paddle
                if(ballY >= (paddleY)*size - 1 && ballY <= (paddleY+1)*size)
                {
                    if((ballX + ballRadius) >= ((paddleX[0]) * size) - 1 && (ballX - ballRadius) <= (paddleX[paddleLength-1]+1) * size)
                    {
                        PlaySound(ballBounceSound, false, false);
                        ba.collideWithSides();
                    }
                }

                //This checks the collisions for the bricks
                if(ballY <= 25*size)
                {
                    for (Brick b : bricks)
                    {
                        double brickX = b.findBrickX();
                        double brickY = b.findBrickY();


                        if(!b.isHit())
                        {
                            //This checks if the ball touches the side
                            if( (ballY <= (brickY+20)) && (ballY >= brickY) )
                            {
                                if(ballX >= (brickX - ballRadius) && ballX <= ((brickX+60) + ballRadius))
                                {
                                    //touching
                                    PlaySound(brickHitSound, false, false);
                                    ba.collideWithSides();//make the ball bounce
                                    Color brickColor = b.findBrickColor();
                                    b.deleteBrick();
                                    addToScore(brickColor);//add the points to the score
                                    bricksTouched++;
                                }
                            }
                            //This checks if the ball touches the top or the bottom
                            if( (ballX <= (brickX+60)) && (ballX >= brickX) )
                            {
                                if(ballY >= (brickY - ballRadius) && ballY <= ((brickY + 20) + ballRadius))
                                {
                                    //touching
                                    PlaySound(brickHitSound, false, false);
                                    ba.collideWithTopOrBottom();//make the ball bounce
                                    Color brickColor = b.findBrickColor();
                                    b.deleteBrick();
                                    addToScore(brickColor);//add the points to the score
                                    bricksTouched++;
                                }
                            }
                        }
                        if(bricksTouched == 110)
                        {
                            newGame();
                        }
                    }
                }
            }
        }

        powerUpCollisionCheck();
        if (!lazers.isEmpty())
        {
            lazerCollisionCheck();
        }
    }

    public Object newPowerUp()
    {
        double randomDouble = Math.random() * 8 + 1;
        int randomInt = (int)randomDouble;
        powerUp = new PowerUp(0, 740, randomInt);//creates a new powerUp
        if (randomInt == 1 || randomInt == 3)
        {
            powerUp.setRadius(30);
        }
        if (randomInt == 2)
        {
            powerUp.setRadius(35);
        }
        if (randomInt == 4 || randomInt == 5)
        {
            powerUp.setRadius(25);
        }
        if (randomInt == 6)
        {
            powerUp.setRadius(20);
        }
        if (randomInt == 7 || randomInt == 8)
        {
            powerUp.setRadius(15);
        }
        powerUpRadius = powerUp.findPowerUpRadius();//sets the radius of the powerUp
        powerUp.setLocation((Math.random() * 680 + 60), -40);//sets a random location of the power up
        powerUps.add(powerUp);//adds the powerUp to the list of active powerUps
        return powerUp;
    }

    private void drawPowerUp(Graphics g)
    {
        for (PowerUp p : powerUps)
        {
            boolean hit = p.findisHit();
            if (!hit)
            {
                ImageIcon powerUpIcon;
                Image powerUpImage;
                powerUpType = p.findPowerUpType();
                powerUpRadius = p.findPowerUpRadius();
                pX = p.findPowerUpX();
                pY = p.findPowerUpY();

                if (powerUpType == 1)//increase paddle size
                {
                    powerUpIcon = new ImageIcon(("powerUp1.png"));
                    powerUpImage = powerUpIcon.getImage();
                    g.drawImage(powerUpImage, (int)(pX - powerUpRadius), (int)(pY - powerUpRadius), (int)(2 * powerUpRadius), (int)(2 * powerUpRadius),null);
                }
                if (powerUpType == 2)//add a ball
                {
                    powerUpIcon = new ImageIcon(("powerUp2.png"));
                    powerUpImage = powerUpIcon.getImage();
                    g.drawImage(powerUpImage, (int)(pX - powerUpRadius), (int)(pY - powerUpRadius), (int)(2 * powerUpRadius), (int)(2 * powerUpRadius),null);
                }
                if (powerUpType == 3)//decrease paddle size
                {
                    powerUpIcon = new ImageIcon(("powerUp3.png"));
                    powerUpImage = powerUpIcon.getImage();
                    g.drawImage(powerUpImage, (int)(pX - powerUpRadius), (int)(pY - powerUpRadius), (int)(2 * powerUpRadius), (int)(2 * powerUpRadius),null);
                }
                if (powerUpType == 4)//increase ball size
                {
                    powerUpIcon = new ImageIcon(("powerUp4.png"));
                    powerUpImage = powerUpIcon.getImage();
                    g.drawImage(powerUpImage, (int)(pX - powerUpRadius), (int)(pY - powerUpRadius), (int)(2 * powerUpRadius), (int)(2 * powerUpRadius),null);
                }
                if (powerUpType == 5)//decrease ball size
                {
                    powerUpIcon = new ImageIcon(("powerUp5.png"));
                    powerUpImage = powerUpIcon.getImage();
                    g.drawImage(powerUpImage, (int)(pX - powerUpRadius), (int)(pY - powerUpRadius), (int)(2 * powerUpRadius), (int)(2 * powerUpRadius),null);
                }
                if (powerUpType == 6)//add 3 balls
                {
                    powerUpIcon = new ImageIcon(("powerUp6.png"));
                    powerUpImage = powerUpIcon.getImage();
                    g.drawImage(powerUpImage, (int)(pX - powerUpRadius), (int)(pY - powerUpRadius), (int)(2 * powerUpRadius), (int)(2 * powerUpRadius),null);
                }
                if (powerUpType == 7)//shoot for a few seconds
                {
                    powerUpIcon = new ImageIcon(("powerUp7.png"));
                    powerUpImage = powerUpIcon.getImage();
                    g.drawImage(powerUpImage, (int)(pX - powerUpRadius), (int)(pY - powerUpRadius), (int)(2 * powerUpRadius), (int)(2 * powerUpRadius),null);
                }
                if (powerUpType == 8)//take away a life
                {
                    powerUpIcon = new ImageIcon(("powerUp8.png"));
                    powerUpImage = powerUpIcon.getImage();
                    g.drawImage(powerUpImage, (int)(pX - powerUpRadius), (int)(pY - powerUpRadius), (int)(2 * powerUpRadius), (int)(2 * powerUpRadius),null);
                }
            }
        }
    }

    public void movePowerUp()
    {
        for (PowerUp p : powerUps)
        {
            boolean hit = p.findisHit();
            if (!hit)
            {
                p.movePowerUp();
            }
        }
    }

    public Object newLazer()
    {
        lazer = new Lazer();
        lazer.setLocation((paddleX[paddleLength/2] * size)+10, (paddleY * size)-10);
        lazers.add(lazer);
        return lazer;
    }

    public void drawLazer(Graphics g)
    {
        for (Lazer l : lazers)
        {
            boolean destroyed = l.findIsDestroyed();
            if (!destroyed)
            {
                l.draw(g);
            }
        }
    }

    public void moveLazer()
    {
        for (Lazer l : lazers)
        {
            boolean destroyed = l.findIsDestroyed();
            if (!destroyed)
            {
                l.moveLazer();
            }
        }
    }

    public void lazerCollisionCheck()
    {
        OUTER_LOOP3:
        for (Lazer l : lazers)
        {
            boolean destroyed = l.findIsDestroyed();
            if (!destroyed)
            {
                lX = l.findLazerX();
                lY = l.findLazerY();

                double rightX = lX + 3.75;
                double topY = lY - 10;

                double leftX = lX - 3.75;
                double bottomY = lY + 10;

                if (bottomY <= 0)
                {
                    l.setIsDestroyed(true);
                    lazers.remove(lazers.indexOf(l));
                    break OUTER_LOOP3;
                }

                if (lY <= 25*size)
                {
                    for (Brick b : bricks)
                    {
                        double brickX = b.findBrickX();
                        double brickY = b.findBrickY();

                        if (!b.isHit())
                        {
                            if ((rightX >= brickX) && (leftX <= brickX+60))
                            {
                                if (topY <= (brickY+20))
                                {
                                    PlaySound(brickHitSound, false, false);
                                    l.setIsDestroyed(true);
                                    lazers.remove(lazers.indexOf(l));

                                    Color brickColor = b.findBrickColor();
                                    b.deleteBrick();
                                    addToScore(brickColor);//add the points to the score
                                    bricksTouched++;

                                    break OUTER_LOOP3;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void powerUpCollisionCheck()
    {
        OUTER_LOOP:
        for (PowerUp p : powerUps)
        {
            boolean hit = p.findisHit();
            if (!hit)
            {
                pX = p.findPowerUpX();
                pY = p.findPowerUpY();
                powerUpType = p.findPowerUpType();
                powerUpRadius = p.findPowerUpRadius();

                ballRadius = ball.findBallRadius();

                double rightX = pX + powerUpRadius;
                double topY = pY - powerUpRadius;

                double leftX = pX - powerUpRadius;
                double bottomY = pY + powerUpRadius;

                if (topY >= h * size)
                {
                    p.setIsHit(true);
                    powerUps.remove(powerUps.indexOf(p));
                    System.out.println("deleted powerUp");
                    break OUTER_LOOP;
                }

                if (rightX >= ((paddleX[0]) * size) - 1 && leftX <= (paddleX[paddleLength-1]+1) * size)
                {
                    if (bottomY >= paddleY*size && topY <= (paddleY+1)*size)
                    {
                        PlaySound(powerUpSound, false, false);
                        if (powerUpType == 1)
                        {
                            if (paddleLength <= 13)
                            {
                                for (int i = 0; i < paddleLength; i++)
                                {
                                    paddleX[i] -= 1;
                                }
                                paddleXNum -= 1;
                                paddleLength += 2;
                                for (int i = (paddleLength-2); i < paddleLength; i++)
                                {
                                    paddleX[i] = paddleXNum;
                                    paddleXNum++;
                                }
                            }
                            System.out.println("increase paddle");
                            p.setIsHit(true);
                            powerUps.remove(powerUps.indexOf(p));
                            break OUTER_LOOP;
                        }
                        if (powerUpType == 2)
                        {
                            newPowerUpBall();
                            System.out.println("add a ball");
                            p.setIsHit(true);
                            powerUps.remove(powerUps.indexOf(p));
                            break OUTER_LOOP;
                        }
                        if (powerUpType == 3)
                        {
                            if (paddleLength >= 5)
                            {
                                for (int i = 0; i < paddleLength; i++)
                                {
                                    paddleX[i] += 1;
                                }
                                paddleLength -= 2;
                                paddleXNum--;
                            }
                            System.out.println("decrease paddle");
                            p.setIsHit(true);
                            powerUps.remove(powerUps.indexOf(p));
                            break OUTER_LOOP;
                        }
                        if (powerUpType == 4)
                        {
                            if (ballRadius <= 20)
                            {
                                for (Ball b : balls)
                                {
                                    b.setRadius(ballRadius+5);
                                }
                                System.out.println("radius increased");
                            }
                            p.setIsHit(true);
                            powerUps.remove(powerUps.indexOf(p));
                            break OUTER_LOOP;
                        }
                        if (powerUpType == 5)
                        {
                            if (ballRadius >= 10)
                            {
                                for (Ball b : balls)
                                {
                                    b.setRadius(ballRadius-5);
                                }
                                System.out.println("radius decreased");
                            }
                            p.setIsHit(true);
                            powerUps.remove(powerUps.indexOf(p));
                            break OUTER_LOOP;
                        }
                        if (powerUpType == 6)
                        {
                            newPowerUpBall();
                            newPowerUpBall();
                            newPowerUpBall();
                            System.out.println("add 3 balls");
                            p.setIsHit(true);
                            powerUps.remove(powerUps.indexOf(p));
                            break OUTER_LOOP;
                        }
                        if (powerUpType == 7)
                        {
                            canShoot = true;
                            timeSinceActive = 0;
                            System.out.println("shoot");
                            p.setIsHit(true);
                            powerUps.remove(powerUps.indexOf(p));
                            break OUTER_LOOP;
                        }
                        if (powerUpType == 8)
                        {
                            livesmanager.minusLives(1);
                            livesmanager.updateLivesText();
                            livesArea.setText(livesmanager.getLivesText());
                            System.out.println("life taken");
                            p.setIsHit(true);
                            powerUps.remove(powerUps.indexOf(p));
                            break OUTER_LOOP;
                        }
                    }
                }
            }
        }
    }

    public void addToScore(Color brickColor)
    {
        if(brickColor.equals(Color.red))
        {
            scoremanager.addScore(40);
            scoremanager.updateScoreText();
            scoreArea.setText(scoremanager.getScoreText());
        }
        if(brickColor.equals(Color.orange))
        {
            scoremanager.addScore(20);
            scoremanager.updateScoreText();
            scoreArea.setText(scoremanager.getScoreText());
        }
        if(brickColor.equals(Color.yellow))
        {
            scoremanager.addScore(10);
            scoremanager.updateScoreText();
            scoreArea.setText(scoremanager.getScoreText());
        }
        if(brickColor.equals(Color.green))
        {
            scoremanager.addScore(5);
            scoremanager.updateScoreText();
            scoreArea.setText(scoremanager.getScoreText());
        }
    }

    public void paintComponent(Graphics g)
    {
        for (int i = 0; i < w; i++)
        {
            for (int j = 0; j < h; j++)
            {
                g.setColor(Color.black);
                g.fill3DRect(i * size, j * size, size, size, true);
            }
        }
        drawBricks(g);
        drawPaddle(g);
        drawBall(g);
        drawPowerUp(g);

        if (!lazers.isEmpty())
        {
            drawLazer(g);
        }
    }

    private void gameOver()
    {
        gameRunning = false;
        timer.stop();
        if(!timerManager.getIsPaused())
        {
            try{
                writeToFile();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        gameFrame.dispose();
        if(!timerManager.getBackTOMenu()) { GameOverMenu.main(null); }
    }

    private void writeToFile() throws IOException
    {
        try(FileWriter fw = new FileWriter("PowerUp_scores.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(scoremanager.getScore());
        }
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(w * size, h * size);//This makes the window the size of the array/game space
    }
}
