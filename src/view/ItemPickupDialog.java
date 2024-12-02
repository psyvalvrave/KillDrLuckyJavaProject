package view;

import javax.swing.*;

import controller.Controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class ItemPickupDialog extends JDialog {
    private JList<String> itemList;
    private JButton pickButton;
    private int playerId;

    public ItemPickupDialog(Frame owner, String title, boolean modal, List<String> items, Controller gameController, int currentPlayerId) {
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
                        JOptionPane.showMessageDialog(owner, output.toString(), "Pick Up", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException | InterruptedException ex) {
                        JOptionPane.showMessageDialog(owner, "Failed to pick up item: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(owner, "Please select an item to pick up.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        add(scrollPane, BorderLayout.CENTER);
        add(pickButton, BorderLayout.SOUTH);

        setSize(300, 200);
        setLocationRelativeTo(owner);
    }
}
