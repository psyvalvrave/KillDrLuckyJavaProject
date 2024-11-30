package world;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * Provides an outline for the world in which the game takes place, including methods to
 * retrieve and manipulate world information and interactions.
 */
public interface WorldOutline extends ReadOnlyWorld {
  /**
   * Gets the descriptive text about statue of the world.
   * 
   * @return a string containing the world's description.
   */
  String getWorldText();

  /**
   * Gets the count of items in the world.
   * 
   * @return the number of items.
   */
  int getItemCount();
  
  /**
   * Update the world statue when something in the game is changed. 
   */
  void setWorldText();
  
  /**
   * Move target into selected room in World Class. 
   * 
   * @param roomName represent the name of the room. 
   * @return A message indicating success or the reason for failure.
   */
  String moveTargetToRoom(String roomName);
  
  /**
   * Move target into next room in World Class by order. 
   * @return A message indicating success or the reason for failure.
   */
  String moveTargetToNextRoom();
  
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
  
  /**
   * Sets the item limit for all players in the world.
   * @param newItemLimit The new limit on the number of items a player can carry.
   */
  void setItemLimit(int newItemLimit);

  /**
   * Retrieves information about occupants (players and target) in a specific room.
   * @param room The room to check for occupants.
   * @return A descriptive string listing all occupants in the specified room.
   */
  String getRoomOccupants(Block room);

  /**
   * Creates a room in the game world with specified properties.
   * @param roomName The name of the room.
   * @param roomId The identifier for the room.
   * @param coordinates The spatial coordinates of the room.
   * @param allRoomData Additional data for room configurations.
   * @return A confirmation message stating the room has been created.
   */
  String callCreateRoom(String roomName, int roomId, int[] coordinates, List<String[]> allRoomData);

  /**
   * Creates a target character in the game world.
   * @param name The name of the target.
   * @param room The starting room for the target.
   * @param health The initial health points for the target.
   * @return A confirmation message stating the target has been created.
   */
  String callCreateTarget(String name, Block room, int health);

  /**
   * Creates an item and places it within a specified room.
   * @param name The name of the item.
   * @param location The index of the room where the item is placed.
   * @param murderValue The potential damage or effect of the item.
   * @return A confirmation message stating the item has been created.
   */
  String callCreateItem(String name, int location, int murderValue);
  
  /**
   * Retrieves the all room ID as list where the specified room.
   *
   * @param roomId The unique identifier of the room.
   * @return The room ID of all the neighbors for certain room, or throws an exception if not found.
   */
  List<Integer> getNeighborRooms(int roomId);

  /**
   * Retrieves and displays information about the room in which the player is currently located.
   * 
   * @param playerId The ID of the player whose room information is being requested.
   * @return A string detailing the room's information.
   */
  String displayPlayerRoomInfo(int playerId);

  /**
   * Creates a pet character in the game world.
   * 
   * @param petName The name of the pet.
   * @param initialRoom The initial room where the pet will be located.
   * @return A string indicating the successful creation and placement of the pet.
   */
  String callCharacterPet(String petName, Block initialRoom);

  /**
   * Retrieves information about the pet in the game.
   * 
   * @return A string containing details about the pet.
   */
  String getPetInfo();

  /**
   * Moves the pet to the next room along its path if there is one defined in its path stack.
   * 
   * @return A string describing the result of the move operation.
   */
  String movePetToNextRoom();

  /**
   * Initializes or reinitializes the Depth-First Search (DFS) path for 
   * the pet based on its current location.
   */
  void initializePetDfs();

  /**
   * Checks if the specified player can be seen by any other player in the game.
   * 
   * @param playerId The ID of the player to check visibility for.
   * @return True if the player can be seen by at least one other player, otherwise false.
   */
  boolean canPlayerBeSeenByAny(int playerId);

  /**
   * Removes the pet from the game, clearing any associated state for testing purpose.
   */
  void removePet();

  /**
   * Directs a player to use the most powerful item they possess 
   * to enhance their murder attempt capability.
   * 
   * @param playerId The ID of the player.
   */
  void usePlayerHighestItem(int playerId);

  void setRunning(boolean running);

  void setCurrentTurn(int currentTurn);

  int getMaxTurns();

  void setMaxTurns(int maxTurns);

  String advanceTurn();

  String getPlayerName(int playerId);
  
  /**
   * Creates an item and adds it to the world.
   * 
   * @param name The name of the item.
   * @param location The room index where the item is located.
   * @param murderValue The impact or damage value of the item.
   * @return The newly created item object.
   */
  Gadget createItem(String name, int location, int murderValue);

  /**
   * Return the target object in the model. 
   * 
   * @return the CharacterTarget target
   */
  CharacterTarget getTarget();

  /**
   * Return the pet object in the model.
   * 
   * @return The CharacterPet object.
   */
  CharacterPet getPet();

  /**
   * Creates a new pet with a specified name starting in a specified room.
   * 
   * @param petNameInput The name of the pet.
   * @param initialRoom The room where the pet should start.
   * @return The new CharacterPet object.
   * @throws IllegalArgumentException If the room index is out of the valid range.
   */
  CharacterPet createPet(String petNameInput, Block initialRoom);

  /**
   * Get the item Object by filtering its name. 
   * 
   * @param itemName The name of the item.
   */
  Gadget getItemByName(String itemName);

  /**
   * Creates a new player with a specified name starting in a specified room.
   * 
   * @param playerName The name of the player.
   * @param startRoomIndex The 1-based index of the room where the player should start.
   * @return The new player object.
   * @throws IllegalArgumentException If the room index is out of the valid range.
   */
  CharacterPlayer createPlayer(String playerName, int startRoomIndex);

  /**
   * Retrieves a list of all items in the world.
   * This method provides a safe copy of the items list to 
   * ensure that the internal list is not modified.
   * 
   * @return A new list containing all the items currently in the world.
   */
  List<Gadget> getItems();

  /**
   * Retrieves a list of all rooms in the world.
   * This method provides a safe copy of the rooms list to 
   * ensure that the internal list is not modified.
   * 
   * @return A new list containing all the rooms currently in the world.
   */
  List<Block> getRooms();

  /**
   * Creates a room and adds it to the world.
   * 
   * @param roomName The name of the room.
   * @param roomId The identifier for the room.
   * @param coordinates The spatial coordinates of the room.
   * @param allRoomData Additional data for room configurations.
   * @return The newly created room object.
   */
  Block createRoom(String roomName, int roomId, int[] coordinates, List<String[]> allRoomData);

  
  CharacterTarget createTarget(String name, Block room, int health);

  void setRunningGui(boolean running);
  
}
