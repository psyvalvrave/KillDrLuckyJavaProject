package world;

/**
 * Represents an item in the game world, implementing the Gadget interface to provide
 * functionalities related to game items.
 */
public class Item implements Gadget {
  private String name;
  private Room location; 
  private int murderValue;

  /**
   * Constructor for the Item class.
   * 
   * @param nameItem name of the item.
   * @param locationItem Room where the item is located.
   * @param murderValueItem Value representing the item's potential damage or effect.
   */
  public Item(String nameItem, Room locationItem, int murderValueItem) {
    if (murderValueItem < 0) {
      throw new IllegalArgumentException("Murder value cannot be negative.");
    }
    this.name = nameItem;
    this.location = locationItem;
    this.murderValue = murderValueItem;
  }

  /**
   * Returns the name of the item.
   * 
   * @return The name of the item.
   */
  @Override
  public String getItemName() {
    return name;
  }

  /**
   * Returns the room where the item is located.
   * 
   * @return The room where the item is located.
   */
  @Override
  public Room getLocationItem() {
    return location;
  }

  /**
   * Returns the murder value of the item.
   * 
   * @return The murder value of the item.
   */
  @Override
  public int getMurderValue() {
    return murderValue;
  }
}
