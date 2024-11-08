package world;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

/**
 * Represents the game world, managing rooms, items, and the target character.
 * The world is initialized from a specified file.
 */
public class World implements WorldOutline {
  private List<Block> rooms;
  private List<Gadget> items;
  private List<CharacterPlayer> players;
  private CharacterTarget target;
  private String worldText;
  private int rows;
  private int cols;
  private List<String[]> roomData;
  private List<String[]> itemData;
  private String targetName;
  private String petName ;
  private int targetHealth = 0;
  private String worldName;
  private int itemLimit = 3;
  private int nextPlayerId = 0;
  private int maxTurn;
  private CharacterPet pet;

  /**
   * Constructor for World that initializes the game from a file.
   * 
   * @param inputSource The path to the text file containing world setup information.
   */
  public World(Readable inputSource) throws IllegalArgumentException {
    rooms = new ArrayList<>();
    items = new ArrayList<>();
    roomData = new ArrayList<>();
    itemData = new ArrayList<>();
    players = new ArrayList<>();
    loadWorld(inputSource);
    establishRoomNeighbors();
    establishRoomVisble();
    initializePetDFS();
    setWorldText();
  }

  /**
   * Loads the world configuration from a specified text file, 
   * setting up rooms, items, and the target.
   * 
   * @param filename the file path to load world data from.
   */
  private void loadWorld(Readable inputSource) {
    if (inputSource == null) {
      throw new IllegalArgumentException("Input source cannot be null");
    }

    Scanner scanner = new Scanner(inputSource);
    int state = 0;  
    int roomCount = 0;  
    int itemCount = 0;
    try {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
          continue;
        }
        switch (state) {
          case 0:  
            String[] dimensions = line.split("\\s+");
            this.rows = Integer.parseInt(dimensions[0]);
            this.cols = Integer.parseInt(dimensions[1]);
            this.worldName = String.join(" ", Arrays.copyOfRange(dimensions, 2, dimensions.length));
            state = 1;
            break;
          case 1:  
            String[] targetDetails = line.split("\\s+", 2);
            targetHealth = Integer.parseInt(targetDetails[0]);
            targetName = targetDetails.length > 1 ? targetDetails[1] : "";
            state = 2;
            break;
          case 2: 
            petName = line;
            state = 3;
            break;
          case 3:  
            roomCount = Integer.parseInt(line);
            state = 4;
            break;
          case 4:  
            if (roomCount > 0) {
              String[] parts = line.split("\\s+");
              if (parts.length < 5) {  
                throw new IllegalArgumentException("Insufficient parts to parse room details.");
              }
              int[] coordinates = {
                  Integer.parseInt(parts[0]),
                  Integer.parseInt(parts[1]),
                  Integer.parseInt(parts[2]),
                  Integer.parseInt(parts[3])
              };
              String roomName = String.join(" ", Arrays.copyOfRange(parts, 4, parts.length));
              int roomId = roomData.size() + 1;  
              roomData.add(new String[]{roomName, 
                  Arrays.toString(coordinates), String.valueOf(roomId)});
              roomCount--;
              if (roomCount == 0) {
                state = 5;
              }
            }
            break;
          case 5:  
            itemCount = Integer.parseInt(line);
            state = 6;
            break;
          case 6:  
            if (itemCount > 0) {
              String[] itemParts = line.split("\\s+");
              String itemName = String.join(" ", Arrays.copyOfRange(itemParts, 
                  2, itemParts.length));
              int location = Integer.parseInt(itemParts[0]);
              int murderValue = Integer.parseInt(itemParts[1]);                      
              itemData.add(new String[]{itemName, String.valueOf(location), 
                  String.valueOf(murderValue)});
              itemCount--;
            }
            break;
          default:
            break;
        }
      }
      scanner.close();
      processRoomData(roomData);
      processItemData(itemData);
      createTarget(targetName, rooms.get(0), targetHealth);
      createPet(petName, target.getLocation());
    } catch (NumberFormatException e) {
      System.err.println("Error parsing numbers from the input: " + e.getMessage());
    } finally {
      scanner.close();
    }
  }
 
  /**
   * Processes room data to create room objects and add them to the world.
   * 
   * @param roomData List of room data strings parsed from the file.
   */
  private void processRoomData(List<String[]> roomDataInput) {
    for (String[] roomInfo : roomDataInput) {
      String roomName = roomInfo[0];
      int[] intCoords = Arrays.stream(roomInfo[1].replace("[", "").replace("]", "").split(", "))
                      .mapToInt(Integer::parseInt)
                      .toArray();
      int roomIdWrite = Integer.parseInt(roomInfo[2]);
      createRoom(roomName, roomIdWrite, intCoords, roomDataInput);
    }
  }

  /**
   * Processes item data to create item objects and add them to the world.
   * 
   * @param itemData List of item data strings parsed from the file.
   */
  private void processItemData(List<String[]> itemDataInput) {
    for (String[] itemInfo : itemDataInput) {
      int location = Integer.parseInt(itemInfo[1]);
      int murderValue = Integer.parseInt(itemInfo[2]);
      createItem(itemInfo[0], location, murderValue);
    }
  }
 

  @Override
  public int getRoomCount() {
    return rooms.size();
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  /**
   * Return the target object in the model. 
   */
  public CharacterTarget getTarget() {
    return target;
  }

  /**
   * Creates an item and adds it to the world.
   * 
   * @param name The name of the item.
   * @param location The room index where the item is located.
   * @param murderValue The impact or damage value of the item.
   * @return The newly created item object.
   */
  public Gadget createItem(String name, int location, int murderValue) {
    if (location < 0 || location >= rooms.size()) {
      throw new IllegalArgumentException("Invalid room location index: " + location);
    }
    Block room = rooms.get(location);
    Item newItem = new Item(name, room, murderValue);
    items.add(newItem);
    room.addItem(newItem); 
    return newItem;
  }

  /**
   * Creates the target character and adds it to the world.
   * 
   * @param name The name of the target.
   * @param room The starting room for the target.
   * @param health The initial health points for the target.
   * @return The newly created target object.
   */
  public CharacterTarget createTarget(String name, Block room, int health) {
    Target newTarget = new Target(name, room, health);
    this.target = newTarget;
    return newTarget;
  }

  /**
   * Creates a room and adds it to the world.
   * 
   * @param roomName The name of the room.
   * @param roomId The identifier for the room.
   * @param coordinates The spatial coordinates of the room.
   * @param allRoomData Additional data for room configurations.
   * @return The newly created room object.
   */
  public Block createRoom(String roomName, int roomId, 
      int[] coordinates, List<String[]> allRoomData) {
    if (roomName == null || roomName.trim().isEmpty()) {
      throw new IllegalArgumentException("Room name cannot be null or empty.");
    }
    if (roomId < 0) {
      throw new IllegalArgumentException("Room ID cannot be negative.");
    }
    if (coordinates == null || coordinates.length != 4) {
      throw new IllegalArgumentException("Coordinates array must contain exactly four integers.");
    }
    if (coordinates[0] > coordinates[2] || coordinates[1] > coordinates[3]) {
      throw new IllegalArgumentException("Invalid coordinates: x1 should be "
          + "<= x2 and y1 should be <= y2.");
    }
    Room newRoom = new Room(roomName, roomId, coordinates, allRoomData);
    rooms.add(newRoom);
    return newRoom;
  }


  @Override
  public BufferedImage drawWorld() {
    BufferedImage image = new BufferedImage(this.cols * 25, this.rows * 25, 
        BufferedImage.TYPE_INT_RGB);
    Graphics g = image.createGraphics();

    g.setColor(Color.WHITE);
    g.fillRect(0, 0, this.cols * 25, this.rows * 25);

    Font font = new Font("Arial", Font.PLAIN, 9);
    g.setFont(font);

    
    for (String[] room : roomData) {
      String roomName = room[0];
      String roomId = room[2];
      String[] coordsStr = room[1].replace("[", "").replace("]", "").split(", ");
      int y1 = Integer.parseInt(coordsStr[0]);
      int x1 = Integer.parseInt(coordsStr[1]);
      int y2 = Integer.parseInt(coordsStr[2]);
      int x2 = Integer.parseInt(coordsStr[3]);

      int x1Draw = x1 * 25;
      int y1Draw = y1 * 25;
      int width = ((x2 - x1 + 1) * 25) - 1;
      int height = ((y2 - y1 + 1) * 25) - 1;

      if (width > 0 && height > 0) {
        g.setColor(Color.WHITE);
        g.fillRect(x1Draw, y1Draw, width, height);

        g.setColor(Color.BLACK);
        g.drawRect(x1Draw, y1Draw, width, height);
        g.drawString(roomName + " (" + roomId + ")", x1Draw + 3, y1Draw + (height / 2) + 5);
      }

      if (target != null && target.getLocation() != null 
          && roomName.equals(target.getLocation().getRoomName())) {
        g.setColor(Color.RED);
        g.fillOval(x1Draw + 10, y1Draw + 10, 10, 10); 
        g.drawString("Target: " + target.getCharacterName(), x1Draw + 25, y1Draw + 15);
      }
      int petOffset = 0;
      if (pet != null && pet.getLocation() != null && roomName.equals(pet.getLocation().getRoomName())) {
        g.setColor(Color.BLUE);
        g.fillOval(x1Draw + 10, y1Draw + 25, 10, 10); 
        g.drawString("Pet: " + pet.getCharacterName(), x1Draw + 25, y1Draw + 30);
        petOffset = 10;
      }
      int playerOffset = 0 + petOffset;
      for (CharacterPlayer player : players) {
        if (player.getLocation() != null && roomName.equals(player.getLocation().getRoomName())) {
          g.setColor(Color.BLACK);
          g.fillOval(x1Draw + 10, y1Draw + 30 + playerOffset, 10, 10); 
          g.drawString("Player: " + player.getCharacterName(), 
              x1Draw + 25, y1Draw + 35 + playerOffset);

          playerOffset += 20; 
        }
      }
    }

    g.dispose();

    try {
      File outputDir = new File("../res");
      if (!outputDir.exists()) {
        outputDir.mkdirs();  
      }
      File outputfile = new File(outputDir, "world.png");
      ImageIO.write(image, "png", outputfile);
    } catch (IOException e) {
      throw new IllegalArgumentException("Error saving world image: " + e.getMessage());
    }
    
    return image;
  }

  @Override
  public void setWorldText() {
      String petInfo = (pet != null) ? String.format("Pet: %s\nPet's Location: %s",
                                                    pet.getCharacterName(),
                                                    (pet.getLocation() != null ? pet.getLocation().getRoomName() : "Unknown"))
                                     : "No pet currently set.";

      this.worldText = String.format("World Name: %s\n" 
                                    + "World Dimensions: %dx%d\n" 
                                    + "Number of Rooms: %d\n" 
                                    + "Number of Items: %d\n" 
                                    + "Target: %s\n" 
                                    + "Target's Health: %d\n" 
                                    + "Target's Location: %s\n"
                                    + "%s", 
                                    this.worldName, this.cols, this.rows, getRoomCount(), getItemCount(),
                                    (target != null ? target.getCharacterName() : "No target"),
                                    (target != null ? target.getHealthPoint() : 0),
                                    (target != null && target.getLocation() != null ? target.getLocation().getRoomName() : "Unknown"),
                                    petInfo);
  }

  
  @Override
  public String getWorldText() {
    return this.worldText;
  }
  
  /**
   * Retrieves a list of all rooms in the world.
   * This method provides a safe copy of the rooms list to 
   * ensure that the internal list is not modified.
   * 
   * @return A new list containing all the rooms currently in the world.
   */
  public List<Block> getRooms() {
    ArrayList<Block> roomsList = new ArrayList<>(rooms);
    return roomsList;
  }

  /**
   * Retrieves a list of all items in the world.
   * This method provides a safe copy of the items list to 
   * ensure that the internal list is not modified.
   * 
   * @return A new list containing all the items currently in the world.
   */
  public List<Gadget> getItems() {
    ArrayList<Gadget> gadgets = new ArrayList<>(items);
    return gadgets;
  }
 
  @Override
  public List<String[]> getRoomData() {
    return new ArrayList<>(roomData);  
  }


  @Override
  public List<String[]> getItemData() {
    return new ArrayList<>(itemData);  
  }
  

  @Override
  public String moveTargetToRoom(String roomName) {
    for (Block room : rooms) {
      if (room.getRoomName().equals(roomName)) {
        if (this.target != null) {
          this.target.move(room);  
          setWorldText();  
          return String.format("Target moved to room: %s", roomName);
        } else {
          throw new IllegalArgumentException("No target set in the world.");
        }
      }
    }
    return String.format("Error: Room with name '%s' not found.", roomName);
  }
  

  @Override
  public String moveTargetToNextRoom() {
    if (this.target != null && this.target.getLocation() != null) {
      int currentRoomId = this.target.getLocation().getRoomId();
      Block nextRoom = null;
      for (Block room : rooms) {
        if (room.getRoomId() == currentRoomId + 1) {
          nextRoom = room;
          break;
        }
      }
  
      if (nextRoom == null) {
        if (rooms.size() > 0) {
          nextRoom = rooms.get(0);  
        }
      }

      if (nextRoom != null) {
        this.target.move(nextRoom);
        setWorldText();
        return String.format("Target moved to next room: %s", nextRoom.getRoomName());
      } else {
        throw new IllegalArgumentException("No rooms available to move the target to.");
      }
    } else {
      throw new IllegalArgumentException("No target set in the world "
          + "or target does not have a current room.");
    }
  }
  
  /**
   * Establishes neighbor relationships among all rooms based on their proximity.
   */
  private void establishRoomNeighbors() {
    for (int i = 0; i < rooms.size(); i++) {
      Block currentRoom = rooms.get(i);
      for (int j = 0; j < rooms.size(); j++) {
        if (i != j) { 
          Block otherRoom = rooms.get(j);
          currentRoom.addNeighbor((Room) otherRoom);
        }
      }
    }
  }
  
  /**
   * Establishes visibility relationships among all rooms based on their line of sight.
   */
  private void establishRoomVisble() {
    for (int i = 0; i < rooms.size(); i++) {
      Block currentRoom = rooms.get(i);
      for (int j = 0; j < rooms.size(); j++) {
        if (i != j) { 
          Block otherRoom = rooms.get(j);
          currentRoom.addVisibleFromRoom((Room) otherRoom);
        }
      }
    }
  }
  
  @Override
  public String getRoomOccupants(Block visibleRoom) {
    StringBuilder occupants = new StringBuilder();
    boolean isOccupied = false;
    if (target != null && target.getLocation().equals(visibleRoom)) {
      occupants.append("Target: ").append(target.getCharacterName()).append("\n");
      isOccupied = true;
    }
    for (CharacterPlayer player : players) {
      if (player.getLocation().equals(visibleRoom)) {
        if (occupants.length() > 0) {
          occupants.append(", ");
        }
        occupants.append("Player: ").append(player.getCharacterName());
        isOccupied = true;
      }
    }
    return isOccupied ? occupants.toString() : "No occupants";
  }
  
  
  /**
   * Creates a new player with a specified name starting in a specified room.
   * 
   * @param playerName The name of the player.
   * @param startRoomIndex The 1-based index of the room where the player should start.
   * @return The new player object.
   * @throws IllegalArgumentException If the room index is out of the valid range.
   */
  public CharacterPlayer createPlayer(String playerName, int startRoomIndex) {
    if (startRoomIndex < 1 || startRoomIndex > rooms.size()) {
      throw new IllegalArgumentException("Invalid room index for player starting room."
          + " Valid index is from 1 to " + rooms.size() + ".");
    }
    Block startRoom = rooms.get(startRoomIndex - 1);
    Player newPlayer = new Player(playerName, startRoom, nextPlayerId++, itemLimit);
    players.add(newPlayer);
    return newPlayer;
  }

  
  @Override
  public void setItemLimit(int newItemLimit) {
    this.itemLimit = newItemLimit;
    for (CharacterPlayer player : players) {
      (player).setItemLimit(newItemLimit);
    }
  }
  
  @Override
  public String displayRoomInfo(String roomName) {
    Block room = getRoomByName(roomName);
    String petRoomName = pet.getLocation().getRoomName();
    if (roomName == petRoomName) {
      return "Pet is currently here; room details are not displayed.";
  }
    String roomInfo = room.getInfo();
    String occupants = getRoomOccupants(room);
    StringBuilder fullInfo = new StringBuilder();
    fullInfo.append(roomInfo).append("\n");
    fullInfo.append("Occupants:\n").append(occupants);
    return fullInfo.toString();
  }
  
  @Override
  public String displayRoomInfo(int roomId) {
    Block room = this.getRoomById(roomId);
    int petRoomId = pet.getLocation().getRoomId();
    if (roomId == petRoomId) {
      return "Pet is currently here; room details are not displayed.";
  }
    String roomInfo = room.getInfo();

    String occupants = getRoomOccupants(room);

    StringBuilder fullInfo = new StringBuilder();
    fullInfo.append(roomInfo).append("\n");
    fullInfo.append("Occupants:\n").append(occupants);

    return fullInfo.toString();
  }
  
  @Override
  public String callCreateRoom(String roomName, int roomId, 
      int[] coordinates, List<String[]> allRoomData) {
    createRoom(roomName, roomId, coordinates, allRoomData);
    return String.format("Room [%s] is created", roomName);
  }


  @Override
  public String callCreateTarget(String name, Block room, int health) {
    createTarget(name, room, health);
    return String.format("Target [%s] is created", name);
  }
  

  @Override
  public String callCreateItem(String name, int location, int murderValue) {
    createItem(name, location, murderValue);
    return String.format("Item [%s] is created", name);
  }
  

  @Override
  public int callCreatePlayer(String playerName, int startRoomIndex) {
    CharacterPlayer player = createPlayer(playerName, startRoomIndex);
    return player.getPlayerId(); 
  }
  

  @Override
  public String getTargetInfo() {
    if (this.target == null) {
      return "No target currently set.";
    }
    return this.target.getCharacterInfo();
  }
  

  @Override
  public String getPlayerInfo(int playerId) {
    for (CharacterPlayer player : players) {
      if (player.getPlayerId() == playerId) {
        return player.getCharacterInfo();
      }
    }
    throw new IllegalArgumentException("Player with ID " + playerId + " not found.");
  }

  
  @Override
  public int getMaxTurn() {
    return maxTurn;
  }

  @Override
  public void setMaxTurn(int maxTurnInput) {
    this.maxTurn = maxTurnInput;
  }
  
  private CharacterPlayer getPlayerById(int playerId) {
    for (CharacterPlayer player : players) {
      if (player.getPlayerId() == playerId) {
        return player;
      }
    }
    throw new IllegalArgumentException("playerID not valid.");
  }

  private Block getRoomById(int roomId) {
    for (Block room : rooms) {
      if (room.getRoomId() == roomId) {
        return room;
      }
    }
    throw new IllegalArgumentException("roomID not valid.");
  }
  
  private Block getRoomByName(String roomName) {
    for (Block room : rooms) {
      if (room.getRoomName().equalsIgnoreCase(roomName)) {
        return room;
      }
    }
    throw new IllegalArgumentException("roomName not valid.");
  }

  @Override
  public String movePlayer(int playerId, int roomId) {
    CharacterPlayer player = getPlayerById(playerId);
    if (player == null) {
      throw new IllegalArgumentException("Player not found.");
    }

    Block targetRoom = getRoomById(roomId);
    if (targetRoom == null) {
      throw new IllegalArgumentException("Target room not found.");
    }

    if (!player.getLocation().getNeighbor().contains(targetRoom)) {
      throw new IllegalArgumentException("Move not allowed. Target room is not a neighbor.");
    }

    player.move(targetRoom);
    return String.format("Player %s moved to room %s.", 
        player.getCharacterName(), targetRoom.getRoomName());
  }

  @Override
  public String playerPickUpItem(int playerId, String itemName) {
    CharacterPlayer player = getPlayerById(playerId);
    if (player == null) {
      throw new IllegalArgumentException("Player not found.");
    }

    Gadget item = player.getLocation().getItem().stream()
        .filter(i -> i.getItemName().equals(itemName))
        .findFirst()
        .orElse(null);

    if (item == null) {
      throw new IllegalArgumentException("Item '" + itemName + "' not found in the room.");
    }

    player.pickItem(item);
    return String.format("Item '%s' picked up successfully by %s.", 
        itemName, player.getCharacterName());
  }

  @Override
  public String playerLookAround(int playerId) {
      CharacterPlayer player = getPlayerById(playerId);
      if (player == null) {
          throw new IllegalArgumentException("Player not found.");
      }
      Block playerRoom = player.getLocation();
      StringBuilder description = new StringBuilder();
      Block petLocation = pet.getLocation();
      if (playerRoom.equals(petLocation)) {
        description.append("You are in the same room as the pet, you can see nothing around.\n");
        return description.toString();
      }
      List<Block> restrictedRooms = new ArrayList<>();
      restrictedRooms.add(petLocation);
      if (petLocation.equals(playerRoom)) {
          description.append("The pet is here, room details are restricted.");
      } else {
          description.append(player.lookAround(restrictedRooms));
          description.append(getRoomOccupants(playerRoom));
      }
      List<Block> visibleRooms = playerRoom.getVisibleFrom();
      if (visibleRooms.isEmpty()) {
          description.append("\nNo visible rooms from your current location.");
      } else {
          description.append("\nVisible rooms from here: ");
          for (Block visibleRoom : visibleRooms) {
              if (petLocation.equals(visibleRoom)) {
                  description.append("\nRoom ").append(visibleRoom.getRoomName()).append(": Presence of pet blocks the view.");
              } else {
                  description.append("\nRoom ").append(visibleRoom.getRoomName()).append(": ");
                  description.append(getRoomOccupants(visibleRoom));
              }
          }
      }
      return description.toString();
  }


  @Override
  public List<String> getRoomItems(int roomId) {
    Block room = getRoomById(roomId); 
    if (room == null) {
      throw new IllegalArgumentException("Room with ID " + roomId + " not found.");
    }
    List<String> itemNames = new ArrayList<>();
    for (Gadget item : room.getItem()) { 
      itemNames.add(item.getItemName()); 
    }
    return itemNames;
  }

  @Override
  public int getPlayerRoomId(int playerId) {
    for (CharacterPlayer player : players) {
      if (player.getPlayerId() == playerId) {
        return player.getLocation().getRoomId();  
      }
    }
    throw new IllegalArgumentException("Player with ID " + playerId + " not found.");
  }

  @Override
  public List<Integer> getNeighborRooms(int roomId) {
    Block room = rooms.get(roomId - 1);
    if (room != null) {
      return room.getNeighbor().stream()
                 .map(Block::getRoomId)
                 .collect(Collectors.toList());
    } else {
      return new ArrayList<>(); 
    }
  }

  @Override
  public String displayPlayerRoomInfo(int playerId) {
    int roomId = getPlayerRoomId(playerId);
    return displayRoomInfo(roomId);
  }
  
  @Override
  public List<String> getPlayerNeighborRoom(int playerId) {
    List<String> neighborDescriptions = new ArrayList<>();
    int roomId = getPlayerRoomId(playerId);
    Block room = getRoomById(roomId);
    if (room == null) {
        neighborDescriptions.add("Room not found for the given player ID.");
        return neighborDescriptions;
    }

    List<Block> neighbors = room.getNeighbor();
    if (neighbors.isEmpty()) {
        neighborDescriptions.add("No neighboring rooms.");
        return neighborDescriptions;
    }

    for (Block block : neighbors) {
        Room neighborRoom = (Room) block; 
        neighborDescriptions.add("Room ID: " + neighborRoom.getRoomId() + ", Room Name: " + neighborRoom.getRoomName());
    }
    return neighborDescriptions;
}
  
  /**
   * Creates a new pet with a specified name starting in a specified room.
   * 
   * @param petName The name of the pet.
   * @param initialRoom The room where the pet should start.
   * @return The new CharacterPet object.
   * @throws IllegalArgumentException If the room index is out of the valid range.
   */
  public CharacterPet createPet(String petName, Block initialRoom) {
    Pet newPet = new Pet(petName, initialRoom);
    this.pet = newPet;
    return newPet;
  }
  
  @Override
  public String callCharacterPet(String petName, Block initialRoom) {
    CharacterPet pet = createPet(petName, initialRoom);
    return pet.getCharacterName();
  }
  
  /**
   * Return the pet object in the model.
   */
  public CharacterPet getPet() {
    return pet;
  }
  
  @Override
  public String getPetInfo() {
      if (this.pet == null) {
          return "No pet currently set.";
      }
      return this.pet.getCharacterInfo();
  }
  
  @Override
  public void initializePetDFS() {
    Block startRoom = pet.getLocation();
    if (startRoom == null) {
        return; 
    }
    Stack<Block> path = new Stack<>();
    Set<Integer> visited = new HashSet<>();
    dfsVisit(startRoom, visited, path);
    pet.setPath(path); 
}
  
  private void dfsVisit(Block room, Set<Integer> visited, Stack<Block> path) {
    visited.add(room.getRoomId());   
    path.push(room);                
    
    if (visited.size() == this.getRoomCount()) {
      return; 
  }

    for (Block neighbor : room.getNeighbor()) {
        if (!visited.contains(neighbor.getRoomId())) {
            dfsVisit(neighbor, visited, path);  
            if (visited.size() == this.getRoomCount()) {
              return; 
          }
            path.push(room); 
        }
    }
}



@Override
public String movePetToNextRoom() {
  if (pet.getPath().isEmpty()) {
    initializePetDFS();
  }
  Block nextRoom = pet.getPath().pop();  
  pet.move(nextRoom);  
  return String.format("Pet moved to room: %s", nextRoom.getRoomName());
}

@Override
public boolean canPlayerBeSeenByAny(int playerId) {
  CharacterPlayer targetPlayer = getPlayerById(playerId);
  if (targetPlayer == null) {
      throw new IllegalArgumentException("Player with ID " + playerId + " does not exist.");
  }
  Block petLocation = pet.getLocation();
  if (petLocation.equals(targetPlayer.getLocation())) {
      return false; 
  }
  for (CharacterPlayer player : players) {
      if (player.getPlayerId() != playerId) {
          if (petLocation.equals(player.getLocation())) {
              continue; 
          }
          if (player.canSee(targetPlayer)) {
              return true;  
          }
      }
  }
  return false;  
}



}
