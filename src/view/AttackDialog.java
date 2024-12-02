package view;


import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.io.IOException;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * A dialog window that provides an interface for the player to choose an item for attacking.
 * This dialog allows the user to either perform a basic attack or use a specific item to attack.
 * It is tightly integrated with the game's controller to perform 
 * attacks based on the player's choices.
 *
 * The dialog includes:
 * - A list of items that can be selected for an attack.
 * - Buttons to execute the attack with the selected item, perform 
 * a basic attack, or cancel the operation.
 *
 * The outcome of the attack, along with any relevant messages, 
 * is displayed to the user through dialog messages.
 */
public class AttackDialog extends JDialog {

  private static final long serialVersionUID = 1L;
  private int playerId;
  private JList<String> itemList;
  private JButton attackButton;
  private JButton cancelButton;
  private JButton basicAttackButton;
  private String selectedItem;

  /**
   * Constructs an AttackDialog with controls for selecting an attack method and executing it.
   *
   * @param owner the Frame from which the dialog is displayed
   * @param gameController the controller handling game logic
   * @param playerIdInput the ID of the player who is attacking
   * @param items a list of strings representing the items available for attack
   */
  public AttackDialog(Frame owner, Controller gameController, 
      int playerIdInput, List<String> items) {
    super(owner, "Select Item for Attack", true);
    this.playerId = playerIdInput;

    itemList = new JList<>(new DefaultListModel<>());
    DefaultListModel<String> model = (DefaultListModel<String>) itemList.getModel();
    items.forEach(model::addElement);

    attackButton = new JButton("Attack with Selected Item");
    attackButton.addActionListener(e -> {
      try {
        performAttack(gameController, true);
      } catch (InterruptedException e1) {
        e1.printStackTrace();
      }
    });
    
    basicAttackButton = new JButton("Poking Eyes");
    basicAttackButton.addActionListener(e -> {
      try {
        performAttack(gameController, false);
      } catch (InterruptedException e1) {
        e1.printStackTrace();
      }
    });
    
    cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(e -> dispose());

    itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(attackButton);
    buttonPanel.add(basicAttackButton);
    buttonPanel.add(cancelButton);
    JScrollPane scrollPane = new JScrollPane(itemList);
    setLayout(new BorderLayout());
    add(scrollPane, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);

    pack();
    setLocationRelativeTo(owner);
  }

  private void performAttack(Controller gameController, boolean useItem) 
      throws InterruptedException {
    if (useItem) {
      selectedItem = itemList.getSelectedValue().split(": ")[0];
    } else {
      selectedItem = null;
    }
    if (selectedItem == null) {
      JOptionPane.showMessageDialog(this, "No item selected. Proceeding with "
          + "poking eyes.", "Warning", JOptionPane.WARNING_MESSAGE);
      selectedItem = ""; 
    }


    try {
      StringBuilder output = new StringBuilder();
      gameController.attackTargetWithItem(playerId, selectedItem, output);
      if (("").equals(selectedItem)) {
        JOptionPane.showMessageDialog(this, "Attack performed by poking eyes" + "\n", ""
            + "Attack", JOptionPane.INFORMATION_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(this, "Attack performed using " + selectedItem + ""
            + "\n", "Attack", JOptionPane.INFORMATION_MESSAGE);
      }
      JOptionPane.showMessageDialog(this, output.toString(), "Attac"
          + "k", JOptionPane.INFORMATION_MESSAGE);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this, "Attack failed: " + e.getMessage(), 
          "Error", JOptionPane.ERROR_MESSAGE);
    }
    dispose();
  }
}
