package controller;

import java.io.IOException;
import world.ReadOnlyWorld;

/**
 * The SaveWorldMapCommand class implements the Command interface to facilitate
 * the saving of the game's world map to an external file. This command directs the
 * game world to render its current state to an image file, which can be used for
 * reviewing the game layout or debugging purposes.
 */
public class SaveWorldMapCommand implements Command {
  private ReadOnlyWorld world;
  
  /**
   * Constructs a SaveWorldMapCommand with access to the game world.
   *
   * @param worldModel The game world whose map is to be saved.
   */
  public SaveWorldMapCommand(ReadOnlyWorld worldModel) {
    this.world = worldModel;
  }

  @Override
  public String execute(Appendable output) throws IOException {
    try {
      world.drawWorld(); 
      output.append("World map saved to 'res/world.png'.\n"); 
    } catch (IOException e) {
      throw e;
    }
    return null;
  }
}
