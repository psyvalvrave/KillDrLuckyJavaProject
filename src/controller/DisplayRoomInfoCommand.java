package controller;

import java.io.IOException;
import java.util.Scanner;
import world.WorldOutline;

/**
 * The DisplayRoomInfoCommand class implements the Command interface to handle the display
 * of information about a specific room within the game. It prompts the user for a room ID
 * and displays detailed information about that room.
 */
public class DisplayRoomInfoCommand implements Command {
  private WorldOutline world;
  private Scanner scanner;

  /**
   * Constructs a new DisplayRoomInfoCommand with a reference to the game world and a scanner
   * to read user input. This setup allows the command to interact with the game world and
   * retrieve information about specific rooms based on user input.
   *
   * @param worldModel The game world from which room information will be retrieved.
   * @param scannerInput A Scanner to read input from the console.
   */
  public DisplayRoomInfoCommand(WorldOutline worldModel, Scanner scannerInput) {
    this.world = worldModel;
    this.scanner = scannerInput;
  }

  @Override
  public void execute(Appendable output) throws IOException {
    try {
      output.append("Enter room ID:\n");
      int roomId = Integer.parseInt(scanner.nextLine());
      String roomInfo = world.displayRoomInfo(roomId);
      output.append(roomInfo + "\n");
    } catch (NumberFormatException e) {
      throw new NumberFormatException(e.getMessage() + "\n");
        
    }
  }
}

