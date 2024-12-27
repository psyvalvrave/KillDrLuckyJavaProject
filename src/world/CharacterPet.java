package world;

import java.util.Stack;

/**
 * The CharacterPet interface defines the necessary behaviors that must be implemented by any class
 * that represents a pet character in the game. Pets may have the ability to 
 * move within the game world and have specific interactions defined by their behaviors.
 */
public interface CharacterPet {
  
  /**
   * Retrieves the current location of the pet.
   * 
   * @return The current room or block where the pet is located.
   */
  Block getLocation();
  
  /**
   * Retrieves the name of the pet.
   * 
   * @return The name of the pet as a String.
   */
  String getCharacterName();
  
  /**
   * Moves the pet to a specified room.
   * 
   * @param room The room to which the pet will be moved.
   */
  void move(Block room);
  
  /**
   * Retrieves information about the pet, typically including its name and current location.
   * 
   * @return A formatted string containing information about the pet.
   */
  String getCharacterInfo();
  
  /**
   * Sets the path for the pet, represented as a stack of blocks. 
   * This path defines the sequence of moves the pet will follow, 
   * typically calculated through some pathfinding algorithm like DFS.
   * 
   * @param pathInput The stack of blocks representing the path for the pet.
   */
  void setPath(Stack<Block> pathInput);
  
  /**
   * Retrieves the current path of the pet as a stack of blocks. This path can be used to determine
   * the sequence of rooms the pet will traverse.
   * 
   * @return A stack of blocks representing the pet's path.
   */
  Stack<Block> getPath();
}
