package view;

import controller.Controller;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

/**
 * A custom panel for displaying the game world as a buffered image. It also manages the coordinates
 * for rooms and players to facilitate interaction, such as clicking on game elements.
 */
public class WorldPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private BufferedImage worldImage;
  private Map<Integer, Rectangle> roomCoordinates = new HashMap<>();
  private Map<Integer, Rectangle> playerCoordinates = new HashMap<>();
  private FrameView clickListener;

  /**
   * Constructs a WorldPanel with a mouse listener that 
   * triggers actions based on the location clicked.
   *
   * @param gameController the controller that handles game logic and mouse interactions.
   */
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
  
  /**
   * Sets the listener for handling clicks within the panel.
   *
   * @param listener the FrameView that will respond to click events.
   */
  public void setClickListener(FrameView listener) {
    this.clickListener = listener;
  }
  
  /**
   * Updates the image displayed within the panel.
   *
   * @param worldImageInput the new image to display.
   */
  public void setWorldImage(BufferedImage worldImageInput) {
    this.worldImage = worldImageInput;
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

  private void handleMouseClick(Controller gameController, int x, int y) 
      throws IOException, InterruptedException {
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
