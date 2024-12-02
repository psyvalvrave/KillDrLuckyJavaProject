package world;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * Provides read-only access to the world state in the game.
 */
public interface ReadOnlyWorld {
  /**
   * Gets whether the game is currently running.
   * 
   * @return true if the game is running, otherwise false.
   */
  boolean getIsRunning();
  
  /**
   * Retrieves the IDs of all players in the game.
   * 
   * @return a list of player IDs.
   */
  List<Integer> getPlayerIds();
  
  /**
   * Retrieves a map of player IDs to their respective names.
   * 
   * @return a map where each key is a player ID and each value is the corresponding player's name.
   */
  Map<Integer, String> getPlayerNames();
  
  /**
   * Retrieves information on whether each player is controlled by the computer.
   * 
   * @return a map where each key is a player ID and each value is 
   *        a boolean indicating if the player is a computer.
   */
  Map<Integer, Boolean> getIsComputer();
  
  /**
   * Gets the ID of the current player.
   * 
   * @return the current player's ID.
   */
  int getCurrentPlayerId();
  
  /**
   * Gets the current turn number in the game.
   * 
   * @return the current turn number.
   */
  int getCurrentTurn();
  
  /**
   * Retrieves the current health points of the target character.
   * 
   * @return The health points of the target.
   */
  int getTargetHealthPoint();
  
  /**
   * Retrieves the location information of a player.
   * 
   * @param playerId The ID of the player whose location is being queried.
   * @return A string representing the player's location.
   */
  String getPlayerLocation(int playerId);
  
  /**
   * Retrieves detailed information about all items held by a player.
   * 
   * @param playerId The ID of the player.
   * @return A string listing all items along with their details such as murder values.
   */
  String getPlayerItemsInfo(int playerId);
  
  /**
   * Gets the count of rooms in the world.
   * 
   * @return the number of rooms.
   */
  int getRoomCount();
  
  /**
   * Creates a player in the game world.
   * @param playerName The name of the player.
   * @param startRoomIndex The starting room index for the player.
   * @return A created playerId.
   */
  int callCreatePlayer(String playerName, int startRoomIndex);
  
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
   * Moves the pet to a specified room, if the player requesting 
   * the move is in the same room as the pet.
   * 
   * @param playerId The ID of the player moving the pet.
   * @param targetRoomId The ID of the room to move the pet to.
   * @return A string indicating the success or failure of the move.
   */
  String movePet(int playerId, int targetRoomId);

  /**
   * Checks if a player can interact with the pet based on their respective locations.
   * 
   * @param playerId The ID of the player attempting to interact with the pet.
   * @return True if the player and pet are in the same room, otherwise false.
   */
  boolean canInteractWithPet(int playerId);

  /**
   * Retrieves a list of names or IDs of rooms adjacent to the player's current room.
   * 
   * @param playerId The ID of the player.
   * @return A list of neighboring room names or IDs.
   */
  List<String> getPlayerNeighborRoom(int playerId);
  

  /**
   * Attempts a murder action by the specified player against the target.
   * 
   * @param playerId The ID of the player attempting the murder.
   * @return A string describing the outcome of the murder attempt.
   */
  String murderAttempt(int playerId);
  
  /**
   * Retrieves a list of item names from a specific room identified by its room ID.
   * @param roomId The ID of the room whose items are to be listed.
   * @return A list containing the names of items in the specified room.
   */
  List<String> getRoomItems(int roomId);

  /**
   * Retrieves a list of items currently in possession of the specified player.
   * 
   * @param playerId The ID of the player whose items are being listed.
   * @return A list of item names held by the player.
   */
  List<String> getPlayerItems(int playerId);
  
  /**
   * Allows a player to use a specific item, potentially affecting murder value.
   * 
   * @param playerId The ID of the player using the item.
   * @param itemName The name of the item to use.
   * @throws IllegalArgumentException If the item is not in the player's possession.
   */
  void usePlayerItem(int playerId, String itemName) throws IllegalArgumentException;
  
  /**
   * Retrieves the room ID where the specified player is currently located.
   *
   * @param playerId The unique identifier of the player.
   * @return The room ID where the player is located, or throws an exception if not found.
   */
  int getPlayerRoomId(int playerId);

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
   * Visualizes or draws the current state of the game world.
   * This method could update the visual representation of the world, 
   * such as a map or textual description.
   * @return A BufferedImage.
   */
  BufferedImage drawWorld();
  

  /**
   * Checks if a murder attempt can be made by the specified player based on game rules.
   * 
   * @param playerId The ID of the player.
   * @return True if the conditions for a murder attempt are met, otherwise false.
   */
  boolean canMurderAttempt(int playerId);
  
  /**
   * Retrieves coordinates for all players in the game world.
   * 
   * @return A map where each key is a player ID and each 
   *        value is the corresponding rectangle representing player coordinates.
   */
  Map<Integer, Rectangle> getPlayerCoordinates();
  
  /**
   * Retrieves coordinates for all rooms in the game world.
   * 
   * @return A map where each key is a room ID and each value 
   *        is the corresponding rectangle representing room coordinates.
   */
  Map<Integer, Rectangle> getRoomCoordinates();
  
  /**
   * Gets whether the GUI for the game is currently running.
   * 
   * @return true if the GUI is running, otherwise false.
   */
  boolean getIsRunningGui();
}
