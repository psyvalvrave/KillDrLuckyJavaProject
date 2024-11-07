package controller;

import java.io.IOException;
import java.util.Scanner;

import world.WorldOutline;

public class DisplayRoomInfoCommand implements Command {
  private WorldOutline world;
  private Scanner scanner;

  public DisplayRoomInfoCommand(WorldOutline world, Scanner scanner) {
      this.world = world;
      this.scanner = scanner;
  }

  @Override
  public void execute(Appendable output) throws IOException {
      try {
          output.append("Enter room ID:\n");
          int roomId = Integer.parseInt(scanner.nextLine());
          String roomInfo = world.displayRoomInfo(roomId);
          output.append(roomInfo + "\n");
      } catch (NumberFormatException e) {
          output.append("Invalid room ID. Please enter a number.\n");
      }
  }
}

