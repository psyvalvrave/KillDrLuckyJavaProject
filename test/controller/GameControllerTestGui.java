package controller;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import mock.MockWorldGui;
import org.junit.Test;

/**
 * Test for Gui Controller.
 */
public class GameControllerTestGui {
  private GameController gameController;
  private StringWriter consoleOutput;
  private FakeRandomNumberGenerator fakeRng;

  @Test
  public void testPlayerAttackGui() throws InterruptedException, IOException {
    consoleOutput = new StringWriter();
    fakeRng = new FakeRandomNumberGenerator();
    int maxTurns = 3; 
    MockWorldGui mockWorld = new MockWorldGui();
    Readable consoleInput = new StringReader("");
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    fakeRng.setNextIntResult(1);
    mockWorld.setRunning(true);
    gameController.loadNewWorld(mockWorld);
    mockWorld.toggleMurderAttempt();
    gameController.attackTargetWithItem(1, "gun", consoleOutput);
    String output = consoleOutput.toString();
    assertTrue(output.contains("Attack is made"));
    assertTrue(output.contains("1"));
    assertTrue(output.contains("Item Use: gun"));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testPlayerMoveGuiExpectException() throws InterruptedException, IOException {
    consoleOutput = new StringWriter();
    fakeRng = new FakeRandomNumberGenerator();
    int maxTurns = 3;
    MockWorldGui mockWorld = new MockWorldGui();
    Readable consoleInput = new StringReader("");
    gameController = new GameController(consoleInput, 
        consoleOutput, fakeRng, maxTurns);

    fakeRng.setNextIntResult(1);
    mockWorld.setRunning(true);
    gameController.loadNewWorld(mockWorld);
    gameController.movePlayerToRoom(1, consoleOutput);
  }
  
  @Test
  public void testPlayerMoveGui_NotNeighbor() throws InterruptedException, IOException {
    consoleOutput = new StringWriter();
    fakeRng = new FakeRandomNumberGenerator();
    int maxTurns = 3; 
    MockWorldGui mockWorld = new MockWorldGui();
    Readable consoleInput = new StringReader("");
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    fakeRng.setNextIntResult(1);
    mockWorld.setRunning(true);
    mockWorld.containsResult = true;
    gameController.loadNewWorld(mockWorld);
    gameController.movePlayerToRoom(1, consoleOutput);
    String output = consoleOutput.toString();
    assertTrue(output.contains("Move result"));
    assertTrue(output.contains("1"));
  }
  
  @Test
  public void testPlayerInfoGui() throws InterruptedException, IOException {
    consoleOutput = new StringWriter();
    fakeRng = new FakeRandomNumberGenerator();
    int maxTurns = 3; 
    MockWorldGui mockWorld = new MockWorldGui();
    Readable consoleInput = new StringReader("");
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    fakeRng.setNextIntResult(1);
    mockWorld.setRunning(true);
    gameController.loadNewWorld(mockWorld);
    gameController.displayPlayerInfo(1, consoleOutput);
    String output = consoleOutput.toString();
    assertTrue(output.contains("Displaying Player Info"));
    assertTrue(output.contains("1"));
  }
  
  @Test
  public void testPlayerPickUpItemGui() throws InterruptedException, IOException {
    consoleOutput = new StringWriter();
    fakeRng = new FakeRandomNumberGenerator();
    int maxTurns = 3; 
    MockWorldGui mockWorld = new MockWorldGui();
    Readable consoleInput = new StringReader("");
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    fakeRng.setNextIntResult(1);
    mockWorld.setRunning(true);
    gameController.loadNewWorld(mockWorld);
    gameController.pickUpItem(1, "blade", consoleOutput);
    String output = consoleOutput.toString();
    assertTrue(output.contains("pick item up"));
    assertTrue(output.contains("1"));
    assertTrue(output.contains("blade"));
  }
  
  @Test
  public void testPlayerLookAroundGui() throws InterruptedException, IOException {
    consoleOutput = new StringWriter();
    fakeRng = new FakeRandomNumberGenerator();
    int maxTurns = 3; 
    MockWorldGui mockWorld = new MockWorldGui();
    Readable consoleInput = new StringReader("");
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    fakeRng.setNextIntResult(1);
    mockWorld.setRunning(true);
    gameController.loadNewWorld(mockWorld);
    mockWorld.toggleMurderAttempt();
    gameController.performLookAround(1, consoleOutput);
    String output = consoleOutput.toString();
    assertTrue(output.contains("look around info"));
    assertTrue(output.contains("Player 1"));
  }
  
  @Test
  public void testPlayerMovePetGui() throws InterruptedException, IOException {
    consoleOutput = new StringWriter();
    fakeRng = new FakeRandomNumberGenerator();
    int maxTurns = 3; 
    MockWorldGui mockWorld = new MockWorldGui();
    Readable consoleInput = new StringReader("");
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    fakeRng.setNextIntResult(1);
    mockWorld.setRunning(true);
    gameController.loadNewWorld(mockWorld);
    mockWorld.toggleMurderAttempt();
    gameController.movePet(1, 99, consoleOutput);
    String output = consoleOutput.toString();
    assertTrue(output.contains("Pet has been moved"));
    assertTrue(output.contains("1"));
    assertTrue(output.contains("room id 99"));
  }
  
  @Test
  public void testGameEndTargetDieGui() throws InterruptedException, IOException {
    consoleOutput = new StringWriter();
    fakeRng = new FakeRandomNumberGenerator();
    int maxTurns = 3; 
    MockWorldGui mockWorld = new MockWorldGui();
    Readable consoleInput = new StringReader("");
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    fakeRng.setNextIntResult(1);
    mockWorld.setRunning(true);
    gameController.loadNewWorld(mockWorld);
    mockWorld.toggleMurderAttempt();
    mockWorld.setTargetHealthPoint(0);
    gameController.passTurn();
    String output = gameController.getResult();
    assertTrue(!mockWorld.getIsRunningGui());
    assertTrue(output.contains("Game over: Target eliminated!"));
    assertTrue(output.contains("Player"));
    assertTrue(output.contains("is the winner"));
  }
  
  @Test
  public void testGameEndMaxTurnGui() throws InterruptedException, IOException {
    consoleOutput = new StringWriter();
    fakeRng = new FakeRandomNumberGenerator();
    int maxTurns = 3; 
    MockWorldGui mockWorld = new MockWorldGui();
    Readable consoleInput = new StringReader("");
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    fakeRng.setNextIntResult(1);
    mockWorld.setRunning(true);
    gameController.loadNewWorld(mockWorld);
    mockWorld.setCurrentTurn(20);
    gameController.passTurn();
    String output = gameController.getResult();
    assertTrue(!mockWorld.getIsRunningGui());
    assertTrue(output.contains("Game over: Maximum number of turns reached!"));
    assertTrue(output.contains("No winner for this game!"));
  }

}
