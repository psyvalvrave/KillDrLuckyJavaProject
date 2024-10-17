package world;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game, implementing the Character interface.
 * This is not yet the requirement for milestone 1, so I just create a basic version, 
 * it will be updated in next iteration. 
 */
public class Player extends AbstractCharacter {
  private int playerId;
  private List<Item> items;
  private int itemLimit;

  /**
   * Constructor for the Player class.
   * 
   * @param playerNameInput Name of the player.
   * @param startingRoomInput Initial room where the player starts.
   */
  public Player(String playerNameInput, Room startingRoomInput, int playerId, int itemLimit) {
    super(playerNameInput, startingRoomInput);
    this.playerId = playerId;
    this.itemLimit = itemLimit;
    this.items = new ArrayList<>();
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
   * Attempts to murder a target within the same room.
   * 
   * @param target The target to murder.
   * @param damage The amount of damage to inflict.
   */
  public void murder(Target target, int damage) {
    if (target != null && currentRoom.equals(target.getLocation())) {
      target.setHealthPoint(target.getHealthPoint() - damage);
      System.out.println(name + " has attacked " + target.getCharacterName() 
          + " with " + damage + " damage.");
    } else {
      System.out.println(name + " cannot attack " 
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
        name, currentRoom.getRoomName());
  }
}
