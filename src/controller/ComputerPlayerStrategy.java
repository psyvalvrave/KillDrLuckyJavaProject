package controller;

import java.io.IOException;
import java.util.List;
import world.WorldOutline;

/**
 * Implements a strategy for a computer-controlled player in a game world. This defines how the
 * computer player makes decisions based on the game's state. Including mechanisms for moving
 * around the world, picking up items, and making murder attempts when conditions are favorable.
 * 
 * The strategy uses output to communicate the outcomes of the computer player's decisions to
 * the game interface or logs. It incorporates random decision-making for movement and 
 * actions to simulate player behavior.
 * 
 */
public class ComputerPlayerStrategy implements PlayerStrategy {
  private WorldOutline world;
  private Appendable output;
  private RandomNumberGenerator rng;

  /**
   * Constructs a new computer player strategy with the specified world, 
   * output medium, and random number generator. This setup allows the computer 
   * player to interact with the game world and report actions taken.
   * 
   * @param worldInput the game world where the computer player operates.
   * @param outputComputer the appendable to which messages about player actions and results.
   * @param rngInput the generator used for making random decisions within the player strategy.
   */
  public ComputerPlayerStrategy(WorldOutline worldInput, Appendable outputComputer, 
      RandomNumberGenerator rngInput) {
    this.world = worldInput;
    this.output = outputComputer;
    this.rng = rngInput;
  }
  
  @Override
  public void executeActions(int playerId) throws InterruptedException, IOException {
    try {
      if (world.canMurderAttempt(playerId)) {
        output.append("Opportunity for murder identified. Computer player preparing to attack.\n");
        world.usePlayerHighestItem(playerId);
        String murderResult = world.murderAttempt(playerId);
        output.append("Murder attempt by computer player: " + murderResult + "\n");
        if (world.getTargetHealthPoint() <= 0) {
          output.append("Target eliminated. " + world.getPlayerNames().get(playerId) + " wins!\n");
          world.setRunning(false);
          output.append("Game Over!\n");
        } else {
          output.append(world.advanceTurn());
        }
      } else {
        int action = rng.nextInt(3); 
        switch (action) {
          case 0:
            moveComputerPlayer(playerId);
            break;
          case 1:
            pickUpItem(playerId);
            break;
          case 2:
            lookAround(playerId);
            break;
          case 3:
            output.append("Computer player " + playerId + " has decided to quit the game.\n");
            world.setRunning(false);
            break;
          default:
            break;
        }
      }
    } catch (IllegalArgumentException e) {
      output.append(e.getMessage() + "\n");
    }
  }

  private void moveComputerPlayer(int playerId) throws IOException, InterruptedException {
    int currentRoomId = world.getPlayerRoomId(playerId);
    List<Integer> neighbors = world.getNeighborRooms(currentRoomId);
    output.append("Current Room ID: " + currentRoomId + "\n");
    output.append("Neighbor Rooms: " + neighbors + "\n");
    if (neighbors.isEmpty()) {
      output.append("No available moves for player " + world.getPlayerNames().get(playerId) + "\n");
      return;
    }
    int roomIndex = rng.nextInt(neighbors.size());
    int targetRoomId = neighbors.get(roomIndex);
    output.append("Computer player tries to move to " + targetRoomId + "\n");
    output.append("Computer player: " + world.movePlayer(playerId, targetRoomId) + "\n");
    output.append(world.advanceTurn());
  }

  private void pickUpItem(int playerId) throws IOException, InterruptedException {
    int roomId = world.getPlayerRoomId(playerId);
    List<String> itemsInRoom = world.getRoomItems(roomId);
    output.append("Start Picking Item Up\n");
    if (!itemsInRoom.isEmpty()) {
      int itemIndex = rng.nextInt(itemsInRoom.size());
      String itemName = itemsInRoom.get(itemIndex).split(": ")[0];
      output.append("Computer player: " + world.playerPickUpItem(playerId, itemName) + "\n");
    } else {
      output.append("No items available to pick up in this room for player " + playerId + "\n");
    }
    output.append(world.advanceTurn());
  }

  private void lookAround(int playerId) throws IOException, InterruptedException {
    output.append("Start Looking Around\n");
    output.append("Computer player: " + world.playerLookAround(playerId) + "\n");
    output.append(world.advanceTurn());
  }
}
