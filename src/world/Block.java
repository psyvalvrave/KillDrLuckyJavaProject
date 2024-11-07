package world;

import java.util.List;

/**
 * Interface for a block in the game, typically representing a room or similar structure,
 * providing methods to manage and interact with these spaces.
 */
public interface Block {
  /**
   * Gets the name of the room.
   * 
   * @return the name of the room.
   */
  String getRoomName();

  /**
   * Gets a list of adjacent rooms.
   * 
   * @return a list of rooms that are considered neighbors to this room.
   */
  List<Block> getNeighbor();

  /**
   * Checks if a specified room is adjacent to this room.
   * 
   * @param room the room to check adjacency against.
   * @return true if the specified room is adjacent, false otherwise.
   */
  boolean isAdjacent(Block room);

  /**
   * Checks if this room is visible from another room.
   * 
   * @param room the room from which visibility is being checked.
   * @return true if this room is visible from the specified room, false otherwise.
   */
  boolean isVisibleFrom(Block room);


  /**
   * Retrieves a list of items located in the room.
   * 
   * @return a list of items currently present in the room.
   */
  List<Gadget> getItem();
  
  /**
   * Retrieves Name of Neighbors in String.
   * 
   * @return a string of current room's neighbors.
   */
  String getNeighborNames();
  
  /**
   * Retrieves ID of Room.
   * 
   * @return an int of current room's ID.
   */
  int getRoomId();
  
  /**
   * Retrieves Visible Room of Room.
   * 
   * @return a list of rooms that are considered visible to this room.
   */
  List<Block> getVisibleFrom();
  
  void addNeighbor(Room room);
  void addVisibleFromRoom(Room room);

  String getInfo();

  void addItem(Gadget item);
}
