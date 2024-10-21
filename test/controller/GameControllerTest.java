package controller;

import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.imageio.ImageIO;
import org.junit.Test;
import world.World;

/**
 * Test file for game controller. 
 */
public class GameControllerTest {

  private GameController gameController;
  private StringWriter consoleOutput;

  @Test
  public void testDisplayRoomInfo() throws InterruptedException, IOException {
    String simulatedUserInput = "1\nHumanPlayer\n1\n4\n1\n1\n0\n"; 
    Readable consoleInput = new StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();
    RandomNumberGenerator rng = new RandomNumberGenerator();
    int maxTurns = 3;  
    gameController = new GameController(consoleInput, consoleOutput, rng, maxTurns);
    
    Readable fileInput = new FileReader("res/mansion.txt");
    World world = new World(fileInput);  

    gameController.playGame(world);

    String output = consoleOutput.toString();

    assertTrue(output.contains("Room Name: Armory"));
    assertTrue(output.contains("Room ID: 1"));
    assertTrue(output.contains("Items: Revolver"));
    assertTrue(output.contains("Occupants:"));
    assertTrue(output.contains("Target: Doctor Lucky"));
    assertTrue(output.contains("Player: HumanPlayer"));
  }
  
  @Test
  public void testGraphicalShow() throws InterruptedException, IOException {
    String simulatedUserInput = "3\n1\nHumanPlayer\n1\n4\n2\n2\n2\n0\n"; 
    Readable consoleInput = new StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();
    RandomNumberGenerator rng = new RandomNumberGenerator();
    int maxTurns = 10;  
    gameController = new GameController(consoleInput, consoleOutput, rng, maxTurns);
    
    Readable fileInput = new FileReader("res/mansion.txt");
    World world = new World(fileInput);  
    File tempFile = File.createTempFile("world", ".png");
    BufferedImage image = world.drawWorld();
    ImageIO.write(image, "png", tempFile);
    gameController.playGame(world);
    System.out.println("Console Output: " + consoleOutput.toString());
    assertTrue(consoleOutput.toString().contains("World map saved to"));
    assertTrue("The world map image should exist after saving.", tempFile.exists());
  }
  
  @Test
  public void testAddHumanPlayer() throws InterruptedException, IOException {
    String simulatedUserInput = "1\nHumanPlayer\n1\n4\n1\n1\n0\n"; 
    Readable consoleInput = new StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();
    RandomNumberGenerator rng = new RandomNumberGenerator();
    int maxTurns = 3;  
    gameController = new GameController(consoleInput, consoleOutput, rng, maxTurns);
    
    Readable fileInput = new FileReader("res/mansion.txt");
    World world = new World(fileInput);  

    gameController.playGame(world);
    assert (consoleOutput.toString().contains("Human player added with ID:"));
  }
  
  @Test
  public void testAddComputerPlayer() throws InterruptedException, IOException {
    String simulatedUserInput = "2\nComputerPlayer\n1\n1\nHumanPlayer\n1\n4\n1\n1\n0\n"; 
    Readable consoleInput = new StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();
    RandomNumberGenerator rng = new RandomNumberGenerator();
    int maxTurns = 3;  
    gameController = new GameController(consoleInput, consoleOutput, rng, maxTurns);
    
    Readable fileInput = new FileReader("res/mansion.txt");
    World world = new World(fileInput);  

    gameController.playGame(world);
    assert (consoleOutput.toString().contains("Computer player added with ID:"));
  }
  
  @Test
  public void testPlayerMove() throws InterruptedException, IOException {
    String simulatedUserInput = "1\nHumanPlayer\n1\n4\n5\n2\n0\n"; 
    Readable consoleInput = new StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();
    RandomNumberGenerator rng = new RandomNumberGenerator();
    int maxTurns = 3;  
    gameController = new GameController(consoleInput, consoleOutput, rng, maxTurns);
    
    Readable fileInput = new FileReader("res/mansion.txt");
    World world = new World(fileInput);  

    gameController.playGame(world);
    assert (consoleOutput.toString().contains("Enter target room ID:"));
    assert (consoleOutput.toString().contains("Player HumanPlayer moved to room Billiard Room"));
  }
  
  @Test
  public void testPlayerPickUp() throws InterruptedException, IOException {
    String simulatedUserInput = "1\nHumanPlayer\n1\n4\n6\n1\n0\n"; 
    Readable consoleInput = new StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();
    RandomNumberGenerator rng = new RandomNumberGenerator();
    int maxTurns = 3;  
    gameController = new GameController(consoleInput, consoleOutput, rng, maxTurns);
    
    Readable fileInput = new FileReader("res/mansion.txt");
    World world = new World(fileInput);  

    gameController.playGame(world);
    assert (consoleOutput.toString().contains("Items available to pick up:"));
    assert (consoleOutput.toString().contains("Item 'Revolver' picked up "
        + "successfully by HumanPlayer"));
  }
  
  @Test
  public void testPlayerLookAround() throws InterruptedException, IOException {
    String simulatedUserInput = "1\nHumanPlayer\n1\n4\n7\n0\n"; 
    Readable consoleInput = new StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();
    RandomNumberGenerator rng = new RandomNumberGenerator();
    int maxTurns = 3;  
    gameController = new GameController(consoleInput, consoleOutput, rng, maxTurns);
    
    Readable fileInput = new FileReader("res/mansion.txt");
    World world = new World(fileInput);  

    gameController.playGame(world);
    assert (consoleOutput.toString().contains("You are in Room Armory."));
    assert (consoleOutput.toString().contains("Current Room Items:"));
    assert (consoleOutput.toString().contains("Neighboring and Visible Rooms:"));
    assert (consoleOutput.toString().contains("Room ID: 2, Room Name: Billiard Room, "
        + "Items: Billiard Cue"));
    assert (consoleOutput.toString().contains("Other Visible Rooms:"));
    assert (consoleOutput.toString().contains("Player: HumanPlayer"));
    assert (consoleOutput.toString().contains("Visible rooms from here:"));
  }
  
  @Test
  public void testPlayerDisplayInfo() throws InterruptedException, IOException {
    String simulatedUserInput = "1\nHumanPlayer\n1\n4\n7\n8\n0\n0\n"; 
    Readable consoleInput = new StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();
    RandomNumberGenerator rng = new RandomNumberGenerator();
    int maxTurns = 3;  
    gameController = new GameController(consoleInput, consoleOutput, rng, maxTurns);
    
    Readable fileInput = new FileReader("res/mansion.txt");
    World world = new World(fileInput);  
  
    gameController.playGame(world);
    assert (consoleOutput.toString().contains("ID: 0, Name: HumanPlayer, "
        + "Current Room: Armory, Items:"));
  }
}
