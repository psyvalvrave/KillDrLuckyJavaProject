package controller;

import java.io.IOException;
import world.WorldOutline;

public class LookAroundCommand implements Command {
    private WorldOutline world;
    private int playerId;

    public LookAroundCommand(WorldOutline world, int playerId) {
        this.world = world;
        this.playerId = playerId;
    }

    @Override
    public void execute(Appendable output) throws IOException {
        try {
          String lookAroundInfo = world.playerLookAround(playerId);
            output.append(lookAroundInfo); 
        } catch (Exception e) {
            output.append("Error saving world map: " + e.getMessage() + "\n");
        }
    }
}
