package world;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * This is the test file to test class World and Room, make sure methods work properly.
 * For incorrect world and room creation or function, it should always fails the test. 
 * It is difficult to create room without the world since creating a room requires
 *  calculate the room's neighbors and visibility through the world input text file, 
 *  which have the data for all other rooms.
 * Create a single room does not work as what it should be. Creating these two class together,
 * and test them together to make sure they are both function properly. 
 */
public class WorldTest {
  private WorldOutline world;
  private Room testRoom;
  
  /**
   * Sets up the test environment before each test execution.
   * This method initializes a new {@link World} instance by loading a predefined configuration from
   * "res/mansion.txt". It then creates a specific 
   * test room named "Test Room" with a unique identifier
   * and defined coordinates. The method also clears any existing rooms in the world and adds the
   * newly created test room to ensure a controlled test environment.
   *
   * The test room has the following characteristics:
   * - Name: "Test Room"
   * - ID: 1
   * - Coordinates: Rectangle from (0,0) to (10,10)
   * 
   * @throws FileNotFoundException if the file specified by inputSource does not exist
   */
  @Before
  public void setUp() throws FileNotFoundException {
    Readable fileInput = new FileReader("res/mansion.txt");
    world = new World(fileInput); 
    testRoom = new Room("Test Room", 1, new int[]{0, 0, 10, 10}, world.getRoomData());
    world.getRooms().clear();
    world.getRooms().add(testRoom);
  }
  
  @Test
  public void testLoadWorldFileNotFound() {
    assertThrows(FileNotFoundException.class, () -> {
      Readable fileInput = new FileReader("nonexistentfile.txt");
      World myworld = new World(fileInput);
    });
  }
  
  @Test
  public void testLoadWorldFileWrongType() {
    assertThrows(FileNotFoundException.class, () -> {
      Readable fileInput = new FileReader("mansion.docx");
      World myworld = new World(fileInput);
    });
  }

  @Test
  public void testRoomCount() {
    int expected = 21;  
    assertEquals(expected, world.getRoomCount());
  }
  
  @Test
  public void testItenCount() {
    int expected = 20;  
    assertEquals(expected, world.getItemCount());
  }

  @Test
  public void testItemInitialization() {
    assertFalse("Item list should not be empty after initialization.", world.getItemCount() == 0);
  }

  @Test
  public void testTargetInitialization() {
    assertNotNull("Target should be initialized.", world.getTarget());
  }

  @Test
  public void testMoveTarget() {
    String targetRoomName = "Parlor";
    world.moveTargetToRoom(targetRoomName);
    assertEquals("Target should be in the Parlor "
        + "room.", targetRoomName, world.getTarget().getLocation().getRoomName());
  }

  @Test
  public void testWorldText() {
    String expectedStart = "World Name: Doctor Lucky's Mansion";  
    assertTrue("World text should start with the correct "
        + "header.", world.getWorldText().startsWith(expectedStart));
  }
  
  @Test
  public void testCreateItemValid() {
    Item item = world.createItem("Test Item", 0, 10);
    Room armory = world.getRooms().get(0);
    assertEquals("The room at index 0 should be 'Armory'", "Armory", armory.getRoomName());
    assertNotNull("Item should not be null", item);
    assertEquals("Item name should match", "Test Item", item.getItemName());
    assertEquals("Murder value should match", 10, item.getMurderValue());
    assertEquals("Item location should be testRoom", armory, item.getLocationItem());
  }

  @Test
  public void testCreateItemInvalidLocation() {
    int invalidLocation = world.getRooms().size(); 
    assertThrows(IllegalArgumentException.class, () -> {
      world.createItem("Test Item", invalidLocation, 10);
    });
  }

