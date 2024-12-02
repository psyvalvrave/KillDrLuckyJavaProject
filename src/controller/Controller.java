package controller;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import view.FrameView;
import world.ReadOnlyWorld;

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
  void playGame(ReadOnlyWorld world) throws InterruptedException, IOException;
  
  /**
   * Adds a new player to the game at a specified starting room. 
   * The player can be either a human or a computer.
   *
   * @param playerName the name of the player to add.
   * @param roomIndex the index of the starting room for the player.
   * @param isComputer flag indicating whether the player is computer-controlled.
   * @throws IOException if there is an error processing the addition.
   * @throws InterruptedException if the thread is interrupted during the process.
   */
  void addPlayer(String playerName, int roomIndex, 
      boolean isComputer) throws IOException, InterruptedException;

  /**
   * Begins the game, initializing any necessary states or configurations.
   *
   * @throws IOException if an I/O error occurs starting the game.
   * @throws InterruptedException if the thread is interrupted during the start.
   */
  void startGame() throws IOException, InterruptedException;

  /**
   * Loads a new world state into the game controller.
   *
   * @param world the new game world to load.
   * @throws IOException if there is an error loading the world.
   */
  void loadNewWorld(ReadOnlyWorld world) throws IOException;

  /**
   * Sets the output destination for game messages and results.
   *
   * @param output the Appendable to write output messages to.
   */
  void setOutput(Appendable output);

  /**
   * Saves an image representation of the current game world.
   *
   * @return a BufferedImage of the current world state.
   */
  BufferedImage saveWorldImg();

  /**
   * Retrieves the coordinates of all players in the game.
   *
   * @return a map of player IDs to their respective graphical coordinates.
   */
  Map<Integer, Rectangle> getPlayerCoordinates();

  /**
   * Retrieves the coordinates of all rooms in the game world.
   *
   * @return a map of room indices to their respective graphical coordinates.
   */
  Map<Integer, Rectangle> getRoomCoordinates();

  /**
   * Gets the ID of the player whose turn it is currently.
   *
   * @return the current player's ID.
   */
  int getCurrentPlayerId();

  /**
   * Checks if the game is currently running.
   *
   * @return true if the game is running, false otherwise.
   */
  boolean getRunning();

  /**
   * Associates a graphical frame with the game controller for display purposes.
   *
   * @param frame the FrameView to use for displaying the game.
   */
  void setGameFrame(FrameView frame);

  /**
   * Returns a list of items available in the room of a specified player.
   *
   * @param playerId the ID of the player whose room's items are queried.
   * @return a list of item names available in the player's room.
   */
  List<String> passRoomItem(int playerId);

  /**
   * Passes the turn to the next player.
   *
   * @throws InterruptedException if the thread is interrupted during the process.
   * @throws IOException if an I/O error occurs while passing the turn.
   */
  void passTurn() throws InterruptedException, IOException;

  /**
   * Allows a player to look around their current location and outputs the result.
   *
   * @param playerId the ID of the player looking around.
   * @param outputView the Appendable to write output messages to.
   * @return a descriptive string of what the player sees.
   * @throws IOException if there is an error appending the result.
   * @throws InterruptedException if the thread is interrupted during the process.
   * 
   */
  String performLookAround(int playerId, 
      Appendable outputView) throws IOException, InterruptedException;

  /**
   * Enables a player to pick up an item in their current location.
   *
   * @param playerId the ID of the player picking up the item.
   * @param itemName the name of the item to pick up.
   * @param outputView the Appendable to write output messages to.
   * @throws IOException if there is an error during the item pickup.
   * @throws InterruptedException if the thread is interrupted during the process.
   */
  void pickUpItem(int playerId, String itemName, 
      Appendable outputView) throws IOException, InterruptedException;

  /**
   * Displays information about a player.
   *
   * @param playerId the ID of the player whose information is to be displayed.
   * @param outputView the Appendable to write output messages to.
   * @return a descriptive string of the player's current status.
   * @throws InterruptedException if the thread is interrupted during the process.
   * @throws IOException if there is an error displaying the information.
   * 
   */
  String displayPlayerInfo(int playerId, 
      Appendable outputView) throws InterruptedException, IOException;

  /**
   * Moves a specified player to a different room.
   *
   * @param roomId the ID of the room to move the player to.
   * @param outputView the Appendable to write output messages to.
   * @throws IOException if there is an error during the move.
   * @throws InterruptedException if the thread is interrupted during the process.
   */
  void movePlayerToRoom(int roomId, Appendable outputView) throws IOException, InterruptedException;

  /**
   * Retrieves the items currently held by a player.
   *
   * @param playerId the ID of the player whose items are queried.
   * @return a list of names of items the player holds.
   */
  List<String> passPlayerItems(int playerId);

  /**
   * Directs a player to attack a target using a specified item.
   *
   * @param playerId the ID of the player making the attack.
   * @param itemName the item used in the attack.
   * @param outputView the Appendable to write the outcome of the attack to.
   * @throws IOException if there is an error processing the attack.
   * @throws InterruptedException if the thread is interrupted during the attack.
   */
  void attackTargetWithItem(int playerId, String itemName, 
      Appendable outputView) throws IOException, InterruptedException;

  /**
   * Moves a pet owned by a player to a different room.
   *
   * @param playerId the ID of the player whose pet is being moved.
   * @param roomId the ID of the room to move the pet to.
   * @param outputView the Appendable to write the outcome of the move to.
   * @throws IOException if there is an error moving the pet.
   * @throws InterruptedException if the thread is interrupted during the process.
   */
  void movePet(int playerId, int roomId, 
      Appendable outputView) throws IOException, InterruptedException;

  /**
   * Sets the maximum number of turns the game will have.
   *
   * @param turn the maximum number of turns to set.
   */
  void setMaxTurn(int turn);

  /**
   * Checks if the game has reached its end.
   *
   * @return true if the game is over, false otherwise.
   */
  boolean getEnd();

  /**
   * Gets the result of the game.
   *
   * @return a string representing the outcome of the game.
   */
  String getResult();

  /**
   * Sets the game's end status.
   *
   * @param end a boolean indicating whether the game is over.
   */
  void setEnd(boolean end);

  /**
   * Represents a player decision to not pick up any item during their turn.
   *
   * @throws InterruptedException if the thread is interrupted during the process.
   * @throws IOException if there is an error processing the decision.
   */
  void pickNothing() throws InterruptedException, IOException;

}

