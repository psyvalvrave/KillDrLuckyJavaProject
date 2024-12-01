// HumanPlayerStrategy.java
package controller;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import world.WorldOutline;

public class HumanPlayerStrategyCLI implements PlayerStrategy {
    private WorldOutline world;
    private Appendable output;
    private Scanner scanner;

    public HumanPlayerStrategyCLI(WorldOutline world, Appendable output, Scanner scanner) {
        this.world = world;
        this.output = output;
        this.scanner = scanner;
    }

    @Override
    public void executeActions(int playerId) throws InterruptedException, IOException {
        displayMenu(playerId);
        String input = scanner.nextLine();
        processPlayerInput(input, playerId);
    }

    private void displayMenu(int playerId) throws IOException, InterruptedException {
        String currentPlayerName = world.getPlayerNames().get(playerId);
        output.append("\n--- Game Menu ---\n");
        output.append("Turn " + world.getCurrentTurn() + "\n");
        output.append("Current player's turn: Player ID " + playerId + " Player Name " + currentPlayerName + "\n");
        output.append(world.getPlayerLocation(playerId) + "\n");
        output.append(world.getPlayerItemsInfo(playerId) + "\n");
        Command command = new TargetInfoCommand(world);
        command.execute(output);
        output.append("1. Display Room Info\n");
        output.append("2. Save World Map\n");
        output.append("3. Attempt to Murder Target\n");
        output.append("4. Move Pet\n");
        output.append("5. Move Player\n");
        output.append("6. Player Pick Up Item\n");
        output.append("7. Player Look Around\n");
        output.append("8. Display Player Info\n");
        output.append("9. Do Nothing\n");
        output.append("10. Display Target Info\n");
        output.append("0. Quit Game\n");
        output.append("Select an option:\n");
    }

