package main;

import controller.GameController;
import controller.RandomNumberGenerator;
import view.GameFrame;
import world.World;

import javax.swing.SwingUtilities;
import java.io.*;
import java.util.Scanner;

public class GameLauncher {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java GameLauncher <world_file> <max_turns>");
            System.exit(1);
        }

        String worldFile = args[0];
        int maxTurns = Integer.parseInt(args[1]);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter '1' for CLI or '2' for GUI:");
        int mode = scanner.nextInt();

        switch (mode) {
            case 1:
                launchCLI(worldFile, maxTurns);
                break;
            case 2:
                launchGUI(worldFile, maxTurns);
                break;
            default:
                System.out.println("Invalid mode: Choose 1 for CLI or 2 for GUI");
                System.exit(1);
        }

        scanner.close();
    }

    private static void launchCLI(String worldFile, int maxTurns) {
        try {
            FileReader fileInput = new FileReader(worldFile);
            Appendable consoleOutput = System.out;
            InputStreamReader consoleInput = new InputStreamReader(System.in);
            RandomNumberGenerator rng = new RandomNumberGenerator();
            World world = new World(fileInput);
            GameController controller = new GameController(consoleInput, consoleOutput, rng, maxTurns);
            controller.playGame(world);
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found - " + worldFile);
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Failed to read the game file: " + e.getMessage());
            System.exit(1);
        } catch (InterruptedException e) {
            System.out.println("The game was interrupted.");
            System.exit(1);
        }
    }

    private static void launchGUI(String worldFile, int maxTurns) {
        SwingUtilities.invokeLater(() -> {
            try {
                RandomNumberGenerator rng = new RandomNumberGenerator();
                GameController controller = new GameController(new StringReader(""), System.out, rng, maxTurns);
                GameFrame frame = new GameFrame(controller, maxTurns, worldFile);
                frame.setVisible(true);
            } catch (Exception e) {
                System.err.println("Failed to initialize the GUI: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
