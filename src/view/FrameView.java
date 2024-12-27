package view;

import controller.Controller;
import java.io.IOException;

/**
 * Interface defining the required methods for a frame view in a game application. This interface
 * ensures that implementing classes provide functionalities to refresh the game world display and
 * handle player and room interactions.
 */
public interface FrameView {
  
  /**
   * Refreshes the display of the game world based on the current state managed 
   * by the provided game controller.
   *
   * @param gameController the controller that manages game state and logic.
   */
  void refreshWorldDisplay(Controller gameController);
  
  /**
   * Handles user interactions when a player is clicked within the game interface.
   *
   * @param gameController the controller handling game logic.
   * @param playerId the identifier of the player that was clicked.
   * @throws InterruptedException if the thread is interrupted during execution.
   */
  void onPlayerClick(Controller gameController, int playerId) throws InterruptedException;
  
  /**
   * Handles user interactions when a room is clicked within the game interface.
   *
   * @param gameController the controller handling game logic.
   * @param roomId the identifier of the room that was clicked.
   * @throws IOException if an I/O error occurs during interaction handling.
   * @throws InterruptedException if the thread is interrupted during execution.
   */
  void onRoomClick(Controller gameController, int roomId) throws IOException, InterruptedException;
  
  /**
   * Retrieves the panel that displays the world within the game interface.
   *
   * @return a {@code WorldPanel} that visually represents the game world.
   */
  WorldPanel getWorldPanel();
}

