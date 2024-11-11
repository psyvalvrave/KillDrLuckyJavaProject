package controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import world.WorldOutline;

/**
 * The CreateComputerPlayerCommand class implements the Command interface to handle the creation of
 * computer-controlled players within the game. It prompts the user for the necessary data
 * to initialize a computer player and adds it to the game world.
 */
public class CreateComputerPlayerCommand implements Command {
  private WorldOutline world;
  private Scanner scanner;
  private List<Integer> playerIds;
  private Map<Integer, String> playerNames;
  private Map<Integer, Boolean> isComputer;

  /**
   * Constructs a new CreateComputerPlayerCommand with the specified parameters to manage
   * the addition of a new computer-controlled player.
   *
   * @param worldModel The game world where the player will be added.
   * @param scannerInput A Scanner to read input from the console.
   * @param playerIdsInput A list of player IDs to track all players.
   * @param playerNamesInput A map associating player IDs with their names.
   * @param isComputerInput A map indicating whether a player ID corresponds 
   *        to a computer-controlled player.
   */
  public CreateComputerPlayerCommand(WorldOutline worldModel, Scanner scannerInput, 
      List<Integer> playerIdsInput, Map<Integer, String> playerNamesInput, 
      Map<Integer, Boolean> isComputerInput) {
    this.world = worldModel;
    this.scanner = scannerInput;
    this.playerIds = playerIdsInput;
    this.playerNames = playerNamesInput;
    this.isComputer = isComputerInput;
  }

  @Override
  public void execute(Appendable output) throws IOException {
    output.append("Enter computer player name:\n");
    String playerName = scanner.nextLine(); 
    try {
      output.append("Enter computer player starting room id:\n");
      int roomIndex = Integer.parseInt(scanner.nextLine()); 
      if (roomIndex < 1 || roomIndex > world.getRoomCount()) {
        output.append("Invalid room index. Please enter a number between "
            + "1 and " + world.getRoomCount() + ".\n");
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
