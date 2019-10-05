package Game;

import utilities.Constants;
import utilities.SoundsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainMenu {


    public static void main(String[] args) {
        JFrame menuFrame = new JFrame("Ryan Burdus: Breakout game");


        //This creates a custom icon for the window using an image
        ImageIcon logoIcon = new ImageIcon(("breakout logo.png"));
        Image logo = logoIcon.getImage();
        menuFrame.setIconImage(logo);

        JPanel topPanel = new JPanel();
        JPanel middlePanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        JLabel logoLabel = new JLabel(logoIcon);
        JLabel nameLabel = new JLabel("BREAKOUT");
        nameLabel.setFont(new Font("Ariel", Font.BOLD, 50));

        topPanel.add(logoLabel);
        middlePanel.add(nameLabel);

        JButton playButton = new JButton("PLAY");
        playButton.setFont(new Font("Ariel", Font.BOLD, 12));
        JButton scoreBoardButton = new JButton("SCOREBOARD");
        scoreBoardButton.setFont(new Font("Ariel", Font.BOLD, 12));
        JButton settingsButton = new JButton("SETTINGS");
        settingsButton.setFont(new Font("Ariel", Font.BOLD, 12));
        JButton exitButton = new JButton("EXIT");
        exitButton.setFont(new Font("Ariel", Font.BOLD, 12));

        bottomPanel.add(BorderLayout.NORTH, playButton);
        bottomPanel.add(BorderLayout.CENTER, scoreBoardButton);
        bottomPanel.add(BorderLayout.CENTER, settingsButton);
        bottomPanel.add(BorderLayout.SOUTH, exitButton);

        menuFrame.add(BorderLayout.NORTH, topPanel);
        menuFrame.add(BorderLayout.CENTER, middlePanel);
        menuFrame.add(BorderLayout.SOUTH, bottomPanel);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();
                new GameModeSelectionMenu("Ryan Burdus: Breakout game");
            }
        });

        scoreBoardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();
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
                menuFrame.dispose();
                new SettingsMenu("Ryan Burdus: Breakout game");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //These are the settings for the frame to have
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setBackground(Constants.BG_COLOR);
        menuFrame.setPreferredSize(new Dimension(400, 350));
        menuFrame.pack();
        menuFrame.setVisible(true);
        menuFrame.setResizable(false);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.repaint();
    }
}
