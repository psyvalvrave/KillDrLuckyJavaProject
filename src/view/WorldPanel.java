package view;

import javax.swing.*;

import controller.Controller;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WorldPanel extends JPanel {
    private BufferedImage worldImage;
    private Map<Integer, Rectangle> roomCoordinates = new HashMap<>();
    private Map<Integer, Rectangle> playerCoordinates = new HashMap<>();
    private FrameView clickListener;

    public WorldPanel(Controller gameController) {
        super();
        this.worldImage = null;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                  handleMouseClick(gameController, e.getX(), e.getY());
                } catch (IOException | InterruptedException e1) {
                  e1.printStackTrace();
                }
            }
        });
    }
    
    public void setClickListener(FrameView listener) {
      this.clickListener = listener;
  }

    public void setWorldImage(BufferedImage worldImage) {
        this.worldImage = worldImage;
        setPreferredSize(new Dimension(worldImage.getWidth(), worldImage.getHeight()));
        revalidate();
        repaint();  
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (worldImage != null) {
            g.drawImage(worldImage, 0, 0, this);
        }
    }

    public void setRoomCoordinates(Map<Integer, Rectangle> coordinates) {
        this.roomCoordinates = coordinates;
    }

    public void setPlayerCoordinates(Map<Integer, Rectangle> coordinates) {
        this.playerCoordinates = coordinates;
    }

    private void handleMouseClick(Controller gameController, int x, int y) throws IOException, InterruptedException {
      boolean found = false;

      for (Map.Entry<Integer, Rectangle> entry : playerCoordinates.entrySet()) {
          if (entry.getValue().contains(x, y)) {
              if (clickListener != null) {
                  clickListener.onPlayerClick(gameController, entry.getKey());
              }
              found = true;
              break;
          }
      }

      if (!found) {
          for (Map.Entry<Integer, Rectangle> entry : roomCoordinates.entrySet()) {
              if (entry.getValue().contains(x, y)) {
                  if (clickListener != null) {
                      clickListener.onRoomClick(gameController, entry.getKey());
                  }
                  break;
              }
          }
      }
  }


}
