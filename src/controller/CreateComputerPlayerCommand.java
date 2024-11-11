package controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import world.WorldOutline;

public class CreateComputerPlayerCommand implements Command {
    private WorldOutline world;
    private Scanner scanner;
    private List<Integer> playerIds;
    private Map<Integer, String> playerNames;
    private Map<Integer, Boolean> isComputer;

    public CreateComputerPlayerCommand(WorldOutline world, Scanner scanner, List<Integer> playerIds, Map<Integer, String> playerNames, Map<Integer, Boolean> isComputer) {
        this.world = world;
        this.scanner = scanner;
        this.playerIds = playerIds;
        this.playerNames = playerNames;
        this.isComputer = isComputer;
    }

    @Override
    public void execute(Appendable output) throws IOException {
        output.append("Enter computer player name:\n");
        String playerName = scanner.nextLine(); 
        try {
            output.append("Enter computer player starting room id:\n");
            int roomIndex = Integer.parseInt(scanner.nextLine()); 
            if (roomIndex < 1 || roomIndex > world.getRoomCount()) {
                output.append("Invalid room index. Please enter a number between 1 and " + world.getRoomCount() + ".\n");
                return;
            }
            int playerId = world.callCreatePlayer(playerName, roomIndex);
            playerIds.add(playerId); 
            playerNames.put(playerId, playerName); 
            isComputer.put(playerId, true); 
            output.append("Computer player added with ID: " + playerId + ".\n");
        } catch (NumberFormatException e) {
          throw new NumberFormatException(e.getMessage() + "\n");
        }
    }
}
