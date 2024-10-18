package world;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Represents a test driver for the first milestone.
 * Outputs are written to an Appendable for flexibility.
 */
public class WorldDriver {
  /**
   * Main method to run the test. All results will be logged in a specified output.
   *
   * @param args default parameter for main
   */
  public static void main(String[] args) {
    try {
      Readable fileInput = new FileReader("res/one_room.txt");  
      Appendable consoleOutput = System.out;
      Appendable fileOutput = new FileWriter("res/example_run.txt");
      World myWorld = new World(fileInput);

      myWorld.drawWorld();  
      consoleOutput.append("finish drawing a world!\n");
      consoleOutput.append("-----------------------------------------------");
      consoleOutput.append("\nDetailed Room Information:");
      for (Room room : myWorld.getRooms()) {
        consoleOutput.append(room.getInfo());
        consoleOutput.append("\n--------------------------------------------------\n");
        consoleOutput.append(room.getRoomName() + " has Neighbors(list seperately): " 
            + room.getNeighborNames());
        consoleOutput.append("\n--------------------------------------------------\n");
      }
      consoleOutput.append(myWorld.getWorldText());
      consoleOutput.append("\n--------------------------------------------------\n");
      myWorld.moveTargetToNextRoom();
      myWorld.moveTargetToNextRoom();
      myWorld.moveTargetToNextRoom();
      myWorld.moveTargetToNextRoom();
      consoleOutput.append(myWorld.getTarget().getCharacterInfo());
      consoleOutput.append("\n--------------------------------------------------\n");
      for (int i = 0; i < 16; i++) {
        myWorld.moveTargetToNextRoom();
        consoleOutput.append("Move " + (i + 1) + ": " + myWorld.getTarget().getCharacterInfo());
        consoleOutput.append("\n--------------------------------------------------\n");
      }
      consoleOutput.append("This is the last room, next move should go back to the first room");
      consoleOutput.append("\n--------------------------------------------------\n");
      myWorld.moveTargetToNextRoom();
      consoleOutput.append(myWorld.getTarget().getCharacterInfo());
      consoleOutput.append("\n--------------------------------------------------\n");
      consoleOutput.append(myWorld.getWorldText());
      //((FileWriter) consoleOutput).close();  
    } catch (IOException e) {
      System.err.println("Failed to read or write: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.err.println("Error with input data: " + e.getMessage());
    }  
  }
}