    private void processPlayerInput(String input, int playerId) throws IOException, InterruptedException {
        boolean validInput = false;
        while (!validInput) {
            try {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        handleDisplayRoomInfoCLI();
                        break;
                    case 2:
                        executeSaveWorldMap();
                        break;
                    case 3:
                        handleMurderTargetCLI(playerId);
                        if (world.getTargetHealthPoint() <= 0) {
                            output.append("Target eliminated. " + world.getPlayerNames().get(playerId) + " wins!\n");
                            world.setRunning(false);
                            output.append("Game Over!\n");
                        } else {
                            output.append(world.advanceTurn());
                        }
                        break;
                    case 4:
                        handleMovePetCLI(playerId);
                        output.append(world.advanceTurn());
                        break;
                    case 5:
                        handleMovePlayerCLI(playerId);
                        output.append(world.advanceTurn());
                        break;
                    case 6:
                        handlePickUpItemCLI(playerId);
                        output.append(world.advanceTurn());
                        break;
                    case 7:
                        Command lookAroundCommand = new LookAroundCommand(world, playerId);
                        lookAroundCommand.execute(output);
                        output.append(world.advanceTurn());
                        break;
                    case 8:
                        handlePlayerInfoCLI();
                        break;
                    case 9:
                        output.append(world.advanceTurn());
                        break;
                    case 0:
                        output.append("Quitting game.\n");
                        world.setRunning(false);
                        break;
                    case 10:
                        Command targetInfoCommand = new TargetInfoCommand(world);
                        targetInfoCommand.execute(output);
                        break;
                    default:
                        output.append("Unknown command. Please try again.\n");
                }
                validInput = true;
            } catch (NumberFormatException e) {
                output.append("Invalid input, please enter a number.\n");
                displayMenu(playerId);
                input = scanner.nextLine();
            } catch (IllegalArgumentException e) {
                output.append("Error processing command: " + e.getMessage() + "\n");
                displayMenu(playerId);
                input = scanner.nextLine();
            } catch (IOException e) {
                output.append("Error processing command: " + e.getMessage() + "\n");
                displayMenu(playerId);
                input = scanner.nextLine();
            }
        }
    }

    // Include the methods for handling each command, similar to those in the GameController
    // For brevity, only one method is shown here
    private void handleDisplayRoomInfoCLI() throws IOException, InterruptedException {
        output.append("Enter room ID (From 1 to " + world.getRoomCount() + "):\n");
        int roomId;
        try {
            roomId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            output.append("Invalid input for room ID. Please enter a valid number.\n");
            return;
        }

        Command displayRoomInfoCommand = new DisplayRoomInfoCommand(world, roomId);
        displayRoomInfoCommand.execute(output);
    }
    
    private void executeSaveWorldMap() throws InterruptedException, IOException {
      Command saveWorldMapCommand = new SaveWorldMapCommand(world);
      try {
          saveWorldMapCommand.execute(output);
      } catch (IOException e) {
          output.append("Error saving world map: " + e.getMessage() + "\n");
      }
  }
    
    private void handlePlayerInfoCLI() throws IOException, InterruptedException {
      int totalPlayerNumber = world.getPlayerIds().size() - 1;
      output.append("Enter the player ID to view their information (from 0 to " + totalPlayerNumber +"):\n");
      int targetPlayerId;
      try {
          targetPlayerId = Integer.parseInt(scanner.nextLine());
      } catch (NumberFormatException e) {
        throw new NumberFormatException(e.getMessage() + "\n");
      }

      Command playerInfoCommand = new PlayerInfoCommand(world, targetPlayerId);
      playerInfoCommand.execute(output);
  }
    
    private void handlePickUpItemCLI(int playerId) throws IOException, InterruptedException {
      int roomId = world.getPlayerRoomId(playerId);
      List<String> itemsInRoom = world.getRoomItems(roomId);

      if (itemsInRoom.isEmpty()) {
          output.append("No items available to pick up in this room.\n");
          return;
      }

      output.append("Items available to pick up:\n");
      for (int i = 0; i < itemsInRoom.size(); i++) {
          output.append((i + 1) + ": " + itemsInRoom.get(i) + "\n");
      }

      output.append("Enter the index of the item to pick up:\n");
      int itemIndex;
      try {
          itemIndex = Integer.parseInt(scanner.nextLine()) - 1;
      } catch (NumberFormatException e) {
        throw new NumberFormatException("Invalid input for item index. Please enter a valid number.\n");
      }

      if (itemIndex < 0 || itemIndex >= itemsInRoom.size()) {
        throw new IllegalArgumentException(
            "Please enter valid number in range");
      }
      
      try {
      String itemName = itemsInRoom.get(itemIndex).split(": ")[0];
      Command pickUpItemCommand = new PickUpItemCommand(world, playerId, itemName);
      pickUpItemCommand.execute(output);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(e.getMessage() + "\n");
      }
  }
    
    private void handleMovePlayerCLI(int playerId) throws IOException, InterruptedException {
      List<String> neighbors = world.getPlayerNeighborRoom(playerId);
      if (neighbors.isEmpty()) {
        throw new IllegalArgumentException("There are no available rooms to move to.\n");
      }

      output.append("You can move to the following rooms:\n");
      for (String neighbor : neighbors) {
          output.append(neighbor + "\n");
      }

      output.append("Enter target room ID:\n");
      int targetRoomId;
      try {
          targetRoomId = Integer.parseInt(scanner.nextLine());
          if (targetRoomId < 1 || targetRoomId > world.getRoomCount()) {
            throw new IllegalArgumentException("Invalid room index. Please enter a number between "
                + "1 and " + world.getRoomCount() + ".\n");
          }
      } catch (NumberFormatException e) {
        throw new NumberFormatException(e.getMessage() + "\n");
      }

      Command movePlayerCommand = new MovePlayerCommand(world, playerId, targetRoomId);
      movePlayerCommand.execute(output);
  }
    
    private void handleMovePetCLI(int playerId) throws IOException, InterruptedException {
      int maxRooms = world.getRoomCount();
      output.append("Enter a room ID for the pet to move to (1-" + maxRooms + "):\n");
      int targetRoomId;
      try {
          targetRoomId = Integer.parseInt(scanner.nextLine());
      } catch (NumberFormatException e) {
          throw new NumberFormatException("Invalid input for room ID. Please enter a valid number.\n");
      }

      if (targetRoomId < 1 || targetRoomId > world.getRoomCount()) {
        throw new IllegalArgumentException("Invalid room ID. "
            + "Please enter a number between 1 and " + maxRooms + ".");
      }

      Command movePetCommand = new MovePetCommand(world, playerId, targetRoomId);
      movePetCommand.execute(output);
  }
    
    private void handleMurderTargetCLI(int playerId) throws IOException, InterruptedException {
      output.append("Attempt to murder the target:\n");
      if (!world.canMurderAttempt(playerId)) {
        throw new IllegalArgumentException(
            "Player is not in the same room as the target.");
      }
      List<String> playerItems = world.getPlayerItems(playerId);
      String itemName = null;
      if (!playerItems.isEmpty()) {
          output.append("Your items:\n");
          for (int i = 0; i < playerItems.size(); i++) {
              output.append((i + 1) + ": " + playerItems.get(i) + "\n");
          }
          output.append("Would you like to use an item to increase your murder chance? (Enter item number above or any other number to not use any):\n");
          String input = scanner.nextLine().trim();
          int choice = Integer.parseInt(input);
          if (choice > 0 && choice <= playerItems.size()) {
              itemName = playerItems.get(choice - 1).split(":")[0];
          }
      } else {
          output.append("You have no items to use.\n");
      }

      Command murderTargetCommand = new MurderTargetCommand(world, playerId, itemName);
      murderTargetCommand.execute(output);
  }


    // Implement other command handling methods similarly...
}
