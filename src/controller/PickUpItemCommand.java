package controller;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import world.WorldOutline;

public class PickUpItemCommand implements Command {
    private WorldOutline world;
    private int playerId;
    private Scanner scanner;

    public PickUpItemCommand(WorldOutline world, int playerId, Scanner scanner) {
        this.world = world;
        this.playerId = playerId;
        this.scanner = scanner;
    }

    @Override
    public void execute(Appendable output) throws IOException {
        try {
            int roomId = world.getPlayerRoomId(playerId);
            List<String> itemsInRoom = world.getRoomItems(roomId);

            if (itemsInRoom.isEmpty()) {
                output.append("No items available to pick up in this room.\n");
                return;
            }

            output.append("Items available to pick up:\n");
            for (int i = 0; i < itemsInRoom.size(); i++) {
                output.append((i + 1) + ": " + itemsInRoom.get(i) + "\n");
            }

            output.append("Enter the index of the item to pick up:\n");
            int itemIndex = Integer.parseInt(scanner.nextLine()) - 1;

            if (itemIndex < 0 || itemIndex >= itemsInRoom.size()) {
                output.append("Invalid item index, please select a valid number.\n");
                return;
            }

            String itemName = itemsInRoom.get(itemIndex).split(": ")[0];
            String pickUpResult = world.playerPickUpItem(playerId, itemName);
            output.append(pickUpResult + "\n");
        } catch (NumberFormatException e) {
            output.append("Invalid input, please enter a number.\n");
        } catch (IllegalArgumentException e) {
            output.append("Error: " + e.getMessage() + "\n");
        }
    }
}
