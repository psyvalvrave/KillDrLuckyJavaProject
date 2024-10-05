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
import javax.imageio.ImageIO;

/**
 * Represents the game world, managing rooms, items, and the target character.
 * The world is initialized from a specified file.
 */
public class World implements WorldOutline {
  private List<Room> rooms;
  private List<Item> items;
  private Target target;
  private String worldText;
  private int rows;
  private int cols;
  private List<String[]> roomData;
  private List<String[]> itemData;
  private String targetName;
  private int targetHealth = 0;
  private String worldName;

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
              if (parts.length < 5) {  // Ensure there are enough parts to avoid index errors
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
      
      // Process the saved room and item data
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
      createRoom(roomName, roomIdWrite - 1, intCoords, roomDataInput);
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

  @Override
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
  @Override
  public Item createItem(String name, int location, int murderValue) {
    if (location < 0 || location >= rooms.size()) {
      throw new IllegalArgumentException("Invalid room location index: " + location);
    }
    Room room = rooms.get(location);
    Item newItem = new Item(name, room, murderValue);
    items.add(newItem);
    room.addItem(newItem); // Assuming Room has a method to add an item
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
  @Override
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
  @Override
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
  public void drawWorld() {
    BufferedImage image = new BufferedImage(this.cols * 25, 
        this.rows * 25, BufferedImage.TYPE_INT_RGB);
    Graphics g = image.createGraphics();

    g.setColor(Color.WHITE);
    g.fillRect(0, 0, this.cols * 25, this.rows * 25); // Fill background

    Font font = new Font("Arial", Font.PLAIN, 9);
    g.setFont(font);

    for (String[] room : roomData) {
      String roomName = room[0];
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
        g.drawString(roomName, x1Draw + 3, y1Draw + (height / 2) + 5); 
      }
      if (target != null && target.getLocation() != null 
          && roomName.equals(target.getLocation().getRoomName())) {
        g.drawString("Target Here", x1Draw + 3, y1Draw + (height / 2) + 20); 
      }
    }

    g.dispose(); 

    try {
      File outputfile = new File("res/world.png");
      ImageIO.write(image, "png", outputfile);
    } catch (IOException e) {
      System.out.println("Error saving world image: " + e.getMessage());
    }
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
    return new ArrayList<>(roomData);  // Returns a copy to protect internal data
  }

  /**
   * Retrieves a list of item data arrays that represent the item details.
   * This method returns a copy of the item data to prevent 
   * external modifications to the internal state.
   * 
   * @return A new list of item data arrays, each containing details of an item.
   */
  public List<String[]> getItemData() {
    return new ArrayList<>(itemData);  // Returns a copy to protect internal data
  }
  
  /**
   * Moves the target to a specified room by room name.
   * 
   * @param roomName The name of the room to move the target to.
   */
  public void moveTargetToRoom(String roomName) {
    for (Room room : rooms) {
      if (room.getRoomName().equals(roomName)) {
        if (this.target != null) {
          this.target.move(room);  
          setWorldText();  
        } else {
          System.out.println("No target set in the world.");
        }
        return;  
      }
    }
  }
  
  /**
   * Moves the target to the next room in sequence or wraps around 
   * to the first room if the end is reached.
   */
  public void moveTargetToNextRoom() {
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
      } else {
        System.out.println("No rooms available to move the target to.");
      }
    } else {
      System.out.println("No target set in the world or target does not have a current room.");
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
}

