package controller;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import world.WorldOutline;

/**
 * The MovePlayerCommand class implements the Command interface to facilitate
 * player movement within the game world. This command is responsible for
 * allowing a player to move from their current location to a neighboring room
 * based on valid choices provided during the command execution.
 */
public class MovePlayerCommand implements Command {
  private WorldOutline world;
  private int playerId;
  private Scanner scanner;

  /**
   * Constructs a MovePlayerCommand with references to the game world, the ID of
   * the player who is moving, and the scanner object used to capture player input.
   *
   * @param worldModel The game world where the movement is to occur.
   * @param playerIdsInput The ID of the player who will be moved.
   * @param scannerInput The scanner to read user input.
   */
  public MovePlayerCommand(WorldOutline worldModel, int playerIdsInput, Scanner scannerInput)  {
    this.world = worldModel;
    this.playerId = playerIdsInput;
    this.scanner = scannerInput;
  }

  @Override
  public void execute(Appendable output) throws IOException {
    try {
      List<String> neighbors = world.getPlayerNeighborRoom(playerId);
      if (neighbors.isEmpty()) {
        throw new IllegalArgumentException("There are no available rooms to move to.\n");
      } else {
        output.append("You can move to the following rooms:\n");
        for (String neighbor : neighbors) {
          output.append(neighbor + "\n");
        }
      }

      output.append("Enter target room ID:\n");
      int targetRoomId = Integer.parseInt(scanner.nextLine());
      String moveResult = world.movePlayer(playerId, targetRoomId);
      output.append(moveResult + "\n");
    } catch (NumberFormatException e) {
      throw new NumberFormatException(e.getMessage() + "\n");
    }
  }
}

