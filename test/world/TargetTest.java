package world;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

/**
 * This is the test file to test class Target, make sure methods work properly.
 * For incorrect target creation or function, it should always fails the test. 
 */
public class TargetTest {
  private World world;
  private Room startingRoom;
  private Target target;

  /**
   * Sets up the environment for each test.
   * This method initializes the {@link World} object with predefined room and target configurations
   * for testing. It loads the world from a specified file and finds a starting room for the
   * target to ensure consistent test conditions.
   *
   * The world is loaded from "res/mansion.txt", and the target is named "Test Target" with a
   * health of 10. The target is placed in the "Armory" room if it exists; otherwise, the target's
   * starting room is set to null.
   */
  @Before
  public void setUp() {
    String input = 
        "36 30 Doctor Lucky's Mansion\n" 
            + "50 Doctor Lucky\n" 
            + "Fortune the Cat\n" 
            + "21\n" 
            + "22 19 23 26 Armory\n" 
            + "16 21 21 28 Billiard Room\n" 
            + "28  0 35  5 Carriage House\n" 
            + "12 11 21 20 Dining Hall\n" 
            + "22 13 25 18 Drawing Room\n" 
            + "26 13 27 18 Foyer\n" 
            + "28 26 35 29 Green House\n" 
            + "30 20 35 25 Hedge Maze\n" 
            + "16  3 21 10 Kitchen\n" 
            + " 0  3  5  8 Lancaster Room\n" 
            + " 4 23  9 28 Library\n" 
            + " 2  9  7 14 Lilac Room\n" 
            + " 2 15  7 22 Master Suite\n" 
            + " 0 23  3 28 Nursery\n" 
            + "10  5 15 10 Parlor\n" 
            + "28 12 35 19 Piazza\n" 
            + " 6  3  9  8 Servants' Quarters\n" 
            + " 8 11 11 20 Tennessee Room\n" 
            + "10 21 15 26 Trophy Room\n" 
            + "22  5 23 12 Wine Cellar\n" 
            + "30  6 35 11 Winter Garden\n" 
            + "20\n" 
            + "8 3 Crepe Pan\n" 
            + "4 2 Letter Opener\n" 
            + "12 2 Shoe Horn\n" 
            + "8 3 Sharp Knife\n" 
            + "0 3 Revolver\n" 
            + "15 3 Civil War Cannon\n" 
            + "2 4 Chain Saw\n" 
            + "16 2 Broom Stick\n" 
            + "1 2 Billiard Cue\n" 
            + "19 2 Rat Poison\n" 
            + "6 2 Trowel\n" 
            + "2 4 Big Red Hammer\n" 
            + "6 2 Pinking Shears\n" 
            + "18 3 Duck Decoy\n" 
            + "13 2 Bad Cream\n" 
            + "18 2 Monkey Hand\n" 
            + "11 2 Tight Hat\n" 
            + "19 2 Piece of Rope\n" 
            + "9 3 Silken Cord\n" 
            + "7 2 Loud Noise\n";
    Reader fileInput = new StringReader(input);
    world = new World(fileInput);
    Room startingRoomTest = (Room) world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    target = new Target("Test Target", startingRoomTest, 10);
  }

  @Test
  public void testTargetConstructorValid() {
    Room startingRoomTest = (Room) world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    assertEquals("Test Target", target.getCharacterName());
    assertEquals(10, target.getHealthPoint());
    assertSame(startingRoomTest, target.getLocation());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTargetConstructorInvalidhp() {
    new Target("Test Target", startingRoom, 0);  
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testTargetConstructorNullRoom() {
    new Target("Test Target", null, 10);
  }

  @Test
  public void testMove() {
    Room newRoom = new Room("New Room", 2, new int[]{11, 11, 20, 20}, new ArrayList<>());
    target.move(newRoom);
    assertSame("Target should have moved to New Room", newRoom, target.getLocation());
  }

  @Test
  public void testSethp() {
    target.setHealthPoint(20);
    assertEquals("Health points should be updated to 20", 20, target.getHealthPoint());
  }
}
