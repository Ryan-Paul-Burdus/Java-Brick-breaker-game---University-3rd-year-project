package Game;

import utilities.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameModeSelectionMenu extends JFrame {
    public GameModeSelectionMenu(String title) {
        super(title);

        //This creates a custom icon for the window using an image
        ImageIcon logoIcon = new ImageIcon(("breakout logo.png"));
        Image logo = logoIcon.getImage();
        setIconImage(logo);

        JPanel topPanel = new JPanel();
        JPanel middlePanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        JLabel logoLabel = new JLabel(logoIcon);
        JLabel nameLabel = new JLabel("BREAKOUT");
        nameLabel.setFont(new Font("Ariel", Font.BOLD, 50));

        topPanel.add(logoLabel);
        middlePanel.add(nameLabel);

        JButton classicModeButton = new JButton("CLASSIC BREAKOUT");
        classicModeButton.setFont(new Font("Ariel", Font.BOLD, 12));
        JButton superBreakoutButton = new JButton("SUPER BREAKOUT");
        superBreakoutButton.setFont(new Font("Ariel", Font.BOLD, 12));
        JButton backButton = new JButton("BACK");
        backButton.setFont(new Font("Ariel", Font.BOLD, 12));

        bottomPanel.add(BorderLayout.NORTH, classicModeButton);
        bottomPanel.add(BorderLayout.CENTER, superBreakoutButton);
        bottomPanel.add(BorderLayout.SOUTH, backButton);

        add(BorderLayout.NORTH, topPanel);
        add(BorderLayout.CENTER, middlePanel);
        add(BorderLayout.SOUTH, bottomPanel);

        classicModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                NormalGame_View.main(null);
            }
        });

        superBreakoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                PowerUpGame_View.main(null);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                MainMenu.main(null);
            }
        });

        //These are the settings for the frame to have
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Constants.BG_COLOR);
        setPreferredSize(new Dimension(400, 350));
        pack();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        repaint();
    }
}
