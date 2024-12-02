package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import view.FrameView;

public class MockGameController extends GameController {
    public String lastMethodCalled;
    public String lastStatus;
    public String lastErrorMessage;
    public String lastPlayerInfo;
    public String lastRoomClickResult;
    public int petRoomId;
    public List<String> roomItemList = new ArrayList<>();
    public List<String> playerItemList = new ArrayList<>();
    private boolean running = false;

    public MockGameController(Readable input, Appendable outputInput, 
        RandomNumberGenerator rngInput, int maxTurnsInput) {
        super(input, outputInput, rngInput, maxTurnsInput);
    }

    @Override
    public void startGame() {
        lastMethodCalled = "startGame";
        running = true;
    }


    @Override
    public void movePlayerToRoom(int roomId, Appendable outputView) {
        lastMethodCalled = "movePlayerToRoom";
        lastRoomClickResult = "Room " + roomId;
    }

    @Override
    public String displayPlayerInfo(int playerId, Appendable outputView) {
        lastMethodCalled = "displayPlayerInfo";
        lastPlayerInfo = "Player " + playerId;
        return lastPlayerInfo;
    }
    
    @Override
    public void pickUpItem(int playerId, String itemName, Appendable outputView) throws IOException, InterruptedException {
      lastMethodCalled = "pickUpItem " + itemName;
    }
    
    @Override
    public String performLookAround(int playerId, Appendable outputView) throws IOException, InterruptedException {
      lastMethodCalled = "performLookAround";
      return lastMethodCalled;
      
    }
    
    @Override
    public void attackTargetWithItem(int playerId, String itemName, Appendable outputView) throws IOException, InterruptedException {
      lastMethodCalled = "attackTargetWithItem " + itemName;
    }
    
    @Override
    public void movePet(int playerId, int roomId, Appendable outputView) throws IOException, InterruptedException {
      petRoomId = roomId;
      lastMethodCalled = "movePet" + roomId;
    }

    @Override
    public int getCurrentPlayerId() {
      return 1;
    }
    
    @Override
    public List<String> passRoomItem(int playerId) {
      return roomItemList;
    }
    
    @Override
    public void passTurn() throws InterruptedException, IOException {
    }
    
    @Override
    public void pickNothing() throws InterruptedException, IOException {
      lastMethodCalled = "pick Nothing";
    }
    
    @Override
    public List<String> passPlayerItems(int playerId){
      return playerItemList;
    }
    
    @Override
    public boolean getRunning() {
        return running;
    }
}
