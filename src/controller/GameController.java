package controller;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.swing.SwingUtilities;
import view.FrameView;
import world.ReadOnlyWorld;
import world.WorldOutline;

/**
 * GameController manages the game's execution flow, processing user 
 * input and maintaining game state.
 */
public class GameController implements Controller {
  private Scanner scanner;
  private Appendable output;
  private int maxTurns;
  private RandomNumberGenerator rng;
  private ReadOnlyWorld rworld;
  private Map<Integer, Rectangle> roomCoordinates;
  private Map<Integer, Rectangle> playerCoordinates;
  private String mode = "Cli";
  private FrameView gameFrame;
  private boolean gameEnd = false;
  private String result;

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
    this.maxTurns = maxTurnsInput;
    this.rng = rngInput;
  }
    
  @Override
  public void playGame(ReadOnlyWorld world) throws InterruptedException, IOException {
    if ("Cli".equals(this.mode)) {
      setupGame((WorldOutline) world);
      if (!world.getIsRunning()) {
        return; 
      }
      
    }
  }
  
  @Override
  public void addPlayer(String playerName, int roomIndex, boolean isComputer) 
      throws IOException, InterruptedException {
    Command command;
    if (isComputer) {
      command = new CreateComputerPlayerCommand(rworld, playerName, roomIndex, 
          rworld.getPlayerIds(), rworld.getPlayerNames(), rworld.getIsComputer());
    } else {
      command = new CreatePlayerCommand(rworld, playerName, roomIndex, 
          rworld.getPlayerIds(), rworld.getPlayerNames(), rworld.getIsComputer());
    }
    command.execute(output);
    updateCoordinates();
  }

  @Override
  public void startGame() throws IOException, InterruptedException {
    try {
      if (rworld.getPlayerIds().isEmpty()) {
        throw new IllegalArgumentException("No players added. Cannot start game.");
      } else {
        print("Game will start with " + rworld.getPlayerIds().size() + " players.");
      }
      ((WorldOutline) rworld).setRunningGui(true);  
      runGameG((WorldOutline) rworld);
    } catch (IllegalArgumentException e) {
      throw e;
    }
  }

  
  @Override
  public void loadNewWorld(ReadOnlyWorld worldInput) throws IOException {
    this.rworld = worldInput;  
    updateCoordinates();
  }
  


  private void setupGame(WorldOutline world) throws InterruptedException, IOException {
    print("Setting up the game.");
    try {
      world.setMaxTurns(maxTurns);
    } catch (NumberFormatException e) {
      print("Invalid input for maximum turns. Setting to default of 20 turns.");
      maxTurns = 20;
      world.setMaxTurns(maxTurns);
    }
  
    boolean addingPlayers = true;
    while (addingPlayers && world.getIsRunning()) {
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
            handleAddPlayerCli(world);
            break;
          case 2: 
            handleAddComputerPlayerCli(world);
            break;
          case 3:  
            executeSaveWorldMap(world);
            break;
          case 4:
            if (world.getPlayerIds().isEmpty()) {
              throw new IllegalArgumentException("No players added. Cannot start game.");
            } else {
              print("Game will start with " + world.getPlayerIds().size() + " players.");
              runGameC(world);
            }
            break;
          default:
            print("Invalid option, please try again.");
        }
      } catch (NumberFormatException e) {
        print("Invalid option, please enter a valid number.");
      } catch (IllegalArgumentException e) {
        print(e.getMessage());
      }   
    }
  }


  private void runGameC(WorldOutline world) throws InterruptedException, IOException {
    print("Game started. Manage your turns.");
    PlayerStrategy computerPlayerStrategy = new ComputerPlayerStrategy(world, output, rng);
    while (world.getIsRunning() && world.getCurrentTurn() < maxTurns) {
      int currentPlayerId = world.getPlayerIds().get(world.getCurrentPlayerId());
      if (world.getIsComputer().get(currentPlayerId)) {
        displayMenu(world);
        computerPlayerStrategy.executeActions(currentPlayerId);
      } else {
        displayMenu(world);
        String input = scanner.nextLine();
        processPlayerInput(input, world, currentPlayerId);
      }
      if (world.getCurrentTurn() >= maxTurns) {
        world.setRunning(false);
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
        switch (choice) {
          case 1:  
            handleDisplayRoomInfoCli(world);
            break;
          case 2:  
            executeSaveWorldMap(world);
            break;
          case 3:
            handleMurderTargetCli(world, playerId);
            if (world.getTargetHealthPoint() <= 0) {
              print("Target eliminated. " + world.getPlayerNames().get(playerId) + " win!");
              world.setRunning(false);
              print("Game Over!");
            } else {
              print(world.advanceTurn());
            }
            break;
          case 4:  
            handleMovePetCli(world, playerId);
            print(world.advanceTurn());
            break;
          case 5:  
            handleMovePlayerCli(world, playerId);
            print(world.advanceTurn());
            break;
          case 6: 
            handlePickUpItemCli(world, playerId);
            print(world.advanceTurn());
            break;
          case 7: 
            Command lookAroundCommand = new LookAroundCommand(world, playerId);
            lookAroundCommand.execute(output);
            print(world.advanceTurn());
            break;
          case 8:  
            handlePlayerInfoCli(world);
            break;
          case 9: 
            print(world.advanceTurn());
            break;
          case 0:
            print("Quitting game.");
            world.setRunning(false);
            break;
          case 10:
            Command targetInfoCommand = new TargetInfoCommand(world);
            targetInfoCommand.execute(output);
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
  
  private void print(String message) throws IOException {
    try {
      output.append(message + "\n");
    } catch (IllegalArgumentException e) {
      System.out.println("Failed to write to output: " + e.getMessage());
    }
  }

  private void displayMenu(WorldOutline world) throws IOException, InterruptedException {
    int currentPlayerId = world.getCurrentPlayerId();
    String currentPlayerName = world.getPlayerNames().get(currentPlayerId);
    print("\n--- Game Menu ---");
    print("Turn " + world.getCurrentTurn());
    print("Current player's turn: Player ID " + currentPlayerId + " Player "
        + "Name " + currentPlayerName);
    print(world.getPlayerLocation(currentPlayerId));
    print(world.getPlayerItemsInfo(currentPlayerId));
    Command command = null;
    command = new TargetInfoCommand(world);
    command.execute(output);
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
  
  private void handleAddPlayerCli(WorldOutline world) throws IOException, InterruptedException {
    output.append("Enter player starting room id:\n");
    int roomIndex;
    try {
      roomIndex = Integer.parseInt(scanner.nextLine());
    } catch (NumberFormatException e) {
      throw new NumberFormatException("Invalid input for room index. "
          + "Please enter a valid number.\n");
    }

    if (roomIndex < 1 || roomIndex > world.getRoomCount()) {
      throw new IllegalArgumentException("Invalid room index. "
          + "Please enter a number between 1 and " + world.getRoomCount() + ".\n");
    }
    output.append("Enter player name:\n");
    String playerName = scanner.nextLine();
    Command createPlayerCommand = new CreatePlayerCommand(world, 
        playerName, roomIndex, world.getPlayerIds(), world.getPlayerNames(), world.getIsComputer());
    createPlayerCommand.execute(output);
  }
  
  private void handleAddComputerPlayerCli(WorldOutline world) 
      throws IOException, InterruptedException {
    int roomIndex;
    output.append("Enter computer player starting room id:\n");
    try {
      roomIndex = Integer.parseInt(scanner.nextLine());
    } catch (NumberFormatException e) {
      throw new NumberFormatException("Invalid input for room index."
          + " Please enter a valid number.\n");
    }
    if (roomIndex < 1 || roomIndex > world.getRoomCount()) {
      throw new IllegalArgumentException("Invalid room index. Please enter a "
          + "number between 1 and " + world.getRoomCount() + ".\n");
    }
    output.append("Enter computer player name:\n");
    String playerName = scanner.nextLine();
    Command createComputerPlayerCommand = 
        new CreateComputerPlayerCommand(world, playerName, roomIndex, 
            world.getPlayerIds(), world.getPlayerNames(), world.getIsComputer());
    createComputerPlayerCommand.execute(output);
  }
  
  private void executeSaveWorldMap(WorldOutline world) throws InterruptedException, IOException {
    Command saveWorldMapCommand = new SaveWorldMapCommand(world);
    try {
      saveWorldMapCommand.execute(output);
    } catch (IOException e) {
      output.append("Error saving world map: " + e.getMessage() + "\n");
    }
  }
  
  private void handleDisplayRoomInfoCli(WorldOutline world) 
      throws IOException, InterruptedException {
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
  
  private void handleMurderTargetCli(WorldOutline world, int playerId) 
      throws IOException, InterruptedException {
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
      output.append("Would you like to use an item to increase your murder chance? "
          + "(Enter item number above or any other number to not use any):\n");
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

  private void handleMovePetCli(WorldOutline world, int playerId) 
      throws IOException, InterruptedException {
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
  
  private void handleMovePlayerCli(WorldOutline world, int playerId) 
      throws IOException, InterruptedException {
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
  
  private void handlePickUpItemCli(WorldOutline world, int playerId) 
      throws IOException, InterruptedException {
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
      throw new NumberFormatException("Invalid input for item index."
          + " Please enter a valid number.\n");
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
  
  private void handlePlayerInfoCli(WorldOutline world) throws IOException, InterruptedException {
    int totalPlayerNumber = world.getPlayerIds().size() - 1;
    output.append("Enter the player ID to view their information "
        + "(from 0 to " + totalPlayerNumber + "):\n");
    int targetPlayerId;
    try {
      targetPlayerId = Integer.parseInt(scanner.nextLine());
    } catch (NumberFormatException e) {
      throw new NumberFormatException(e.getMessage() + "\n");
    }

    Command playerInfoCommand = new PlayerInfoCommand(world, targetPlayerId);
    playerInfoCommand.execute(output);
  }

  @Override
  public void setOutput(Appendable outputInput) {
    this.output = outputInput;
  }
  
  @Override
  public BufferedImage saveWorldImg() {
    return rworld.drawWorld();
  }

  private void updateCoordinates() {
    if (rworld != null) {
      rworld.drawWorld();
      this.roomCoordinates = rworld.getRoomCoordinates();
      this.playerCoordinates = rworld.getPlayerCoordinates();
    }
  }
  
  @Override
  public Map<Integer, Rectangle> getRoomCoordinates() {
    return roomCoordinates;
  }

  @Override
  public Map<Integer, Rectangle> getPlayerCoordinates() {
    return playerCoordinates;
  }
  
  @Override
  public int getCurrentPlayerId() {
    return rworld.getCurrentPlayerId();
  }
  
  @Override
  public boolean getRunning() {
    return rworld.getIsRunningGui();
  }
  
  @Override
  public boolean getEnd() {
    return this.gameEnd;
  }
  
  @Override
  public void setEnd(boolean end) {
    this.gameEnd = end;
  }
  
  @Override
  public String getResult() {
    return this.result;
  }
  
  private void handleComputerPlayer(int currentPlayerId) throws InterruptedException, IOException {
    PlayerStrategy computerPlayerStrategy = 
        new ComputerPlayerStrategy((WorldOutline) rworld, output, rng);
    computerPlayerStrategy.executeActions(currentPlayerId);
      
  }
  
  private void runGameG(WorldOutline world) throws InterruptedException, IOException {
    this.gameEnd = false;
    while (world.getIsRunning()) {
      int currentPlayerId = world.getCurrentPlayerId();
      if (world.getIsComputer().get(currentPlayerId)) {
        handleComputerPlayer(currentPlayerId);
        updateCoordinates();
        updateGameStatus();
        gameFrame.refreshWorldDisplay(this);
        Thread.sleep(1000);
      } else {
        SwingUtilities.invokeLater(() -> {
          try {
            prepareForPlayerTurn(currentPlayerId);
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
        updateGameStatus();
        return;  
      }
      if (!world.getIsRunning()) {
        break; 
      }

    }
  }

  private void updateGameStatus() {
    if (rworld.getCurrentTurn() >= maxTurns) {
      ((WorldOutline) rworld).setRunningGui(false);
      ((WorldOutline) rworld).setRunning(false);
      this.gameEnd = true;
      this.result = "Game over: Maximum number of turns reached!\nNo winner for this game!";
    } else if (rworld.getTargetHealthPoint() <= 0) {
      ((WorldOutline) rworld).setRunningGui(false);
      ((WorldOutline) rworld).setRunning(false);
      this.gameEnd = true;
      this.result = "Game over: Target eliminated!\n";
      int winner;
      if (((WorldOutline) rworld).getCurrentPlayerId() == 0) {
        winner = rworld.getPlayerIds().get(rworld.getPlayerIds().size() - 1);
      } else {
        winner = rworld.getCurrentPlayerId() - 1;
      }
      String winnerName = rworld.getPlayerNames().get(winner);
      this.result += "Player " + winnerName + " with ID " + winner + " is the winner!";
    }
  }



  private void prepareForPlayerTurn(int playerId) throws IOException {
    print("human player " + playerId + " to take their turn.");
  }

  @Override
  public void setGameFrame(FrameView frame) {
    this.gameFrame = frame;
  }

  @Override
  public void movePlayerToRoom(int roomId, Appendable outputView) 
      throws IOException, InterruptedException {
    int currentPlayerId = getCurrentPlayerId();
    try {
      Command movePlayerCommand = new MovePlayerCommand(rworld, currentPlayerId, roomId);
      movePlayerCommand.execute(outputView);
      updateCoordinates();
      passTurn();
    } catch (IllegalArgumentException e) {
      throw e; 
    }
  }

  @Override
  public String displayPlayerInfo(int playerId, Appendable outputView) 
      throws InterruptedException, IOException {
    Command playerInfoCommand = new PlayerInfoCommand(rworld, playerId);
    return playerInfoCommand.execute(outputView);
  }

  @Override
  public List<String> passRoomItem(int playerId) {
    return rworld.getRoomItems(rworld.getPlayerRoomId(playerId));
  }

  @Override
  public void pickUpItem(int playerId, String itemName, Appendable outputView) 
      throws IOException, InterruptedException {
    try {
      Command pickUpItemCommand = new PickUpItemCommand(rworld, playerId, itemName);
      pickUpItemCommand.execute(outputView);
      passTurn();
    } catch (IllegalArgumentException e) {
      throw e; 
    }
  }

  @Override
  public void passTurn() throws InterruptedException, IOException {
    ((WorldOutline) rworld).advanceTurn();
    runGameG((WorldOutline) rworld);
  }
  
  @Override
  public String performLookAround(int playerId, Appendable outputView) 
      throws IOException, InterruptedException {
    Command lookAroundCommand = new LookAroundCommand(rworld, playerId);
    String resultLook = lookAroundCommand.execute(outputView);
    passTurn();
    return resultLook;
  }
  
  @Override
  public List<String> passPlayerItems(int playerId) {
    List<String> playerItems = rworld.getPlayerItems(playerId);
    return playerItems;
  }
  
  @Override
  public void attackTargetWithItem(int playerId, String itemName, Appendable outputView) 
      throws IOException, InterruptedException {
    try {
      Command murderTargetCommand = new MurderTargetCommand(rworld, playerId, itemName);
      murderTargetCommand.execute(outputView);
      passTurn();
    } catch (IllegalArgumentException e) {
      throw e; 
    }
  }

  @Override
  public void movePet(int playerId, int roomId, Appendable outputView) 
      throws IOException, InterruptedException {
    try {
      Command movePetCommand = new MovePetCommand(rworld, playerId, roomId);
      movePetCommand.execute(outputView);
      passTurn();
    } catch (IllegalArgumentException e) {
      throw e;
    }
  }
  
  @Override
  public void setMaxTurn(int turn) {
    ((WorldOutline) rworld).setMaxTurns(turn);
    
  }
  
  @Override
  public void pickNothing() throws InterruptedException, IOException {
    ((WorldOutline) rworld).advanceTurn();
    runGameG((WorldOutline) rworld);
  }




}


  

 







  

