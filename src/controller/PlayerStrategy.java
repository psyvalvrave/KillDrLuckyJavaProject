package controller;

import java.io.IOException;

/**
 * Represents a strategy pattern for player actions within a game. This interface
 * defines the contract for implementing different strategies that determine how
 * a player executes actions based on their role and current state within the game.
 *
 * Implementations of this interface should encapsulate all logic necessary for
 * making decisions and performing actions during a player's turn, including handling
 * any necessary exceptions that might arise during action execution.
 */
public interface PlayerStrategy {
  
  /**
   * Executes the actions for a player based on the strategy implemented. This method
   * is responsible for all the operations a player might perform during their turn,
   * such as moving, picking up items, or interacting with other players or game elements.
   *
   * @param playerId The identifier of the player whose actions are to be executed.
   * @throws InterruptedException If the thread running the strategy is interrupted.
   * @throws IOException If an input or output exception occurs during action execution.
   */
  void executeActions(int playerId) throws InterruptedException, IOException;
  
}
