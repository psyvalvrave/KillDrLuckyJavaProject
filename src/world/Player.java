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
public class Player implements CharacterPlayer {
  private final String name;
  private Block currentRoom;
  private final int playerId;
  private List<Gadget> items;
  private int itemLimit;
  private int murderPoint = 1;

  /**
   * Constructor for the Player class.
   * 
   * @param playerNameInput Name of the player.
   * @param startingRoomInput Initial room where the player starts.
   * @param playerIdInput player ID set, should be set by default in model
   * @param itemLimitInput Numbers of item can carry
   */
  public Player(String playerNameInput, Block startingRoomInput, 
      int playerIdInput, int itemLimitInput) {
    this.name = playerNameInput;
    this.currentRoom = startingRoomInput;
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
  public Block getLocation() {
    return currentRoom;
  }

  @Override
  public void murder(CharacterTarget target) {
    if (target == null) {
        throw new IllegalArgumentException("No target specified.");
    }
    if (!currentRoom.equals(target.getLocation())) {
        throw new IllegalArgumentException(name + " cannot attack " + target.getCharacterName() + " from current location.");
    }
    target.setHealthPoint(target.getHealthPoint() - murderPoint);
    this.murderPoint = 1;
  }
  
  @Override
  public void useItem(Gadget item) {
    if (item == null || !items.contains(item)) {
        throw new IllegalArgumentException("Specified item is not in possession.");
    }
    this.murderPoint = item.getMurderValue();  
    items.remove(item); 
}
  
  @Override
  public String getCharacterInfo() {
    return String.format("ID: %d, Name: %s, Current Room: %s, Items: %s", 
            playerId, getCharacterName(), getLocation().getRoomName(), listItems());
  }
  
  public List<Gadget> getItem() {
    ArrayList<Gadget> gadgets = new ArrayList<>(items);
    return gadgets;
  }
  
  public int getPlayerId() {
    return playerId;
  }
  
  @Override
  public String toString() {
    return String.format("Player [ID=%d, Name=%s, Current Room=%s, Items=%s]",
            playerId, getCharacterName(), getLocation().getRoomName(), listItems());
  }
  
  /**
   * Retrieves a list of items currently in possession of the player.
   * This method provides a defensive copy of the items 
   * list to ensure the encapsulation is maintained.
   *
   * @return A new list containing all the items the player currently holds.
   */
  public String listItems() {
    if (items.isEmpty()) {
      return "None";
    }
    StringBuilder sb = new StringBuilder();
    for (Gadget item : items) {
      if (sb.length() > 0) {
        sb.append(", ");
      }
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
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Player other = (Player) obj;
    return playerId == other.playerId 
        && Objects.equals(name, other.name) 
        && Objects.equals(currentRoom, other.currentRoom) 
        && Objects.equals(items, other.items);
  }
  

  public void pickItem(Gadget item) throws IllegalArgumentException {
    Room currentRoom = (Room) getLocation();
    if (!currentRoom.getItem().contains(item)) {
      throw new IllegalArgumentException("Item not available in the room.");
    }
    if (items.size() >= itemLimit) {
      throw new IllegalArgumentException("Item limit reached. Cannot pick up any more items.");
    }
    items.add((Item) item);
    currentRoom.removeItem((Item) item);
  }
  
  @Override
  public String lookAround() {
    Room currentRoom = (Room) getLocation();
    StringBuilder info = new StringBuilder();
    info.append("Current Room ID: ").append(currentRoom.getRoomId()).append("\n")
        .append("Current Room Name: ").append(currentRoom.getRoomName()).append("\n")
        .append("Current Room Items: ").append(currentRoom.listItems()).append("\n");

    List<Block> visibleAndNeighbor = currentRoom.getVisibleFrom().stream()
        .filter(currentRoom.getNeighbor()::contains)
        .collect(Collectors.toList());

    if (!visibleAndNeighbor.isEmpty()) {
      info.append("Neighboring and Visible Rooms:\n");
      for (Block room : visibleAndNeighbor) {
        info.append("  Room ID: ").append(room.getRoomId())
            .append(", Room Name: ").append(room.getRoomName())
            .append(", Items: ").append(((Room) room).listItems()).append("\n");
      }
    }

    List<Block> visibleNotNeighbor = currentRoom.getVisibleFrom().stream()
        .filter(room -> !currentRoom.getNeighbor().contains(room))
        .collect(Collectors.toList());

    if (!visibleNotNeighbor.isEmpty()) {
      info.append("Other Visible Rooms:\n");
      for (Block room : visibleNotNeighbor) {
        info.append("  Room ID: ").append(room.getRoomId())
            .append(", Room Name: ").append(room.getRoomName())
            .append(", Items: ").append(((Room) room).listItems()).append("\n");
      }
    }

    return info.toString();
  }
  
  @Override
  public String lookAround(List<Block> restrictedRooms) {
    Room currentRoom = (Room) getLocation();
    StringBuilder info = new StringBuilder();
    info.append("Current Room ID: ").append(currentRoom.getRoomId()).append("\n")
        .append("Current Room Name: ").append(currentRoom.getRoomName()).append("\n")
        .append("Current Room Items: ").append(currentRoom.listItems()).append("\n");

    List<Block> visibleRooms = currentRoom.getVisibleFrom();
    if (!visibleRooms.isEmpty()) {
        info.append("Visible Rooms:\n");
        for (Block room : visibleRooms) {
            if (restrictedRooms.contains(room)) {
                info.append("  Room ID: ").append(room.getRoomId())
                    .append(", Room Name: ").append(room.getRoomName())
                    .append(": Restricted details due to pet presence.\n");
            } else {
                info.append("  Room ID: ").append(room.getRoomId())
                    .append(", Room Name: ").append(room.getRoomName())
                    .append(", Items: ").append(((Room) room).listItems()).append("\n");
            }
        }
    } else {
        info.append("No visible rooms from your current location.\n");
    }
    return info.toString();
}

  

  @Override
  public void move(Block targetRoom) {
    if (targetRoom == null) {
      throw new IllegalArgumentException("Room cannot be null.");
    }
    if (!currentRoom.isAdjacent(targetRoom)) {
      throw new IllegalArgumentException("Cannot move to a non-neighboring room.");
    }
    this.currentRoom = targetRoom;
  }
  
  /**
   * Adjusts the item limit for the player and removes excess items if necessary.
   *
   * @param newItemLimit The new limit on the number of items the player can hold.
   */
  public void setItemLimit(int newItemLimit) {
    if (newItemLimit < 0) {
      throw new IllegalArgumentException("newItemLimit cannot be negative");
  }
    this.itemLimit = newItemLimit;
    while (items.size() > itemLimit) {
      items.remove(items.size() - 1); 
    }
  }

  /**
   * Retrieves the current limit on the number of items the player can hold.
   *
   * @return The maximum number of items the player can carry.
   */
  public int getItemLimit() {
    return this.itemLimit;
  }
  
  @Override
  public String getCharacterName() {
    return name;
  }
  
  @Override
  public boolean canSee(CharacterPlayer otherPlayer) {
    Block otherPlayerLocation = otherPlayer.getLocation();
    if (this.currentRoom.equals(otherPlayerLocation)) {
        return true;
    }
    List<Block> neighbors = this.currentRoom.getNeighbor();
    for (Block neighbor : neighbors) {
        if (neighbor.equals(otherPlayerLocation)) {
            return true;
        }
    }
    return false; 
}


  
  
  
}
