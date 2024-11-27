package controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import world.ReadOnlyWorld;

/**
 * The CreatePlayerCommand class implements the Command interface to handle the creation of
 * human-controlled players within the game. It prompts the user for the necessary information
 * to add a new human player to the game world.
 */
public class CreatePlayerCommand implements Command {
  private ReadOnlyWorld world;
  private String playerName;
  private int roomIndex;
  private List<Integer> playerIds;
  private Map<Integer, String> playerNames;
  private Map<Integer, Boolean> isComputer;

  /**
   * Constructs a new CreatePlayerCommand with specified parameters to manage
   * the addition of a new human player.
   *
   * @param worldModel The game world where the player will be added.
   * @param scannerInput A Scanner to read input from the console.
   * @param playerIdsInput A list of player IDs to keep track of all players.
   * @param playerNamesInput A map associating player IDs with their names.
   * @param isComputerInput A map indicating whether a player ID corresponds 
   *        to a computer-controlled player.
   */
  public CreatePlayerCommand(ReadOnlyWorld worldModel, String playerName, int roomIndex, 
      List<Integer> playerIdsInput, Map<Integer, String> playerNamesInput, 
      Map<Integer, Boolean> isComputerInput) {
    this.world = worldModel;
    this.playerName = playerName;
    this.roomIndex = roomIndex;
    this.playerIds = playerIdsInput;
    this.playerNames = playerNamesInput;
    this.isComputer = isComputerInput;
  }

  @Override
  public void execute(Appendable output) throws IOException {
      if (roomIndex < 1 || roomIndex > world.getRoomCount()) {
          output.append("Invalid room index. Please enter a number between 1 and " + world.getRoomCount() + ".\n");
          return;
      }
      int playerId = world.callCreatePlayer(playerName, roomIndex);
      playerIds.add(playerId);
      playerNames.put(playerId, playerName);
      isComputer.put(playerId, false);
      output.append("Human player added with ID: " + playerId + ".\n");
  }
}
