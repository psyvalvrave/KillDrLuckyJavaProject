package main;

import controller.GameController;
import controller.RandomNumberGenerator;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Scanner;
import javax.swing.SwingUtilities;
import view.GameFrame;
import world.World;
import world.WorldOutline;

/**
 * Launches the game application with the provided command-line arguments. This class contains the
 * entry point for the game, responsible for initializing the game based on input parameters.
 */
public class GameLauncher {
  /**
   * The main method that serves as the entry point for the game. 
   * It expects two command-line arguments:
   * the path to the world file and the maximum number of turns. 
   * If the required arguments are not provided,
   * it displays the correct usage and exits the program.
   *
   * @param args command-line arguments passed to the program. Expects exactly two arguments:
   *             1. {@code world_file} - the path to the file containing the game 
   *                 world configuration.
   *             2. {@code max_turns} - the maximum number of turns the game should run.
   */
  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println("Usage: java GameLauncher <world_file> <max_turns>");
      System.exit(1);
    }

    String worldFile = args[0];
    int maxTurns = Integer.parseInt(args[1]);

    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter '1' for Cli or '2' for Gui:");
    int mode = scanner.nextInt();

    switch (mode) {
      case 1:
        launchCli(worldFile, maxTurns);
        break;
      case 2:
        launchGui(worldFile, maxTurns);
        break;
      default:
        System.out.println("Invalid mode: Choose 1 for Cli or 2 for Gui");
        System.exit(1);
    }

    scanner.close();
  }

  private static void launchCli(String worldFile, int maxTurns) {
    try {
      FileReader fileInput = new FileReader(worldFile);
      Appendable consoleOutput = System.out;
      InputStreamReader consoleInput = new InputStreamReader(System.in);
      RandomNumberGenerator rng = new RandomNumberGenerator();
      WorldOutline world = new World(fileInput);
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

  private static void launchGui(String worldFile, int maxTurns) {
    SwingUtilities.invokeLater(() -> {
      try {
        RandomNumberGenerator rng = new RandomNumberGenerator();
        GameController controller = new GameController(new StringReader(""), 
            System.out, rng, maxTurns);
        GameFrame frame = new GameFrame(controller, maxTurns, worldFile);
        frame.setVisible(true);
      } catch (IOException e) {
        System.err.println("Failed to initialize the Gui: " + e.getMessage());
        e.printStackTrace();
        System.exit(1);
      }
    });
  }
}
