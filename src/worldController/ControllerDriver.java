package worldController;

import world.World;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

public class ControllerDriver {

    public static void main(String[] args) throws FileNotFoundException {
      Readable fileInput = new FileReader("res/mansion.txt");  
        Appendable consoleOutput = System.out;
        Readable consoleInput = new InputStreamReader(System.in);

        GameController controller = new GameController(consoleInput, consoleOutput);
        
        World world = new World(fileInput); 

        controller.playGame(world);
    }
}
