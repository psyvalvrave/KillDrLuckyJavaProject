package controller;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import world.WorldOutline;

public class MurderTargetCommand implements Command {
    private WorldOutline world;
    private int playerId;
    private Scanner scanner;

    public MurderTargetCommand(WorldOutline world, int playerId, Scanner scanner) {
        this.world = world;
        this.playerId = playerId;
        this.scanner = scanner;
    }

    @Override
    public void execute(Appendable output) throws IOException {
        try {
            output.append("Attempt to murder the target:\n");
            List<String> playerItems = world.getPlayerItems(playerId);
            if (!playerItems.isEmpty()) {
                output.append("Your items:\n");
                for (int i = 0; i < playerItems.size(); i++) {
                    output.append((i + 1) + ": " + playerItems.get(i) + "\n");
                }
                output.append("Would you like to use an item to increase your murder point?\n");
                output.append("1: Yes\n0: No\n");
                String input = scanner.nextLine().trim();
                int choice = Integer.parseInt(input);
                if (choice == 1) {
                    output.append("Select the item number you wish to use:\n");
                    input = scanner.nextLine().trim();
                    int itemIndex = Integer.parseInt(input) - 1;
                    if (itemIndex >= 0 && itemIndex < playerItems.size()) {
                        String itemName = playerItems.get(itemIndex);
                        world.usePlayerItem(playerId, itemName);
                        output.append("You have used " + itemName + " to increase your murder point.\n");
                    } else {
                        output.append("Invalid item index, please select a valid number.\n");
                        return;
                    }
                }
            } else {
                output.append("You have no items to use.\n");
            }
            String result = world.murderAttempt(playerId);
            output.append(result + "\n");
        } catch (NumberFormatException e) {
            output.append("Invalid input. Please enter a valid number.\n");
        } catch (IllegalArgumentException e) {
            output.append("Error: " + e.getMessage() + "\n");
        }
    }

}
