package view;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A dialog window for moving a pet to a specified room within the game. 
 * It allows the player to enter a room ID and instructs the game 
 * controller to move the pet accordingly.
 */
public class PetMoveDialog extends JDialog {
  private static final long serialVersionUID = 1L;
  private int playerId;
  private JTextField roomInputField;
  private JButton moveButton;
  private JButton cancelButton;

  /**
   * Constructs a PetMoveDialog which provides an interface to move a pet.
   *
   * @param owner the parent frame from which this dialog is displayed.
   * @param gameController the controller that handles game logic.
   * @param playerIdInput the ID of the player whose pet is to be moved.
   */
  public PetMoveDialog(Frame owner, Controller gameController, int playerIdInput) {
    super(owner, "Move Pet", true);
    this.playerId = playerIdInput;

    setupUi(gameController);
    pack();
    setLocationRelativeTo(owner);
  }

  private void setupUi(Controller gameController) {
    roomInputField = new JTextField(10);
    moveButton = new JButton("Move");
    cancelButton = new JButton("Cancel");

    moveButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
              performMove(gameController);
            } catch (IOException | InterruptedException e1) {
              e1.printStackTrace();
            }
        }
    });

    cancelButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    });

    JPanel panel = new JPanel(new FlowLayout());
    panel.add(new JLabel("Enter room ID to move pet:"));
    panel.add(roomInputField);
    panel.add(moveButton);
    panel.add(cancelButton);

    setLayout(new BorderLayout());
    add(panel, BorderLayout.CENTER);
  }

  private void performMove(Controller gameController) throws IOException, InterruptedException {
    try {
      int roomId = Integer.parseInt(roomInputField.getText().trim());
      StringBuilder output = new StringBuilder();
      gameController.movePet(playerId, roomId, output);
      JOptionPane.showMessageDialog(this, output.toString(), "Move "
          + "Pet", JOptionPane.INFORMATION_MESSAGE);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this, "Invalid room number."
          + "", "Error", JOptionPane.ERROR_MESSAGE);
    } 
    dispose();
  }
}
