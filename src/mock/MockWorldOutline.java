package mock;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import world.Room;
import world.WorldOutline;

/**
 * A mock implementation of the WorldOutline interface for unit testing.
 * This class allows setting expected outcomes and behaviors to simulate
 * different scenarios without interacting with a real game world.
 */
public class MockWorldOutline implements WorldOutline {
  private int roomCount = 3;
  private String movePlayerResult = "Default move result";
  private String roomInfoResult = "Default room info";
  private String lookAroundResult = "Default look around info";
  private String pickItemUpResult = "Default pick item up";
  private int mockId = 0;
  
  /**
   * Sets the number of rooms in the mock world.
   *
   * @param count the number of rooms to set.
   */
  public void setRoomCount(int count) {
    this.roomCount = count;
  }

  /**
   * Sets the result for the playerPickUpItem method.
   *
   * @param result the result to return when playerPickUpItem is called.
   */
  public void setMovePlayerResult(String result) {
    this.movePlayerResult = result;
  }
  
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

  @Override
  public int getRoomCount() {
    return roomCount;
  }

  @Override
  public String movePlayer(int playerId, int roomId) {
    return movePlayerResult;
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
    return lookAroundResult;
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
  public String getRoomOccupants(Room room) {
    return null;
  }

  @Override
  public String callCreateRoom(String roomName, int roomId, int[] coordinates,
      List<String[]> allRoomData) {
    return null;
  }

  @Override
  public String callCreateTarget(String name, Room room, int health) {
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
    return null;
  }

  @Override
  public String getPlayerInfo(int playerId) {
    return null;
  }

  @Override
  public int getMaxTurn() {
    return 0;
  }

  @Override
  public void setMaxTurn(int maxTurn) {
    
  }

  @Override
  public String playerPickUpItem(int playerId, String itemName) {
    return pickItemUpResult;
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
}
