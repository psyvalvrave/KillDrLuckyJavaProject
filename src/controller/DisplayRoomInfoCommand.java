package controller;

import java.io.IOException;
import world.ReadOnlyWorld;

/**
 * The DisplayRoomInfoCommand class implements the Command interface to handle the display
 * of information about a specific room within the game. It prompts the user for a room ID
 * and displays detailed information about that room.
 */
public class DisplayRoomInfoCommand implements Command {
  private ReadOnlyWorld world;
  private int roomId;

  /**
   * Constructs a new DisplayRoomInfoCommand with a reference to the game world and a scanner
   * to read user input. This setup allows the command to interact with the game world and
   * retrieve information about specific rooms based on user input.
   *
   * @param worldModel The game world from which room information will be retrieved.
   * @param scannerInput A Scanner to read input from the console.
   */
  public DisplayRoomInfoCommand(ReadOnlyWorld worldModel, int roomIdInput) {
    this.world = worldModel;
    this.roomId = roomIdInput;
  }

  @Override
  public void execute(Appendable output) throws IOException {
      String roomInfo = world.displayRoomInfo(roomId);
      output.append(roomInfo + "\n");
  }
}

