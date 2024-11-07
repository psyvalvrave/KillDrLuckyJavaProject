package world;

import java.util.List;

/**
 * The CharacterPlayer interface defines the necessary behaviors 
 * that must be implemented by any class that represents a player character.
 */
public interface CharacterPlayer {
  
    // Method to retrieve the player's location as a Block
    Block getLocation();

    // Method to move the player to a new room
    void move(Block room);

    // Method to get detailed character information
    String getCharacterInfo();

    // Method to retrieve a list of gadgets currently in the player's possession
    List<Gadget> getItem();

    // Method for picking up an item
    void pickItem(Gadget item) throws IllegalArgumentException;

    // Method to provide a detailed description of the player's surroundings
    String lookAround();

    // Method to adjust the item limit for the player
    void setItemLimit(int newItemLimit);

    // Method to retrieve the current item limit
    int getItemLimit();
    
    String getCharacterName();
    
    int getPlayerId();
    
    void murder(CharacterTarget target, int damage);

    // Additional methods can be added based on other necessary player interactions
}
