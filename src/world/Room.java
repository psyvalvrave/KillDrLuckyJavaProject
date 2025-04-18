package world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a room in the game world, implementing the Block interface to manage
 * and interact with room-like spaces.
 */
public class Room implements Block {
  private String roomName;
  private int roomId; 
  private List<Block> neighbors;
  private List<Block> visibleFrom;
  private List<Gadget> items;
  private final int[] locationRoom;  
  private List<String[]> allRoomData;

  /**
   * Constructor for Room.
   * 
   * @param roomNameInput Name of the room.
   * @param roomIdInput  The ID of the room.
   * @param locationRoomInput Array of four integers representing the room coordinates.
   * @param allRoomDataInput reference to the overall room data 
   */
  public Room(String roomNameInput, int roomIdInput, int[] locationRoomInput,
      List<String[]> allRoomDataInput) {
    this.roomName = roomNameInput;
    this.roomId  = roomIdInput;
    this.locationRoom = locationRoomInput;
    this.visibleFrom = new ArrayList<>();
    this.items = new ArrayList<>(); 
    this.neighbors = new ArrayList<>();
    this.allRoomData = allRoomDataInput;
  }

  /**
   * Returns the name of the room.
   * 
   * @return the room name
   */
  @Override
  public String getRoomName() {
    return roomName;
  }

  @Override
  public List<Block> getNeighbor() {
    ArrayList<Block> rooms = new ArrayList<>(neighbors);
    return rooms;
    
  }
  
  @Override
  public List<Block> getVisibleFrom() {
    ArrayList<Block> rooms = new ArrayList<>(visibleFrom);
    return rooms;
  }
  
  @Override
  public String getNeighborNames() {
    if (neighbors.isEmpty()) {
      return "No neighbors";
    }
    StringBuilder neighborNames = new StringBuilder();
    for (Block neighbor : neighbors) {
      if (neighborNames.length() > 0) {
        neighborNames.append(", ");
      }
      neighborNames.append(neighbor.getRoomName());
    }
    return neighborNames.toString();
  }

  /**
   * Determines if the specified room is a neighbor based on adjacency of coordinates.
   * 
   * @param room the room to check against this room
   * @return true if the rooms are adjacent, false otherwise
   */
  private boolean areNeighbors(Room room) {
    if (this == room) {
      return false;  
    }
    
    return (this.locationRoom[0] - 1 <= room.locationRoom[2] 
        && this.locationRoom[2] + 1 >= room.locationRoom[0]) 
        && (this.locationRoom[1] - 1 <= room.locationRoom[3] 
            && this.locationRoom[3] + 1 >= room.locationRoom[1]); 
  }
  
  /**
   * Checks if this room is visible from another room based on direct line of sight.
   * 
   * @param otherRoom the room to check visibility from
   * @return true if this room is visible from the other room, false otherwise
   */
  private boolean canSeeFrom(Room otherRoom) {
    boolean horizontalAlignment = (this.locationRoom[1] <= otherRoom.locationRoom[3] 
        && this.locationRoom[3] >= otherRoom.locationRoom[1]) 
        || (otherRoom.locationRoom[1] <= this.locationRoom[3] 
        && otherRoom.locationRoom[3] >= this.locationRoom[1]);
    boolean verticalAlignment = (this.locationRoom[0] <= otherRoom.locationRoom[2] 
        && this.locationRoom[2] >= otherRoom.locationRoom[0]) 
        || (otherRoom.locationRoom[0] <= this.locationRoom[2] 
        && otherRoom.locationRoom[2] >= this.locationRoom[0]);

    Set<Integer> occupiedX = new HashSet<>();
    Set<Integer> occupiedY = new HashSet<>();

    if (horizontalAlignment) {
      int startY = Math.max(this.locationRoom[1], otherRoom.locationRoom[1]);
      int endY = Math.min(this.locationRoom[3], otherRoom.locationRoom[3]);
      for (String[] roomDataEntry : allRoomData) {
        int[] coords = Arrays.stream(roomDataEntry[1].replace("[", "").replace("]", "").split(", "))
            .mapToInt(Integer::parseInt).toArray();
        if (coords[1] <= endY && coords[3] >= startY) {
          for (int x = coords[0]; x <= coords[2]; x++) {
            occupiedX.add(x);
          }
        }
      }
      int startX = Math.min(this.locationRoom[0], otherRoom.locationRoom[0]);
      int endX = Math.max(this.locationRoom[2], otherRoom.locationRoom[2]);
      for (int x = startX; x <= endX; x++) {
        if (!occupiedX.contains(x)) {
          return false; 
        }
      }
    }

    if (verticalAlignment) {
      int startX = Math.max(this.locationRoom[0], otherRoom.locationRoom[0]);
      int endX = Math.min(this.locationRoom[2], otherRoom.locationRoom[2]);
      for (String[] roomDataEntry : allRoomData) {
        int[] coords = Arrays.stream(roomDataEntry[1].replace("[", "").replace("]", "")
            .split(", ")).mapToInt(Integer::parseInt).toArray();
        if (coords[0] <= endX && coords[2] >= startX) {
          for (int y = coords[1]; y <= coords[3]; y++) {
            occupiedY.add(y);
          }
        }
      }
      int startY = Math.min(this.locationRoom[1], otherRoom.locationRoom[1]);
      int endY = Math.max(this.locationRoom[3], otherRoom.locationRoom[3]);
      for (int y = startY; y <= endY; y++) {
        if (!occupiedY.contains(y)) {
          return false; 
        }
      }
    }

    return horizontalAlignment || verticalAlignment;
  }

