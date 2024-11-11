package world;

/**
 * Represents a character in the game, providing methods for movement and interaction.
 */
public interface CharacterTarget {
  /**
   * Gets the current location of the character.
   * 
   * @return the current room.
   */
  Block getLocation();

  /**
   * Gets the name of the character.
   * 
   * @return the character's name.
   */
  String getCharacterName();

  /**
   * Moves the character to a specified room.
   * 
   * @param room the room to move to.
   */
  void move(Block room);
  
  /**
   * Retrieves detailed information about the target including its current location and health.
   * 
   * @return A formatted string containing the name, health, and location of the target.
   */
  String getCharacterInfo();

  /**
   * Retrieves the current health points of this target.
   * 
   * @return The current health points of the character.
   */
  int getHealthPoint();
  
  /**
   * Sets the health points of this target to the specified value.
   * 
   * @param hp The new health points value for the character.
   */
  void setHealthPoint(int hp);

}
