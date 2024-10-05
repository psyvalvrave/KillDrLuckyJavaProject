package world;

/**
 * Represents a player in the game, implementing the Character interface.
 * This is not yet the requirement for milestone 1, so I just create a basic version, 
 * it will be updated in next iteration. 
 */
public class Player implements Character {
  private String playerName;
  private Room currentRoom;

  /**
   * Constructor for the Player class.
   * 
   * @param playerNameInput Name of the player.
   * @param startingRoomInput Initial room where the player starts.
   */
  public Player(String playerNameInput, Room startingRoomInput) {
    this.playerName = playerNameInput;
    this.currentRoom = startingRoomInput;
  }

  /**
   * Return player's location. 
   * 
   * @return The location of the player, should be room.
   */
  @Override
  public Room getLocation() {
    return currentRoom;
  }

  /**
   * Return player's name.
   * 
   * @return The name of the player.
   */
  @Override
  public String getCharacterName() {
    return playerName;
  }
  
  /**
   * It allow player to move from one room to the other. 
   */
  @Override
  public void move(Room room) {
    if (room != null) {
      currentRoom = room;
      System.out.println(playerName + " has moved to " + room.getRoomName());
    }
  }

  /**
   * Attempts to murder a target within the same room.
   * 
   * @param target The target to murder.
   * @param damage The amount of damage to inflict.
   */
  public void murder(Target target, int damage) {
    if (target != null && currentRoom.equals(target.getLocation())) {
      target.setHealthPoint(target.getHealthPoint() - damage);
      System.out.println(playerName + " has attacked " + target.getCharacterName() 
          + " with " + damage + " damage.");
    } else {
      System.out.println(playerName + " cannot attack " 
          + (target == null ? "nobody" : target.getCharacterName()) 
          + " from current location.");
    }
  }
  
  /**
   * Retrieves detailed information about the player including its current location.
   * 
   * @return A formatted string containing the name, health, and location of the target.
   */
  public String getCharacterInfo() {
    return String.format("Target Name: %s\nCurrent Location: %s",
        playerName, currentRoom.getRoomName());
  }
}
