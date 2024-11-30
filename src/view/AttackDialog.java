package view;

import javax.swing.*;

import controller.Controller;

import java.awt.*;
import java.util.List;

public class AttackDialog extends JDialog {
    private Controller gameController;
    private int playerId;
    private JList<String> itemList;
    private JButton attackButton;
    private JButton cancelButton;
    private JButton basicAttackButton;
    private String selectedItem;

    public AttackDialog(Frame owner, Controller gameController, int playerId, List<String> items) {
        super(owner, "Select Item for Attack", true);
        this.gameController = gameController;
        this.playerId = playerId;

        itemList = new JList<>(new DefaultListModel<>());
        DefaultListModel<String> model = (DefaultListModel<String>) itemList.getModel();
        items.forEach(model::addElement);

        attackButton = new JButton("Attack with Selected Item");
        attackButton.addActionListener(e -> performAttack(true));
        
        basicAttackButton = new JButton("Poking Eyes");
        basicAttackButton.addActionListener(e -> performAttack(false));
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(itemList);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(attackButton);
        buttonPanel.add(basicAttackButton);
        buttonPanel.add(cancelButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    private void performAttack(boolean useItem) {
      if (useItem) {
        selectedItem = itemList.getSelectedValue().split(": ")[0];
      } else {
        selectedItem = null;
      }
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "No item selected. Proceeding with poking eyes.", "Warning", JOptionPane.WARNING_MESSAGE);
            selectedItem = ""; 
        }


        try {
          StringBuilder output = new StringBuilder();
            gameController.attackTargetWithItem(playerId, selectedItem, output);
            if (selectedItem == "") {
              JOptionPane.showMessageDialog(this, "Attack performed by poking eyes" + "\n", "Attack", JOptionPane.INFORMATION_MESSAGE);
            } else {
              JOptionPane.showMessageDialog(this, "Attack performed using " + selectedItem + "\n", "Attack", JOptionPane.INFORMATION_MESSAGE);
            }
            JOptionPane.showMessageDialog(this, output.toString(), "Attack", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Attack failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        dispose();
    }
}
