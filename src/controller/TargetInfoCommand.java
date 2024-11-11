package controller;

import java.io.IOException;
import world.WorldOutline;

/**
 * The TargetInfoCommand class implements the Command interface to provide
 * functionality for retrieving and displaying information about the game's target.
 * This command allows users to view details about the target character within the game.
 */
public class TargetInfoCommand implements Command {
  private WorldOutline world;

  /**
   * Constructs a TargetInfoCommand with access to the game world, from which
   * target information can be retrieved.
   *
   * @param worldModel The game world that contains the target's data.
   */
  public TargetInfoCommand(WorldOutline worldModel) {
    this.world = worldModel;
  }

  @Override
  public void execute(Appendable output) throws IOException {
    try {
      String targetInfo = world.getTargetInfo();
      output.append(targetInfo + "\n");
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage() + "\n");
    }
  }
}
