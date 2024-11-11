package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import world.WorldOutline;

/**
 * GameController manages the game's execution flow, processing user 
 * input and maintaining game state.
 */
public class GameController implements Controller {
  private Scanner scanner;
  private Appendable output;
  private int maxTurns;
  private int currentTurn = 1;
  private boolean gameStarted = false;
  private List<Integer> playerIds;
  private int currentPlayerIndex = 0;
  private Map<Integer, Boolean> isComputer;
  private RandomNumberGenerator rng;
  private Map<Integer, String> playerNames = new HashMap<>();
  private boolean isRunning = true;

  /**
   * Constructs a new GameController with the specified input, 
   * output, random number generator, and maximum turns.
   *
   * @param input The source of commands for the game.
   * @param outputInput The output destination for game messages.
   * @param rngInput The random number generator for game logic requiring randomness.
   * @param maxTurnsInput The maximum number of turns before the game forcibly ends.
   */
  public GameController(Readable input, Appendable outputInput, 
      RandomNumberGenerator rngInput, int maxTurnsInput) {
    this.scanner = new Scanner(input);
    this.output = outputInput;
    this.playerIds = new ArrayList<>();
    this.isComputer = new HashMap<>();
    this.rng = rngInput;
    this.maxTurns = maxTurnsInput;
  }
    
  @Override
  public void playGame(WorldOutline world) throws InterruptedException, IOException {
    setupGame(world);
    if (gameStarted && isRunning) {
      runGame(world);
    }
    if (!isRunning) {
      return; 
    }
  }

  private void setupGame(WorldOutline world) throws InterruptedException, IOException {
    print("Setting up the game.");
    try {
      world.setMaxTurn(maxTurns);
    } catch (NumberFormatException e) {
      print("Invalid input for maximum turns. Setting to default of 20 turns.");
      maxTurns = 20;
      world.setMaxTurn(maxTurns);
    }
  
    boolean addingPlayers = true;
    while (addingPlayers && isRunning) {
      print("Add players:");
      print("1. Add Human-Controlled Player");
      print("2. Add Computer-Controlled Player");
      print("3. Save the World Map, Check It to Know Where You Want to Start");
      print("4. Start Game");
      String input = scanner.nextLine(); 
      try {
        int choice = Integer.parseInt(input);
        Command command = null;
        switch (choice) {
          case 1: 
            command = new CreatePlayerCommand(world, scanner, playerIds, playerNames, isComputer);
            command.execute(output);
            break;
          case 2: 
            command = new CreateComputerPlayerCommand(world, scanner, playerIds, playerNames, isComputer);
            command.execute(output);
            break;
          case 3:  
            command = new SaveWorldMapCommand(world);
            command.execute(output);
            break;
          case 4:
            if (playerIds.isEmpty()) {
              print("No players added. Cannot start game.");
            } else {
              gameStarted = true;
              print("Game will start with " + playerIds.size() + " players.");
              runGame(world);
            }
            break;
          default:
            print("Invalid option, please try again.");
        }
      } catch (NumberFormatException e) {
        print("Invalid option, please enter a valid number.");
      }
    }
  }


  private void runGame(WorldOutline world) throws InterruptedException, IOException {
    print("Game started. Manage your turns.");

    while (isRunning && currentTurn < maxTurns) {
      int currentPlayerId = playerIds.get(currentPlayerIndex);
      if (isComputer.get(currentPlayerId)) {
        computerPlayerActions(world, currentPlayerId);
      } else {
        displayMenu(world);
        String input = scanner.nextLine();
        processPlayerInput(input, world, currentPlayerId);
      }
      if (currentTurn >= maxTurns) {
        setRunning(false);
        print("Game over: Maximum number of turns reached!");
      }
    }
  }
  
  private void processPlayerInput(String input, WorldOutline world, 
      int playerId) throws IOException, InterruptedException {
    boolean validInput = false;
    while (!validInput) {
    try {
      int choice = Integer.parseInt(input);
      Command command = null;
      switch (choice) {
        case 1:  
          command = new DisplayRoomInfoCommand(world, scanner);
          command.execute(output);
          break;
        case 2:  
          command = new SaveWorldMapCommand(world);
          command.execute(output);
          break;
        case 3:
          command = new MurderTargetCommand(world, playerId, scanner);  
          command.execute(output);
          if (world.getTargetHealthPoint() <= 0) {
            print("Target eliminated. " + playerNames.get(playerId) + " win!");
            setRunning(false);
            print("Game Over!");
          } else {
            advanceTurn(world);
          }
          break;
        case 4:  
          command = new MovePetCommand(world, playerId, scanner);
          command.execute(output);
          advanceTurn(world);
          break;
        case 5:  
          command = new MovePlayerCommand(world, playerId, scanner);
          command.execute(output);
          advanceTurn(world);
          break;
        case 6: 
          command = new PickUpItemCommand(world, playerId, scanner);
          command.execute(output);
          advanceTurn(world);
          break;
        case 7: 
        command = new LookAroundCommand(world, playerId);
        command.execute(output);
          advanceTurn(world);
          break;
        case 8:  
          command = new PlayerInfoCommand(world, playerIds, scanner);
          command.execute(output);
          break;
        case 9: 
          advanceTurn(world);
          break;
        case 0:
          print("Quitting game.");
          setRunning(false);
          break;
        case 10:
          command = new TargetInfoCommand(world);
          command.execute(output);
          break;
        default:
          print("Unknown command. Please try again.");
      }
      validInput = true;
    } catch (NumberFormatException e) {
      print("Invalid input, please enter a number.");
      displayMenu(world);
      input = scanner.nextLine();
    } catch (IllegalArgumentException e) {
      print("Error processing command: " + e.getMessage());
      displayMenu(world);
      input = scanner.nextLine();
    } catch (IOException e) {
      print("Error processing command: " + e.getMessage());
      displayMenu(world);
      input = scanner.nextLine();
    }
    }
  }
  
