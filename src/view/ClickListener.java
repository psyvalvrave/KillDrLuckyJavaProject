// File: view/ClickListener.java
package view;

import java.io.IOException;

public interface ClickListener {
    void onPlayerClick(int playerId) throws InterruptedException;
    void onRoomClick(int roomId) throws IOException, InterruptedException;
}
