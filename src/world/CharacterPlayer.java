package world;

import java.util.List;

/**
 * The CharacterPlayer interface defines the necessary behaviors 
 * that must be implemented by any class that represents a player character.
 */
public interface CharacterPlayer {
  
    Block getLocation();

    void move(Block room);

    String getCharacterInfo();

    List<Gadget> getItem();

    void pickItem(Gadget item) throws IllegalArgumentException;

    String lookAround();

    void setItemLimit(int newItemLimit);

    int getItemLimit();
    
    String getCharacterName();
    
    int getPlayerId();
    
    void murder(CharacterTarget target, int damage);

    boolean canSee(CharacterPlayer otherPlayer);

    String lookAround(List<Block> restrictedRooms);

}
