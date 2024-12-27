package world;

import java.util.List;

/**
 * The CharacterPlayer interface defines the necessary behaviors 
 * that must be implemented by any class that represents a player character.
 */
public interface CharacterPlayer {
  /**
   * Retrieves the current location of the player.
   * 
   * @return The Block where the player is currently located.
   */
  Block getLocation();

  /**
   * Moves the player to a specified room.
   * 
   * @param room The room to which the player should move.
   */
  void move(Block room);

  /**
   * Provides information about the player in a formatted string.
   * 
   * @return A string detailing the player's information.
   */
  String getCharacterInfo();

  /**
   * Retrieves a list of gadgets currently held by the player.
   * 
   * @return A list of gadgets.
   */
  List<Gadget> getItem();

  /**
   * Allows the player to pick up a gadget.
   * 
   * @param item The gadget to be picked up.
   * @throws IllegalArgumentException if the item cannot be picked up.
   */
  void pickItem(Gadget item) throws IllegalArgumentException;

  /**
   * Provides a description of the player's surroundings.
   * 
   * @return A string describing what the player can see around them.
   */
  String lookAround();

  /**
   * Provides a description of the player's surroundings with restrictions based on provided rooms.
   * 
   * @param restrictedRooms A list of rooms that are restricted from the player's view.
   * @return A string describing what the player can see, considering restrictions.
   */
  String lookAround(List<Block> restrictedRooms);
  
  /**
   * Sets the maximum number of items the player can carry.
   * 
   * @param newItemLimit The new item limit.
   */
  void setItemLimit(int newItemLimit);

  /**
   * Retrieves the current limit on the number of items the player can carry.
   * 
   * @return The maximum number of items the player can hold.
   */
  int getItemLimit();
  
  /**
   * Retrieves the character's name.
   * 
   * @return The name of the character.
   */
  String getCharacterName();
  
  /**
   * Retrieves the player's unique ID.
   * 
   * @return The ID of the player.
   */
  int getPlayerId();

  /**
   * Determines if this player can see another player based on their locations.
   * 
   * @param otherPlayer The other player to check visibility against.
   * @return True if the other player is visible; false otherwise.
   */
  boolean canSee(CharacterPlayer otherPlayer);

  /**
   * Attempts to murder a target character.
   * 
   * @param target The target to be attacked.
   */
  void murder(CharacterTarget target);

  /**
   * Uses a specified item to potentially affect the outcome of an action.
   * 
   * @param item The item to be used.
   */
  void useItem(Gadget item);

  /**
   * Uses the item with the highest murder value from the player's inventory.
   */
  void useHighestItem();

}
