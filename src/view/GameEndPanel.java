package view;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Font;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Represents the panel displayed at the end of a game, showing the result and providing buttons
 * to load a previous game, restart the current game, or quit the game. This panel is a part of
 * the game's graphical user interface and interacts directly with the game controller to invoke
 * these actions.
 */
public class GameEndPanel extends JPanel {

  private static final long serialVersionUID = 1L;
  
  /**
   * Constructs a GameEndPanel with a message indicating the game's outcome and buttons for
   * possible next steps.
   *
   * @param resultText the text to display as the game's result.
   * @param gameController the controller used to handle the actions of the buttons.
   */
  public GameEndPanel(String resultText, Controller gameController) {
    setLayout(new BorderLayout());
    JLabel resultLabel = new JLabel(resultText, SwingConstants.CENTER);
    resultLabel.setFont(new Font("Serif", Font.BOLD, 20));
    add(resultLabel, BorderLayout.CENTER);

    JButton loadButton = new JButton("Load Game");
    JButton restartButton = new JButton("Restart Game");
    JButton quitButton = new JButton("Quit Game");
    loadButton.addActionListener(e -> ((GameFrame) SwingUtilities
        .getWindowAncestor(this)).loadWorldFromFile(gameController));
    restartButton.addActionListener(e -> {
      try {
        ((GameFrame) SwingUtilities.getWindowAncestor(this)).restartWorld(gameController);
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }
    });
    quitButton.addActionListener(e -> System.exit(0));
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(loadButton);
    buttonPanel.add(restartButton);
    buttonPanel.add(quitButton);
    add(buttonPanel, BorderLayout.SOUTH);
  }
}
