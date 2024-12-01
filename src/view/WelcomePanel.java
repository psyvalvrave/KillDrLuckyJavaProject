package view;

import javax.swing.*;
import java.awt.*;

public class WelcomePanel extends JPanel {
    private JLabel lineOne;
    private JLabel lineTwo;

    public WelcomePanel(String textOne, String textTwo) {
      super(new GridBagLayout()); 
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel welcomeLabel = new JLabel("Welcome to the Game!");
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;

        lineOne = new JLabel(textOne);
        lineTwo = new JLabel(textTwo);

        lineOne.setFont(new Font("Serif", Font.BOLD, 24));
        lineTwo.setFont(new Font("Serif", Font.BOLD, 24));

        add(lineOne, constraints);
        add(lineTwo, constraints);
    }
}
