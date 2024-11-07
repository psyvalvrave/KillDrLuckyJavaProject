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

  int getHealthPoint();
  
  void setHealthPoint(int hp);

}
