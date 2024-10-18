package world;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
  public Player(String playerNameInput, Room startingRoomInput, int playerIdInput, int itemLimitInput) {
    super(playerNameInput, startingRoomInput);
    this.playerId = playerIdInput;
    this.itemLimit = itemLimitInput;
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
    if (target == null) {
      throw new IllegalArgumentException("No target specified.");
  }
  if (!currentRoom.equals(target.getLocation())) {
      throw new IllegalArgumentException(name + " cannot attack " + target.getCharacterName() + " from current location.");
  }
  target.setHealthPoint(target.getHealthPoint() - damage);
  }
  
  /**
   * Retrieves detailed information about the player including its current location.
   * 
   * @return A formatted string containing the name, health, and location of the target.
   */
  @Override
  public String getCharacterInfo() {
      return String.format("ID: %d, Name: %s, Current Room: %s, Items: %s", 
              playerId, getCharacterName(), getLocation().getRoomName(), listItems());
  }
  
  public List<Item> getItem() {
    return new ArrayList<>(items);
  }
  
  public int getPlayerId() {
    return playerId;
  }
  
  @Override
  public String toString() {
      return String.format("Player [ID=%d, Name=%s, Current Room=%s, Items=%s]",
              playerId, getCharacterName(), getLocation().getRoomName(), listItems());
  }
  
  public String listItems() {
    if (items.isEmpty()) {
        return "None";
    }
    StringBuilder sb = new StringBuilder();
    for (Item item : items) {
        if (sb.length() > 0) sb.append(", ");
        sb.append(item.getItemName());
    }
    return sb.toString();
  }
  
  @Override
  public int hashCode() {
      return Objects.hash(playerId, name, currentRoom, items);
  }

  @Override
  public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null || getClass() != obj.getClass()) return false;
      Player other = (Player) obj;
      return playerId == other.playerId &&
             Objects.equals(name, other.name) &&
             Objects.equals(currentRoom, other.currentRoom) &&
             Objects.equals(items, other.items);
  }
  
  public void pickItem(Item item) throws IllegalArgumentException {
    Room currentRoom = getLocation();
    if (!currentRoom.getItem().contains(item)) {
        throw new IllegalArgumentException("Item not available in the room.");
    }
    if (items.size() >= itemLimit) {
        throw new IllegalArgumentException("Item limit reached. Cannot pick up any more items.");
    }
    items.add(item);
    currentRoom.removeItem(item);
  }
  
  public String lookAround() {
    Room currentRoom = getLocation();
    StringBuilder info = new StringBuilder();
    info.append("Current Room ID: ").append(currentRoom.getRoomId()).append("\n")
        .append("Current Room Name: ").append(currentRoom.getRoomName()).append("\n")
        .append("Current Room Items: ").append(currentRoom.listItems()).append("\n");

    List<Room> visibleAndNeighbor = currentRoom.getVisibleFrom().stream()
        .filter(currentRoom.getNeighbor()::contains)
        .collect(Collectors.toList());

    if (!visibleAndNeighbor.isEmpty()) {
        info.append("Neighboring and Visible Rooms:\n");
        for (Room room : visibleAndNeighbor) {
            info.append("  Room ID: ").append(room.getRoomId())
                .append(", Room Name: ").append(room.getRoomName())
                .append(", Items: ").append(room.listItems()).append("\n");
        }
    }

    List<Room> visibleNotNeighbor = currentRoom.getVisibleFrom().stream()
        .filter(room -> !currentRoom.getNeighbor().contains(room))
        .collect(Collectors.toList());

    if (!visibleNotNeighbor.isEmpty()) {
        info.append("Other Visible Rooms:\n");
        for (Room room : visibleNotNeighbor) {
            info.append("  Room ID: ").append(room.getRoomId())
                .append(", Room Name: ").append(room.getRoomName())
                .append(", Items: ").append(room.listItems()).append("\n");
        }
    }

    return info.toString();
  }
  
  /**
   * Moves the player to a new room if it is a neighbor.
   *
   * @param room The new room to move the player to.
   * @throws IllegalArgumentException If the room is null or not a neighbor.
   */
  @Override
  public void move(Room room) {
      if (room == null) {
          throw new IllegalArgumentException("Room cannot be null.");
      }
      if (!currentRoom.getNeighbor().contains(room)) {
          throw new IllegalArgumentException("Cannot move to a non-neighboring room.");
      }
      this.currentRoom = room;
  }
  
  public void setItemLimit(int newItemLimit) {
    this.itemLimit = newItemLimit;
    while (items.size() > itemLimit) {
        items.remove(items.size() - 1); 
    }
}

  public int getItemLimit() {
    return this.itemLimit;
  }
  
}