  private void computerPlayerActions(WorldOutline world, 
      int playerId) throws InterruptedException, IOException {
    try {
      if (world.canMurderAttempt(playerId)) {
        print("Opportunity for murder identified. Computer player preparing to attack.");
        world.usePlayerHighestItem(playerId);  
        String murderResult = world.murderAttempt(playerId);
        print("Murder attempt by computer player: " + murderResult);
        if (world.getTargetHealthPoint() <= 0) {
          print("Target eliminated. " + playerNames.get(playerId) + " win!");
          setRunning(false);
          print("Game Over!");
        } else {
          advanceTurn(world);
        }
      } else {
      int action = rng.nextInt(3);  
      switch (action) {
        case 0:
          int currentRoomId = world.getPlayerRoomId(playerId); 
          List<Integer> neighbors = world.getNeighborRooms(currentRoomId); 
          print("Current Room ID: " + currentRoomId);
          print("Neighbor Rooms: " + neighbors);
          if (neighbors.isEmpty()) {
            print("No available moves for player " + playerNames.get(playerId));
            return;
          }
          int roomIndex = rng.nextInt(neighbors.size());
          int targetRoomId = neighbors.get(roomIndex);
          print("Computer player try to move to " + targetRoomId);
          print("Computer player: " + (world.movePlayer(playerId, targetRoomId)));
          Thread.sleep(100);
          advanceTurn(world);
          break;
        case 1:
          print("Start Picking Item Up");
          int roomId = world.getPlayerRoomId(playerId);
          List<String> itemsInRoom = world.getRoomItems(roomId);
          if (!itemsInRoom.isEmpty()) {
            int itemIndex = rng.nextInt(itemsInRoom.size());
            String itemName = itemsInRoom.get(itemIndex);
            print("Computer player: " + world.playerPickUpItem(playerId, itemName));
          } else {
            print("No items available to pick up in this room for player " + playerId);
          }
          Thread.sleep(100);
          advanceTurn(world);
          break;
        case 2:
          print("Start Looking Around");
          print("Computer player: " + world.playerLookAround(playerId));
          Thread.sleep(100);
          advanceTurn(world);
          break;
        case 3:
          print("Computer player " + playerId + " has decided to quit the game.");
          setRunning(false);
          break;
        default:
          break;
      }
      }
    } catch (IllegalArgumentException e) {
      print(e.getMessage());
    }
  }

  private void advanceTurn(WorldOutline world) throws IOException {
    currentTurn++;
    if (currentTurn < maxTurns) {
      currentPlayerIndex = (currentPlayerIndex + 1) % playerIds.size();
      String currentPlayerName = playerNames.get(currentPlayerIndex);
      print("Turn " + currentTurn + ". Now Player " + currentPlayerName + " with "
          + "ID " + playerIds.get(currentPlayerIndex) + "'s turn.");
      world.moveTargetToNextRoom();  
      print(world.movePetToNextRoom());
    } else {
      print("Maximum turns reached. Ending game.");
    }
  }


  private void print(String message) throws IOException {
    try {
      output.append(message + "\n");
    } catch (IllegalArgumentException e) {
      System.out.println("Failed to write to output: " + e.getMessage());
    }
  }

  private void displayMenu(WorldOutline world) throws IOException {
    int currentPlayerId = playerIds.get(currentPlayerIndex);
    String currentPlayerName = playerNames.get(currentPlayerId);
    print("\n--- Game Menu ---");
    print("Turn " + currentTurn);
    print("Current player's turn: Player ID " + currentPlayerId + " Player "
        + "Name " + currentPlayerName);
    print(world.getPlayerLocation(currentPlayerId));
    print(world.getPlayerItemsInfo(currentPlayerId));
    print("1. Display Room Info");
    print("2. Save World Map");
    print("3. Attempt to Murder Target");
    print("4. Move Pet");
    print("5. Move Player");
    print("6. Player Pick Up Item");
    print("7. Player Look Around");
    print("8. Display Player Info");
    print("9. Do Nothing");
    print("10. Display Target Info");
    print("0. Quit Game");
    print("Select an option:");
  }
  
  /**
   * Checks if the game is currently running.
   * This method is typically used to verify the game's active 
   * status within game loops or conditional checks.
   *
   * @return {@code true} if the game is actively running, 
   *        {@code false} otherwise.
   */
  @Override
  public boolean getIsRunning() {
    return isRunning;
  }
  
  private void setRunning(boolean isRunningInput) {
    this.isRunning = isRunningInput;
  }
}
