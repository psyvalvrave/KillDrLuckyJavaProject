package controller;

import java.io.IOException;
import world.ReadOnlyWorld;

/**
 * The PickUpItemCommand class implements the Command interface to allow a player
 * to pick up items from their current room in the game world. It handles item listing,
 * selection, and pickup processes, interfacing with the game world to update the 
 * player's inventory.
 */
public class PickUpItemCommand implements Command {
  private ReadOnlyWorld world;
  private int playerId;
  private String itemName;

  /**
   * Constructs a PickUpItemCommand with necessary details about the game world, player,
   * and a scanner for input.
   *
   * @param worldModel The game world where the interaction takes place.
   * @param playerIdsInput The ID of the player picking up an item.
   * @param scannerInput A Scanner object for reading user input.
   */
  public PickUpItemCommand(ReadOnlyWorld worldModel, int playerIdsInput, String itemName) {
    this.world = worldModel;
    this.playerId = playerIdsInput;
    this.itemName = itemName;
  }

  @Override
  public void execute(Appendable output) throws IOException {
      try {
          String pickUpResult = world.playerPickUpItem(playerId, itemName);
          output.append(pickUpResult + "\n");
      } catch (IllegalArgumentException e) {
          output.append("Error picking up item: " + e.getMessage() + "\n");
      }
  }
}
