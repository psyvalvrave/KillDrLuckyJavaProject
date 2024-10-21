package controller;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import mock.MockWorldOutline;
import org.junit.Test;

/**
 * Test file for controller test with monk model. 
 */
public class GameControllerMonkTest {
  private GameController gameController;
  private StringWriter consoleOutput;
  private FakeRandomNumberGenerator fakeRng;
  
  @Test
  public void testEndGameWithMaxTurn() throws InterruptedException, IOException {
    String simulatedUserInput = "1\nHumanPlayer\n1\n2\nComputerPlayer\n1\n4\n7\n7\n7\n7\n7\n"; 
    Readable consoleInput = new StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();
    fakeRng = new FakeRandomNumberGenerator();
    int maxTurns = 2; 
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    MockWorldOutline mockWorld = new MockWorldOutline();

    mockWorld.setLookAroundResult("You see a distant mountain");

    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);

    fakeRng.setNextIntResult(1);

    gameController.playGame(mockWorld);

    String output = consoleOutput.toString();

    System.out.println(output);
    assertTrue(output.contains("Maximum turns reached. Ending game."));
    assertTrue(output.contains("Game over: Maximum number of turns reached."));

  }

  @Test
  public void testEachPlayerMoveCostTurn() throws InterruptedException, IOException {
    String simulatedUserInput = "1\nHumanPlayer\n1\n2\nComputerPlayer\n1\n4\n5\n1\n"; 
    Readable consoleInput = new StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();
    fakeRng = new FakeRandomNumberGenerator();
    int maxTurns = 3; 
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    MockWorldOutline mockWorld = new MockWorldOutline();

    mockWorld.setMovePlayerResult("Player moved");

    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);

    fakeRng.setNextIntResult(1);

    gameController.playGame(mockWorld);

    String output = consoleOutput.toString();

    assertTrue(output.contains("Player moved"));
    assertTrue(output.contains("Turn 2. Now Player ComputerPlayer"));
    assertTrue(output.contains("Start Picking Item Up"));
    assertTrue(output.contains("Computer player: Default pick item up"));

  }
  
  @Test
  public void testEachPlayerPickUpCostTurn() throws InterruptedException, IOException {
    String simulatedUserInput = "1\nHumanPlayer\n1\n2\nComputerPlayer\n1\n4\n6\n1\n"; 
    Readable consoleInput = new StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();
    fakeRng = new FakeRandomNumberGenerator();
    int maxTurns = 3; 
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    MockWorldOutline mockWorld = new MockWorldOutline();

    mockWorld.setPlayerPickUpItemResult("Sword picked up");

    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);

    fakeRng.setNextIntResult(1);

    gameController.playGame(mockWorld);

    String output = consoleOutput.toString();

    assertTrue(output.contains("Sword picked up"));
    assertTrue(output.contains("Turn 2. Now Player ComputerPlayer"));
    assertTrue(output.contains("Start Picking Item Up"));
    assertTrue(output.contains("Computer player: Sword picked up"));

  }
  
  @Test
  public void testEachPlayerLookAroundCostTurn() throws InterruptedException, IOException {
    String simulatedUserInput = "1\nHumanPlayer\n1\n2\nComputerPlayer\n1\n4\n7\n1\n"; 
    Readable consoleInput = new StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();
    fakeRng = new FakeRandomNumberGenerator();
    int maxTurns = 3; 
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    MockWorldOutline mockWorld = new MockWorldOutline();

    mockWorld.setLookAroundResult("You see a distant mountain");

    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);

    fakeRng.setNextIntResult(1);

    gameController.playGame(mockWorld);

    String output = consoleOutput.toString();

    assertTrue(output.contains("You see a distant mountain"));
    assertTrue(output.contains("Turn 2. Now Player ComputerPlayer"));
    assertTrue(output.contains("Start Picking Item Up"));
    assertTrue(output.contains("Computer player: Default pick item up"));

  }
  
  @Test
  public void testTurnSwitchBetweenPlayer() throws InterruptedException, IOException {
    String simulatedUserInput = "1\nHumanPlayer\n1\n2\nComputerPlayer\n1\n4\n7\n7\n7\n7\n7\n"; 
    Readable consoleInput = new StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();
    fakeRng = new FakeRandomNumberGenerator();
    int maxTurns = 5; 
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    MockWorldOutline mockWorld = new MockWorldOutline();

    mockWorld.setLookAroundResult("You see a distant mountain");

    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);

    fakeRng.setNextIntResult(1);

    gameController.playGame(mockWorld);

    String output = consoleOutput.toString();

    assertTrue(output.contains("Player ID 0 Player Name HumanPlayer"));
    assertTrue(output.contains("Turn 2. Now Player ComputerPlayer"));
    assertTrue(output.contains("Turn 3. Now Player HumanPlayer with ID 0's turn"));

  }
  
}
