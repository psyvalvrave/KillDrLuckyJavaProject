package worldController;

import world.World;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

public class ControllerDriver {

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
      Readable fileInput = new FileReader("res/mansion.txt");  
        Appendable consoleOutput = System.out;
        Readable consoleInput = new InputStreamReader(System.in);
        RandomNumberGenerator rng = new RandomNumberGenerator();
        GameController controller = new GameController(consoleInput, consoleOutput, rng);
        
        World world = new World(fileInput); 

        controller.playGame(world);
    }
}
