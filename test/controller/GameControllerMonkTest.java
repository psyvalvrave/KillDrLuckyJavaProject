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
    MockWorldOutline mockWorld = new MockWorldOutline();

    mockWorld.setLookAroundResult("You see a distant mountain");

    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);

    fakeRng.setNextIntResult(1);

    gameController.playGame(mockWorld);

    String output = consoleOutput.toString();
    assertTrue(output.contains("Maximum turns reached. Ending game."));
    assertTrue(output.contains("Game over: Maximum number of turns reached!"));

  }
  
  @Test
  public void testEndGameWithTargetDie() throws InterruptedException, IOException {
    String simulatedUserInput = "1\nHumanPlayer\n1\n4\n3\n0\n"; 
    Readable consoleInput = new StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();
    fakeRng = new FakeRandomNumberGenerator();
    int maxTurns = 20; 
    MockWorldOutline mockWorld = new MockWorldOutline();
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    mockWorld.toggleMurderAttempt();
    fakeRng.setNextIntResult(1);
    gameController.playGame(mockWorld);
    String output = consoleOutput.toString();
    assertTrue(output.contains("Target eliminated."));
    assertTrue(output.contains("Game Over!"));
  }

  @Test
  public void testEachPlayerMoveCostTurn() throws InterruptedException, IOException {
    String simulatedUserInput = "1\nHumanPlayer\n1\n2\nComputerPlayer\n3\n4\n5\n1\n0\n"; 
    Readable consoleInput = new StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();
    fakeRng = new FakeRandomNumberGenerator();
    int maxTurns = 3; 
    MockWorldOutline mockWorld = new MockWorldOutline();
    mockWorld.setMovePlayerResult("Player moved");
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    fakeRng.setNextIntResult(1);
    gameController.playGame(mockWorld);
    String output = consoleOutput.toString();
    assertTrue(output.contains("Player moved"));
    assertTrue(output.contains("Turn 2"));
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
  public void testEachPlayerMovePetCostTurn() throws InterruptedException, IOException {
    String simulatedUserInput = "1\nHumanPlayer\n1\n2\nComputerPlayer\n1\n4\n4\n1\n"; 
    Readable consoleInput = new StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();
    fakeRng = new FakeRandomNumberGenerator();
    int maxTurns = 3; 
    MockWorldOutline mockWorld = new MockWorldOutline();
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    fakeRng.setNextIntResult(1);
    gameController.playGame(mockWorld);
    String output = consoleOutput.toString();
    assertTrue(output.contains("Pet has been moved by"));
    assertTrue(output.contains("to room id"));
    assertTrue(output.contains("Turn 2. Now Player ComputerPlayer"));
    assertTrue(output.contains("Start Picking Item Up"));
    assertTrue(output.contains("Computer player: Default pick item up"));
  }
  
  @Test
  public void testEachPlayerMurderAttemptCostTurn() throws InterruptedException, IOException {
    String simulatedUserInput = "1\nHumanPlayer\n1\n2\nComputerPlayer\n2\n4\n3\n0\n0\n"; 
    Readable consoleInput = new StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();
    fakeRng = new FakeRandomNumberGenerator();
    int maxTurns = 3; 
    MockWorldOutline mockWorld = new MockWorldOutline();
    mockWorld.toggleMurderAttempt();
    mockWorld.setTargetHealthPoint(1);
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    fakeRng.setNextIntResult(1);
    gameController.playGame(mockWorld);
    String output = consoleOutput.toString();
    System.out.println(output);
    assertTrue(output.contains("Attack is made!"));
    assertTrue(output.contains("Turn 2. Now Player ComputerPlayer"));
  }
  
  @Test
  public void testTurnSwitchBetweenPlayer() throws InterruptedException, IOException {
    String simulatedUserInput = "1\nHumanPlayer\n1\n2\nComputerPlayer\n1\n4\n7\n7\n7\n7\n7\n"; 
    Readable consoleInput = new StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();
    fakeRng = new FakeRandomNumberGenerator();
    int maxTurns = 5; 
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
  
  @Test
  public void testTargetInfoCommandShowsTargetInfo() throws InterruptedException, IOException {
      String simulatedUserInput = "1\nHumanPlayer\n1\n2\nComputerPlayer\n1\n4\n10\n0\n";
      Readable consoleInput = new StringReader(simulatedUserInput);
      StringWriter consoleOutput = new StringWriter();
      FakeRandomNumberGenerator fakeRng = new FakeRandomNumberGenerator();
      int maxTurns = 10; 
      GameController gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
      MockWorldOutline mockWorld = new MockWorldOutline();
      gameController.playGame(mockWorld);
      String output = consoleOutput.toString();
      assertTrue("The output should contain the target information.",
                 output.contains("Target: Doctor Lucky is currently in the Drawing Room"));
  }
  
  
  @Test
  public void testPlayerLocationDisplayedAtTurnStart() throws IOException, InterruptedException {
    String simulatedUserInput = "1\nHumanPlayer\n1\n2\nComputerPlayer\n1\n4\n10\n0\n";
    Readable consoleInput = new StringReader(simulatedUserInput);
    StringWriter consoleOutput = new StringWriter();
    FakeRandomNumberGenerator fakeRng = new FakeRandomNumberGenerator();
    int maxTurns = 10; 
    GameController gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    MockWorldOutline mockWorld = new MockWorldOutline();
      gameController.playGame(mockWorld);
      assertTrue("The output should contain the player's location",
                 consoleOutput.toString().contains("Current Room: Test Chamber"));
  }
  
  @Test
  public void testMovePetCommand() throws InterruptedException, IOException {
      String simulatedUserInput = "1\nHumanPlayer\n1\n2\nComputerPlayer\n1\n4\n4\n2\n0\n";
      Readable consoleInput = new StringReader(simulatedUserInput);
      StringWriter consoleOutput = new StringWriter();
      FakeRandomNumberGenerator fakeRng = new FakeRandomNumberGenerator();
      int maxTurns = 10; 
      GameController gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
      MockWorldOutline mockWorld = new MockWorldOutline();
      gameController.playGame(mockWorld);
      String output = consoleOutput.toString();
      assertTrue("The output should indicate the pet has been successfully moved",
                 output.contains("Pet has been moved by"));
      assertTrue("The output should contain the room ID where the pet was moved",
                 output.contains("to room id"));
  }
  
  
  
  
}
