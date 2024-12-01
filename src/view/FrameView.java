package view;

import java.io.IOException;

public interface FrameView {
  void onPlayerClick(int playerId) throws InterruptedException;
  void onRoomClick(int roomId) throws IOException, InterruptedException;
  void refreshWorldDisplay();
}

