package controller;

import java.io.IOException;
import java.util.Scanner;
import java.util.List;
import world.WorldOutline;

public class PlayerInfoCommand implements Command {
    private WorldOutline world;
    private List<Integer> playerIds;
    private Scanner scanner;

    public PlayerInfoCommand(WorldOutline world, List<Integer> playerIds, Scanner scanner) {
        this.world = world;
        this.playerIds = playerIds;
        this.scanner = scanner;
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
