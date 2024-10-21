package world;

/**
 * Represents a target in the game, capable of basic movement and interactions,
 * by implementing the Character interface.
 */
public class Target extends AbstractCharacter {
  private int healthPoint; 
  
  /**
   * Constructor for the Target class.
   * 
   * @param targetNameInput Name of the target.
   * @param startingRoomInput Initial room where the target starts.
   * @param hpInput Initial health points of the target.
   * @throws IllegalArgumentException If hp is less than 1 or startingRoom is null.
   */
  public Target(String targetNameInput, Room startingRoomInput, int hpInput) {
    super(targetNameInput, startingRoomInput);
    if (hpInput < 1) {
      throw new IllegalArgumentException("Health points must be at least 1.");
    }
    if (startingRoomInput == null) {
      throw new IllegalArgumentException("Starting room cannot be null.");
    }
    this.healthPoint = hpInput;
  }

  /**
   * Gets the current health points of the target.
   * 
   * @return the current health points.
   */
  public int getHealthPoint() {
    return healthPoint;
  }

  /**
   * Sets the health points of the target.
   * 
   * @param hp New health points to set.
   */
  public void setHealthPoint(int hp) {
    this.healthPoint = hp;
  }
  
  /**
   * Retrieves detailed information about the target including its current location and health.
   * 
   * @return A formatted string containing the name, health, and location of the target.
   */
  @Override
  public String getCharacterInfo() {
    return String.format("Target Name: %s\nHealth Points: %d\nCurrent Location: %s",
        name, healthPoint, currentRoom.getRoomName());
  }
  
  @Override
  public String toString() {
    return String.format("Target[name=%s, Current Room=%s, Health Points=%d]",
                         name, currentRoom.getRoomName(), healthPoint);
  }

}