  @Override
  public boolean isAdjacent(Block room) {
    return neighbors.contains(room);
  }

  @Override
  public boolean isVisibleFrom(Block room) {
    return visibleFrom.contains(room);
  }

  @Override
  public List<Gadget> getItem() {
    return new ArrayList<>(items);
  }

 
  @Override
  public void addNeighbor(Room room) {
    if (room != null && !neighbors.contains(room) && this.areNeighbors(room)) {
      neighbors.add(room);
      room.neighbors.add(this); 
    }
  }

 
  @Override
  public void addVisibleFromRoom(Room room) {
    if (room != null && !visibleFrom.contains(room) && this.canSeeFrom(room)) {
      visibleFrom.add(room);
    }
  }

  @Override
  public void addItem(Gadget item) {
    if (item != null && !items.contains(item)) {
      items.add(item);
    }
  }
  
  
  /**
   * Returns the coordinates of this room.
   * 
   * @return an array containing the coordinates of the room
   */
  public int[] getCoordinates() {
    return locationRoom;
  }
  
  @Override
  public String getInfo() {
    StringBuilder info = new StringBuilder();
    info.append("Room Name: ").append(roomName).append("\n");
    info.append("Room ID: ").append(roomId).append("\n");
    info.append("Coordinates: [")
        .append(locationRoom[0]).append(", ")
        .append(locationRoom[1]).append(", ")
        .append(locationRoom[2]).append(", ")
        .append(locationRoom[3]).append("]\n");

    info.append("Neighbors: ");
    if (neighbors.isEmpty()) {
      info.append("None");
    } else {
      for (Block neighbor : neighbors) {
        info.append(neighbor.getRoomName()).append(", ");
      }
      info.setLength(info.length() - 2);  
    }
    info.append("\n");

    info.append("Visible From: ");
    if (visibleFrom.isEmpty()) {
      info.append("None");
    } else {
      for (Block visible : visibleFrom) {
        info.append(visible.getRoomName()).append(", ");
      }
      info.setLength(info.length() - 2);  
    }
    info.append("\n");

    info.append("Items: ").append(listItems());

    return info.toString();
  }


  @Override
  public int getRoomId() {
    return roomId;
  }
  
  /**
   * Return selected item in current room. 
   * 
   * @param item the selected item.
   */
  public void removeItem(Item item) {
    items.remove(item);
  }
  
  /**
   * Gets the list item as string for this room.
   * 
   * @return the room item as string
   */
  public String listItems() {
    if (items.isEmpty()) {
      return "None";
    }
    StringBuilder itemsList = new StringBuilder();
    for (Gadget item : items) {
      itemsList.append(item.getItemName()).append(", ");
    }
    itemsList.setLength(itemsList.length() - 2); 
    return itemsList.toString();
  }
  
  @Override
  public String toString() {
    return String.format("Room[name=%s, id=%d, items=%s]",
            roomName, roomId, items.isEmpty() ? "None" : listItems());
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Room room = (Room) obj;
    return roomId == room.roomId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(roomId);
  }


} 