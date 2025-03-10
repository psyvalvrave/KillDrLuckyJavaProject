package controller;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import mock.MockWorldOutline;
import org.junit.Test;

/**
 * Test file for computer controller. 
 */
public class GameComputerControllerTest {
  private GameController gameController;
  private StringWriter consoleOutput;
  private FakeRandomNumberGenerator fakeRng;

  @Test
  public void testComputerPlayerMoves() throws InterruptedException, IOException {
    int maxTurns = 2; 
    String simulatedUserInput = "2\n1\nComputerPlayer\n4\n0\n"; 
    Readable consoleInput = new java.io.StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();   
    fakeRng = new FakeRandomNumberGenerator();
    MockWorldOutline mockWorld = new MockWorldOutline();
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    gameController.playGame(mockWorld);
    fakeRng.setNextIntResult(0); 
    String output = consoleOutput.toString();
    assertTrue(output.contains("Default move result"));
  }
  
  @Test
  public void testComputerPlayerPicksUpItem() throws InterruptedException, IOException {
    int maxTurns = 2; 
    String simulatedUserInput = "2\n1\nComputerPlayer\n4\n0\n"; 
    Readable consoleInput = new java.io.StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();   
    fakeRng = new FakeRandomNumberGenerator();
    MockWorldOutline mockWorld = new MockWorldOutline();
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    fakeRng.setNextIntResult(1); 
    gameController.playGame(mockWorld);
    String output = consoleOutput.toString();
    assertTrue(output.contains("Computer player: Default pick item up"));
  }
  
  @Test
  public void testComputerPlayerLookAround() throws InterruptedException, IOException {
    int maxTurns = 2; 
    String simulatedUserInput = "2\n1\nComputerPlayer\n4\n0\n"; 
    Readable consoleInput = new java.io.StringReader(simulatedUserInput);
    consoleOutput = new StringWriter();   
    fakeRng = new FakeRandomNumberGenerator();
    MockWorldOutline mockWorld = new MockWorldOutline();
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    fakeRng.setNextIntResult(2); 
    gameController.playGame(mockWorld);
    String output = consoleOutput.toString();
    assertTrue(output.contains("Computer player: Default look around info"));
  }
  
  @Test
  public void testComputerPlayerMurderWhenAvailable() throws InterruptedException, IOException {
    consoleOutput = new StringWriter();   
    fakeRng = new FakeRandomNumberGenerator();
    MockWorldOutline mockWorld = new MockWorldOutline();
    mockWorld.toggleMurderAttempt();
    String simulatedUserInput = "2\n1\nComputerPlayer\n4\n0\n"; 
    Readable consoleInput = new java.io.StringReader(simulatedUserInput);
    int maxTurns = 2; 
    gameController = new GameController(consoleInput, consoleOutput, fakeRng, maxTurns);
    gameController.playGame(mockWorld);
    String output = consoleOutput.toString();
    assertTrue(output.contains("Opportunity for murder identified. "
        + "Computer player preparing to attack."));
  }

}
