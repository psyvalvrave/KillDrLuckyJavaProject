package mock;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import world.Block;
import world.CharacterPet;
import world.CharacterPlayer;
import world.CharacterTarget;
import world.Gadget;
import world.Room;
import world.WorldOutline;

/**
 * A mock implementation of the WorldOutline interface for unit testing.
 * This class allows setting expected outcomes and behaviors to simulate
 * different scenarios without interacting with a real game world.
 */
public class MockWorldGui implements WorldOutline {
  private int roomCount = 100;
  private String movePlayerResult = "Move result";
  private String roomInfoResult = "room info";
  private String lookAroundResult = "look around info";
  private String pickItemUpResult = "pick item up";
  private int mockId = 0;
  private int id = 0;
  private int turn = 1;
  private boolean mockMurderAttempt = false;
  private int mockTargetHealthPoint = 3;
  private boolean mockRunning = true;
  private String useItem;
  private MockPlayer player = new MockPlayer();
  public boolean containsResult = false;
  
  /**
   * Sets the number of rooms in the mock world.
   *
   * @param count the number of rooms to set.
   */
  public void setRoomCount(int count) {
    this.roomCount = count;
  }
  

  /**
   * Sets the result for the movePlayer method.
   *
   * @param result the result to return when movePlayer is called.
   */
  public void setMovePlayerResult(String result) {
    this.movePlayerResult = result;
  }
  
  /**
   * Sets the result for the playerPickUpItem method.
   *
   * @param result the result to return when playerPickUpItem is called.
   */
  public void setPlayerPickUpItemResult(String result) {
    this.pickItemUpResult = result;
  }

  /**
   * Sets the result for the playerPickUpItem method.
   *
   * @param result the result to return when playerPickUpItem is called.
   */
  public void setRoomInfoResult(String result) {
    this.roomInfoResult = result;
  }

  /**
   * Sets the result for the playerLookAround method.
   *
   * @param result the result to return when playerLookAround is called.
   */
  public void setLookAroundResult(String result) {
    this.lookAroundResult = result;
  }
  
  /**
   * Change the conditional check for murder for test purpose.
   */
  public void toggleMurderAttempt() {
    this.mockMurderAttempt = !this.mockMurderAttempt;
  }
  
  /**
   * Sets the mockTargetHealthPoint for test part. 
   * The game will not just end when we test with Mock. 
   *
   * @param hp the health point for target, this should be > 0.
   */
  public void setTargetHealthPoint(int hp) {
    this.mockTargetHealthPoint = hp;
  }

  @Override
  public int getRoomCount() {
    return roomCount;
  }

  public class MockPlayer {
    public MockLocation location = new MockLocation();
    
    public MockPlayer() {
    }

    public MockLocation getLocation() {
        return location;
    }
}

public class MockLocation {
  public Set<Room> neighbors = new MockNeighbors();
    
    public MockLocation() {
  }

    public Set<Room> getNeighbor() {
        return neighbors;
    }
}

public class MockNeighbors extends HashSet<Room> {
  private static final long serialVersionUID = 1L;
  public MockNeighbors() {
}
  @Override
    public boolean contains(Object o) {
        return containsResult; 
    }
}

  @Override
  public String movePlayer(int playerId, int roomId) {
    if (player.getLocation().getNeighbor().contains(roomId)) {
      return movePlayerResult + " " + roomId;
    } else {
      throw new IllegalArgumentException("Move not allowed. Target room is not a neighbor.");
    }
  }
  
  @Override
  public String displayRoomInfo(String roomName) {
    return null;
  }

  @Override
  public String displayRoomInfo(int roomId) {
    return roomInfoResult;
  }

  @Override
  public String playerLookAround(int playerId) {
    return lookAroundResult + " by Player " + playerId;
  }

  @Override
  public String getWorldText() {
    return null;
  }

  @Override
  public int getItemCount() {
    return 0;
  }

  @Override
  public BufferedImage drawWorld() {
    return null;
  }

  @Override
  public void setWorldText() {
 
  }

  @Override
  public String moveTargetToRoom(String roomName) {
    return null;
  }

  @Override
  public String moveTargetToNextRoom() {
    return null;
  }

  @Override
  public List<String[]> getRoomData() {
    return null;
  }

  @Override
  public List<String[]> getItemData() {
    return null;
  }

  @Override
  public void setItemLimit(int newItemLimit) {    
  }

  @Override
  public String callCreateRoom(String roomName, int roomId, int[] coordinates,
      List<String[]> allRoomData) {
    return null;
  }

  @Override
  public String callCreateTarget(String name, Block room, int health) {
    return null;
  }

  @Override
  public String callCreateItem(String name, int location, int murderValue) {
    return null;
  }

  @Override
  public int callCreatePlayer(String playerName, int startRoomIndex) {
    return mockId++;
  }

  @Override
  public String getTargetInfo() {
    return ("Target: Doctor Lucky is currently in the Drawing Room");
  }

  @Override
  public String getPlayerInfo(int playerId) {
    return "Displaying Player Info " + playerId;
  }

  @Override
  public String playerPickUpItem(int playerId, String itemName) {
    return playerId+ " " + pickItemUpResult + " " + itemName;
  }

