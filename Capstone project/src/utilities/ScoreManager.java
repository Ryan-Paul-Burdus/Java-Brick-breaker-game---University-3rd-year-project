package utilities;

public class ScoreManager {
    public static volatile int score;
    public static volatile String scoreString;

    public void updateScore(int newScore)
    {
        score = newScore;
    }
    public void addScore(int scoreToAdd) { score = score + scoreToAdd; }
    public int getScore()
    {
        return score;
    }
    public void updateScoreText()
    {
        scoreString = "Score: " + getScore();
    }
    public String getScoreText() {
        return scoreString;
    }
}
