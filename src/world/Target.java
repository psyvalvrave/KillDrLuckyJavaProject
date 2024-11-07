package world;

/**
 * Represents a target in the game, capable of basic movement and interactions,
 * by implementing the Character interface.
 */
public class Target implements CharacterTarget{
  private final String name;
  private Block currentRoom;
  private int healthPoint; 
  
  /**
   * Constructor for the Target class.
   * 
   * @param targetNameInput Name of the target.
   * @param startingRoomInput Initial room where the target starts.
   * @param hpInput Initial health points of the target.
   * @throws IllegalArgumentException If hp is less than 1 or startingRoom is null.
   */
  public Target(String targetNameInput, Block startingRoomInput, int hpInput) {
    this.name = targetNameInput;
    this.currentRoom = startingRoomInput;
    if (hpInput < 1) {
      throw new IllegalArgumentException("Health points must be at least 1.");
    }
    if (startingRoomInput == null) {
      throw new IllegalArgumentException("Starting room cannot be null.");
    }
    this.healthPoint = hpInput;
  }

  @Override
  public int getHealthPoint() {
    return healthPoint;
  }

  @Override
  public void setHealthPoint(int hp) {
    this.healthPoint = hp;
  }
  
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
  
  @Override
  public Block getLocation() {
    return (Room) currentRoom;
  }
  

  @Override
  public String getCharacterName() {
    return name;
  }
  
  @Override
  public void move(Block room) {
    if (room == null) {
      throw new IllegalArgumentException("Room cannot be null.");
    }
    this.currentRoom = room;
  }

}
