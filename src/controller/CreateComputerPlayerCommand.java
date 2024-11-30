package controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import world.ReadOnlyWorld;

/**
 * The CreateComputerPlayerCommand class implements the Command interface to handle the creation of
 * computer-controlled players within the game. It prompts the user for the necessary data
 * to initialize a computer player and adds it to the game world.
 */
public class CreateComputerPlayerCommand implements Command {
  private ReadOnlyWorld world;
  private String playerName;
  private int roomIndex;
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
  public CreateComputerPlayerCommand(ReadOnlyWorld world, String playerName, int roomIndex,
      List<Integer> playerIds, Map<Integer, String> playerNames,
      Map<Integer, Boolean> isComputer) {
    this.world = world;
    this.playerName = playerName;
    this.roomIndex = roomIndex;
    this.playerIds = playerIds;
    this.playerNames = playerNames;
    this.isComputer = isComputer;
  }

  @Override
  public String execute(Appendable output) throws IOException {
      if (roomIndex < 1 || roomIndex > world.getRoomCount()) {
        throw new IllegalArgumentException("Invalid room index. Please enter a number between 1 and " + world.getRoomCount() + ".\n");
      }
      int playerId = world.callCreatePlayer(playerName, roomIndex);
      playerIds.add(playerId);
      playerNames.put(playerId, playerName);
      isComputer.put(playerId, true);
      output.append("Computer player added with ID: " + playerId + ".\n");
      return null;
  }
}
