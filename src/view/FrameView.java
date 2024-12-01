package view;

import java.io.IOException;

import controller.Controller;

public interface FrameView {
  void refreshWorldDisplay(Controller gameController);
  void onPlayerClick(Controller gameController, int playerId) throws InterruptedException;
  void onRoomClick(Controller gameController, int roomId) throws IOException, InterruptedException;
}

