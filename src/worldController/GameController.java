package worldController;

import world.WorldOutline;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GameController {
    private Scanner scanner;
    private Appendable output;
    private int maxTurns;
    private int currentTurn = 1;
    private boolean gameStarted = false;
    private List<Integer> playerIds;
    private int currentPlayerIndex = 0;
    private Map<Integer, Boolean> isComputer;
    private RandomNumberGenerator rng;

    public GameController(Readable input, Appendable output, RandomNumberGenerator rng) {
        this.scanner = new Scanner(input);
        this.output = output;
        this.playerIds = new ArrayList<>();
        this.isComputer = new HashMap<>();
        this.rng = rng;
    }

    public void playGame(WorldOutline world) throws InterruptedException {
        setupGame(world);
        if (gameStarted) {
            runGame(world);
        }
    }

    private void setupGame(WorldOutline world) throws InterruptedException {
      print("Setting up the game.");
      print("Please enter the maximum number of turns:");
      try {
          maxTurns = Integer.parseInt(scanner.nextLine());
          world.setMaxTurn(maxTurns);
      } catch (NumberFormatException e) {
          print("Invalid input for maximum turns. Setting to default of 20 turns.");
          maxTurns = 20;
          world.setMaxTurn(maxTurns);
      }

      boolean addingPlayers = true;
      while (addingPlayers) {
          print("Add players:");
          print("1. Add Human-Controlled Player");
          print("2. Add Computer-Controlled Player");
          print("3. Start Game");
          String input = scanner.nextLine();
          try {
              int choice = Integer.parseInt(input);
              switch (choice) {
                case 1: // Human-Controlled Player
                    print("Enter player name and starting room index:");
                    String humanPlayerName = scanner.nextLine();
                    try {
                        int humanRoomIndex = Integer.parseInt(scanner.nextLine());
                        int humanPlayerId = world.callCreatePlayer(humanPlayerName, humanRoomIndex);
                        playerIds.add(humanPlayerId);
                        isComputer.put(humanPlayerId, false); // Explicitly mark as human-controlled
                        print("Human player added with ID: " + humanPlayerId);
                    } catch (NumberFormatException e) {
                        print("Invalid room index, please enter a valid number.");
                    }
                    break;
                case 2: // Computer-Controlled Player
                    print("Enter player name and starting room index:");
                    String computerPlayerName = scanner.nextLine();
                    try {
                        int computerRoomIndex = Integer.parseInt(scanner.nextLine());
                        int computerPlayerId = world.callCreatePlayer(computerPlayerName, computerRoomIndex);
                        playerIds.add(computerPlayerId);
                        isComputer.put(computerPlayerId, true); // Explicitly mark as computer-controlled
                        print("Computer player added with ID: " + computerPlayerId);
                    } catch (NumberFormatException e) {
                        print("Invalid room index, please enter a valid number.");
                    }
                    break;
                case 3:
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
      boolean isRunning = true;

      while (isRunning && currentTurn < maxTurns) {
          int currentPlayerId = playerIds.get(currentPlayerIndex);
          if (isComputer.get(currentPlayerId)) {
              // Simulate computer player actions
              computerPlayerActions(world, currentPlayerId);
          } else {
              // Normal player actions
              displayMenu();
              String input = scanner.nextLine();
              processPlayerInput(input, world, currentPlayerId);
          }
          // Check if the game should continue or not after actions
          if (currentTurn >= maxTurns) {
              print("Game over: Maximum number of turns reached.");
              isRunning = false;
          }
      }
  }
    
    private void processPlayerInput(String input, WorldOutline world, int playerId) {
      try {
          int choice = Integer.parseInt(input);
          switch (choice) {
              case 1:  // Display room information
                  print("Enter room ID:");
                  int roomId = Integer.parseInt(scanner.nextLine());
                  print(world.displayRoomInfo(roomId));
                  break;
              case 2:  // Save world map
                  saveWorldMap(world);
                  break;
              case 5:  // Move player
                  print("Enter target room ID:");
                  int targetRoomId = Integer.parseInt(scanner.nextLine());
                  print(world.movePlayer(playerId, targetRoomId));
                  advanceTurn(world);
                  break;
              case 6:  // Pick up item
                  pickUpItem(world, playerId);
                  advanceTurn(world);
                  break;
              case 7:  // Player look around
                  print(world.playerLookAround(playerId));
                  advanceTurn(world);
                  break;
              case 8:  // Display player info
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
              case 9:  // Explicitly proceed to next turn
                  advanceTurn(world);
                  break;
              case 0:  // Quit game
                  print("Quitting game.");
                  System.exit(0);  // Exit the game
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
          int currentRoomId = world.getPlayerRoomId(playerId);  
          List<Integer> neighbors = world.getNeighborRooms(currentRoomId);  

          if (neighbors.isEmpty()) {
              print("No available moves for player " + playerId);
              return;
          }

          int action = rng.nextInt(3);  // Assuming 0: Move, 1: Pick Up Item, 2: Look Around
          switch (action) {
              case 0:
                  int roomIndex = rng.nextInt(neighbors.size());
                  int targetRoomId = neighbors.get(roomIndex);
                  world.movePlayer(playerId, targetRoomId);
                  print("Computer player " + playerId + " moved to room " + targetRoomId);
                  Thread.sleep(1000);
                  advanceTurn(world);
                  break;
              case 1:
                int roomId = world.getPlayerRoomId(playerId);
                List<String> itemsInRoom = world.getRoomItems(roomId);
                if (!itemsInRoom.isEmpty()) {
                    int itemIndex = rng.nextInt(itemsInRoom.size());
                    String itemName = itemsInRoom.get(itemIndex);
                    print("Computer player " + playerId + " automatically picked up: " + itemName);
                    print(world.playerPickUpItem(playerId, itemName));
                } else {
                    print("No items available to pick up in this room for player " + playerId);
                }
                  Thread.sleep(1000);
                  advanceTurn(world);
                  break;
              case 2:
                  print(world.playerLookAround(playerId));
                  Thread.sleep(1000);
                  advanceTurn(world);
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
          print("Turn " + currentTurn + ". Now Player ID " + playerIds.get(currentPlayerIndex) + "'s turn.");
          world.moveTargetToNextRoom();  // Move the target at the end of each player's turn
      } else {
          print("Maximum turns reached. Ending game.");
      }
  }




    private void saveWorldMap(WorldOutline world) {
        world.drawWorld();
        print("World map saved to 'world_map.png'.");
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
        print("\n--- Game Menu ---");
        print("Current player's turn: Player ID " + currentPlayerId);
        print("1. Display Room Info");
        print("2. Save World Map");
        print("5. Move Player");
        print("6. Player Pick Up Item");
        print("7. Player Look Around");
        print("8. Display Player Info");
        print("9. Next Turn");
        print("0. Quit Game");
        print("Select an option:");
    }
}
