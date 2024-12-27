package view;

import static org.junit.Assert.assertEquals;

import controller.MockGameController;
import controller.RandomNumberGenerator;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;


/**
 * Test for view part.
 */
public class GameFrameTest {
  private MockGameController mockController;
  private GameFrame gameFrame;

  /**
   * Set up before each test.
   */
  @Before
  public void setUp() throws IOException {
    Appendable consoleOutput = System.out;
    InputStreamReader consoleInput = new InputStreamReader(System.in);
    RandomNumberGenerator rng = new RandomNumberGenerator();
    mockController = new MockGameController(consoleInput, consoleOutput, rng, 10);
    gameFrame = new GameFrame(mockController, 10, "res/mansion.txt");
    gameFrame.setVisible(true);
    
    Map<Integer, Rectangle> roomCoordinates = new HashMap<>();
    roomCoordinates.put(1, new Rectangle(50, 50, 100, 100)); 
    gameFrame.getWorldPanel().setRoomCoordinates(roomCoordinates);

    Map<Integer, Rectangle> playerCoordinates = new HashMap<>();
    playerCoordinates.put(1, new Rectangle(200, 200, 50, 50)); 
    gameFrame.getWorldPanel().setPlayerCoordinates(playerCoordinates);
    
    mockController.startGame();
  }
  
  
  
  @Test
  public void testKeyPress_PickUpItem_No_Item() {
    KeyEvent keyEvent = new KeyEvent(gameFrame, KeyEvent.KEY_PRESSED, 
        System.currentTimeMillis(), 0, KeyEvent.VK_P, 'p');
    gameFrame.dispatchEvent(keyEvent);
    assertEquals("pick Nothing", mockController.lastMethodCalled);
  }
  
  @Test
  public void testKeyPress_PickUpItem_Items() throws IllegalArgumentException {
    KeyEvent keyEvent = new KeyEvent(gameFrame, KeyEvent.KEY_PRESSED, 
        System.currentTimeMillis(), 0, KeyEvent.VK_P, 'p');
    mockController.roomItemList.add("item1");
    gameFrame.dispatchEvent(keyEvent);
    assertEquals("pickUpItem item1", mockController.lastMethodCalled);
  }
  
  @Test
  public void testKeyPress_LookAround() throws IllegalArgumentException {
    KeyEvent keyEvent = new KeyEvent(gameFrame, KeyEvent.KEY_PRESSED, 
        System.currentTimeMillis(), 0, KeyEvent.VK_L, 'l');
    gameFrame.dispatchEvent(keyEvent);

    assertEquals("performLookAround", mockController.lastMethodCalled);
  }

  @Test
  public void testKeyPress_Attack_Nothing() throws IllegalArgumentException {
    KeyEvent keyEvent = new KeyEvent(gameFrame, KeyEvent.KEY_PRESSED, 
        System.currentTimeMillis(), 0, KeyEvent.VK_A, 'a');
    gameFrame.dispatchEvent(keyEvent);
    assertEquals("attackTargetWithItem ", mockController.lastMethodCalled);
  }
  
  @Test
  public void testKeyPress_Attack() throws IllegalArgumentException {
    KeyEvent keyEvent = new KeyEvent(gameFrame, KeyEvent.KEY_PRESSED, 
        System.currentTimeMillis(), 0, KeyEvent.VK_A, 'a');
    mockController.playerItemList.add("item1");
    gameFrame.dispatchEvent(keyEvent);
    assertEquals("attackTargetWithItem item1", mockController.lastMethodCalled);
  }
  
  @Test
  public void testKeyPress_MovePet() throws IllegalArgumentException {
    KeyEvent keyEvent = new KeyEvent(gameFrame, KeyEvent.KEY_PRESSED, 
        System.currentTimeMillis(), 0, KeyEvent.VK_A, 'm');
    gameFrame.dispatchEvent(keyEvent);
    assertEquals("movePet" + mockController.petRoomId, 
        mockController.lastMethodCalled);
  }
  
  @Test
  public void testMouseClick_Room() throws IllegalArgumentException {
    MouseEvent mouseEvent = new MouseEvent(gameFrame.getWorldPanel(), 
        MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 75, 75, 1, false);
    gameFrame.getWorldPanel().dispatchEvent(mouseEvent);
    assertEquals("movePlayerToRoom", mockController.lastMethodCalled);
    assertEquals("Room 1", mockController.lastRoomClickResult);
  }

  @Test
  public void testMouseClick_Player() throws IllegalArgumentException {
    MouseEvent mouseEvent = new MouseEvent(gameFrame.getWorldPanel(), 
        MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 225, 225, 1, false);
    gameFrame.getWorldPanel().dispatchEvent(mouseEvent);

    assertEquals("displayPlayerInfo", mockController.lastMethodCalled);
    assertEquals("Player 1", mockController.lastPlayerInfo);
  }

    
    
}
