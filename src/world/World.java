package world;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

/**
 * Represents the game world, managing rooms, items, and the target character.
 * The world is initialized from a specified file.
 */
public class World implements WorldOutline {
  private List<Room> rooms;
  private List<Item> items;
  private List<Player> players;
  private Target target;
  private String worldText;
  private int rows;
  private int cols;
  private List<String[]> roomData;
  private List<String[]> itemData;
  private String targetName;
  private int targetHealth = 0;
  private String worldName;
  private int itemLimit = 3;
  private int nextPlayerId = 0;
  private int maxTurn;

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
          case 1:  // Read target health and name
            String[] targetDetails = line.split("\\s+", 2);
            targetHealth = Integer.parseInt(targetDetails[0]);
            targetName = targetDetails.length > 1 ? targetDetails[1] : "";
            state = 2;
            break;
          case 2:  // Read room count
            roomCount = Integer.parseInt(line);
            state = 3;
            break;
          case 3:  // Room details
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
                state = 4;
              }
            }
            break;
          case 4:  // Read item count
            itemCount = Integer.parseInt(line);
            state = 5;
            break;
          case 5:  // Item details
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

  public Target getTarget() {
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
  public Item createItem(String name, int location, int murderValue) {
    if (location < 0 || location >= rooms.size()) {
      throw new IllegalArgumentException("Invalid room location index: " + location);
    }
    Room room = rooms.get(location);
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
  public Target createTarget(String name, Room room, int health) {
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
  public Room createRoom(String roomName, int roomId, 
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


  /**
   * Draws the world to an image file.
   */
  public BufferedImage drawWorld() {
    BufferedImage image = new BufferedImage(this.cols * 25, this.rows * 25, BufferedImage.TYPE_INT_RGB);
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

        if (target != null && target.getLocation() != null && roomName.equals(target.getLocation().getRoomName())) {
            g.setColor(Color.RED);
            g.fillOval(x1Draw + 10, y1Draw + 10, 10, 10); // Red dot for the target
            g.drawString("Target: " + target.getCharacterName(), x1Draw + 25, y1Draw + 15);
        }
        int playerOffset = 0;
        for (Player player : players) {
          if (player.getLocation() != null && roomName.equals(player.getLocation().getRoomName())) {
              g.setColor(Color.BLACK);
              g.fillOval(x1Draw + 10, y1Draw + 30 + playerOffset, 10, 10); 
              g.drawString("Player: " + player.getCharacterName(), x1Draw + 25, y1Draw + 35 + playerOffset);

              playerOffset += 20; 
          }
      }
    }

    g.dispose();

    try {
        File outputfile = new File("../res/world.png");
        ImageIO.write(image, "png", outputfile);
    } catch (IOException e) {
        throw new IllegalArgumentException("Error saving world image: " + e.getMessage());
    }
    
    return image;
}

  /**
   * Generates and update the world text with detailed information about the world.
   */
  public void setWorldText() {
    this.worldText = String.format("World Name: %s\n" 
  + "World Dimensions: %dx%d\n" 
                                   + "Number of Rooms: %d\n" 
                                   + "Number of Items: %d\n" 
                                   + "Target: %s\n" 
                                   + "Target's Health: %d\n" 
                                   + "Target's Location: %s",
                                   this.worldName, this.cols,
                                   this.rows, getRoomCount(), getItemCount(),
                                   (target != null ? target.getCharacterName() : "No target"),
                                   (target != null ? target.getHealthPoint() : 0),
                                   (target != null && target.getLocation() != null 
                                   ? target.getLocation().getRoomName() : "Unknown"));
  }
  
  /**
   * Gets the current descriptive text of the world.
   * 
   * @return A string summarizing the current state of the world.
   */
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
  public List<Room> getRooms() {
    return new ArrayList<>(rooms); 
  }

  /**
   * Retrieves a list of all items in the world.
   * This method provides a safe copy of the items list to 
   * ensure that the internal list is not modified.
   * 
   * @return A new list containing all the items currently in the world.
   */
  public List<Item> getItems() {
    return new ArrayList<>(items); 
  }
  
  /**
   * Retrieves a list of room data arrays that represent the room details.
   * This method returns a copy of the room data to prevent 
   * external modifications to the internal state.
   * 
   * @return A new list of room data arrays, each containing details of a room.
   */
  public List<String[]> getRoomData() {
    return new ArrayList<>(roomData);  
  }

  /**
   * Retrieves a list of item data arrays that represent the item details.
   * This method returns a copy of the item data to prevent 
   * external modifications to the internal state.
   * 
   * @return A new list of item data arrays, each containing details of an item.
   */
  public List<String[]> getItemData() {
    return new ArrayList<>(itemData);  
  }
  
  /**
   * Moves the target to a specified room by room name.
   * 
   * @param roomName The name of the room to move the target to.
   */
  public String moveTargetToRoom(String roomName) {
    for (Room room : rooms) {
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
  
  /**
   * Moves the target to the next room in sequence or wraps around 
   * to the first room if the end is reached.
   */
  public String moveTargetToNextRoom() {
    if (this.target != null && this.target.getLocation() != null) {
      int currentRoomId = this.target.getLocation().getRoomId();
      Room nextRoom = null;
      for (Room room : rooms) {
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
      throw new IllegalArgumentException("No target set in the world or target does not have a current room.");
    }
  }
  
  /**
   * Establishes neighbor relationships among all rooms based on their proximity.
   */
  private void establishRoomNeighbors() {
    for (int i = 0; i < rooms.size(); i++) {
      Room currentRoom = rooms.get(i);
      for (int j = 0; j < rooms.size(); j++) {
        if (i != j) { 
          Room otherRoom = rooms.get(j);
          currentRoom.addNeighbor(otherRoom);
        }
      }
    }
  }
  
  /**
   * Establishes visibility relationships among all rooms based on their line of sight.
   */
  private void establishRoomVisble() {
    for (int i = 0; i < rooms.size(); i++) {
      Room currentRoom = rooms.get(i);
      for (int j = 0; j < rooms.size(); j++) {
        if (i != j) { 
          Room otherRoom = rooms.get(j);
          currentRoom.addVisibleFromRoom(otherRoom);
        }
      }
    }
  }
  
  /**
   * Returns detailed information about which players and whether the target is in the specified room.
   * @param room The room to check for occupancy details.
   * @return A descriptive string of all occupants in the room.
   */
  public String getRoomOccupants(Room room) {
      StringBuilder occupants = new StringBuilder();
      boolean isOccupied = false;

      if (target != null && target.getLocation().equals(room)) {
          occupants.append("Target: ").append(target.getCharacterName()).append("\n");
          isOccupied = true;
      }

      for (Player player : players) {
          if (player.getLocation().equals(room)) {
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
   * @param playerName The name of the player.
   * @param startRoomIndex The 1-based index of the room where the player should start.
   * @return The new player object.
   * @throws IllegalArgumentException If the room index is out of the valid range.
   */
  public Player createPlayer(String playerName, int startRoomIndex) {
      if (startRoomIndex < 1 || startRoomIndex > rooms.size()) {
          throw new IllegalArgumentException("Invalid room index for player starting room. Valid index is from 1 to " + rooms.size() + ".");
      }
      Room startRoom = rooms.get(startRoomIndex - 1);
      Player newPlayer = new Player(playerName, startRoom, nextPlayerId++, itemLimit);
      players.add(newPlayer);
      return newPlayer;
  }

  
  /**
   * Setter for item limit. Updates the item limit for all players in the world.
   * 
   * @param newItemLimit The new item limit to be set for all players.
   */
  public void setItemLimit(int newItemLimit) {
      this.itemLimit = newItemLimit;
      for (Player player : players) {
          player.setItemLimit(newItemLimit);
      }
  }
  
  /**
   * Displays the detailed information of a specific room by name, including the room's
   * properties (name, ID, coordinates, neighbors, visible rooms, items) and the occupants (players and target).
   * 
   * @param roomName The name of the room to display.
   * @return A string containing the room information and occupants.
   * @throws IllegalArgumentException if the room name does not exist.
   */
  public String displayRoomInfo(String roomName) {
      Room room = getRoomByName(roomName);
      
      String roomInfo = room.getInfo();
      String occupants = getRoomOccupants(room);

      StringBuilder fullInfo = new StringBuilder();
      fullInfo.append(roomInfo).append("\n");
      fullInfo.append("Occupants:\n").append(occupants);

      return fullInfo.toString();
  }
  
  /**
   * Displays the detailed information of a specific room, including the room's
   * properties (name, ID, coordinates, neighbors, visible rooms, items) and the occupants (players and target).
   * 
   * @param roomId The ID of the room to display.
   * @return A string containing the room information and occupants.
   */
  public String displayRoomInfo(int roomId) {
      Room room = this.getRoomById(roomId);

      String roomInfo = room.getInfo();

      String occupants = getRoomOccupants(room);

      StringBuilder fullInfo = new StringBuilder();
      fullInfo.append(roomInfo).append("\n");
      fullInfo.append("Occupants:\n").append(occupants);

      return fullInfo.toString();
  }
  
  /**
   * Creates a room in the game world with specified properties.
   * 
   * @param roomName The name of the room.
   * @param roomId The identifier for the room.
   * @param coordinates The spatial coordinates of the room.
   * @param allRoomData Additional data for room configurations.
   * @return A confirmation message stating the room has been created.
   */
  @Override
  public String callCreateRoom(String roomName, int roomId, int[] coordinates, List<String[]> allRoomData) {
    createRoom(roomName, roomId, coordinates, allRoomData);
    return String.format("Room [%s] is created", roomName);
  }

  /**
   * Creates a target character in the game world.
   * @param name The name of the target.
   * @param room The starting room for the target.
   * @param health The initial health points for the target.
   * @return A confirmation message stating the target has been created.
   */
  @Override
  public String callCreateTarget(String name, Room room, int health) {
    createTarget(name, room, health);
    return String.format("Target [%s] is created", name);
  }
  
  /**
   * Creates an item and places it within a specified room.
   * @param name The name of the item.
   * @param location The index of the room where the item is placed.
   * @param murderValue The potential damage or effect of the item.
   * @return A confirmation message stating the item has been created.
   */
  @Override
  public String callCreateItem(String name, int location, int murderValue) {
    createItem(name, location, murderValue);
    return String.format("Item [%s] is created", name);
  }
  
  /**
   * Creates a player in the game world.
   * @param playerName The name of the player.
   * @param startRoomIndex The starting room index for the player.
   * @return A confirmation message stating the player has been created.
   */
  @Override
  public int callCreatePlayer(String playerName, int startRoomIndex) {
    Player player = createPlayer(playerName, startRoomIndex);
    return player.getPlayerId(); 
  }
  
  /**
   * Retrieves detailed information about the current target in the game.
   * @return Information about the target, otherwise a notification that no target is set.
   */
  @Override
  public String getTargetInfo() {
      if (this.target == null) {
          return "No target currently set.";
      }
      return this.target.getCharacterInfo();
  }
  
  /**
   * Retrieves detailed information about a player identified by their ID.
   * @param playerId The unique identifier of the player.
   * @return Information about the player.
   * @throws IllegalArgumentException if no player with the given ID is found.
   */
  @Override
  public String getPlayerInfo(int playerId) {
      for (Player player : players) {
          if (player.getPlayerId() == playerId) {
              return player.getCharacterInfo();
          }
      }
      throw new IllegalArgumentException("Player with ID " + playerId + " not found.");
  }

  
  /**
   * Gets the maximum allowed turns for the game.
   * 
   * @return The maximum allowed turns.
   */
  public int getMaxTurn() {
      return maxTurn;
  }

  /**
   * Sets the maximum allowed turns for the game.
   * 
   * @param maxTurn The maximum turns to set.
   */
  public void setMaxTurn(int maxTurn) {
      this.maxTurn = maxTurn;
  }
  
  private Player getPlayerById(int playerId) {
    for (Player player : players) {
        if (player.getPlayerId() == playerId) {
            return player;
        }
    }
    throw new IllegalArgumentException("playerID not valid.");
}

  private Room getRoomById(int roomId) {
    for (Room room : rooms) {
        if (room.getRoomId() == roomId) {
            return room;
        }
    }
    throw new IllegalArgumentException("roomID not valid.");
}
  
  private Room getRoomByName(String roomName) {
    for (Room room : rooms) {
        if (room.getRoomName().equalsIgnoreCase(roomName)) {
            return room;
        }
    }
    throw new IllegalArgumentException("roomName not valid.");
}

/**
 * Moves a player to a specified room.
 *
 * @param playerId The ID of the player to move.
 * @param roomId The ID of the room to move the player to.
 * @throws IllegalArgumentException If input parameters are invalid.
 * @return A message indicating success or the reason for failure.
 */
@Override
public String movePlayer(int playerId, int roomId) {
    Player player = getPlayerById(playerId);
    if (player == null) {
        throw new IllegalArgumentException("Player not found.");
    }

    Room targetRoom = getRoomById(roomId);
    if (targetRoom == null) {
        throw new IllegalArgumentException("Target room not found.");
    }

    if (!player.getLocation().getNeighbor().contains(targetRoom)) {
        throw new IllegalArgumentException("Move not allowed. Target room is not a neighbor.");
    }

    player.move(targetRoom);
    return String.format("Player %s moved to room %s.", player.getCharacterName(), targetRoom.getRoomName());
}

/**
 * Allows a player to pick up an item from their current location.
 *
 * @param playerId The ID of the player picking up the item.
 * @param itemName The name of the item to pick up.
 * @throws IllegalArgumentException If input parameters are invalid or the action is not allowed.
 * @return A message indicating success or the reason for failure.
 */
@Override
public String playerPickUpItem(int playerId, String itemName) {
    Player player = getPlayerById(playerId);
    if (player == null) {
        throw new IllegalArgumentException("Player not found.");
    }

    Item item = player.getLocation().getItem().stream()
        .filter(i -> i.getItemName().equals(itemName))
        .findFirst()
        .orElse(null);

    if (item == null) {
        throw new IllegalArgumentException("Item '" + itemName + "' not found in the room.");
    }

    player.pickItem(item);
    return String.format("Item '%s' picked up successfully by %s.", itemName, player.getCharacterName());
}

/**
 * Provides information about the current room and visible areas from it.
 *
 * @param playerId The ID of the player looking around.
 * @return A string describing the surroundings.
 * @throws IllegalArgumentException If the player ID is not found.
 * @return A message indicating success or the reason for failure.
 */
@Override
public String playerLookAround(int playerId) {
    Player player = getPlayerById(playerId);
    if (player == null) {
        throw new IllegalArgumentException("Player not found.");
    }

    return player.lookAround();
}

/**
 * Retrieves a list of item names from a specific room identified by its room ID.
 * @param roomId The ID of the room whose items are to be listed.
 * @return A list containing the names of items in the specified room.
 * @throws IllegalArgumentException if the room ID does not correspond to an existing room.
 */
@Override
public List<String> getRoomItems(int roomId) {
    Room room = getRoomById(roomId); // This uses your existing method to find a room by ID.
    if (room == null) {
        throw new IllegalArgumentException("Room with ID " + roomId + " not found.");
    }
    List<String> itemNames = new ArrayList<>();
    for (Item item : room.getItem()) { 
        itemNames.add(item.getItemName()); 
    }
    return itemNames;
}

/**
 * Retrieves the room ID where the specified player is currently located.
 *
 * @param playerId The unique identifier of the player.
 * @return The room ID where the player is located, or throws an exception if not found.
 */
@Override
public int getPlayerRoomId(int playerId) {
    for (Player player : players) {
        if (player.getPlayerId() == playerId) {
            return player.getLocation().getRoomId();  
        }
    }
    throw new IllegalArgumentException("Player with ID " + playerId + " not found.");
}

/**
 * Retrieves the all room ID as list where the specified room.
 *
 * @param roomId The unique identifier of the room.
 * @return The room ID of all the neighbors for certain room, or throws an exception if not found.
 */
public List<Integer> getNeighborRooms(int roomId) {
  Room room = rooms.get(roomId-1);
  if (room != null) {
      return room.getNeighbor().stream()
                 .map(Room::getRoomId)
                 .collect(Collectors.toList());
  } else {
      return new ArrayList<>(); 
  }
}



}
