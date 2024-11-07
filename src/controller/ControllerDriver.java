package controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import world.World;

/**
 * Entry point for the game controller that orchestrates game play by initializing 
 * the game world, setting up the game controller, and managing turns.
 * This driver class parses command-line arguments to configure the game settings 
 * and handles exceptions related to file reading and game interruptions.
 */
public class ControllerDriver {
  /**
   * Main method to start the game based on user inputs provided as command line arguments.
   * It expects two arguments: the file path of the world configuration and the maximum 
   * number of turns the game should allow.
   * 
   * @param args Command line arguments where args[0] is the path to the world file 
   *        and args[1] is the maximum number of turns as a string.
   * @throws IOException If there are issues in reading the input file.
   */
  public static void main(String[] args) throws IOException {
    if (args.length < 2) {
      System.out.println("Usage: java ControllerDriver <world_file> <max_turns>");
      System.exit(1);
    }

    String worldFile = args[0];
    int maxTurns = 100;

    try {
      maxTurns = Integer.parseInt(args[1]);
      if (maxTurns <= 0) {
        System.out.println("Error: 'max_turns' must be a positive integer.");
        System.exit(1);
      }
    } catch (NumberFormatException e) {
      System.out.println("Error: 'max_turns' must be an integer.");
      System.exit(1);
    }

    try {
      Readable fileInput = new FileReader(worldFile);
      Appendable consoleOutput = System.out;
      Readable consoleInput = new InputStreamReader(System.in);
      RandomNumberGenerator rng = new RandomNumberGenerator();
      World world = new World(fileInput); 
      world.drawWorld();
      GameController controller = new GameController(consoleInput, consoleOutput, rng, maxTurns);
      controller.playGame(world);
    } catch (FileNotFoundException e) {
      System.out.println("Error: File not found - " + worldFile);
      System.exit(1);
    } catch (InterruptedException e) {
      System.out.println("The game was interrupted.");
      System.exit(1);
    }
  }
}
