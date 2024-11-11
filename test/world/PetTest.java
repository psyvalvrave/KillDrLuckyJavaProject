package world;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Stack;
import org.junit.Before;
import org.junit.Test;

/**
 * Test file for pet class. 
 */
public class PetTest {
  private Pet pet;
  private Room testRoom;
  
  /**
   * Sets up the testing environment before each test.
   * This method initializes a room with a predefined configuration, 
   * and create a new pet in it.
   */
  @Before
  public void setUp() {
    testRoom = new Room("Test Room", 1, new int[]{0, 0, 10, 10}, new ArrayList<>());
    pet = new Pet("Fortune the Cat", testRoom);
  }

  @Test
  public void testGetLocation() {
    assertEquals("Initial location should be 'Test Room'", testRoom, pet.getLocation());
  }

  @Test
  public void testMoveChangeLocation() {
    Room newRoom = new Room("New Room", 2, new int[]{0, 0, 5, 5}, new ArrayList<>());
    pet.move(newRoom);
    assertSame("Location should be updated to 'New Room'", newRoom, pet.getLocation());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMove_nullRoomShouldThrow() {
    pet.move(null);
  }

  @Test
public void testGetCharacterInfo() {
    String expectedInfo = "Pet Name: Fortune the Cat\nCurrent Location: Test Room";
    assertEquals("Character info should match expected format", 
        expectedInfo, pet.getCharacterInfo());
  }

  @Test
public void testSetPathReversePath() {
    Stack<Block> pathInput = new Stack<>();
    pathInput.push(new Room("Room A", 3, new int[]{0, 0, 5, 5}, new ArrayList<>()));
    pathInput.push(new Room("Room B", 4, new int[]{0, 0, 5, 5}, new ArrayList<>()));
    pet.setPath(pathInput);

    Stack<Block> expectedPath = new Stack<>();
    expectedPath.push(pathInput.get(1));
    expectedPath.push(pathInput.get(0));

    assertEquals("Path should be reversed in stack", expectedPath, pet.getPath());
  }
}
