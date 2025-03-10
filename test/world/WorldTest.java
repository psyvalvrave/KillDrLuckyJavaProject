package world;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
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
  private World world;
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
    testRoom = new Room("Test Room", 1, new int[]{0, 0, 10, 10}, world.getRoomData());
    world.getRooms().clear();
    world.getRooms().add(testRoom);
    world.movePetToNextRoom();
  }
  
  @Test
  public void testLoadWorldFileNotFound() {
    assertThrows(FileNotFoundException.class, () -> {
      Readable fileInput = new FileReader("nonexistentfile.txt");
      new World(fileInput);
    });
  }
  
  @Test
  public void testLoadWorldFileWrongType() {
    assertThrows(FileNotFoundException.class, () -> {
      Readable fileInput = new FileReader("mansion.docx");
      new World(fileInput);
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
    Item item = (Item) world.createItem("Test Item", 0, 10);
    Room armory = (Room) world.getRooms().get(0);
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
    Target target = (Target) world.createTarget("Test Target", testRoom, 100);
    assertNotNull(target);
    assertEquals("Test Target", target.getCharacterName());
    assertEquals(100, target.getHealthPoint());
    assertEquals(testRoom, target.getLocation());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateTargetInvalidhp() {
    world.createTarget("Test Target", testRoom, 0);
  }


  @Test
  public void testCreateRoomValid() {
    int[] coordinates = {0, 0, 5, 5};
    Room room = (Room) world.createRoom("New Room", 2, coordinates, new ArrayList<>());
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
    world.createTarget("Test Target", (Room) world.getRooms().get(0), 10);
    Room secondRoom = (Room) world.getRooms().get(1); 
    world.moveTargetToNextRoom(); 
    assertEquals("Target should be in second room", secondRoom, world.getTarget().getLocation());
  }
  
  @Test
  public void testMoveTargetToFirstRoomWhenInLastRoom() {
    Room lastRoom = (Room) world.getRooms().get(world.getRooms().size() - 1);
    world.createTarget("Test Target", lastRoom, 10);
    world.moveTargetToNextRoom();
    Room firstRoom = (Room) world.getRooms().get(0);
    assertEquals("Target should cycle back to the first "
        + "room", firstRoom, world.getTarget().getLocation());
  }
  
  @Test
  public void testRoomExistence() {
    Room armory = null;
    Room billiardRoom = null;
    Room carriageHouse = null;

    for (Block room : world.getRooms()) {
      if (room.getRoomName().equals("Armory")) {
        armory = (Room) room;
      } else if (room.getRoomName().equals("Billiard Room")) {
        billiardRoom = (Room) room;
      } else if (room.getRoomName().equals("Carriage House")) {
        carriageHouse = (Room) room;
      }
    }

    assertNotNull("Armory should not be null", armory);
    assertNotNull("Billiard Room should not be null", billiardRoom);
    assertNotNull("Carriage House should not be null", carriageHouse);
  }
  
  @Test
  public void testRoomAdjacency() {
    Room armory = (Room) world.getRooms().stream()
                       .filter(room -> room.getRoomName().equals("Armory"))
                       .findFirst()
                       .orElse(null);
    Room billiardRoom = (Room) world.getRooms().stream()
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
    Room armory = (Room) world.getRooms().stream()
                       .filter(room -> room.getRoomName().equals("Armory"))
                       .findFirst()
                       .orElse(null);
    Room carriageHouse = (Room) world.getRooms().stream()
                              .filter(room -> room.getRoomName().equals("Carriage House"))
                              .findFirst()
                              .orElse(null);

    assertFalse("Armory should not be visible from Carriage "
        + "House", armory.isVisibleFrom(carriageHouse));
  }
  
  @Test
  public void testRoomDetails() {
    Room armory = (Room) world.getRooms().stream()
                       .filter(room -> room.getRoomName().equals("Armory"))
                       .findFirst()
                       .orElse(null);
    
    assertNotNull("Armory should not be null", armory);
    String expectedDetails = "Room Name: Armory\n"
        + "Room ID: 1\n" 
        + "Coordinates: [22, 19, 23, 26]\n"
        + "Neighbors: Billiard Room, Dining Hall, Drawing Room\n"
        + "Visible From: Billiard Room, Dining Hall, Drawing Room, "
        + "Library, Master Suite, Nursery, Tennessee Room, Trophy Room, Wine Cellar\n"
        + "Items: Revolver";
    assertEquals("Details should match expected output", expectedDetails, armory.getInfo());
  }
  
  @Test
  public void testGetRoomName() {
    Room armory = (Room) world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    assertEquals("Room name should match", "Armory", armory.getRoomName());
  }
  
  @Test
  public void testGetNeighbors() {
    Room armory = (Room) world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    Room drawingRoom = (Room) world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Drawing Room"))
        .findFirst()
        .orElse(null);
    List<Block> neighbors = armory.getNeighbor();
    assertNotNull("Neighbors list should not be null", neighbors);
    assertTrue("Neighbors should contain neighbor1", neighbors.contains(drawingRoom));
    assertEquals("Should have exactly two neighbors", 3, neighbors.size());
  }
  
  @Test
  public void testGetNeighborNames() {
    Room armory = (Room) world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    String expected = "Billiard Room, Dining Hall, Drawing Room";
    assertEquals("Neighbor names should match output", expected, armory.getNeighborNames());
  }
  
  @Test
  public void testIsAdjacentTrue() {
    Room armory = (Room) world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    Room adjacentRoom = (Room) world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Drawing Room"))
        .findFirst()
        .orElse(null);
    assertTrue("Room should be adjacent to adjacentRoom", armory.isAdjacent(adjacentRoom));
  }
  
  @Test
  public void testIsAdjacentFalse() {
    Room armory = (Room) world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    Room adjacentRoom = (Room) world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Library"))
        .findFirst()
        .orElse(null);
    assertTrue("Room should NOT be adjacent to adjacentRoom", !armory.isAdjacent(adjacentRoom));
  }
  
  @Test
  public void testIsVisibleFromTrue() {
    Room armory = (Room) world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    Room visibleRoom = (Room) world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Library"))
        .findFirst()
        .orElse(null);
    assertTrue("Room should be visible from visibleRoom", armory.isVisibleFrom(visibleRoom));
  }
  
  @Test
  public void testIsVisibleFromFalse() {
    Room armory = (Room) world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    Room visibleRoom = (Room) world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Kitchen"))
        .findFirst()
        .orElse(null);
    assertTrue("Room should NOT be visible from visibleRoom", !armory.isVisibleFrom(visibleRoom));
  }
  
  @Test
  public void testGetItems() {
    Room armory = (Room) world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    Item testItem = (Item) world.getItems().stream()
        .filter(room -> room.getItemName().equals("Revolver"))
        .findFirst()
        .orElse(null);
    List<Gadget> items = armory.getItem();
    assertNotNull("Items list should not be null", items);
    assertTrue("Items list should contain testItem", items.contains(testItem));
    assertEquals("Items list should contain exactly 1 items", 1, items.size());
  }
  
  @Test
  public void testGetCoordinates() {
    Room armory = (Room) world.getRooms().stream()
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
    Room armory = (Room) world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    Room drawingRoom = (Room) world.getRooms().stream()
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
    Room armory = (Room) world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    Room parlor = (Room) world.getRooms().stream()
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
    Room armory = (Room) world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    Room drawingRoom = (Room) world.getRooms().stream()
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
    Room armory = (Room) world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Armory"))
        .findFirst()
        .orElse(null);
    Room parlor = (Room) world.getRooms().stream()
        .filter(room -> room.getRoomName().equals("Parlor"))
        .findFirst()
        .orElse(null);
    armory.addVisibleFromRoom(parlor);
    assertFalse("Library should not be added as a neighbor to Armory "
        + "because they are not adjacent", armory.getVisibleFrom().contains(parlor));
    assertFalse("Armory should not be added as a neighbor to "
        + "Library", parlor.getVisibleFrom().contains(armory));
  }
  
  @Test
  public void testCreatePlayer() {
    Player player = (Player) world.createPlayer("Alice", 1);  
    assertNotNull("Player should be created", player);
    assertEquals("Player's name should be Alice", "Alice", player.getCharacterName());
    assertEquals("Player should start in the first room", 
        world.getRooms().get(0), player.getLocation());
    assertEquals("Player's item limit should be 3", 3, player.getItemLimit());
  }
  
  @Test
  public void testCreateMultiplePlayers() {
    int initialItemLimit = 3;
    world.setItemLimit(initialItemLimit); 

    Player player1 = (Player) world.createPlayer("Alice", 1);
    Player player2 = (Player) world.createPlayer("Bob", 2);
    Player player3 = (Player) world.createPlayer("Charlie", 3);

    assertNotNull("Player 1 should be created", player1);
    assertNotNull("Player 2 should be created", player2);
    assertNotNull("Player 3 should be created", player3);

    assertEquals("Player 1's name should be Alice", "Alice", player1.getCharacterName());
    assertEquals("Player 2's name should be Bob", "Bob", player2.getCharacterName());
    assertEquals("Player 3's name should be Charlie", "Charlie", player3.getCharacterName());

    assertEquals("Player 1 should start in the first room", 
        world.getRooms().get(0), player1.getLocation());
    assertEquals("Player 2 should start in the second room", 
        world.getRooms().get(1), player2.getLocation());
    assertEquals("Player 3 should start in the third room", 
        world.getRooms().get(2), player3.getLocation());

    assertEquals("Player 1's item limit should be 3", initialItemLimit, player1.getItemLimit());
    assertEquals("Player 2's item limit should be 3", initialItemLimit, player2.getItemLimit());
    assertEquals("Player 3's item limit should be 3", initialItemLimit, player3.getItemLimit());

    assertNotEquals("Player 1 and Player 2 should have different IDs", 
        player1.getPlayerId(), player2.getPlayerId());
    assertNotEquals("Player 2 and Player 3 should have different IDs", 
        player2.getPlayerId(), player3.getPlayerId());
    assertNotEquals("Player 1 and Player 3 should have different IDs", 
        player1.getPlayerId(), player3.getPlayerId());
  }


  @Test
  public void testSetItemLimit() {
    world.setItemLimit(3); 
    Player player = (Player) world.createPlayer("Bob", 1);
    assertEquals("Item limit should be updated to 3", 3, player.getItemLimit());

    Player existingPlayer = (Player) world.createPlayer("Charlie", 1);
    world.setItemLimit(2);
    assertEquals("Existing player's item limit should be adjusted to 2", 
        2, existingPlayer.getItemLimit());
  }

  @Test
  public void testCreatePlayerWithInvalidRoom() {
    int invalidRoomIndex = world.getRooms().size(); 
    assertThrows(IllegalArgumentException.class, 
        () -> world.createPlayer("Dave", invalidRoomIndex + 1));
  }

  @Test
  public void testAdjustItemLimitWithItems() {
    Room startingRoom = (Room) world.getRooms().get(0);
    Item sword = new Item("Sword", startingRoom, 10);
    Item shield = new Item("Shield", startingRoom, 5);
    Item bow = new Item("Bow", startingRoom, 7);
    startingRoom.addItem(sword);
    startingRoom.addItem(shield);
    startingRoom.addItem(bow);
    Player player = (Player) world.createPlayer("Eve", 1);
    player.pickItem(sword);
    player.pickItem(shield);
    player.pickItem(bow);
    world.setItemLimit(2);
    assertEquals("Player should have no more than 2 items", 2, player.getItem().size());
  }

  @Test
  public void testReduceItemLimitAndRemoveExcessItems() {
    Room startingRoom = (Room) world.getRooms().get(0); 
    Item sword = new Item("Sword", startingRoom, 10);
    Item shield = new Item("Shield", startingRoom, 5);
    Item bow = new Item("Bow", startingRoom, 7);
    startingRoom.addItem(sword);
    startingRoom.addItem(shield);
    startingRoom.addItem(bow);
    Player playerFrank = (Player) world.createPlayer("Frank", 1);
    playerFrank.pickItem(sword);
    playerFrank.pickItem(shield);
    playerFrank.pickItem(bow);

    playerFrank.setItemLimit(2); 
    assertEquals("Item limit should be updated to 2", 2, playerFrank.getItemLimit());
    assertEquals("Player should have only 2 items after item limit reduction", 
        2, playerFrank.getItem().size());

    assertTrue("Inventory should still contain Sword", playerFrank.getItem().contains(sword));
    assertTrue("Inventory should still contain Shield", playerFrank.getItem().contains(shield));
    assertFalse("Inventory should no longer contain Bow", playerFrank.getItem().contains(bow));
  }
  
  @Test
  public void testGetRoomOccupants() {
    Room armory = (Room) world.getRooms().get(0);
    world.createPlayer("Alice", 1);
    world.createPlayer("Bob", 1);
    String occupants = world.getRoomOccupants(armory);
    assertTrue("Occupants should include the target", occupants.contains("Target: Doctor Lucky"));
    assertTrue("Occupants should include player Alice", occupants.contains("Player: Alice"));
    assertTrue("Occupants should include player Bob", occupants.contains("Player: Bob"));
  }
  
  @Test
  public void testDisplayRoomInfoValid() {
    String roomInfo = world.displayRoomInfo("Armory");
    assertNotNull("Room info should not be null", roomInfo);
    assertTrue("Room info should contain the room name", roomInfo.contains("Armory"));
    assertTrue("Room info should contain the room ID", roomInfo.contains("Room ID: 1"));
    assertTrue("Room info should list coordinates", 
        roomInfo.contains("Coordinates: [22, 19, 23, 26]"));
    assertTrue("Room info should list items (if any set up in setup)", 
        roomInfo.contains("Items: Revolver"));
    assertTrue("Room info should list occupants (if any set up in setup)", 
        roomInfo.contains("Target: Doctor Lucky"));
    world.moveTargetToNextRoom();
    roomInfo = world.displayRoomInfo("Armory");
    assertTrue("Room info should list occupants (if any set up in setup)", 
        roomInfo.contains("No occupants"));
  
  }
  
  @Test
  public void testDisplayRoomInfoValidWithPlayerId() {
    Player player = (Player) world.createPlayer("Alice", 1);
    String roomInfo = world.displayPlayerRoomInfo(player.getPlayerId());
    assertNotNull("Room info should not be null", roomInfo);
    assertTrue("Room info should contain the room name", roomInfo.contains("Armory"));
    assertTrue("Room info should contain the room ID", roomInfo.contains("Room ID: 1"));
    assertTrue("Room info should list coordinates", 
        roomInfo.contains("Coordinates: [22, 19, 23, 26]"));
    assertTrue("Room info should list items (if any set up in setup)", 
        roomInfo.contains("Items: Revolver"));
    assertTrue("Room info should list occupants (if any set up in setup)", 
        roomInfo.contains("Target: Doctor Lucky"));
    world.moveTargetToNextRoom();
    roomInfo = world.displayPlayerRoomInfo(player.getPlayerId());
    assertTrue("Room info should list occupants (if any set up in setup)", 
        !roomInfo.contains("Target: Doctor Lucky"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDisplayRoomInfoInvalid() {
    world.displayRoomInfo("Nonexistent Room");
  }
  
  @Test
  public void testPetStartingLocation() {
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
    Block armory = world.getRooms().get(0);
    assertEquals("Initial location should be 'Aromry'", armory, world.getPet().getLocation());
  }
  
  @Test
  public void testGetTargetInfo() {
    String targetInfo = world.getTargetInfo();
    assertTrue("Should contain 'Health Points'", targetInfo.contains("Health Points"));
    assertTrue("Should contain 'Current Location'", targetInfo.contains("Current Location"));
  }
  
  @Test
  public void testPlayerLocation() {
    world.createPlayer("Alice", 1); 
    String playerLocationInfo = world.getPlayerLocation(0);
    assertTrue("Should contain 'Room ID'", playerLocationInfo.contains("Room ID"));
    assertTrue("Should contain 'Room Name'", playerLocationInfo.contains("Room Name"));
  }
  
  @Test
  public void testMovePetToDifferentRoom() {
    world.createPlayer("Alice", 2); 
    String moveResult = world.movePet(0, 2); 
    assertEquals("Pet has been moved to Billiard Room.", moveResult);
  }
  
  @Test
  public void testinitializePetDfs() {
    world.initializePetDfs(); 
    CharacterPet pet = world.getPet(); 
    Stack<Block> path = pet.getPath();
    StringBuilder pathString = new StringBuilder();
    while (!path.isEmpty()) {
      Block room = path.pop();
      pathString.append(room.getRoomName()).append(" -> ");
    }
    String expectedPath = "Billiard Room -> Armory -> Dining Hall -> "
        + "Drawing Room -> Foyer -> Piazza -> "
        + "Hedge Maze -> Green House -> Hedge Maze -> Piazza -> Winter Garden -> Carriage House -> "
        + "Winter Garden -> Piazza -> Foyer -> Drawing Room -> Wine Cellar -> Kitchen -> Parlor -> "
        + "Servants' Quarters -> Lancaster Room -> Lilac Room -> "
        + "Master Suite -> Library -> Nursery -> "
        + "Library -> Trophy Room -> Tennessee Room";  
    String actualPath = pathString.toString().trim();
    if (actualPath.endsWith("->")) {
      actualPath = actualPath.substring(0, actualPath.length() - 3);
    }
    assertEquals("DFS path does not match expected", expectedPath.trim(), actualPath);
  }
  
  
  @Test
  public void testDfsResetsOnPetMove() {
    world.createPlayer("Alice", 2); 
    String moveResult = world.movePet(0, 21);
    assertTrue("Move message should confirm the move", moveResult.contains("Pet has been moved"));
    CharacterPet pet = world.getPet(); 
    Stack<Block> newPath = pet.getPath();
    assertFalse("Path should not be empty after move", newPath.isEmpty());
    StringBuilder pathString = new StringBuilder();
    while (!newPath.isEmpty()) {
      pathString.append(newPath.pop().getRoomName()).append(" -> ");
    }
    String expectedPath = "Winter Garden -> Carriage House -> Winter Garden -> Piazza -> Foyer -> "
        + "Drawing Room -> Armory -> Billiard Room -> Dining Hall -> Kitchen -> Parlor -> "
        + "Servants' Quarters -> Lancaster Room -> Lilac Room ->"
        + " Master Suite -> Library -> Nursery -> "
        + "Library -> Trophy Room -> Tennessee Room -> Trophy Room ->";  
    assertTrue("Path should start with the expected initial rooms",
        pathString.toString().startsWith(expectedPath));
  }
  
  @Test
  public void testVisibilityPlayersWithoutPet() {
    world.createPlayer("Bob", 1);
    world.createPlayer("Eric", 1);
    assertTrue("Player should be seen by others in the same room", world.canPlayerBeSeenByAny(1));
  }
  
  @Test
  public void testVisibilityPlayersWithPet() {
    world.createPlayer("Bob", 2);
    world.movePet(0, 21);
    world.createPlayer("Eric", 21);
    world.createPlayer("Ada", 21);
    assertTrue("Player should be seen by others in the "
        + "same room even with pet", world.canPlayerBeSeenByAny(2));
  }
  
  @Test
  public void testVisibilityPlayersNeighborSpaceWithoutPet() {
    world.createPlayer("Bob", 1);
    world.createPlayer("Eric", 5);
    assertTrue("Player should be seen by others in the neigbor "
        + "room", world.canPlayerBeSeenByAny(1));
  }
  
  @Test
  public void testVisibilityPlayersNeighborSpaceWithPetInBeSeen() {
    world.createPlayer("Bob", 2);
    world.movePet(0, 3);
    world.createPlayer("Eric", 21);
    world.createPlayer("Ada", 3);
    assertFalse("Player should not be seen by others in the neigbor "
        + "room when pet exists as the one being seen", world.canPlayerBeSeenByAny(2));
  }
  
  @Test
  public void testVisibilityPlayersNeighborSpaceWithPetInOtherPlayer() {
    world.createPlayer("Bob", 2);
    world.movePet(0, 21);
    world.createPlayer("Eric", 21);
    world.createPlayer("Ada", 3);
    assertTrue("Player should be seen by others in the neigbor "
        + "room when pet exists with other player", world.canPlayerBeSeenByAny(2));
  }
  
  @Test
  public void testAttackWithoutItem() {
    world.createPlayer("Bob", 1);
    CharacterTarget target = world.getTarget();
    int initialHealth = target.getHealthPoint();
    String murderResult = world.murderAttempt(0);
    int finalHealth = target.getHealthPoint();
    assertTrue("The string does contain 'Success'", murderResult.contains("Success"));
    assertEquals("Initial health should be 50", 50, initialHealth);
    assertEquals("Final health should be 49 after the attack", 49, finalHealth);
  }
  
  @Test
  public void testUseItem() {
    world.createPlayer("Bob", 1);
    world.playerPickUpItem(0, "Revolver");
    world.usePlayerItem(0, "Revolver");
    for (Block room : world.getRooms()) {
      for (Gadget item : room.getItem()) {
        assertFalse("Revolver should disappear from the game", 
            item.getItemName().equals("Revolver"));
      }
    }
  }
  
  @Test
  public void testUseItemAgain() {
    world.createPlayer("Bob", 1);
    world.playerPickUpItem(0, "Revolver");
    world.usePlayerItem(0, "Revolver");
    for (Block room : world.getRooms()) {
      for (Gadget item : room.getItem()) {
        assertFalse("Revolver should disappear from the game", 
            item.getItemName().equals("Revolver"));
      }
    }
    world.murderAttempt(0);
    assertThrows(IllegalArgumentException.class, () -> {
      world.usePlayerItem(0, "Revolver");
    });
  }
  
  @Test
  public void testUseNonCarryingItem() {
    CharacterPlayer bob = world.createPlayer("Bob", 1);
    for (Gadget item : bob.getItem()) {
      assertFalse("Bob should not have item Machine Gun", 
          !item.getItemName().equals("Piece of Rope"));
    }
    world.playerPickUpItem(0, "Revolver");
    assertThrows(IllegalArgumentException.class, () -> {
      world.usePlayerItem(0, "Piece of Rope");
    });

  }
  
  @Test
  public void testAttackWithItem() {
    world.createPlayer("Bob", 1);
    world.playerPickUpItem(0, "Revolver");
    CharacterTarget target = world.getTarget();
    int initialHealth = target.getHealthPoint();
    world.usePlayerItem(0, "Revolver");
    String murderResult = world.murderAttempt(0);
    int finalHealth = target.getHealthPoint();
    assertTrue("The string does contain 'Success'", murderResult.contains("Success"));
    assertEquals("Initial health should be 50", 50, initialHealth);
    assertEquals("Final health should be 49 after the attack", 47, finalHealth);
  }
  
  @Test
  public void testAttackNotInSameSpace() {
    CharacterPlayer bob = world.createPlayer("Bob", 2);
    CharacterTarget target = world.getTarget();
    assertFalse("Bob and target should not be in the same location", 
        bob.getLocation().equals(target.getLocation()));
    assertThrows(IllegalArgumentException.class, () -> { 
      world.murderAttempt(0); 
    });
  }
  
  @Test
  public void testAttackWithOtherPlayerPresent() {
    world.createPlayer("Bob", 1);
    world.createPlayer("Ada", 1);
    CharacterTarget target = world.getTarget();
    int initialHealth = target.getHealthPoint();
    String murderResult = world.murderAttempt(0);
    int finalHealth = target.getHealthPoint();
    assertTrue("The string does contain 'Success'", murderResult.contains("Failed"));
    assertEquals("Initial health should be 50", 50, initialHealth);
    assertEquals("Final health should be 50 after the failed attack", 50, finalHealth);
  }
  
  @Test
  public void testAttackWithOtherPlayerPresentInNeighborWithPet() {
    world.createPlayer("Bob", 1);
    CharacterPlayer ada = world.createPlayer("Ada", 2);
    CharacterTarget target = world.getTarget();
    int initialHealth = target.getHealthPoint();
    String murderResult = world.murderAttempt(0);
    assertTrue("The string does contain 'Success'", murderResult.contains("Failed"));
    assertEquals("Pet should be in the same room", ada.getLocation(), world.getPet().getLocation());
    assertEquals("Initial health should be 50", 50, initialHealth);
    int finalHealth = target.getHealthPoint();
    assertEquals("Final health should be 50 after the failed attack", 50, finalHealth);
  }
  
  @Test
  public void testAttackWithOtherPlayerPresentInNeighborWithoutPet() {
    world.createPlayer("Bob", 1);
    CharacterPlayer ada = world.createPlayer("Ada", 2);
    CharacterTarget target = world.getTarget();
    world.movePetToNextRoom();
    int initialHealth = target.getHealthPoint();
    String murderResult = world.murderAttempt(0);
    assertTrue("The string does contain 'Success'", murderResult.contains("Failed"));
    assertNotEquals("Pet should be in the same room", 
        ada.getLocation(), world.getPet().getLocation());
    assertEquals("Initial health should be 50", 50, initialHealth);
    int finalHealth = target.getHealthPoint();
    assertEquals("Final health should be 50 after the failed attack", 50, finalHealth);
  }
  
  @Test
  public void testAttackWhilePetInSameRoomWithOtherPlayerPresentInNeighbor() {
    world.createPlayer("Bob", 2);
    world.createPlayer("Ada", 19);
    world.moveTargetToRoom("Billiard Room");
    CharacterTarget target = world.getTarget();
    int initialHealth = target.getHealthPoint();
    String murderResult = world.murderAttempt(0);
    assertTrue("The string does contain 'Success'", murderResult.contains("Success"));
    assertEquals("Pet should be in the same room", target.getLocation(), 
        world.getPet().getLocation());
    assertEquals("Initial health should be 50", 50, initialHealth);
    int finalHealth = target.getHealthPoint();
    assertEquals("Final health should be 50 after the failed attack", 49, finalHealth);
  }

}
