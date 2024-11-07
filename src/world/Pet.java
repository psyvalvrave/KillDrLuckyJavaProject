package world;

import java.util.Stack;

public class Pet implements CharacterPet {
    private String name;
    private Block currentRoom;
    private Stack<Block> path;

    public Pet(String name, Block initialRoom) {
        if (initialRoom == null) {
            throw new IllegalArgumentException("Initial room cannot be null.");
        }
        this.name = name;
        this.currentRoom = initialRoom;
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
            for (int i = pathInput.size() - 1; i > 0; i--) {
                this.path.push(pathInput.get(i)); 
            }
        }
    }

    @Override
    public Stack<Block> getPath() {
      return path;
    }
}
