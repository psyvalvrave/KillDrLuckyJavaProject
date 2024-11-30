package controller;

import java.io.IOException;
import world.ReadOnlyWorld;

/**
 * The MurderTargetCommand class implements the Command interface to facilitate
 * a player's attempt to murder a target in the game. This command handles
 * checking whether the player is in the same room as the target, displaying
 * and selecting usable items, and executing the murder attempt with optional item usage.
 */
public class MurderTargetCommand implements Command {
  private ReadOnlyWorld world;
  private int playerId;
  private String itemName;

  /**
   * Constructs a MurderTargetCommand with the necessary context to perform
   * a murder attempt.
   *
   * @param worldModel The game world where the interaction occurs.
   * @param playerIdsInput The ID of the player attempting the murder.
   * @param scannerInput A Scanner object for reading user input.
   */
  public MurderTargetCommand(ReadOnlyWorld worldModel, int playerIdsInput, String itemName) {
    this.world = worldModel;
    this.playerId = playerIdsInput;
    this.itemName = itemName;
  }

  @Override
  public String execute(Appendable output) throws IOException {
      try {
          output.append("Attempt to murder the target:\n");
          if (!world.canMurderAttempt(playerId)) {
              throw new IllegalArgumentException("Player is not in the same room as the target.");
          }

          if (itemName != null && !itemName.isEmpty()) {
              world.usePlayerItem(playerId, itemName);
              output.append("You have used " + itemName + " to increase your murder point.\n");
          } else {
              output.append("No item used for this murder attempt.\n");
          }
          
          String result = world.murderAttempt(playerId);
          output.append(result + "\n");
          return result;
      } catch (IllegalArgumentException e) {
          output.append("Error: " + e.getMessage() + "\n");
          throw e;
      }
  }

}
