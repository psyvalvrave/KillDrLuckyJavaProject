package controller;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import world.WorldOutline;

/**
 * The MurderTargetCommand class implements the Command interface to facilitate
 * a player's attempt to murder a target in the game. This command handles
 * checking whether the player is in the same room as the target, displaying
 * and selecting usable items, and executing the murder attempt with optional item usage.
 */
public class MurderTargetCommand implements Command {
  private WorldOutline world;
  private int playerId;
  private Scanner scanner;

  /**
   * Constructs a MurderTargetCommand with the necessary context to perform
   * a murder attempt.
   *
   * @param worldModel The game world where the interaction occurs.
   * @param playerIdsInput The ID of the player attempting the murder.
   * @param scannerInput A Scanner object for reading user input.
   */
  public MurderTargetCommand(WorldOutline worldModel, int playerIdsInput, Scanner scannerInput) {
    this.world = worldModel;
    this.playerId = playerIdsInput;
    this.scanner = scannerInput;
  }

  @Override
  public void execute(Appendable output) throws IOException {
    try {
      output.append("Attempt to murder the target:\n");
      if (!world.canMurderAttempt(playerId)) {
        throw new IllegalArgumentException(
            "Player is not in the same room as the target.");
      }
      List<String> playerItems = world.getPlayerItems(playerId);
      if (!playerItems.isEmpty()) {
        output.append("Your items:\n");
        for (int i = 0; i < playerItems.size(); i++) {
          output.append((i + 1) + ": " + playerItems.get(i) + "\n");
        }
        output.append("Would you like to use an item to increase your murder point?\n");
        output.append("1: Yes\n0: No\n");
        String input = scanner.nextLine().trim();
        int choice = Integer.parseInt(input);
        if (choice == 1) {
          output.append("Select the item number you wish to use:\n");
          input = scanner.nextLine().trim();
          int itemIndex = Integer.parseInt(input) - 1;
          if (itemIndex >= 0 && itemIndex < playerItems.size()) {
            String itemName = playerItems.get(itemIndex).split(": ")[0];;
            world.usePlayerItem(playerId, itemName);
            output.append("You have used " + itemName + " to increase your murder point.\n");
          } else {
            output.append("Invalid item index, please select a valid number.\n");
            throw new IllegalArgumentException(
                "Please enter valid number in range");
          }
        }
      } else {
        output.append("You have no items to use.\n");
      }
      String result = world.murderAttempt(playerId);
      output.append(result + "\n");
    } catch (NumberFormatException e) {
      throw new NumberFormatException(e.getMessage() + "\n");
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage() + "\n");
    }
  }

}
