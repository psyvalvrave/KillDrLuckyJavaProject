package world;

import java.util.List;

/**
 * Provides an outline for the world in which the game takes place, including methods to
 * retrieve and manipulate world information and interactions.
 */
public interface WorldOutline {
  /**
   * Gets the descriptive text about statue of the world.
   * 
   * @return a string containing the world's description.
   */
  String getWorldText();

  /**
   * Gets the count of rooms in the world.
   * 
   * @return the number of rooms.
   */
  int getRoomCount();

  /**
   * Gets the count of items in the world.
   * 
   * @return the number of items.
   */
  int getItemCount();

  /**
   * Gets the target player in the world.
   * 
   * @return the target player object.
   */
  Target getTarget();

  /**
   * Creates a new item in the game world.
   * 
   * @param name the name of the item.
   * @param location the location of the item in terms of room identifier.
   * @param murderValue the damage value of the item when used.
   * @return the newly created item.
   */
  Item createItem(String name, int location, int murderValue);

  /**
   * Creates a new target character in the game world.
   * 
   * @param name the name of the target character.
   * @param room the room where the target starts.
   * @param health the initial health of the target.
   * @return the newly created target character.
   */
  Target createTarget(String name, Room room, int health);

  /**
   * Creates a new room in the game world.
   * 
   * @param roomName the name of the room.
   * @param roomId the ID of the room.
   * @param coordinates the coordinates defining the room's boundaries on the grid.
   * @param allRoomData show the room data as list of string[]
   * @return the newly created room.
   */
  Room createRoom(String roomName, int roomId, int[] coordinates, List<String[]> allRoomData);

  /**
   * Visualizes or draws the current state of the game world.
   * This method could update the visual representation of the world, 
   * such as a map or textual description.
   */
  void drawWorld();
  
  /**
   * Update the world statue when something in the game is changed. 
   */
  void setWorldText();
  
  /**
   * Move target into selected room in World Class. 
   * 
   * @param roomName represent the name of the room. 
   */
  void moveTargetToRoom(String roomName);
  
  /**
   * Move target into next room in World Class by order. 
   */
  void moveTargetToNextRoom();
  
  /**
   * Gets the List of all the room object in this world.
   * @return List of Room objects.
   */
  List<Room> getRooms();
  
  /**
   * Gets the List of all the Item object in this world.
   * @return List of Item objects.
   */
  List<Item> getItems();
  
  /**
   * Gets the List of String[] which are detail from the part in input text file about rooms.
   * @return List of String[]
   */
  List<String[]> getRoomData();
  
  /**
   * Gets the List of String[] which are detail from the part in input text file about items.
   * @return List of String[]
   */
  List<String[]> getItemData();
  
  Player createPlayer(String playerName, int startRoomIndex);
  
  void setItemLimit(int newItemLimit);
  
  String getRoomOccupants(Room room);
  
  String displayRoomInfo(String roomName);
}
