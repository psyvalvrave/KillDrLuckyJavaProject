package controller;

import java.io.IOException;
import world.WorldOutline;

public class TargetInfoCommand implements Command {
    private WorldOutline world;

    public TargetInfoCommand(WorldOutline world) {
        this.world = world;
    }

    @Override
    public void execute(Appendable output) throws IOException {
        try {
            String targetInfo = world.getTargetInfo();
            output.append(targetInfo + "\n");
        } catch (Exception e) {
            output.append("Error retrieving target info: " + e.getMessage() + "\n");
        }
    }
}
