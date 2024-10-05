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
      Readable fileInput = new FileReader("res/mansion.txt");  
      Appendable fileOutput = new FileWriter("res/example_run.txt");
      World myWorld = new World(fileInput);

      myWorld.drawWorld();  
      fileOutput.append("finish drawing a world!");
      fileOutput.append("-----------------------------------------------");
      fileOutput.append("\nDetailed Room Information:");
      for (Room room : myWorld.getRooms()) {
        fileOutput.append(room.getInfo());
        fileOutput.append("--------------------------------------------------");
        fileOutput.append(room.getRoomName() + " has Neighbors(list seperately): " 
            + room.getNeighborNames());
        fileOutput.append("--------------------------------------------------");
      }
      fileOutput.append(myWorld.getWorldText());
      fileOutput.append("--------------------------------------------------");
      myWorld.moveTargetToNextRoom();
      myWorld.moveTargetToNextRoom();
      myWorld.moveTargetToNextRoom();
      myWorld.moveTargetToNextRoom();
      fileOutput.append(myWorld.getTarget().getCharacterInfo());
      fileOutput.append("--------------------------------------------------");
      for (int i = 0; i < 16; i++) {
        myWorld.moveTargetToNextRoom();
        fileOutput.append("Move " + (i + 1) + ": " + myWorld.getTarget().getCharacterInfo());
        fileOutput.append("--------------------------------------------------");
      }
      fileOutput.append("This is the last room, next move should go back to the first room");
      fileOutput.append("--------------------------------------------------");
      myWorld.moveTargetToNextRoom();
      fileOutput.append(myWorld.getTarget().getCharacterInfo());
      fileOutput.append("--------------------------------------------------");
      fileOutput.append(myWorld.getWorldText());
      ((FileWriter) fileOutput).close();  
    } catch (IOException e) {
      System.err.println("Failed to read or write: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.err.println("Error with input data: " + e.getMessage());
    }  
  }
}
 

 