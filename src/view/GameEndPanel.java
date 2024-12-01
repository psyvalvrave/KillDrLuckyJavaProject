package view;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import controller.Controller;

public class GameEndPanel extends JPanel {

    public GameEndPanel(String resultText, Controller gameController) {
        setLayout(new BorderLayout());
        JLabel resultLabel = new JLabel(resultText, SwingConstants.CENTER);
        resultLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(resultLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton loadButton = new JButton("Load Game");
        JButton restartButton = new JButton("Restart Game");
        JButton quitButton = new JButton("Quit Game");

        loadButton.addActionListener(e -> ((GameFrame) SwingUtilities.getWindowAncestor(this)).loadWorldFromFile(gameController));
        restartButton.addActionListener(e -> {
            try {
                ((GameFrame) SwingUtilities.getWindowAncestor(this)).restartWorld(gameController);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        quitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(loadButton);
        buttonPanel.add(restartButton);
        buttonPanel.add(quitButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
