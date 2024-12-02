package view;


import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * A dialog for selecting and picking up an item within a game. 
 * This dialog presents a list of available items
 * and allows the player to select one for pickup. 
 * It interacts directly with the game controller to execute
 * the pickup action based on the player's selection.
 */
public class ItemPickupDialog extends JDialog {
  private static final long serialVersionUID = 1L;
  private JList<String> itemList;
  private JButton pickButton;
  private int playerId;

  /**
   * Constructs an ItemPickupDialog which allows a player to pick an item from a list.
   *
   * @param owner the parent frame from which this dialog is displayed
   * @param title the title of the dialog window
   * @param modal specifies whether the dialog should block user input to other top-level windows
   * @param items the list of item names available for pickup
   * @param gameController the controller managing game logic and interactions
   * @param currentPlayerId the ID of the player who is picking up the item
   */
  public ItemPickupDialog(Frame owner, String title, boolean modal, 
      List<String> items, Controller gameController, int currentPlayerId) {
    super(owner, title, modal);
    this.playerId = currentPlayerId;
    setLayout(new BorderLayout());
    itemList = new JList<>(items.toArray(new String[0]));
    itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane scrollPane = new JScrollPane(itemList);

    pickButton = new JButton("Pick Item");
    pickButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          int selectedIndex = itemList.getSelectedIndex();
          if (selectedIndex != -1) {
              String selectedItem = itemList.getSelectedValue().split(": ")[0];
              try {
                StringBuilder output = new StringBuilder();
                gameController.pickUpItem(playerId, selectedItem, output);
                JOptionPane.showMessageDialog(owner, output.toString(), "Pick "
                    + "Up", JOptionPane.INFORMATION_MESSAGE);
              } catch (IOException | InterruptedException ex) {
                JOptionPane.showMessageDialog(owner, "Failed to pick up "
                    + "item: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
              }
              dispose();
          } else {
            JOptionPane.showMessageDialog(owner, "Please select an item to "
                + "pick up.", "Warning", JOptionPane.WARNING_MESSAGE);
          }
        }
    });

    add(scrollPane, BorderLayout.CENTER);
    add(pickButton, BorderLayout.SOUTH);

    setSize(300, 200);
    setLocationRelativeTo(owner);
  }
}
