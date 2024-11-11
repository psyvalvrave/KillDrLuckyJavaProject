package controller;

import java.io.IOException;
import java.util.Scanner;
import world.WorldOutline;

/**
 * The MovePetCommand class implements the Command interface to enable players
 * to move their pet to a different room within the game world. This command
 * handles the interaction between the player and the pet, ensuring that
 * movement rules are followed and providing feedback to the player.
 */
public class MovePetCommand implements Command {
  private WorldOutline world;
  private int playerId;
  private Scanner scanner;

  /**
   * Constructs a MovePetCommand with a reference to the game world, the ID
   * of the player executing the command, and the input scanner. This setup
   * allows the command to interact with the game world and accept user input
   * for moving the pet.
   *
   * @param worldModel The game world context where the command is executed.
   * @param playerIdsInput The ID of the player attempting to move the pet.
   * @param scannerInput The scanner for reading user input during command execution.
   */
  public MovePetCommand(WorldOutline worldModel, int playerIdsInput, Scanner scannerInput) {
    this.world = worldModel;
    this.playerId = playerIdsInput;
    this.scanner = scannerInput;
  }

  @Override
  public void execute(Appendable output) throws IOException {
    try {
      if (!world.canInteractWithPet(playerId)) {
        throw new IllegalArgumentException("You must be in the same room as the pet to move it.");
      }
      int maxRooms = world.getRoomCount();
      output.append("Enter a room ID for the pet to move to (1-" + maxRooms + "):\n");
      int targetRoomId = Integer.parseInt(scanner.nextLine());
      if (targetRoomId < 1 || targetRoomId > maxRooms) {
        throw new IllegalArgumentException("Invalid room ID. "
            + "Please enter a number between 1 and " + maxRooms + ".");
      } else {
        String moveResult = world.movePet(playerId, targetRoomId);
        output.append(moveResult + "\n");
      }
    } catch (NumberFormatException e) {
      throw new NumberFormatException(e.getMessage() + "\n");
    }
  }
}
