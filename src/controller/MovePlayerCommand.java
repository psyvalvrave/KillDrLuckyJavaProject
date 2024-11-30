package controller;

import java.io.IOException;
import world.ReadOnlyWorld;

/**
 * The MovePlayerCommand class implements the Command interface to facilitate
 * player movement within the game world. This command is responsible for
 * allowing a player to move from their current location to a neighboring room
 * based on valid choices provided during the command execution.
 */
public class MovePlayerCommand implements Command {
  private ReadOnlyWorld world;
  private int playerId;
  private int targetRoomId;

  /**
   * Constructs a MovePlayerCommand with references to the game world, the ID of
   * the player who is moving, and the scanner object used to capture player input.
   *
   * @param worldModel The game world where the movement is to occur.
   * @param playerIdsInput The ID of the player who will be moved.
   * @param scannerInput The scanner to read user input.
   */
  public MovePlayerCommand(ReadOnlyWorld worldModel, int playerIdsInput, int targetRoomId)  {
    this.world = worldModel;
    this.playerId = playerIdsInput;
    this.targetRoomId = targetRoomId;
  }

  @Override
  public String execute(Appendable output) throws IOException {
      try {
          String moveResult = world.movePlayer(playerId, targetRoomId);
          output.append(moveResult + "\n");
      } catch (IllegalArgumentException e) {
          output.append("Error moving player: " + e.getMessage() + "\n");
          throw e;
      }
      return null;
  }
}

