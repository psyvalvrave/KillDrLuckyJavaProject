package controller;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import world.WorldOutline;

public class MovePlayerCommand implements Command {
  private WorldOutline world;
  private int playerId;
  private Scanner scanner;

  public MovePlayerCommand(WorldOutline world, int playerId, Scanner scanner)  {
      this.world = world;
      this.playerId = playerId;
      this.scanner = scanner;
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

