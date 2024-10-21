package world;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.FileNotFoundException;
import java.io.FileReader;
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
   * 
   * @throws FileNotFoundException if the file specified by inputSource does not exist
   */
  @Before
  public void setUp() throws FileNotFoundException {
    Readable fileInput = new FileReader("res/mansion.txt");
    world = new World(fileInput);
    Room startingRoomTest = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    target = new Target("Test Target", startingRoomTest, 10);
  }

  @Test
  public void testTargetConstructorValid() {
    Room startingRoomTest = world.getRooms().stream()
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
