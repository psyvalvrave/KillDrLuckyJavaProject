package controller;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import world.WorldOutline;

/**
 * The PickUpItemCommand class implements the Command interface to allow a player
 * to pick up items from their current room in the game world. It handles item listing,
 * selection, and pickup processes, interfacing with the game world to update the 
 * player's inventory.
 */
public class PickUpItemCommand implements Command {
  private WorldOutline world;
  private int playerId;
  private Scanner scanner;

  /**
   * Constructs a PickUpItemCommand with necessary details about the game world, player,
   * and a scanner for input.
   *
   * @param worldModel The game world where the interaction takes place.
   * @param playerIdsInput The ID of the player picking up an item.
   * @param scannerInput A Scanner object for reading user input.
   */
  public PickUpItemCommand(WorldOutline worldModel, int playerIdsInput, Scanner scannerInput) {
    this.world = worldModel;
    this.playerId = playerIdsInput;
    this.scanner = scannerInput;
  }

  @Override
  public void execute(Appendable output) throws IOException {
    try {
      int roomId = world.getPlayerRoomId(playerId);
      List<String> itemsInRoom = world.getRoomItems(roomId);

      if (itemsInRoom.isEmpty()) {
        output.append("No items available to pick up in this room.\n");
        return;
      }

      output.append("Items available to pick up:\n");
      for (int i = 0; i < itemsInRoom.size(); i++) {
        output.append((i + 1) + ": " + itemsInRoom.get(i) + "\n");
      }

      output.append("Enter the index of the item to pick up:\n");
      int itemIndex = Integer.parseInt(scanner.nextLine()) - 1;

      if (itemIndex < 0 || itemIndex >= itemsInRoom.size()) {
        throw new IllegalArgumentException(
            "Please enter valid number in range");
      }

      String itemName = itemsInRoom.get(itemIndex).split(": ")[0];
      String pickUpResult = world.playerPickUpItem(playerId, itemName);
      output.append(pickUpResult + "\n");
    } catch (NumberFormatException e) {
      throw new NumberFormatException(e.getMessage() + "\n");
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage() + "\n");
    }
  }
}
