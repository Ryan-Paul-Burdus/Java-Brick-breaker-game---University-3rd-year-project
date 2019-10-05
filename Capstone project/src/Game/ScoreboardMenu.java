package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreboardMenu extends JFrame {

    public ScoreboardMenu(String title) throws IOException {
        super(title);

        //This creates a custom icon for the window using an image
        ImageIcon logoIcon = new ImageIcon(("breakout logo.png"));
        Image logo = logoIcon.getImage();
        setIconImage(logo);

        BufferedReader br = null;
        br = new BufferedReader(new FileReader("Normal_scores.txt"));
        List<Integer> normalScores = new ArrayList();
        String line = null;
        while ((line = br.readLine()) != null)
        {
            String []stringNumbers = line.split(" ");
            for(String stringNumber : stringNumbers)
            {
                normalScores.add(Integer.parseInt(stringNumber));
            }
        }
        br.close();

        BufferedReader br2 = null;
        br2 = new BufferedReader(new FileReader("PowerUp_scores.txt"));
        List<Integer> powerUpScores = new ArrayList();
        line = null;
        while ((line = br2.readLine()) != null)
        {
            String []stringNumbers2 = line.split(" ");
            for(String stringNumber : stringNumbers2)
            {
                powerUpScores.add(Integer.parseInt(stringNumber));
            }
        }
        br2.close();


        JPanel topPanel = new JPanel();
        JLabel scoreLabel = new JLabel();
        scoreLabel.setText("Your latest score was: " + normalScores.get(normalScores.size() - 1));
        topPanel.add(scoreLabel);

        JPanel middlePanel = new JPanel();
        JTabbedPane tabbedPane = new JTabbedPane();
        JTextArea scoresArea1 = new JTextArea();
        scoresArea1.setFont(scoresArea1.getFont().deriveFont(17.5f));
        scoresArea1.append("Classic breakout top 10 High-scores: \n");
        scoresArea1.setEditable(false);
        JTextArea scoresArea2 = new JTextArea();
        scoresArea2.setFont(scoresArea1.getFont().deriveFont(17.5f));
        int rankingNumber = 1;
        Collections.sort(normalScores);
        Collections.reverse(normalScores);
        for(int i = 0; i < normalScores.size(); i++)
        {
            scoresArea1.append(String.valueOf(rankingNumber + ": " + normalScores.get(i) + "\n"));
            rankingNumber++;
        }
        scoresArea2.append("Super breakout top 10 High-scores: \n");
        scoresArea2.setEditable(false);
        rankingNumber = 1;
        Collections.sort(powerUpScores);
        Collections.reverse(powerUpScores);
        for(int i = 0; i < powerUpScores.size(); i++)
        {
            scoresArea2.append(String.valueOf(rankingNumber + ": " + powerUpScores.get(i) + "\n"));
            rankingNumber++;
        }

        tabbedPane.addTab("Classic breakout high-scores", null, scoresArea1);
        tabbedPane.addTab("Super breakout high-scores", null, scoresArea2);
        middlePanel.setLayout(new BorderLayout());
        middlePanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton mainMenuButton = new JButton("MAIN MENU");
        JButton exitButton = new JButton("EXIT");
        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                MainMenu.main(null);
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        bottomPanel.add(mainMenuButton);
        bottomPanel.add(exitButton);

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(topPanel, constraints);
        constraints.weightx = 0.5;
        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(middlePanel, constraints);
        constraints.anchor = GridBagConstraints.PAGE_END;
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.weightx = 0.5;
        constraints.weighty = 0;
        add(bottomPanel, constraints);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setSize(500, 500);
        setVisible(true);
        setLocationRelativeTo(null);
        repaint();
    }
}
