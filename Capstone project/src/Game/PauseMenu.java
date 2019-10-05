package Game;

import utilities.TimerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PauseMenu extends JFrame implements ActionListener {
    static TimerManager timerManager;

    public PauseMenu(String title) {
        super(title);
        timerManager = new TimerManager();
        addKeyListener(new PressingKeys());
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        //This creates a custom icon for the window using an image
        ImageIcon logoIcon = new ImageIcon(("breakout logo.png"));
        Image logo = logoIcon.getImage();
        setIconImage(logo);

        JPanel panel1 = new JPanel();
        JLabel backToGame = new JLabel("PRESS 'ESC' TO RETURN TO THE GAME");
        panel1.add(backToGame);
        add(panel1, BorderLayout.NORTH);

        JPanel panel2 = new JPanel();
        JButton mainMenuButton = new JButton("MAIN MENU");
        JButton exitButton = new JButton("EXIT");
        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                timerManager.updateBackToMenu(true);
                MainMenu.main(null);
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { System.exit(0); }
        });
        panel2.add(mainMenuButton);
        panel2.add(exitButton);
        add(panel2, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(300, 100);
        setVisible(true);
        setLocationRelativeTo(null);
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private class PressingKeys extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e) {
            int action = e.getKeyCode();

            if(action == KeyEvent.VK_ESCAPE)
            {
                timerManager.updateIsPaused(false);
                timerManager.updateTimeDelay(30);
                dispose();
            }
        }
    }
}
