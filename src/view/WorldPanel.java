package view;

import javax.swing.*;
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
    private ClickListener clickListener;

    public WorldPanel() {
        super();
        this.worldImage = null;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                  handleMouseClick(e.getX(), e.getY());
                } catch (IOException | InterruptedException e1) {
                  e1.printStackTrace();
                }
            }
        });
    }
    
    public void setClickListener(ClickListener listener) {
      this.clickListener = listener;
  }

    public void setWorldImage(BufferedImage worldImage) {
        this.worldImage = worldImage;
        setPreferredSize(new Dimension(worldImage.getWidth(), worldImage.getHeight()));
        revalidate();
        repaint();  // This will trigger the paintComponent to redraw the image
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

    private void handleMouseClick(int x, int y) throws IOException, InterruptedException {
      boolean found = false;

      for (Map.Entry<Integer, Rectangle> entry : playerCoordinates.entrySet()) {
          if (entry.getValue().contains(x, y)) {
              if (clickListener != null) {
                  clickListener.onPlayerClick(entry.getKey());
              }
              found = true;
              break;
          }
      }

      if (!found) {
          for (Map.Entry<Integer, Rectangle> entry : roomCoordinates.entrySet()) {
              if (entry.getValue().contains(x, y)) {
                  if (clickListener != null) {
                      clickListener.onRoomClick(entry.getKey());
                  }
                  break;
              }
          }
      }
  }
    
    public void printCoordinates() {
      //System.out.println("Room Coordinates:");
      //roomCoordinates.forEach((key, value) -> System.out.println("Room ID: " + key + " -> Bounds: " + value));

      System.out.println("Player Coordinates:");
      playerCoordinates.forEach((key, value) -> System.out.println("Player ID: " + key + " -> Bounds: " + value));
  }


}
