package worldController;

import world.WorldOutline;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameController {
    private Scanner scanner;
    private Appendable output;
    private int maxTurns;
    private int currentTurn = 0;
    private boolean gameStarted = false;
    private List<Integer> playerIds;
    private int currentPlayerIndex = 0;

    public GameController(Readable input, Appendable output) {
        this.scanner = new Scanner(input);
        this.output = output;
        this.playerIds = new ArrayList<>();
    }

    public void playGame(WorldOutline world) {
        setupGame(world);
        if (gameStarted) {
            runGame(world);
        }
    }

    private void setupGame(WorldOutline world) {
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
                  case 1:
                  case 2:
                      print("Enter player name and starting room index:");
                      String playerName = scanner.nextLine();
                      try {
                          int roomIndex = Integer.parseInt(scanner.nextLine());
                          int playerId = world.callCreatePlayer(playerName, roomIndex);
                          playerIds.add(playerId);
                          print("Player added with ID: " + playerId);
                      } catch (NumberFormatException e) {
                          print("Invalid room index, please enter a valid number.");
                      }
                      break;
                  case 3:
                      if (playerIds.isEmpty()) {
                          print("No players added. Cannot start game.");
                      } else {
                          addingPlayers = false;
                          gameStarted = true;
                          print("Game will start with " + playerIds.size() + " players.");
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


    private void runGame(WorldOutline world) {
      print("Game started. Manage your turns.");
      boolean isRunning = true;

      while (isRunning && currentTurn < maxTurns) {
          displayMenu();
          String input = scanner.nextLine();
          try {
              int choice = Integer.parseInt(input);
              int playerId = playerIds.get(currentPlayerIndex);

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
                    pickUpItem(world, playerIds.get(currentPlayerIndex));
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
                      isRunning = false;
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

      if (currentTurn >= maxTurns) {
          print("Game over: Maximum number of turns reached.");
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
        BufferedImage image = world.drawWorld();
        try {
            File outputfile = new File("res/world_map.png");
            ImageIO.write(image, "png", outputfile);
            print("World map saved to 'world_map.png'.");
        } catch (Exception e) {
            print("Failed to save world map: " + e.getMessage());
        }
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
