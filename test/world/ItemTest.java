package world;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * This is the test file to test class Item, make sure methods work properly.
 * For incorrect item creation or function, it should always fails the test. 
 */
public class ItemTest {
  private Gadget testItem;
  private Room testRoom;
   
  /**
   * Sets up the testing environment before each test.
   * This method initializes a room with a predefined configuration, 
   * and create a new item in it.
   */
  @Before
  public void setUp() {
    testRoom = new Room("Test Room", 1, new int[]{0, 0, 10, 10}, null);  
    testItem = new Item("Test Item", testRoom, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testItemWithNegativeMurderValue() {
    new Item("Test Item", new Room("Test Room", 1, new int[]{0, 0, 10, 10}, null), -1);
  }

  @Test
  public void testGetItemName() {
    assertEquals("Item name should match", "Test Item", testItem.getItemName());
  }

  @Test
  public void testGetLocationItem() {
    assertEquals("Item location should be testRoom", testRoom, testItem.getLocationItem());
  }

  @Test
  public void testGetMurderValue() {
    assertEquals("Murder value should match", 10, testItem.getMurderValue());
  }
}