  @Test
  public void testCreateTargetValid() {
    Target target = world.createTarget("Test Target", testRoom, 100);
    assertNotNull(target);
    assertEquals("Test Target", target.getCharacterName());
    assertEquals(100, target.getHealthPoint());
    assertEquals(testRoom, target.getLocation());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateTargetInvalidhp() {
    Target target = world.createTarget("Test Target", testRoom, 0);
  }


  @Test
  public void testCreateRoomValid() {
    int[] coordinates = {0, 0, 5, 5};
    Room room = world.createRoom("New Room", 2, coordinates, new ArrayList<>());
    assertNotNull(room);
    assertEquals("New Room", room.getRoomName());
    assertArrayEquals(coordinates, room.getCoordinates());
  }

  @Test
  public void testCreateRoomWithInvalidData() {
    int[] invalidCoordinates = {-1, -1, -5, -5}; 
    assertThrows(IllegalArgumentException.class, () -> {
      world.createRoom("Invalid Room", 3, invalidCoordinates, new ArrayList<>());
    });
    int[] invalidFiveCoordinates = {1, 1, 5, 5, 1}; 
    assertThrows(IllegalArgumentException.class, () -> {
      world.createRoom("Invalid Room", 3, invalidFiveCoordinates, new ArrayList<>());
    });
  }
  
  @Test
  public void testMoveTargetToNextRoom() {
    world.createTarget("Test Target", world.getRooms().get(0), 10);
    Room secondRoom = world.getRooms().get(1); 
    world.moveTargetToNextRoom(); 
    assertEquals("Target should be in second room", secondRoom, world.getTarget().getLocation());
  }
  
  @Test
  public void testMoveTargetToFirstRoomWhenInLastRoom() {
    Room lastRoom = world.getRooms().get(world.getRooms().size() - 1);
    world.createTarget("Test Target", lastRoom, 10);
    world.moveTargetToNextRoom();
    Room firstRoom = world.getRooms().get(0);
    assertEquals("Target should cycle back to the first "
        + "room", firstRoom, world.getTarget().getLocation());
  }
  
  @Test
  public void testRoomExistence() {
    Room armory = null;
    Room billiardRoom = null;
    Room carriageHouse = null;

    for (Room room : world.getRooms()) {
      if (room.getRoomName().equals("Armory")) {
        armory = room;
      } else if (room.getRoomName().equals("Billiard Room")) {
        billiardRoom = room;
      } else if (room.getRoomName().equals("Carriage House")) {
        carriageHouse = room;
      }
    }

    assertNotNull("Armory should not be null", armory);
    assertNotNull("Billiard Room should not be null", billiardRoom);
    assertNotNull("Carriage House should not be null", carriageHouse);
  }
  
  @Test
  public void testRoomAdjacency() {
    Room armory = world.getRooms().stream()
                       .filter(room -> room.getRoomName().equals("Armory"))
                       .findFirst()
                       .orElse(null);
    Room billiardRoom = world.getRooms().stream()
                             .filter(room -> room.getRoomName().equals("Billiard Room"))
                             .findFirst()
                             .orElse(null);
    assertTrue("Armory should be adjacent to Billiard "
        + "Room", armory.getNeighbor().contains(billiardRoom));
    assertTrue("Billiard Room should be adjacent to "
        + "Armory", billiardRoom.getNeighbor().contains(armory));
  }
  
  @Test
  public void testVisibility() {
    Room armory = world.getRooms().stream()
                       .filter(room -> room.getRoomName().equals("Armory"))
                       .findFirst()
                       .orElse(null);
    Room carriageHouse = world.getRooms().stream()
                              .filter(room -> room.getRoomName().equals("Carriage House"))
                              .findFirst()
                              .orElse(null);

    assertFalse("Armory should not be visible from Carriage "
        + "House", armory.isVisibleFrom(carriageHouse));
  }
  
  @Test
  public void testRoomDetails() {
    Room armory = world.getRooms().stream()
                       .filter(room -> room.getRoomName().equals("Armory"))
                       .findFirst()
                       .orElse(null);
    
    assertNotNull("Armory should not be null", armory);
    String expectedDetails = "Room Name: Armory\n"
        + "Room ID: 0\n" 
        + "Coordinates: [22, 19, 23, 26]\n"
        + "Neighbors: Billiard Room, Dining Hall, Drawing Room\n"
        + "Visible From: Billiard Room, Dining Hall, Drawing Room, "
        + "Library, Master Suite, Nursery, Tennessee Room, Trophy Room, Wine Cellar\n"
        + "Items: Revolver";
    assertEquals("Details should match expected output", expectedDetails, armory.getInfo());
  }
  
  @Test
  public void testGetRoomName() {
    Room armory = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    assertEquals("Room name should match", "Armory", armory.getRoomName());
  }
  
  @Test
  public void testGetNeighbors() {
    Room armory = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    Room drawingRoom = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Drawing Room"))
        .findFirst()
        .orElse(null);
    List<Room> neighbors = armory.getNeighbor();
    assertNotNull("Neighbors list should not be null", neighbors);
    assertTrue("Neighbors should contain neighbor1", neighbors.contains(drawingRoom));
    assertEquals("Should have exactly two neighbors", 3, neighbors.size());
  }
  
  @Test
  public void testGetNeighborNames() {
    Room armory = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    String expected = "Billiard Room, Dining Hall, Drawing Room";
    assertEquals("Neighbor names should match output", expected, armory.getNeighborNames());
  }
  
  @Test
  public void testIsAdjacentTrue() {
    Room armory = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    Room adjacentRoom = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Drawing Room"))
        .findFirst()
        .orElse(null);
    assertTrue("Room should be adjacent to adjacentRoom", armory.isAdjacent(adjacentRoom));
  }
  
  @Test
  public void testIsAdjacentFalse() {
    Room armory = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    Room adjacentRoom = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Library"))
        .findFirst()
        .orElse(null);
    assertTrue("Room should NOT be adjacent to adjacentRoom", !armory.isAdjacent(adjacentRoom));
  }
  
  @Test
  public void testIsVisibleFromTrue() {
    Room armory = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    Room visibleRoom = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Library"))
        .findFirst()
        .orElse(null);
    assertTrue("Room should be visible from visibleRoom", armory.isVisibleFrom(visibleRoom));
  }
  
  @Test
  public void testIsVisibleFromFalse() {
    Room armory = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    Room visibleRoom = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Kitchen"))
        .findFirst()
        .orElse(null);
    assertTrue("Room should NOT be visible from visibleRoom", !armory.isVisibleFrom(visibleRoom));
  }
  
  @Test
  public void testGetItems() {
    Room armory = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    Item testItem = world.getItems().stream()
        .filter(room -> room.getItemName().equals("Revolver"))
        .findFirst()
        .orElse(null);
    List<Item> items = armory.getItem();
    assertNotNull("Items list should not be null", items);
    assertTrue("Items list should contain testItem", items.contains(testItem));
    assertEquals("Items list should contain exactly 1 items", 1, items.size());
  }
  
  @Test
  public void testGetCoordinates() {
    Room armory = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    int[] coordinates = armory.getCoordinates();
    assertNotNull("Coordinates should not be null", coordinates);
    assertArrayEquals("Coordinates should match the "
        + "room's location", new int[]{22, 19, 23, 26}, coordinates);
  }
  
  @Test
  public void testAddNeighborSuccess() {
    Room armory = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    Room drawingRoom = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Drawing Room"))
        .findFirst()
        .orElse(null);
   
    armory.addNeighbor(drawingRoom);
    assertTrue("Drawing Room should now be a neighbor of "
        + "Armory", armory.getNeighbor().contains(drawingRoom));
    assertTrue("Armory should also be a neighbor of "
        + "Drawing Room", drawingRoom.getNeighbor().contains(armory));
  }
  
  @Test
  public void testAddNeighborFail() {
    Room armory = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    Room parlor = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Parlor"))
        .findFirst()
        .orElse(null);
    armory.addNeighbor(parlor);
    assertFalse("Library should not be added as a neighbor to "
        + "Armory because they are not adjacent", armory.getNeighbor().contains(parlor));
    assertFalse("Armory should not be added as a neighbor to "
        + "Library", parlor.getNeighbor().contains(armory));
  }
  
  @Test
  public void testAddVisbleFromSuccess() {
    Room armory = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    Room drawingRoom = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Drawing Room"))
        .findFirst()
        .orElse(null);
   
    armory.addVisibleFromRoom(drawingRoom);
    assertTrue("Drawing Room should now be a neighbor of "
        + "Armory", armory.getVisibleFrom().contains(drawingRoom));
    assertTrue("Armory should also be a neighbor of Drawing "
        + "Room", drawingRoom.getVisibleFrom().contains(armory));
  }
  
  @Test
  public void testAddVisbleFromFail() {
    Room armory = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    Room parlor = world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Parlor"))
        .findFirst()
        .orElse(null);
    armory.addVisibleFromRoom(parlor);
    assertFalse("Library should not be added as a neighbor to Armory "
        + "because they are not adjacent", armory.getVisibleFrom().contains(parlor));
    assertFalse("Armory should not be added as a neighbor to "
        + "Library", parlor.getVisibleFrom().contains(armory));
  }
}
