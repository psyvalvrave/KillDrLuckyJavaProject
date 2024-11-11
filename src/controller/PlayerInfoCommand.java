package controller;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import world.WorldOutline;

/**
 * The PlayerInfoCommand class implements the Command interface to enable
 * the display of detailed information about a specific player within the game.
 * This command interacts with the game world to retrieve player details 
 * based on the provided player ID.
 */
public class PlayerInfoCommand implements Command {
  private WorldOutline world;
  private List<Integer> playerIds;
  private Scanner scanner;

  /**
   * Constructs a PlayerInfoCommand with access to the game world, a list of player IDs,
   * and a scanner for user input.
   *
   * @param worldModel The game world where player information is managed.
   * @param playerIdsInput A list of all player IDs currently active in the game.
   * @param scannerInput A Scanner object for reading user input.
   */
  public PlayerInfoCommand(WorldOutline worldModel, List<Integer> playerIdsInput, 
      Scanner scannerInput) {
    this.world = worldModel;
    this.playerIds = playerIdsInput;
    this.scanner = scannerInput;
  }

  @Override
  public void execute(Appendable output) throws IOException {
    try {
      output.append("Enter the player ID to view their information:\n");
      int targetPlayerId = Integer.parseInt(scanner.nextLine());
      if (!playerIds.contains(targetPlayerId)) {
        output.append("No player found with ID: " + targetPlayerId + "\n");
      } else {
        String playerInfo = world.getPlayerInfo(targetPlayerId);
        output.append(playerInfo + "\n");
      }
    } catch (NumberFormatException e) {
      throw new NumberFormatException(e.getMessage() + "\n");
    } 
  }
}
