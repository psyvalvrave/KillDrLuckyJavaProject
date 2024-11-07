package world;

import java.util.Stack;

public interface CharacterPet {
    Block getLocation();
    String getCharacterName();
    void move(Block room);
    String getCharacterInfo();
    void setPath(Stack<Block> pathInput);
    Stack<Block> getPath();
}
