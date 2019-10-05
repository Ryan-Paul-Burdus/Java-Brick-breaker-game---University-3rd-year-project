package Game;

import utilities.SoundsManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.ByteOrder;

public class SettingsMenu extends JFrame implements ActionListener, ChangeListener {

    static SoundsManager soundsmanager;

    public SettingsMenu(String title){
        super(title);
        addKeyListener(new PressingKeys());
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        soundsmanager = new SoundsManager();


        //This creates a custom icon for the window using an image
        ImageIcon logoIcon = new ImageIcon(("breakout logo.png"));
        Image logo = logoIcon.getImage();
        setIconImage(logo);

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        JPanel topPanel = new JPanel();
        JPanel middlePanel = new JPanel(new GridLayout(0, 1));
        JPanel bottomPanel = new JPanel();

        JPanel paddleColorPanel = new JPanel();
        JPanel ballColorPanel = new JPanel();
        JPanel musicVolumePanel = new JPanel();
        JPanel sfxVolumePanel = new JPanel();
        JPanel displayModePanel = new JPanel();
        JPanel difficultyPanel = new JPanel();

        //Creates the settings title
        JLabel settingsLabel = new JLabel("<html><b><u>SETTINGS</u></b></html>");
        Font settingsFont = new Font("Ariel", Font.BOLD, 50);
        settingsLabel.setFont(settingsFont);


        //Creates the paddle color settings
        JLabel paddleColorLabel = new JLabel("PADDLE COLOUR:");
        paddleColorLabel.setFont(new Font("Ariel", Font.BOLD, 20));
        JRadioButton whitePaddleColorButton = new JRadioButton("WHITE");
        JRadioButton greenPaddleColorButton = new JRadioButton("GREEN");
        JRadioButton bluePaddleColorButton = new JRadioButton("BLUE");
        JRadioButton redPaddleColorButton = new JRadioButton("RED");
        ButtonGroup paddleColorGroup = new ButtonGroup();
        paddleColorGroup.add(whitePaddleColorButton);
        paddleColorGroup.add(greenPaddleColorButton);
        paddleColorGroup.add(bluePaddleColorButton);
        paddleColorGroup.add(redPaddleColorButton);
        paddleColorPanel.add(paddleColorLabel, BorderLayout.WEST);
        paddleColorPanel.add(whitePaddleColorButton, BorderLayout.EAST);
        paddleColorPanel.add(greenPaddleColorButton, BorderLayout.EAST);
        paddleColorPanel.add(bluePaddleColorButton, BorderLayout.EAST);
        paddleColorPanel.add(redPaddleColorButton, BorderLayout.EAST);

        //Creates the ball color settings
        JLabel ballColorLabel = new JLabel("BALL COLOUR:");
        ballColorLabel.setFont(new Font("Ariel", Font.BOLD, 20));
        JRadioButton greyBallButton = new JRadioButton("GREY");
        JRadioButton redBallButton = new JRadioButton("RED");
        JRadioButton whiteBallButton = new JRadioButton("WHITE");
        JRadioButton yellowBallButton = new JRadioButton("YELLOW");
        JRadioButton blueBallButton = new JRadioButton("BLUE");
        ButtonGroup ballColorGroup = new ButtonGroup();
        ballColorGroup.add(greyBallButton);
        ballColorGroup.add(redBallButton);
        ballColorGroup.add(whiteBallButton);
        ballColorGroup.add(yellowBallButton);
        ballColorGroup.add(blueBallButton);
        ballColorPanel.add(ballColorLabel, BorderLayout.WEST);
        ballColorPanel.add(greyBallButton, BorderLayout.EAST);
        ballColorPanel.add(redBallButton, BorderLayout.EAST);
        ballColorPanel.add(whiteBallButton, BorderLayout.EAST);
        ballColorPanel.add(yellowBallButton, BorderLayout.EAST);
        ballColorPanel.add(blueBallButton, BorderLayout.EAST);

        //Creates the music volume settings
        JLabel musicVolumeLabel = new JLabel("MUSIC VOLUME: " + soundsmanager.getMusicVolume());
        musicVolumeLabel.setFont(new Font("Ariel", Font.BOLD, 20));
        JSlider musicVolume = new JSlider(JSlider.HORIZONTAL, 0, 100, (int)soundsmanager.getMusicVolume());
        musicVolume.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                musicVolumeLabel.setText("MUSIC VOLUME: " + musicVolume.getValue());
                soundsmanager.updateMusicVolume(musicVolume.getValue());
            }
        });
        musicVolumePanel.add(musicVolumeLabel, BorderLayout.WEST);
        musicVolumePanel.add(musicVolume, BorderLayout.EAST);
        //fix the function of this 

        JLabel sfxVolumeLabel = new JLabel("SFX VOLUME: " + soundsmanager.getSfxVolume());
        sfxVolumeLabel.setFont(new Font("Ariel", Font.BOLD, 20));
        JSlider sfxVolume = new JSlider(JSlider.HORIZONTAL, 0, 100, (int)soundsmanager.getSfxVolume());
        sfxVolume.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sfxVolumeLabel.setText("SFX VOLUME: " + sfxVolume.getValue());
                soundsmanager.updateSfxVolume(sfxVolume.getValue());
            }
        });
        sfxVolumePanel.add(sfxVolumeLabel, BorderLayout.WEST);
        sfxVolumePanel.add(sfxVolume, BorderLayout.EAST);

        JLabel displayModeLabel = new JLabel("DISPLAY MODE:");
        displayModeLabel.setFont(new Font("Ariel", Font.BOLD, 20));
        JRadioButton fullScreenButton = new JRadioButton("FULLSCREEN");
        JRadioButton windowedButton = new JRadioButton("WINDOWED");
        ButtonGroup displayModeGroup = new ButtonGroup();
        displayModeGroup.add(fullScreenButton);
        displayModeGroup.add(windowedButton);
        displayModePanel.add(displayModeLabel, BorderLayout.WEST);
        displayModePanel.add(fullScreenButton, BorderLayout.EAST);
        displayModePanel.add(windowedButton, BorderLayout.EAST);

        JLabel difficultyLabel = new JLabel("DIFFICULTY:");
        difficultyLabel.setFont(new Font("Ariel", Font.BOLD, 20));
        JRadioButton easyButton = new JRadioButton("EASY");
        JRadioButton normalButton = new JRadioButton("NORMAL");
        JRadioButton hardButton = new JRadioButton("HARD");
        ButtonGroup difficulties = new ButtonGroup();
        difficulties.add(easyButton);
        difficulties.add(normalButton);
        difficulties.add(hardButton);
        difficultyPanel.add(difficultyLabel, BorderLayout.WEST);
        difficultyPanel.add(easyButton, BorderLayout.EAST);
        difficultyPanel.add(normalButton, BorderLayout.EAST);
        difficultyPanel.add(hardButton, BorderLayout.EAST);

        JButton menuButton = new JButton("MAIN MENU");
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                MainMenu.main(null);
            }
        });

        topPanel.add(settingsLabel);
        middlePanel.add(paddleColorPanel);
        middlePanel.add(ballColorPanel);
        middlePanel.add(musicVolumePanel);
        middlePanel.add(sfxVolumePanel);
        middlePanel.add(displayModePanel);
        middlePanel.add(difficultyPanel);
        bottomPanel.add(menuButton);


        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(topPanel, constraints);

        Border raisedBevel = BorderFactory.createRaisedBevelBorder();
        Border loweredBevel = BorderFactory.createLoweredBevelBorder();
        middlePanel.setBorder(BorderFactory.createCompoundBorder(raisedBevel, loweredBevel));
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(middlePanel, constraints);

        constraints.anchor = GridBagConstraints.PAGE_END;
        constraints.gridx = 0;
        constraints.gridy = 3;
        add(bottomPanel, constraints);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(525, 380);
        setVisible(true);
        setLocationRelativeTo(null);
        repaint();
    }

    public void actionPerformed(ActionEvent e) {

    }

    public void stateChanged(ChangeEvent e) {
        //choose what happens with the sliders
    }

    private class PressingKeys extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e) {
            int action = e.getKeyCode();

            if(action == KeyEvent.VK_ESCAPE)
            {
                dispose();
                MainMenu.main(null);
            }
        }
    }
}
