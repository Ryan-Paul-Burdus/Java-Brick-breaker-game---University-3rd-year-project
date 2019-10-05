# Java-Brick-breaker-game---University-3rd-year-project
### Created by Ryan Burdus, January 2019
A classic brick breaker game made for my 3rd year final project.
This game is a recreation of the classic game "Breakout" and was **made in IntelliJ with Java** where the main features of the game are:
- Working base brick breaker game 
- Working power-up based brick breaker game
- Scores connected to a text file which can be displayed in their own menu
- Settings menu 
- Pause menu

The player will be able to play a normal game of Breakout or Super breakout, and when they finish, they will then be able to see the scores table and will then be able to choose whether to play again or to close the game and end the program. The player can also choose to return to the main menu and choose to play again on any game mode, go to score menu, or look at settings.

**All code and art used within this game has been created by myself (Ryan Burdus) and nobody else.**

## Code Highlights

- Power-up collision
```Java 
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
```

- Adding scores
```Java
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
```

- Power-up spawn timer
```Java
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
```

- Power-up creation
```Java
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
```

## How to run the game 
 - To run the program you will need to have IntelliJ 2018.1.6 installed on your computer and to load up the project once this is finished.

- To run the game as a build you will need to run the .jar that is found within this project folder.

## Screenshots

- Normal mode gameplay screen

![alt text](https://github.com/Ryan-Paul-Burdus/Java-Brick-breaker-game---University-3rd-year-project/blob/master/Screenshots/Breakout%20normal%20gameplay.png "Normal mode gameplay screen")

- Power-up mode gameplay screen

![alt text](https://github.com/Ryan-Paul-Burdus/Java-Brick-breaker-game---University-3rd-year-project/blob/master/Screenshots/breakout%20Super%20breakout.png "Power-up mode gameplay screen")

- Scores screen

![alt text](https://github.com/Ryan-Paul-Burdus/Java-Brick-breaker-game---University-3rd-year-project/blob/master/Screenshots/Breakout%20scores.png "Scores screen")

- Settings screen

![alt text](https://github.com/Ryan-Paul-Burdus/Java-Brick-breaker-game---University-3rd-year-project/blob/master/Screenshots/Breakout%20settings.png "Settings screen")
