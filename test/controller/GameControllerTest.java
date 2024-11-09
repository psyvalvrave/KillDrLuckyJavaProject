package controller;

import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Reader;
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
    
    String input = 
        "36 30 Doctor Lucky's Mansion\n" +
        "50 Doctor Lucky\n" +
        "Fortune the Cat\n" +
        "21\n" +
        "22 19 23 26 Armory\n" +
        "16 21 21 28 Billiard Room\n" +
        "28  0 35  5 Carriage House\n" +
        "12 11 21 20 Dining Hall\n" +
        "22 13 25 18 Drawing Room\n" +
        "26 13 27 18 Foyer\n" +
        "28 26 35 29 Green House\n" +
        "30 20 35 25 Hedge Maze\n" +
        "16  3 21 10 Kitchen\n" +
        " 0  3  5  8 Lancaster Room\n" +
        " 4 23  9 28 Library\n" +
        " 2  9  7 14 Lilac Room\n" +
        " 2 15  7 22 Master Suite\n" +
        " 0 23  3 28 Nursery\n" +
        "10  5 15 10 Parlor\n" +
        "28 12 35 19 Piazza\n" +
        " 6  3  9  8 Servants' Quarters\n" +
        " 8 11 11 20 Tennessee Room\n" +
        "10 21 15 26 Trophy Room\n" +
        "22  5 23 12 Wine Cellar\n" +
        "30  6 35 11 Winter Garden\n" +
        "20\n" +
        "8 3 Crepe Pan\n" +
        "4 2 Letter Opener\n" +
        "12 2 Shoe Horn\n" +
        "8 3 Sharp Knife\n" +
        "0 3 Revolver\n" +
        "15 3 Civil War Cannon\n" +
        "2 4 Chain Saw\n" +
        "16 2 Broom Stick\n" +
        "1 2 Billiard Cue\n" +
        "19 2 Rat Poison\n" +
        "6 2 Trowel\n" +
        "2 4 Big Red Hammer\n" +
        "6 2 Pinking Shears\n" +
        "18 3 Duck Decoy\n" +
        "13 2 Bad Cream\n" +
        "18 2 Monkey Hand\n" +
        "11 2 Tight Hat\n" +
        "19 2 Piece of Rope\n" +
        "9 3 Silken Cord\n" +
        "7 2 Loud Noise";
    Reader fileInput = new StringReader(input);
    World world = new World(fileInput);  
    world.movePetToNextRoom();

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
    
    String input = 
        "36 30 Doctor Lucky's Mansion\n" +
        "50 Doctor Lucky\n" +
        "Fortune the Cat\n" +
        "21\n" +
        "22 19 23 26 Armory\n" +
        "16 21 21 28 Billiard Room\n" +
        "28  0 35  5 Carriage House\n" +
        "12 11 21 20 Dining Hall\n" +
        "22 13 25 18 Drawing Room\n" +
        "26 13 27 18 Foyer\n" +
        "28 26 35 29 Green House\n" +
        "30 20 35 25 Hedge Maze\n" +
        "16  3 21 10 Kitchen\n" +
        " 0  3  5  8 Lancaster Room\n" +
        " 4 23  9 28 Library\n" +
        " 2  9  7 14 Lilac Room\n" +
        " 2 15  7 22 Master Suite\n" +
        " 0 23  3 28 Nursery\n" +
        "10  5 15 10 Parlor\n" +
        "28 12 35 19 Piazza\n" +
        " 6  3  9  8 Servants' Quarters\n" +
        " 8 11 11 20 Tennessee Room\n" +
        "10 21 15 26 Trophy Room\n" +
        "22  5 23 12 Wine Cellar\n" +
        "30  6 35 11 Winter Garden\n" +
        "20\n" +
        "8 3 Crepe Pan\n" +
        "4 2 Letter Opener\n" +
        "12 2 Shoe Horn\n" +
        "8 3 Sharp Knife\n" +
        "0 3 Revolver\n" +
        "15 3 Civil War Cannon\n" +
        "2 4 Chain Saw\n" +
        "16 2 Broom Stick\n" +
        "1 2 Billiard Cue\n" +
        "19 2 Rat Poison\n" +
        "6 2 Trowel\n" +
        "2 4 Big Red Hammer\n" +
        "6 2 Pinking Shears\n" +
        "18 3 Duck Decoy\n" +
        "13 2 Bad Cream\n" +
        "18 2 Monkey Hand\n" +
        "11 2 Tight Hat\n" +
        "19 2 Piece of Rope\n" +
        "9 3 Silken Cord\n" +
        "7 2 Loud Noise";
    Reader fileInput = new StringReader(input);
    World world = new World(fileInput);  
    File tempFile = File.createTempFile("world", ".png");
    BufferedImage image = world.drawWorld();
    ImageIO.write(image, "png", tempFile);
    gameController.playGame(world);
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
    
    String input = 
        "36 30 Doctor Lucky's Mansion\n" +
        "50 Doctor Lucky\n" +
        "Fortune the Cat\n" +
        "21\n" +
        "22 19 23 26 Armory\n" +
        "16 21 21 28 Billiard Room\n" +
        "28  0 35  5 Carriage House\n" +
        "12 11 21 20 Dining Hall\n" +
        "22 13 25 18 Drawing Room\n" +
        "26 13 27 18 Foyer\n" +
        "28 26 35 29 Green House\n" +
        "30 20 35 25 Hedge Maze\n" +
        "16  3 21 10 Kitchen\n" +
        " 0  3  5  8 Lancaster Room\n" +
        " 4 23  9 28 Library\n" +
        " 2  9  7 14 Lilac Room\n" +
        " 2 15  7 22 Master Suite\n" +
        " 0 23  3 28 Nursery\n" +
        "10  5 15 10 Parlor\n" +
        "28 12 35 19 Piazza\n" +
        " 6  3  9  8 Servants' Quarters\n" +
        " 8 11 11 20 Tennessee Room\n" +
        "10 21 15 26 Trophy Room\n" +
        "22  5 23 12 Wine Cellar\n" +
        "30  6 35 11 Winter Garden\n" +
        "20\n" +
        "8 3 Crepe Pan\n" +
        "4 2 Letter Opener\n" +
        "12 2 Shoe Horn\n" +
        "8 3 Sharp Knife\n" +
        "0 3 Revolver\n" +
        "15 3 Civil War Cannon\n" +
        "2 4 Chain Saw\n" +
        "16 2 Broom Stick\n" +
        "1 2 Billiard Cue\n" +
        "19 2 Rat Poison\n" +
        "6 2 Trowel\n" +
        "2 4 Big Red Hammer\n" +
        "6 2 Pinking Shears\n" +
        "18 3 Duck Decoy\n" +
        "13 2 Bad Cream\n" +
        "18 2 Monkey Hand\n" +
        "11 2 Tight Hat\n" +
        "19 2 Piece of Rope\n" +
        "9 3 Silken Cord\n" +
        "7 2 Loud Noise";
    Reader fileInput = new StringReader(input);
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
    
    String input = 
        "36 30 Doctor Lucky's Mansion\n" +
        "50 Doctor Lucky\n" +
        "Fortune the Cat\n" +
        "21\n" +
        "22 19 23 26 Armory\n" +
        "16 21 21 28 Billiard Room\n" +
        "28  0 35  5 Carriage House\n" +
        "12 11 21 20 Dining Hall\n" +
        "22 13 25 18 Drawing Room\n" +
        "26 13 27 18 Foyer\n" +
        "28 26 35 29 Green House\n" +
        "30 20 35 25 Hedge Maze\n" +
        "16  3 21 10 Kitchen\n" +
        " 0  3  5  8 Lancaster Room\n" +
        " 4 23  9 28 Library\n" +
        " 2  9  7 14 Lilac Room\n" +
        " 2 15  7 22 Master Suite\n" +
        " 0 23  3 28 Nursery\n" +
        "10  5 15 10 Parlor\n" +
        "28 12 35 19 Piazza\n" +
        " 6  3  9  8 Servants' Quarters\n" +
        " 8 11 11 20 Tennessee Room\n" +
        "10 21 15 26 Trophy Room\n" +
        "22  5 23 12 Wine Cellar\n" +
        "30  6 35 11 Winter Garden\n" +
        "20\n" +
        "8 3 Crepe Pan\n" +
        "4 2 Letter Opener\n" +
        "12 2 Shoe Horn\n" +
        "8 3 Sharp Knife\n" +
        "0 3 Revolver\n" +
        "15 3 Civil War Cannon\n" +
        "2 4 Chain Saw\n" +
        "16 2 Broom Stick\n" +
        "1 2 Billiard Cue\n" +
        "19 2 Rat Poison\n" +
        "6 2 Trowel\n" +
        "2 4 Big Red Hammer\n" +
        "6 2 Pinking Shears\n" +
        "18 3 Duck Decoy\n" +
        "13 2 Bad Cream\n" +
        "18 2 Monkey Hand\n" +
        "11 2 Tight Hat\n" +
        "19 2 Piece of Rope\n" +
        "9 3 Silken Cord\n" +
        "7 2 Loud Noise";
    Reader fileInput = new StringReader(input);
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
    
    String input = 
        "36 30 Doctor Lucky's Mansion\n" +
        "50 Doctor Lucky\n" +
        "Fortune the Cat\n" +
        "21\n" +
        "22 19 23 26 Armory\n" +
        "16 21 21 28 Billiard Room\n" +
        "28  0 35  5 Carriage House\n" +
        "12 11 21 20 Dining Hall\n" +
        "22 13 25 18 Drawing Room\n" +
        "26 13 27 18 Foyer\n" +
        "28 26 35 29 Green House\n" +
        "30 20 35 25 Hedge Maze\n" +
        "16  3 21 10 Kitchen\n" +
        " 0  3  5  8 Lancaster Room\n" +
        " 4 23  9 28 Library\n" +
        " 2  9  7 14 Lilac Room\n" +
        " 2 15  7 22 Master Suite\n" +
        " 0 23  3 28 Nursery\n" +
        "10  5 15 10 Parlor\n" +
        "28 12 35 19 Piazza\n" +
        " 6  3  9  8 Servants' Quarters\n" +
        " 8 11 11 20 Tennessee Room\n" +
        "10 21 15 26 Trophy Room\n" +
        "22  5 23 12 Wine Cellar\n" +
        "30  6 35 11 Winter Garden\n" +
        "20\n" +
        "8 3 Crepe Pan\n" +
        "4 2 Letter Opener\n" +
        "12 2 Shoe Horn\n" +
        "8 3 Sharp Knife\n" +
        "0 3 Revolver\n" +
        "15 3 Civil War Cannon\n" +
        "2 4 Chain Saw\n" +
        "16 2 Broom Stick\n" +
        "1 2 Billiard Cue\n" +
        "19 2 Rat Poison\n" +
        "6 2 Trowel\n" +
        "2 4 Big Red Hammer\n" +
        "6 2 Pinking Shears\n" +
        "18 3 Duck Decoy\n" +
        "13 2 Bad Cream\n" +
        "18 2 Monkey Hand\n" +
        "11 2 Tight Hat\n" +
        "19 2 Piece of Rope\n" +
        "9 3 Silken Cord\n" +
        "7 2 Loud Noise";
    Reader fileInput = new StringReader(input);
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
    
    String input = 
        "36 30 Doctor Lucky's Mansion\n" +
        "50 Doctor Lucky\n" +
        "Fortune the Cat\n" +
        "21\n" +
        "22 19 23 26 Armory\n" +
        "16 21 21 28 Billiard Room\n" +
        "28  0 35  5 Carriage House\n" +
        "12 11 21 20 Dining Hall\n" +
        "22 13 25 18 Drawing Room\n" +
        "26 13 27 18 Foyer\n" +
        "28 26 35 29 Green House\n" +
        "30 20 35 25 Hedge Maze\n" +
        "16  3 21 10 Kitchen\n" +
        " 0  3  5  8 Lancaster Room\n" +
        " 4 23  9 28 Library\n" +
        " 2  9  7 14 Lilac Room\n" +
        " 2 15  7 22 Master Suite\n" +
        " 0 23  3 28 Nursery\n" +
        "10  5 15 10 Parlor\n" +
        "28 12 35 19 Piazza\n" +
        " 6  3  9  8 Servants' Quarters\n" +
        " 8 11 11 20 Tennessee Room\n" +
        "10 21 15 26 Trophy Room\n" +
        "22  5 23 12 Wine Cellar\n" +
        "30  6 35 11 Winter Garden\n" +
        "20\n" +
        "8 3 Crepe Pan\n" +
        "4 2 Letter Opener\n" +
        "12 2 Shoe Horn\n" +
        "8 3 Sharp Knife\n" +
        "0 3 Revolver\n" +
        "15 3 Civil War Cannon\n" +
        "2 4 Chain Saw\n" +
        "16 2 Broom Stick\n" +
        "1 2 Billiard Cue\n" +
        "19 2 Rat Poison\n" +
        "6 2 Trowel\n" +
        "2 4 Big Red Hammer\n" +
        "6 2 Pinking Shears\n" +
        "18 3 Duck Decoy\n" +
        "13 2 Bad Cream\n" +
        "18 2 Monkey Hand\n" +
        "11 2 Tight Hat\n" +
        "19 2 Piece of Rope\n" +
        "9 3 Silken Cord\n" +
        "7 2 Loud Noise";
    Reader fileInput = new StringReader(input);
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
    
    String input = 
        "36 30 Doctor Lucky's Mansion\n" +
        "50 Doctor Lucky\n" +
        "Fortune the Cat\n" +
        "21\n" +
        "22 19 23 26 Armory\n" +
        "16 21 21 28 Billiard Room\n" +
        "28  0 35  5 Carriage House\n" +
        "12 11 21 20 Dining Hall\n" +
        "22 13 25 18 Drawing Room\n" +
        "26 13 27 18 Foyer\n" +
        "28 26 35 29 Green House\n" +
        "30 20 35 25 Hedge Maze\n" +
        "16  3 21 10 Kitchen\n" +
        " 0  3  5  8 Lancaster Room\n" +
        " 4 23  9 28 Library\n" +
        " 2  9  7 14 Lilac Room\n" +
        " 2 15  7 22 Master Suite\n" +
        " 0 23  3 28 Nursery\n" +
        "10  5 15 10 Parlor\n" +
        "28 12 35 19 Piazza\n" +
        " 6  3  9  8 Servants' Quarters\n" +
        " 8 11 11 20 Tennessee Room\n" +
        "10 21 15 26 Trophy Room\n" +
        "22  5 23 12 Wine Cellar\n" +
        "30  6 35 11 Winter Garden\n" +
        "20\n" +
        "8 3 Crepe Pan\n" +
        "4 2 Letter Opener\n" +
        "12 2 Shoe Horn\n" +
        "8 3 Sharp Knife\n" +
        "0 3 Revolver\n" +
        "15 3 Civil War Cannon\n" +
        "2 4 Chain Saw\n" +
        "16 2 Broom Stick\n" +
        "1 2 Billiard Cue\n" +
        "19 2 Rat Poison\n" +
        "6 2 Trowel\n" +
        "2 4 Big Red Hammer\n" +
        "6 2 Pinking Shears\n" +
        "18 3 Duck Decoy\n" +
        "13 2 Bad Cream\n" +
        "18 2 Monkey Hand\n" +
        "11 2 Tight Hat\n" +
        "19 2 Piece of Rope\n" +
        "9 3 Silken Cord\n" +
        "7 2 Loud Noise";
    Reader fileInput = new StringReader(input);
    World world = new World(fileInput);  
    world.movePetToNextRoom();
    gameController.playGame(world);
    assert (consoleOutput.toString().contains("Current Room Items:"));
    assert (consoleOutput.toString().contains("Visible Rooms:"));
    assert (consoleOutput.toString().contains("Room ID: 2, Room Name: Billiard Room"));
    assert (consoleOutput.toString().contains("Player: HumanPlayer"));
    assert (consoleOutput.toString().contains("Visible rooms from here:"));
  }
  
  @Test
  public void testPlayerDisplayInfo() throws InterruptedException, IOException {
    String simulatedUserInput = "1\nHumanPlayer\n1\n4\n8\n0\n0\n"; 
    Readable consoleInput = new StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();
    RandomNumberGenerator rng = new RandomNumberGenerator();
    int maxTurns = 3;  
    gameController = new GameController(consoleInput, consoleOutput, rng, maxTurns);
    
    String input = 
        "36 30 Doctor Lucky's Mansion\n" +
        "50 Doctor Lucky\n" +
        "Fortune the Cat\n" +
        "21\n" +
        "22 19 23 26 Armory\n" +
        "16 21 21 28 Billiard Room\n" +
        "28  0 35  5 Carriage House\n" +
        "12 11 21 20 Dining Hall\n" +
        "22 13 25 18 Drawing Room\n" +
        "26 13 27 18 Foyer\n" +
        "28 26 35 29 Green House\n" +
        "30 20 35 25 Hedge Maze\n" +
        "16  3 21 10 Kitchen\n" +
        " 0  3  5  8 Lancaster Room\n" +
        " 4 23  9 28 Library\n" +
        " 2  9  7 14 Lilac Room\n" +
        " 2 15  7 22 Master Suite\n" +
        " 0 23  3 28 Nursery\n" +
        "10  5 15 10 Parlor\n" +
        "28 12 35 19 Piazza\n" +
        " 6  3  9  8 Servants' Quarters\n" +
        " 8 11 11 20 Tennessee Room\n" +
        "10 21 15 26 Trophy Room\n" +
        "22  5 23 12 Wine Cellar\n" +
        "30  6 35 11 Winter Garden\n" +
        "20\n" +
        "8 3 Crepe Pan\n" +
        "4 2 Letter Opener\n" +
        "12 2 Shoe Horn\n" +
        "8 3 Sharp Knife\n" +
        "0 3 Revolver\n" +
        "15 3 Civil War Cannon\n" +
        "2 4 Chain Saw\n" +
        "16 2 Broom Stick\n" +
        "1 2 Billiard Cue\n" +
        "19 2 Rat Poison\n" +
        "6 2 Trowel\n" +
        "2 4 Big Red Hammer\n" +
        "6 2 Pinking Shears\n" +
        "18 3 Duck Decoy\n" +
        "13 2 Bad Cream\n" +
        "18 2 Monkey Hand\n" +
        "11 2 Tight Hat\n" +
        "19 2 Piece of Rope\n" +
        "9 3 Silken Cord\n" +
        "7 2 Loud Noise";
    Reader fileInput = new StringReader(input);
    World world = new World(fileInput);  
  
    gameController.playGame(world);
    assert (consoleOutput.toString().contains("ID: 0, Name: HumanPlayer, "
        + "Current Room: Armory, Items:"));
  }
}
