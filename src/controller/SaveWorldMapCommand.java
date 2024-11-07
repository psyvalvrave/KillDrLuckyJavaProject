package controller;

import java.io.IOException;
import world.WorldOutline;

public class SaveWorldMapCommand implements Command {
    private WorldOutline world;

    public SaveWorldMapCommand(WorldOutline world) {
        this.world = world;
    }

    @Override
    public void execute(Appendable output) throws IOException {
        try {
            world.drawWorld(); 
            output.append("World map saved to 'res/world.png'.\n"); 
        } catch (IOException e) {
            output.append("Error saving world map: " + e.getMessage() + "\n");
        }
    }
}
