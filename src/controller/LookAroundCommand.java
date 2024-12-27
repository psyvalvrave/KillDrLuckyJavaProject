package controller;

import java.io.IOException;
import world.ReadOnlyWorld;

/**
 * The LookAroundCommand class implements the Command interface to enable players
 * to observe their surroundings within the game environment. This command
 * provides the player with information about the room they are currently in
 * and any visible adjacent rooms.
 */
public class LookAroundCommand implements Command {
  private ReadOnlyWorld world;
  private int playerId;

  /**
   * Constructs a LookAroundCommand with a reference to the game world and the ID
   * of the player executing the command. This setup allows the command to access
   * and interact with the player's current surroundings based on their 
   * location within the game world.
   *
   * @param worldModel The game world context where the command is executed.
   * @param playerIdsInput The ID of the player who is looking around.
   */
  public LookAroundCommand(ReadOnlyWorld worldModel, int playerIdsInput) {
    this.world = worldModel;
    this.playerId = playerIdsInput;
  }

  @Override
  public String execute(Appendable output) throws IOException {
    try {
      String lookAroundInfo = world.playerLookAround(playerId);
      output.append(lookAroundInfo); 
      return lookAroundInfo;
    } catch (IllegalArgumentException e) {
      throw e;
    }
  }
}
