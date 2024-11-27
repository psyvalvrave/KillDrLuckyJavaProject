package controller;

import java.io.IOException;
import world.WorldOutline;

/**
 * The interface defines the necessary methods for a game controller.
 * This interface allows for a standardized way to interact with game worlds, providing
 * the functionality to initiate and control game play.
 */
public interface Controller {

  /**
   * Initiates and controls the game play for a given game world.
   * This method should handle all necessary game loop mechanics and 
   * manage the game state according to the rules defined in the provided 
   * world implementation.
   *
   * @param world The game world where the game is to be played, providing
   *              the necessary context and environment.
   * @throws InterruptedException if the game thread is interrupted during execution.
   * @throws IOException If an I/O error occurs, such as a failure to 
   *        read data necessary for the game.
   */
  void playGame(WorldOutline world) throws InterruptedException, IOException;

  void addPlayer(String playerName, int roomIndex, boolean isComputer) throws IOException, InterruptedException;
  void startGame();
  void loadNewWorld(Readable source) throws IOException;
  void updateWorldInView();
}
