package world;

/**
 * Abstract class representing a character in the game world.
 * This class provides common functionality for all characters, including basic movement
 * and information retrieval methods.
 */
public abstract class AbstractCharacter implements Character {
  protected final String name;
  protected Room currentRoom;
  
  public AbstractCharacter(String nameInput, Room currentRoomInput) {
    this.name = nameInput;
    this.currentRoom = currentRoomInput;
  }
    
  /**
   * Retrieves the current location of the AbstractCharacter.
   * 
   * @return The room in which the target is currently located.
   */
  @Override
  public Room getLocation() {
    return currentRoom;
  }
  
  /**
   * Retrieves the name of the AbstractCharacter.
   * 
   * @return The name of the AbstractCharacter.
   */
  @Override
  public String getCharacterName() {
    return name;
  }

  @Override
  public abstract String getCharacterInfo();
  
  /**
   * Moves the target to a new room.
   * 
   * @param room The new room to move the AbstractCharacter to.
   * @throws IllegalArgumentException If the room is null.
   */
  @Override
  public void move(Room room) {
    if (room == null) {
      throw new IllegalArgumentException("Room cannot be null.");
    }
    this.currentRoom = room;
  }
}
