package world;

/**
 * Represents a target in the game, capable of basic movement and interactions,
 * by implementing the Character interface.
 */
public class Target implements Character {
  private String targetName;
  private Room currentRoomTarget;
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
    if (hpInput < 1) {
      throw new IllegalArgumentException("Health points must be at least 1.");
    }
    if (startingRoomInput == null) {
      throw new IllegalArgumentException("Starting room cannot be null.");
    }
    this.targetName = targetNameInput;
    this.currentRoomTarget = startingRoomInput;
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
   * Retrieves the current location of the target.
   * 
   * @return The room in which the target is currently located.
   */
  @Override
  public Room getLocation() {
    return currentRoomTarget;
  }

  /**
   * Retrieves the name of the target.
   * 
   * @return The name of the target.
   */
  @Override
  public String getCharacterName() {
    return targetName;
  }

  /**
   * Moves the target to a new room.
   * 
   * @param room The new room to move the target to.
   * @throws IllegalArgumentException If the room is null.
   */
  @Override
  public void move(Room room) {
    if (room == null) {
      throw new IllegalArgumentException("Room cannot be null.");
    }
    currentRoomTarget = room;
  }
  
  /**
   * Retrieves detailed information about the target including its current location and health.
   * 
   * @return A formatted string containing the name, health, and location of the target.
   */
  public String getCharacterInfo() {
    return String.format("Target Name: %s\nHealth Points: %d\nCurrent Location: %s",
                         targetName, healthPoint, currentRoomTarget.getRoomName());
  }
}
