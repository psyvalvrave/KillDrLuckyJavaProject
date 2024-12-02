package view;

import javax.swing.*;
import controller.Controller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PetMoveDialog extends JDialog {
    private int playerId;
    private JTextField roomInputField;
    private JButton moveButton;
    private JButton cancelButton;

    public PetMoveDialog(Frame owner, Controller gameController, int playerId) {
        super(owner, "Move Pet", true);
        this.playerId = playerId;

        setupUI(gameController);
        pack();
        setLocationRelativeTo(owner);
    }

    private void setupUI(Controller gameController) {
        roomInputField = new JTextField(10);
        moveButton = new JButton("Move");
        cancelButton = new JButton("Cancel");

        moveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performMove(gameController);
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

    private void performMove(Controller gameController) {
        try {
            int roomId = Integer.parseInt(roomInputField.getText().trim());
            StringBuilder output = new StringBuilder();
            gameController.movePet(playerId, roomId, output);
            JOptionPane.showMessageDialog(this, output.toString(), "Move Pet", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid room number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        dispose();
    }
}
