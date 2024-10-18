package world;

import org.junit.Before;
import org.junit.Test;
import java.io.FileReader;
import java.io.FileNotFoundException;
import static org.junit.Assert.*;

public class PlayerTest {
    private World world;
    private Player player;
    private Room startingRoom;
    private Room closeRoom;
    private Room farRoom;
    private Item item1;
    private Item item2;
    private Target target;

    @Before
    public void setUp() throws FileNotFoundException {
        Readable fileInput = new FileReader("res/three_rooms_player_test.txt");
        world = new World(fileInput);
        startingRoom = world.getRooms().get(0);
        closeRoom = world.getRooms().get(1);
        farRoom = world.getRooms().get(2);
        item1 = world.getItems().get(0);
        item2 = world.getItems().get(1);

        player = new Player("Test Player", startingRoom, 1, 2);
        target = new Target("Doctor Lucky", startingRoom, 100);
    }

    @Test
    public void testMoveToNeighborRoom() {
        player.move(closeRoom);
        assertEquals("Player should have moved to the neighboring room.", closeRoom, player.getLocation());
    }
    
    

    @Test(expected = IllegalArgumentException.class)
    public void testMoveToNonNeighborRoom() {
        player.move(farRoom);
        // This should throw an IllegalArgumentException as "farRoom" is not a neighbor of "startingRoom"
    }

    @Test
    public void testPickItem_Success() {
        assertEquals("Starting room should have items before picking.", 2, startingRoom.getItem().size());
        player.pickItem(item1);
        assertTrue("Player should have item1 in their inventory.", player.getItem().contains(item1));
        assertFalse("Starting room should no longer have item1.", startingRoom.getItem().contains(item1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPickItem_ItemNotInRoom() {
        // Create an item that's not added to any room
        Item itemNotInRoom = new Item("Ghost Item", null, 5);
        player.pickItem(itemNotInRoom);
        // This should throw an IllegalArgumentException as the item is not in the player's current room
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPickItem_ExceedsLimit() {
        // Player can only carry 2 items and already has one from previous test
        player.pickItem(item1);  // First item
        player.pickItem(item2);  // Second item
        // Add a third item to test the limit
        Item item3 = new Item("Excess Item", startingRoom, 5);
        startingRoom.addItem(item3);
        player.pickItem(item3);
    }

    @Test
    public void testLookAround() {
        String expectedInfo = "Current Room ID: " + startingRoom.getRoomId() + "\n" +
                              "Current Room Name: " + startingRoom.getRoomName() + "\n" +
                              "Current Room Items: " + startingRoom.listItems() + "\n" +
                              "Neighboring and Visible Rooms:\n" +
                              "  Room ID: " + closeRoom.getRoomId() + ", Room Name: " + closeRoom.getRoomName() + ", Items: " + closeRoom.listItems() + "\n";
        assertEquals("Look around should provide accurate room info.", expectedInfo, player.lookAround());
    }
    
    @Test
    public void testMurderSuccess() {
        int damage = 25;
        player.murder(target, damage);
        assertEquals("Target health should be reduced by the damage amount.", 75, target.getHealthPoint());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMurderFailureDifferentRoom() {
        // Move target to a different room
        target.move(farRoom);
        int damage = 25;
        player.murder(target, damage); // This should throw IllegalArgumentException
    }
}
