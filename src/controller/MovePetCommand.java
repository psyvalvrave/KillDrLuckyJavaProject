package controller;

import java.io.IOException;
import world.ReadOnlyWorld;

/**
 * The MovePetCommand class implements the Command interface to enable players
 * to move their pet to a different room within the game world. This command
 * handles the interaction between the player and the pet, ensuring that
 * movement rules are followed and providing feedback to the player.
 */
public class MovePetCommand implements Command {
  private ReadOnlyWorld world;
  private int playerId;
  private int targetRoomId;

  /**
   * Constructs a MovePetCommand with a reference to the game world, the ID
   * of the player executing the command, and the input scanner. This setup
   * allows the command to interact with the game world and accept user input
   * for moving the pet.
   *
   * @param worldModel The game world context where the command is executed.
   * @param playerIdsInput The ID of the player attempting to move the pet.
   * @param targetRoomIdInput The room id for reading user input during command execution.
   */
  public MovePetCommand(ReadOnlyWorld worldModel, int playerIdsInput, int targetRoomIdInput) {
    this.world = worldModel;
    this.playerId = playerIdsInput;
    this.targetRoomId = targetRoomIdInput;
  }

  @Override
  public String execute(Appendable output) throws IOException {
    try {
      if (!world.canInteractWithPet(playerId)) {
        throw new IllegalArgumentException("You must be in the same room as the pet to move it.");
      }
      if (targetRoomId < 1 || targetRoomId > world.getRoomCount()) {
        throw new IllegalArgumentException("Invalid room ID. Please enter a "
            + "number between 1 and " + world.getRoomCount() + ".");
      }
      String moveResult = world.movePet(playerId, targetRoomId);
      output.append(moveResult + "\n");
      return moveResult;
    } catch (IllegalArgumentException e) {
      output.append("Error: " + e.getMessage() + "\n");
      throw e;
    }
  }
}
