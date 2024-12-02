package view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A panel used to display welcome messages at the start of the game. This panel is set up
 * with a grid layout to position the textual content centrally.
 */
public class WelcomePanel extends JPanel {

  private static final long serialVersionUID = 1L;
  private JLabel lineOne;
  private JLabel lineTwo;

  /**
   * Constructs a WelcomePanel with two lines of text.
   *
   * @param textOne the content of the first line of the welcome message.
   * @param textTwo the content of the second line of the welcome message.
   */
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
