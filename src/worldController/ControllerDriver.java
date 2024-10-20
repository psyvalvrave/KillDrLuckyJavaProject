package worldController;

import world.World;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

public class ControllerDriver {

    public static void main(String[] args) {
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
