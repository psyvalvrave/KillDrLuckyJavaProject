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
  
  /**
   * Adds a neighboring room to this room. Neighbors are typically adjacent rooms 
   * to which a player can directly move.
   * 
   * @param room The room to be added as a neighbor.
   */
  void addNeighbor(Room room);
  
  /**
   * Adds a room to the list of rooms that are visible from this room.
   * Visibility from a room does not necessarily imply physical adjacency, 
   * but means that actions or events in the visible room can be observed from the current room.
   * 
   * @param room The room to be added to the visible from list.
   */
  void addVisibleFromRoom(Room room);

  /**
   * Retrieves detailed information about the room, typically including ID, 
   * name, and other relevant details.
   * 
   * @return A string containing detailed information about the room.
   */
  String getInfo();

  /**
   * Adds an item to the room. This item can then be interacted with by players in the room.
   * 
   * @param item The gadget to be added to the room.
   */
  void addItem(Gadget item);
}
