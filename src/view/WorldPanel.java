package view;

import javax.swing.*;

import world.ReadOnlyWorld;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WorldPanel extends JPanel {
    private ReadOnlyWorld world;

    public WorldPanel() {
      setPreferredSize(new Dimension(600, 400));
    }

    public void updateWorld(ReadOnlyWorld newWorld) {
        this.world = newWorld;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (world != null) {
            BufferedImage image = world.drawWorld();
            g.drawImage(image, 0, 0, this);
        }
    }
}
