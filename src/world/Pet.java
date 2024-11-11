package world;

import java.util.Stack;

/**
 * Represents a pet in the game, which can move within the game 
 * world and follow paths defined by
 * Depth-First Search (DFS) or other algorithms.
 */
public class Pet implements CharacterPet {
  private String name;
  private Block currentRoom;
  private Stack<Block> path;

  /**
   * Constructs a new Pet with a name and an initial room.
   * 
   * @param nameInput the name of the pet, used for identification within the game.
   * @param initialRoomInput the starting location of the pet within the game world.
   */
  public Pet(String nameInput, Block initialRoomInput) {
    this.name = nameInput;
    this.currentRoom = initialRoomInput;
    this.path = new Stack<>();
  }

  @Override
  public Block getLocation() {
    return currentRoom;
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

  @Override
  public String getCharacterInfo() {
    return String.format("Pet Name: %s\nCurrent Location: %s",
        name, currentRoom.getRoomName());
  }
  
  @Override
  public void setPath(Stack<Block> pathInput) {
    this.path = new Stack<>();
    if (pathInput != null) {
      for (int i = pathInput.size() - 1; i >= 0; i--) {
        this.path.push(pathInput.get(i)); 
      }
    }
  }

  @Override
  public Stack<Block> getPath() {
    return path;
  }
}