  @Override
  public List<String> getRoomItems(int roomId) {
    return Arrays.asList("1", "2", "3");
  }

  @Override
  public int getPlayerRoomId(int playerId) {
    return 0;
  }

  @Override
  public List<Integer> getNeighborRooms(int roomId) {
    return Arrays.asList(1, 2, 3);
  }

  @Override
  public String getRoomOccupants(Block room) {
    return null;
  }
  
  /**
   * Return nothing as list of all items in Mock for testing purpose.
   * 
   * @return Null.
   */
  public List<Gadget> getItems() {
    return null;
  }
  
  /**
   * Return nothing as list of all rooms in Mock for testing purpose.
   *
   * @return Null.
   */
  public List<Block> getRooms() {
    return null;
  }

  @Override
  public String displayPlayerRoomInfo(int playerId) {
    return null;
  }

  @Override
  public List<String> getPlayerNeighborRoom(int playerId) {
    List<String> roomList = new ArrayList<>();
    roomList.add("room1");
    return roomList;
  }

  @Override
  public String callCharacterPet(String petName, Block initialRoom) {
    return null;
  }

  @Override
  public String getPetInfo() {
    return null;
  }

  @Override
  public String movePetToNextRoom() {
    return null;
  }

  @Override
  public void initializePetDfs() {   
  }

  @Override
  public boolean canPlayerBeSeenByAny(int playerId) {
    return false;
  }

  @Override
  public boolean canInteractWithPet(int playerId) {
    return true;
  }

  @Override
  public String movePet(int playerId, int targetRoomId) {
    String movePet = "Pet has been moved by " + playerId + " to room id " + targetRoomId;
    return movePet;
  }

  @Override
  public int getTargetHealthPoint() {
    return mockTargetHealthPoint;
  }

  @Override
  public List<String> getPlayerItems(int playerId) {
    return Arrays.asList();
  }

  @Override
  public String murderAttempt(int playerId) {
    return "Attack is made with " + playerId + " " + useItem;
  }

  @Override
  public void usePlayerItem(int playerId, String itemName) throws IllegalArgumentException {
    useItem = "Item Use: " + itemName;
  }

  @Override
  public boolean canMurderAttempt(int playerId) {
    return mockMurderAttempt;
  }

  @Override
  public void removePet() {
    
  }

  @Override
  public String getPlayerLocation(int playerId) {
    return "Current Room: Test Chamber";
  }

  @Override
  public void usePlayerHighestItem(int playerId) {
    
  }

  @Override
  public String getPlayerItemsInfo(int playerId) {
    return "";
  }

  @Override
  public boolean getIsRunning() {
    return mockRunning;
  }

  @Override
  public void setRunning(boolean running) {
    mockRunning = running;
  }

  @Override
  public int getCurrentTurn() {
    return turn;
  }

  @Override
  public void setCurrentTurn(int currentTurn) {
    this.turn = currentTurn;
  }

  @Override
  public int getMaxTurns() {
    return 3;
  }

  @Override
  public void setMaxTurns(int maxTurns) {
    
  }

  @Override
  public String advanceTurn() {
    this.turn = turn + 1;
    return "next turn";
  }

  @Override
  public String getPlayerName(int playerId) {
    return null;
  }

  @Override
  public int getCurrentPlayerId() {
    int tempId = id;
    id = id + 1;
    return tempId;
  }

  @Override
  public List<Integer> getPlayerIds() {
    List<Integer> list = new ArrayList<>();
    list.add(0);
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);
    return list;
  }

  @Override
  public Map<Integer, String> getPlayerNames() {
    Map<Integer, String> map = new HashMap<>();
    map.put(0, "FirstName");
    map.put(1, "SecondName");
    map.put(2, "ThirdName");
    map.put(3, "ForthName");
    map.put(4, "FifthName");
    return map;
  }

  @Override
  public Map<Integer, Boolean> getIsComputer() {
    Map<Integer, Boolean> map = new HashMap<>();
    map.put(0, false);
    map.put(1, false);
    map.put(2, false);
    map.put(3, false);
    map.put(4, false);
    return map;
  }

  @Override
  public Gadget createItem(String name, int location, int murderValue) {
    return null;
  }

  @Override
  public CharacterTarget getTarget() {
    return null;
  }

  @Override
  public CharacterPet getPet() {
    return null;
  }

  @Override
  public CharacterPet createPet(String petNameInput, Block initialRoom) {
    return null;
  }

  @Override
  public Gadget getItemByName(String itemName) {
    return null;
  }

  @Override
  public CharacterPlayer createPlayer(String playerName, int startRoomIndex) {
    return null;
  }

  @Override
  public Block createRoom(String roomName, int roomId, int[] coordinates,
      List<String[]> allRoomData) {
    return null;
  }

  @Override
  public CharacterTarget createTarget(String name, Block room, int health) {
    return null;
  }

  @Override
  public Map<Integer, Rectangle> getPlayerCoordinates() {
    return null;
  }

  @Override
  public Map<Integer, Rectangle> getRoomCoordinates() {
    return null;
  }

  @Override
  public boolean getIsRunningGui() {
    return false;
  }

  @Override
  public void setRunningGui(boolean running) {
    this.mockRunning = running;
  }
}
