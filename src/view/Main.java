package view;

import java.io.StringReader;

import javax.swing.SwingUtilities;
import controller.GameController;
import controller.RandomNumberGenerator;

/**
 * Entry point for the GUI version of the game that initializes the game controller 
 * and the game frame.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
              StringReader dummyInput = new StringReader("");
                RandomNumberGenerator rng = new RandomNumberGenerator();
                int maxTurn = 200;
                GameController controller = new GameController(dummyInput, System.out, rng, maxTurn);                 
                GameFrame frame = new GameFrame(controller, maxTurn, "res/mansion.txt");
                frame.setVisible(true);
            } catch (Exception e) {
                System.err.println("Failed to initialize the game: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
