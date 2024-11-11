package world;

import java.awt.image.BufferedImage;
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
   * Visualizes or draws the current state of the game world.
   * This method could update the visual representation of the world, 
   * such as a map or textual description.
   * @return A BufferedImage.
   */
  BufferedImage drawWorld();
  
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
   * Displays detailed information about a room identified by its name.
   * @param roomName The name of the room.
   * @return A string containing detailed information about the room and its occupants.
   */
  String displayRoomInfo(String roomName);

  /**
   * Displays detailed information about a room identified by its ID.
   * @param roomId The ID of the room.
   * @return A string containing detailed information about the room and its occupants.
   */
  String displayRoomInfo(int roomId);

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
   * Creates a player in the game world.
   * @param playerName The name of the player.
   * @param startRoomIndex The starting room index for the player.
   * @return A created playerId.
   */
  int callCreatePlayer(String playerName, int startRoomIndex);

  /**
   * Retrieves detailed information about the current target in the game.
   * @return Information about the target, otherwise a notification that no target is set.
   */
  String getTargetInfo();

  /**
   * Retrieves detailed information about a player identified by their ID.
   * @param playerId The unique identifier of the player.
   * @return Information about the player, otherwise a notification that no such player exists.
   */
  String getPlayerInfo(int playerId);

  /**
   * Retrieves the maximum number of turns allowed in the game.
   * @return The maximum number of turns.
   */
  int getMaxTurn();

  /**
   * Sets the maximum number of turns allowed in the game.
   * @param maxTurn The maximum number of turns to set.
   */
  void setMaxTurn(int maxTurn);

  /**
   * Moves a player to a specified room.
   * @param playerId The ID of the player to move.
   * @param roomId The ID of the destination room.
   * @return A message describing the result of the move attempt.
   */
  String movePlayer(int playerId, int roomId);

  /**
   * Allows a player to pick up an item.
   * @param playerId The ID of the player.
   * @param itemName The name of the item to pick up.
   * @return A message describing the result of the pickup attempt.
   */
  String playerPickUpItem(int playerId, String itemName);

  /**
   * Allows a player to look around their current location, identifying visible rooms and items.
   * @param playerId The ID of the player.
   * @return A descriptive message about what the player sees around them.
   */
  String playerLookAround(int playerId);
  
  /**
   * Retrieves a list of item names from a specific room identified by its room ID.
   * @param roomId The ID of the room whose items are to be listed.
   * @return A list containing the names of items in the specified room.
   */
  List<String> getRoomItems(int roomId);
  
  /**
   * Retrieves the room ID where the specified player is currently located.
   *
   * @param playerId The unique identifier of the player.
   * @return The room ID where the player is located, or throws an exception if not found.
   */
  int getPlayerRoomId(int playerId);
  
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
   * Retrieves a list of names or IDs of rooms adjacent to the player's current room.
   * 
   * @param playerId The ID of the player.
   * @return A list of neighboring room names or IDs.
   */
  List<String> getPlayerNeighborRoom(int playerId);

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
   * Checks if a player can interact with the pet based on their respective locations.
   * 
   * @param playerId The ID of the player attempting to interact with the pet.
   * @return True if the player and pet are in the same room, otherwise false.
   */
  boolean canInteractWithPet(int playerId);

  /**
   * Moves the pet to a specified room, if the player requesting 
   * the move is in the same room as the pet.
   * 
   * @param playerId The ID of the player moving the pet.
   * @param targetRoomId The ID of the room to move the pet to.
   * @return A string indicating the success or failure of the move.
   */
  String movePet(int playerId, int targetRoomId);

  /**
   * Retrieves the current health points of the target character.
   * 
   * @return The health points of the target.
   */
  int getTargetHealthPoint();

  /**
   * Retrieves a list of items currently in possession of the specified player.
   * 
   * @param playerId The ID of the player whose items are being listed.
   * @return A list of item names held by the player.
   */
  List<String> getPlayerItems(int playerId);

  /**
   * Attempts a murder action by the specified player against the target.
   * 
   * @param playerId The ID of the player attempting the murder.
   * @return A string describing the outcome of the murder attempt.
   */
  String murderAttempt(int playerId);

  /**
   * Allows a player to use a specific item, potentially affecting murder value.
   * 
   * @param playerId The ID of the player using the item.
   * @param itemName The name of the item to use.
   * @throws IllegalArgumentException If the item is not in the player's possession.
   */
  void usePlayerItem(int playerId, String itemName) throws IllegalArgumentException;

  /**
   * Checks if a murder attempt can be made by the specified player based on game rules.
   * 
   * @param playerId The ID of the player.
   * @return True if the conditions for a murder attempt are met, otherwise false.
   */
  boolean canMurderAttempt(int playerId);

  /**
   * Removes the pet from the game, clearing any associated state for testing purpose.
   */
  void removePet();

  /**
   * Retrieves the location information of a player.
   * 
   * @param playerId The ID of the player whose location is being queried.
   * @return A string representing the player's location.
   */
  String getPlayerLocation(int playerId);

  /**
   * Directs a player to use the most powerful item they possess 
   * to enhance their murder attempt capability.
   * 
   * @param playerId The ID of the player.
   */
  void usePlayerHighestItem(int playerId);

  /**
   * Retrieves detailed information about all items held by a player.
   * 
   * @param playerId The ID of the player.
   * @return A string listing all items along with their details such as murder values.
   */
  String getPlayerItemsInfo(int playerId);
  
}
