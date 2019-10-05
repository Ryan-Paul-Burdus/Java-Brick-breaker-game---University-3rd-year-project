package Game;

import utilities.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NormalGame_View extends JComponent implements ActionListener {
    static int w = 37, h = 40;//The width and height for the game area
    static int[][] a = new int[h][w];//an array for the game area
    static int size = 20;//The size of each game space

    private Timer timer;//Timer for the game to repaint between
    static TimerManager timerManager;
    public boolean isPaused = false;

    private boolean gameRunning = true;//Bool to check if the game is running/being played
    private boolean ballIsFree = false;//Bool to check if the ball is free from the player/paddle

    private int paddleSize = 5;//The amount of array spaces for the paddle
    private boolean left = false;//Bool for the left movement
    private boolean right = false;//Bool for the right movement

    //Values for the paddles position
    private int paddleY = 34;
    private final int paddleX[] = new int[37];

    Ball ball;
    double ballRadius;
    double ballX;
    double ballY;

    Brick brick;
    public List<Brick> bricks;
    private int bricksPerRow = 11;
    private int bricksTouched;

    static ScoreManager scoremanager;
    static LivesManager livesmanager;
    static SoundsManager soundsmanager;

    static JLabel livesArea = new JLabel();
    static JLabel scoreArea = new JLabel();
    static JFrame gameFrame;
    static JPanel bottomPanel2;

    boolean isMouseControl = false;

    File brickHitSound;
    File ballBounceSound;
    File loseLifeSound;
    File backgroundMusic;

    public static void main(String[] args) {
        gameFrame = new JFrame("Ryan Burdus: Breakout game");

        scoremanager = new ScoreManager();
        livesmanager = new LivesManager();
        timerManager = new TimerManager();

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
        bottomPanel2.add(new NormalGame_View(a));
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


    public NormalGame_View(int[][] a)
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
        int paddleXNum = 16;
        for(int i = 0; i < paddleSize; i++)
        {
            paddleX[i] = paddleXNum;
            paddleXNum++;
        }

        brickHitSound = new File("brickHitSound.wav");
        ballBounceSound = new File("ballHitSound.wav");
        loseLifeSound = new File("LoseLife.wav");
        backgroundMusic = new File("backgroundMusic.wav");

        newGame();


        //Creates and starts a new timer for the game to be run on
        timerManager.updateTimeDelay(30);
        timer = new Timer(timerManager.getTimeDelay(), this);
        timer.start();
        if (gameRunning)
        {
            PlaySound(backgroundMusic, true);
        }

    }

    private void newGame()
    {
        newBall();
        setUpBricks();
        bricksTouched = 0;
    }

    static void PlaySound(File Sound, boolean isLooping)
    {
        try
        {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(Sound));
            clip.start();
            for (int i = 0; i < clip.getMicrosecondLength()/1000000; i++) { }
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
            ballX = ball.findBallX();
            ballY = ball.findBallY();
            collisionCheck();
            movePaddle();
            moveBall();

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
            for(int i = 0; i < paddleSize; i++)
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
                    paddleX[0] -= 1;
                    paddleX[1] -= 1;
                    paddleX[2] -= 1;
                    paddleX[3] -= 1;
                    paddleX[4] -= 1;

                }
                if((mouseX/20) > paddleX[2] && (paddleX[4] <= (w-2)))
                {
                    paddleX[0] += 1;
                    paddleX[1] += 1;
                    paddleX[2] += 1;
                    paddleX[3] += 1;
                    paddleX[4] += 1;
                }

            }
        }
        //These two move the player to the left or right by one each timer tick
        if(left)
        {
            for(int i = 0; i < paddleSize; i++)
            {
                paddleX[i] -= 1;
            }
        }
        if(right)
        {
            for(int i = 0; i < paddleSize; i++)
            {
                paddleX[i] += 1;
            }
        }
    }

    private Object newBall()
    {
        ball = new Ball(0, 740, 0, 810);//Creates an instance of the ball with the boundaries of the game area
        ballRadius = ball.findBallRadius();//Gets the radius of the ball
        ballIsFree = false;
        ball.setLocation((paddleX[2] * size) + ballRadius, ((paddleY-1) * size)+(ballRadius/2));//Starting location for the ball
        return ball;
    }

    private void drawBall(Graphics g)
    {
        if (gameRunning)
        {
            ball.draw(g);
        }
    }

    public void moveBall()
    {
        if (ballIsFree)
        {
            ball.moveBall();//move the ball freely
        }
        else
        {
            if(isMouseControl)
            {
                Point b = bottomPanel2.getMousePosition();
                if(b != null)
                {
                    int mouseX = (int) b.getX();
                    if(((mouseX/20) < (paddleX[2]+10) && (paddleX[0] >= 1)))
                    {
                        ball.setLocation((paddleX[2]*size) + ballRadius, ballY);

                    }
                    if((mouseX/20) > (paddleX[2]+10) && (paddleX[4] <= (w-2)))
                    {
                        ball.setLocation((paddleX[2]*size) + ballRadius, ballY);
                    }
                }
            }
            //Moves the ball when attached to the paddle
            if(left)
            {
                ball.moveBallLeft();
            }
            if(right)
            {
                ball.moveBallRight();
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

            //This checks to see if the player presses the space button to release the ball
            if(action == KeyEvent.VK_SPACE)
            {
                ballIsFree = true;
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
        if(paddleX[4] >= (w - 1))
        {
            right = false;
        }
        if(paddleX[0] <= 0)
        {
            left = false;
        }

        //This makes the ball bounce of the walls
        if((ballX-ballRadius) <= 15 || (ballX+ballRadius) >= w * size)
        {
            PlaySound(ballBounceSound, false);
            ball.collideWithSides();
        }
        if(ballY <= 15)
        {
            PlaySound(ballBounceSound, false);
            ball.collideWithTopOrBottom();
        }
        if(ballY >= ((h * size)+ballRadius))
        {
            //lose a life
            PlaySound(loseLifeSound, false);
            livesmanager.minusLives(1);
            livesmanager.updateLivesText();
            livesArea.setText(livesmanager.getLivesText());
            newBall();
        }

        //This makes the ball bounce off the top and bottom of the paddle
        if(ballX+ballRadius >= ((paddleX[0]) * size) - 1 && ballX-ballRadius <= (paddleX[4]+1) * size)
        {
            if((ballY + ballRadius) >= (paddleY)*size - 1 && (ballY - ballRadius) <= (paddleY+1)*size)
            {
                PlaySound(ballBounceSound, false);
                ball.collideWithTopOrBottom();
            }
        }

        //This makes the ball bounce of the sides of the paddle
        if(ballY >= (paddleY)*size - 1 && ballY <= (paddleY+1)*size)
        {
            if((ballX + ballRadius) >= ((paddleX[0]) * size) - 1 && (ballX - ballRadius) <= (paddleX[4]+1) * size)
            {
                PlaySound(ballBounceSound, false);
                ball.collideWithSides();
            }
        }

        //This checks the collisions for the bricks
        if(ballY <= 25*size)
        {
            for (Brick b : bricks)
            {
                double brickX = b.findBrickX();
                double brickY = b.findBrickY();


                if(b.isHit() == false)
                {
                    //This checks if the ball touches the side
                    if( (ballY <= (brickY+20)) && (ballY >= brickY) )
                    {
                        if(ballX >= (brickX - ballRadius) && ballX <= ((brickX+60) + ballRadius))
                        {
                            //touching
                            PlaySound(brickHitSound, false);
                            ball.collideWithSides();//make the ball bounce
                            Color brickColor = b.findBrickColor();
                            b.deleteBrick();
                            addToScore(brickColor);//add the points to the score
                            bricksTouched++;
                        }
                    }
                    //This checks if the ball touches the top or teh bottom
                    if( (ballX <= (brickX+60)) && (ballX >= brickX) )
                    {
                        if(ballY >= (brickY - ballRadius) && ballY <= ((brickY + 20) + ballRadius))
                        {
                            //touching
                            PlaySound(brickHitSound, false);
                            ball.collideWithTopOrBottom();//make the ball bounce
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
        try(FileWriter fw = new FileWriter("Normal_scores.txt", true);
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
