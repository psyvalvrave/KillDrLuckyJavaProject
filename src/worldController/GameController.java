package worldController;

import world.WorldOutline;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GameController implements Controller{
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


    public GameController(Readable input, Appendable output, RandomNumberGenerator rng, int maxTurns) {
        this.scanner = new Scanner(input);
        this.output = output;
        this.playerIds = new ArrayList<>();
        this.isComputer = new HashMap<>();
        this.rng = rng;
        this.maxTurns = maxTurns;
    }
    
  @Override
  public void playGame(WorldOutline world) throws InterruptedException {
    setupGame(world);
    if (gameStarted && isRunning) {
      runGame(world);
    }
    if (!isRunning) {
      return; 
      }
    }

    private void setupGame(WorldOutline world) throws InterruptedException {
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
              switch (choice) {
                case 1: 
                    print("Enter player name:");
                    String humanPlayerName = scanner.nextLine();
                    try {
                      print("Enter player starting room id:");
                        int humanRoomIndex = Integer.parseInt(scanner.nextLine());
                        if (humanRoomIndex < 1 || humanRoomIndex > world.getRoomCount()) {
                          print("Invalid room index. Please enter a number between 1 and " + world.getRoomCount());
                          break;
                      }
                        int humanPlayerId = world.callCreatePlayer(humanPlayerName, humanRoomIndex);
                        playerIds.add(humanPlayerId);
                        playerNames.put(humanPlayerId, humanPlayerName);
                        isComputer.put(humanPlayerId, false); 
                        print("Human player added with ID: " + humanPlayerId);
                    } catch (NumberFormatException e) {
                        print("Invalid room index, please enter a valid number.");
                    }
                    break;
                case 2: 
                  print("Enter computer player name:");
                    String computerPlayerName = scanner.nextLine();
                    try {
                      print("Enter computer player starting room id:");
                        int computerRoomIndex = Integer.parseInt(scanner.nextLine());
                        if (computerRoomIndex < 1 || computerRoomIndex > world.getRoomCount()) {
                          print("Invalid room index. Please enter a number between 1 and " + world.getRoomCount());
                          break;
                      }
                        int computerPlayerId = world.callCreatePlayer(computerPlayerName, computerRoomIndex);
                        playerIds.add(computerPlayerId);
                        playerNames.put(computerPlayerId, computerPlayerName);
                        isComputer.put(computerPlayerId, true); 
                        print("Computer player added with ID: " + computerPlayerId);
                    } catch (NumberFormatException e) {
                        print("Invalid room index, please enter a valid number.");
                    }
                    break;
                case 3:  
                  saveWorldMap(world);
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


    private void runGame(WorldOutline world) throws InterruptedException {
      print("Game started. Manage your turns.");

      while (isRunning && currentTurn < maxTurns) {
          int currentPlayerId = playerIds.get(currentPlayerIndex);
          if (isComputer.get(currentPlayerId)) {
              computerPlayerActions(world, currentPlayerId);
          } else {
              displayMenu();
              String input = scanner.nextLine();
              processPlayerInput(input, world, currentPlayerId);
          }
          if (currentTurn >= maxTurns) {
              print("Game over: Maximum number of turns reached.");
              setRunning(false);
          }
      }
  }
    
    private void processPlayerInput(String input, WorldOutline world, int playerId) {
      try {
          int choice = Integer.parseInt(input);
          switch (choice) {
              case 1:  
                  print("Enter room ID:");
                  int roomId = Integer.parseInt(scanner.nextLine());
                  print(world.displayRoomInfo(roomId));
                  break;
              case 2:  
                  saveWorldMap(world);
                  break;
              case 5:  
                  print("Enter target room ID:");
                  int targetRoomId = Integer.parseInt(scanner.nextLine());
                  print(world.movePlayer(playerId, targetRoomId));
                  advanceTurn(world);
                  break;
              case 6: 
                  pickUpItem(world, playerId);
                  advanceTurn(world);
                  break;
              case 7:  
                  print(world.playerLookAround(playerId));
                  advanceTurn(world);
                  break;
              case 8:  
                  print("Enter the player ID to view their information:");
                  try {
                      int targetPlayerId = Integer.parseInt(scanner.nextLine());
                      if (!playerIds.contains(targetPlayerId)) {
                          print("No player found with ID: " + targetPlayerId);
                      } else {
                          print(world.getPlayerInfo(targetPlayerId));
                      }
                  } catch (NumberFormatException e) {
                      print("Invalid input, please enter a valid player ID number.");
                  } catch (Exception e) {
                      print("Error retrieving player info: " + e.getMessage());
                  }
                  break;
              case 9: 
                  advanceTurn(world);
                  break;
              case 0:  
                  print("Quitting game.");
                  setRunning(false);  
                  break;
              default:
                  print("Unknown command. Please try again.");
          }
      } catch (NumberFormatException e) {
          print("Invalid input, please enter a number.");
      } catch (Exception e) {
          print("Error processing command: " + e.getMessage());
      }
  }
    private void computerPlayerActions(WorldOutline world, int playerId) throws InterruptedException {
      try {
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
                  Thread.sleep(300);
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
                  Thread.sleep(300);
                  advanceTurn(world);
                  break;
              case 2:
                print("Start Looking Around");
                  print("Computer player: " + world.playerLookAround(playerId));
                  Thread.sleep(300);
                  advanceTurn(world);
                  break;
              case 3:
                print("Computer player " + playerId + " has decided to quit the game.");
                setRunning(false);
                break;
          }
      } catch (IllegalArgumentException e) {
          print(e.getMessage());
      }
  }


    
    private void pickUpItem(WorldOutline world, int playerId) {
      try {
          int roomId = world.getPlayerRoomId(playerId);
          List<String> itemsInRoom = world.getRoomItems(roomId);

          if (itemsInRoom.isEmpty()) {
              print("No items available to pick up in this room.");
              return;
          }

          print("Items available to pick up:");
          for (int i = 0; i < itemsInRoom.size(); i++) {
              print((i + 1) + ": " + itemsInRoom.get(i));
          }

          print("Enter the index of the item to pick up:");
          int itemIndex = Integer.parseInt(scanner.nextLine()) - 1;

          if (itemIndex < 0 || itemIndex >= itemsInRoom.size()) {
              print("Invalid item index, please select a valid number.");
              return;
          }

          String itemName = itemsInRoom.get(itemIndex);
          print(world.playerPickUpItem(playerId, itemName));
      } catch (NumberFormatException e) {
          print("Invalid input, please enter a number.");
      } catch (Exception e) {
          print("Error: " + e.getMessage());
      }
  }

  private void advanceTurn(WorldOutline world) {
      currentTurn++;
      if (currentTurn < maxTurns) {
          currentPlayerIndex = (currentPlayerIndex + 1) % playerIds.size();
          String currentPlayerName = playerNames.get(currentPlayerIndex);
          print("Turn " + currentTurn + ". Now Player " + currentPlayerName + " with ID " + playerIds.get(currentPlayerIndex) + "'s turn.");
          world.moveTargetToNextRoom();  
      } else {
          print("Maximum turns reached. Ending game.");
      }
  }




    private void saveWorldMap(WorldOutline world) {
        world.drawWorld();
        print("World map saved to 'res/world.png'.");
    }

    private void print(String message) {
        try {
            output.append(message + "\n");
        } catch (Exception e) {
            System.out.println("Failed to write to output: " + e.getMessage());
        }
    }

    private void displayMenu() {
        int currentPlayerId = playerIds.get(currentPlayerIndex);
        String currentPlayerName = playerNames.get(currentPlayerId);
        print("\n--- Game Menu ---");
        print("Current player's turn: Player ID " + currentPlayerId + " Player Name " + currentPlayerName);
        print("1. Display Room Info");
        print("2. Save World Map");
        print("5. Move Player");
        print("6. Player Pick Up Item");
        print("7. Player Look Around");
        print("8. Display Player Info");
        print("9. Do Nothing");
        print("0. Quit Game");
        print("Select an option:");
    }
    
    @Override
    public boolean getIsRunning() {
      return isRunning;
    }
    
    private void setRunning(boolean isRunning) {
      this.isRunning = isRunning;
    }
}
