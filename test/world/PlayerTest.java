package world;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import org.junit.Before;
import org.junit.Test;

/**
 * Test file for player class. 
 */
public class PlayerTest {
  private World world;
  private Player player;
  private Player playerFar;
  private Player secondPlayerInRoom;
  private Room startingRoom;
  private Room closeRoom;
  private Room farRoom;
  private Item item1;
  private Item item2;
  private Target target;
  
  /**
   * Sets up the testing environment before each test.
   * This method initializes a new World with a predefined configuration, 
   * sets up rooms, players, and items
   * to facilitate various test scenarios. It also creates multiple players 
   * and a target within the world to simulate interaction.
   *
   * @throws FileNotFoundException if the configuration file for the world is not found.
   */
  @Before
  public void setUp() throws FileNotFoundException {
    Readable fileInput = new FileReader("res/three_rooms_player_test.txt");
    world = new World(fileInput);
    startingRoom = world.getRooms().get(0);
    closeRoom = world.getRooms().get(1);
    farRoom = world.getRooms().get(2);
    item1 = world.getItems().get(0);
    item2 = world.getItems().get(1);

    player = world.createPlayer("Test Player", 1);
    playerFar = world.createPlayer("Test Far Player", 3);
    secondPlayerInRoom = world.createPlayer("Test Second Player", 1);
    target = new Target("Doctor Lucky", startingRoom, 100);
  }
  
  @Test
  public void testStartPositionCorret() {
    assertEquals("Player should start in startingRoom.", startingRoom, player.getLocation());
  }

  @Test
  public void testMoveToNeighborRoom() {
    player.move(closeRoom);
    assertEquals("Player should have moved to the neighboring room.", 
        closeRoom, player.getLocation());
  }
  
  

  @Test(expected = IllegalArgumentException.class)
  public void testMoveToNonNeighborRoom() {
    player.move(farRoom);
  }

  @Test
  public void testPickItem_Success() {
    assertEquals("Starting room should have items before picking.", 
        2, startingRoom.getItem().size());
    player.pickItem(item1);
    assertTrue("Player should have item1 in their inventory.", player.getItem().contains(item1));
    assertFalse("Starting room should no longer have item1.", 
        startingRoom.getItem().contains(item1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPickItem_ItemNotInRoom() {
    Item itemNotInRoom = new Item("Ghost Item", null, 5);
    player.pickItem(itemNotInRoom);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPickItem_ExceedsLimit() {
    player.pickItem(item1);  
    player.pickItem(item2); 
    Item item3 = new Item("Item3", startingRoom, 5);
    Item item4 = new Item("Excess Item", startingRoom, 5);
    startingRoom.addItem(item3);
    startingRoom.addItem(item4);
    player.pickItem(item3);
    player.pickItem(item4);
  }

  @Test
  public void testLookAround() {
    String expectedInfo = "Current Room ID: " + startingRoom.getRoomId() + "\n" 
                          + "Current Room Name: " + startingRoom.getRoomName() + "\n" 
                          + "Current Room Items: " + startingRoom.listItems() + "\n" 
                          + "Neighboring and Visible Rooms:\n" 
                          + "  Room ID: " + closeRoom.getRoomId() + ", Room Name: " 
                          + closeRoom.getRoomName() + ", Items: " + closeRoom.listItems() + "\n";
    assertEquals("Look around should provide accurate room info.", 
        expectedInfo, player.lookAround());
  }
  
  @Test
  public void testLookAroundNoItem() {
    String expectedInfo = "Current Room ID: " + farRoom.getRoomId() + "\n" 
                          + "Current Room Name: " + farRoom.getRoomName() + "\n" 
                          + "Current Room Items: None" + "\n";
    assertEquals("Look around should provide accurate room info.", 
        expectedInfo, playerFar.lookAround());
  }
  
  @Test
  public void testMurderSuccess() {
    int damage = 25;
    player.murder(target, damage);
    assertEquals("Target health should be reduced by the damage amount.", 
        75, target.getHealthPoint());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMurderFailureDifferentRoom() {
    target.move(farRoom);
    int damage = 25;
    player.murder(target, damage); 
  }
  
  @Test
  public void testPlayerSeesOtherPlayerInRoom() {
    String lookAroundOutput = world.playerLookAround(player.getPlayerId());
    assertTrue("Look around output should contain second player's name.", 
               lookAroundOutput.contains(secondPlayerInRoom.getCharacterName()));
  }
  
  @Test
  public void testPlayerSeesTargetInRoom() {
    String lookAroundOutput = world.playerLookAround(player.getPlayerId());
    assertTrue("Look around output should contain target's name.", 
               lookAroundOutput.contains(target.getCharacterName()));
  }

  @Test
  public void testPlayerSeesOtherPlayerInVisibleRoom() {
    player.move(closeRoom);
    String lookAroundOutput = world.playerLookAround(player.getPlayerId());
    assertTrue("Look around output should mention visible room containing a player.", 
               lookAroundOutput.contains("Visible rooms from here") 
               && lookAroundOutput.contains(closeRoom.getRoomName()) 
               && lookAroundOutput.contains(secondPlayerInRoom.getCharacterName()));
  }
  
  @Test
  public void testGetPlayerInfoWithItems() {
    startingRoom.addItem(item1);
    player.pickItem(item1);
    String expectedInfo = String.format("ID: %d, Name: %s, Current Room: %s, Items: %s",
        player.getPlayerId(), player.getCharacterName(),
            startingRoom.getRoomName(), item1.getItemName());
    assertEquals("Player info should include the item.", expectedInfo, player.getCharacterInfo());
    assertEquals("Player info should include the item.", expectedInfo, 
        world.getPlayerInfo(player.getPlayerId()));
  }

  @Test
  public void testGetPlayerInfoWithoutItems() {
    String expectedInfo = String.format("ID: %d, Name: %s, Current Room: %s, Items: None",
        secondPlayerInRoom.getPlayerId(), secondPlayerInRoom.getCharacterName(),
            startingRoom.getRoomName());
    assertEquals("Player info should indicate no items.", expectedInfo, 
        secondPlayerInRoom.getCharacterInfo());
    assertEquals("Player info should include the item.", expectedInfo, 
        world.getPlayerInfo(secondPlayerInRoom.getPlayerId()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPlayerInfoInvalidPlayer() {
    world.getPlayerInfo(999);  
  }

}
