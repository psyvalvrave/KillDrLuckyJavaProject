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
        } catch (IllegalArgumentException e) {
          throw new IllegalArgumentException(e.getMessage() + "\n");
        }
    }
}
