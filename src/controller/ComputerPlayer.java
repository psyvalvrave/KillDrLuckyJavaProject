package controller;

import java.io.IOException;

public interface ComputerPlayer {

  void executeActions(int playerId) throws InterruptedException, IOException;
  
}
