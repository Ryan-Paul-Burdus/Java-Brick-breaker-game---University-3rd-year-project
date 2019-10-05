package Game;

import utilities.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GameOverMenu {
    public static void main(String[] args) {
        JFrame gameOverMenuFrame = new JFrame("Ryan Burdus: Breakout game");


        //This creates a custom icon for the window using an image
        ImageIcon logoIcon = new ImageIcon(("breakout logo.png"));
        Image logo = logoIcon.getImage();
        gameOverMenuFrame.setIconImage(logo);

        JPanel topPanel = new JPanel();
        JPanel middlePanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        JLabel logoLabel = new JLabel(logoIcon);
        JLabel gameOverLabel = new JLabel("GAME OVER!");
        gameOverLabel.setFont(new Font("Ariel", Font.BOLD, 50));

        topPanel.add(logoLabel);
        middlePanel.add(gameOverLabel);

        JButton playAgainButton = new JButton("PLAY AGAIN");
        playAgainButton.setFont(new Font("Ariel", Font.BOLD, 12));
        JButton scoreBoardButton = new JButton("SCOREBOARD");
        scoreBoardButton.setFont(new Font("Ariel", Font.BOLD, 12));
        JButton settingsButton = new JButton("SETTINGS");
        settingsButton.setFont(new Font("Ariel", Font.BOLD, 12));
        JButton backButton = new JButton("BACK");
        backButton.setFont(new Font("Ariel", Font.BOLD, 12));

        bottomPanel.add(BorderLayout.CENTER, playAgainButton);
        bottomPanel.add(BorderLayout.CENTER, scoreBoardButton);
        bottomPanel.add(BorderLayout.CENTER, settingsButton);
        bottomPanel.add(BorderLayout.CENTER, backButton);

        gameOverMenuFrame.add(BorderLayout.NORTH, topPanel);
        gameOverMenuFrame.add(BorderLayout.CENTER, middlePanel);
        gameOverMenuFrame.add(BorderLayout.SOUTH, bottomPanel);

        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameOverMenuFrame.dispose();
                NormalGame_View.main(null);
            }
        });

        scoreBoardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameOverMenuFrame.dispose();
                try {
                    new ScoreboardMenu("Ryan Burdus: Breakout game");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //send to the settings frame
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameOverMenuFrame.dispose();
                MainMenu.main(null);
            }
        });

        //These are the settings for the frame to have
        gameOverMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameOverMenuFrame.setBackground(Constants.BG_COLOR);
        gameOverMenuFrame.setPreferredSize(new Dimension(375, 350));
        gameOverMenuFrame.pack();
        gameOverMenuFrame.setVisible(true);
        gameOverMenuFrame.setResizable(false);
        gameOverMenuFrame.setLocationRelativeTo(null);
        gameOverMenuFrame.repaint();
    }
}
