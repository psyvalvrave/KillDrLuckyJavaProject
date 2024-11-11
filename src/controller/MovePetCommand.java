package controller;

import java.io.IOException;
import java.util.Scanner;

import world.WorldOutline;

public class MovePetCommand implements Command {
    private WorldOutline world;
    private int playerId;
    private Scanner scanner;

    public MovePetCommand(WorldOutline world, int playerId, Scanner scanner) {
        this.world = world;
        this.playerId = playerId;
        this.scanner = scanner;
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
              throw new IllegalArgumentException("Invalid room ID. Please enter a number between 1 and " + maxRooms + ".");
            } else {
                String moveResult = world.movePet(playerId, targetRoomId);
                output.append(moveResult + "\n");
            }
        } catch (NumberFormatException e) {
          throw new NumberFormatException(e.getMessage() + "\n");
        }
    }
}
