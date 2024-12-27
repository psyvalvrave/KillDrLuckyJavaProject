package controller;

import java.io.IOException;
import world.ReadOnlyWorld;

/**
 * The PlayerInfoCommand class implements the Command interface to enable
 * the display of detailed information about a specific player within the game.
 * This command interacts with the game world to retrieve player details 
 * based on the provided player ID.
 */
public class PlayerInfoCommand implements Command {
  private ReadOnlyWorld world;
  private int playerId;

  /**
   * Constructs a PlayerInfoCommand with access to the game world, a list of player IDs,
   * and a scanner for user input.
   *
   * @param worldModel The game world where player information is managed.
   * @param playerIdsInput A list of all player IDs currently active in the game.
   */
  public PlayerInfoCommand(ReadOnlyWorld worldModel, int playerIdsInput) {
    this.world = worldModel;
    this.playerId = playerIdsInput;
  }

  @Override
  public String execute(Appendable output) throws IOException {
    try {
      if (!world.getPlayerIds().contains(playerId)) {
        output.append("No player found with ID: " + playerId + "\n");
      } else {
        String playerInfo = world.getPlayerInfo(playerId);
        output.append(playerInfo + "\n");
        return playerInfo;
      }
    } catch (IllegalArgumentException e) {
      output.append("Error retrieving player information: " + e.getMessage() + "\n");
      throw e;
    }
    return null;
  }
}
