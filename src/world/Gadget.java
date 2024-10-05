package world;


/**
 * Represents an item in the game that can be used as a gadget or tool.
 */
public interface Gadget {
  /**
   * Gets the name of the item.
   * 
   * @return the item's name.
   */
  String getItemName();

  /**
   * Gets the murder value of the item, indicating its potential damage when used.
   * 
   * @return the item's murder value.
   */
  int getMurderValue();

  /**
   * Gets the location of the item within the room.
   * 
   * @return the Room representing the item's location.
   */
  Room getLocationItem();
}
