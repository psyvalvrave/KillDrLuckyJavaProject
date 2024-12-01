package controller;

import java.io.IOException;

public interface PlayerStrategy {

  void executeActions(int playerId) throws InterruptedException, IOException;
  
}
