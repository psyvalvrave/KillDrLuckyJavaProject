package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.Controller;
import world.ReadOnlyWorld;
import world.World;

public class GameFrame extends JFrame {
    private ReadOnlyWorld world;
    private JLabel statusLabel;
    private WorldPanel worldPanel;
    private JTextArea infoTextArea;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private Controller gameController;

    public GameFrame(Controller gameController) {
      this.gameController = gameController;
        setTitle("Game World");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 600);
        setMinimumSize(new Dimension(300, 300));
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        initializeMenu();
        initializeComponents();
        add(mainPanel);  // Ensure mainPanel is added to the JFrame
    }

    private void initializeMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        JMenuItem loadGame = new JMenuItem("Load Game Configuration");
        JMenuItem quitGame = new JMenuItem("Quit Game");

        loadGame.addActionListener(e -> loadWorldFromFile());
        quitGame.addActionListener(e -> System.exit(0));

        gameMenu.add(loadGame);
        gameMenu.add(quitGame);

        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }

    private void loadWorldFromFile() {
      // File chooser logic
      if (result == JFileChooser.APPROVE_OPTION) {
          File selectedFile = fileChooser.getSelectedFile();
          try {
              FileReader fileReader = new FileReader(selectedFile);
              gameController.loadNewWorld(fileReader);
              worldPanel.updateWorld((World) gameController.getWorld());
              statusLabel.setText("Game loaded. Please set up the game.");
              cardLayout.show(mainPanel, "SetupPanel");
          } catch (IOException e) {
              JOptionPane.showMessageDialog(this, "Error loading world: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          }
      }
  }



    private void startNewGame(Readable inputSource) {
      SwingUtilities.invokeLater(() -> {
          try {
              this.world = new World(inputSource);  // Assuming World can initialize from a Readable
              worldPanel.updateWorld((World) world);  // Make sure WorldPanel can accept World
              statusLabel.setText("Game loaded. Please set up the game.");
              cardLayout.show(mainPanel, "SetupPanel");  // Switch to the setup panel
          } catch (Exception e) {
              JOptionPane.showMessageDialog(this, "Error initializing game: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          }
      });
  }



    private void initializeComponents() {
      worldPanel = new WorldPanel();
      JScrollPane scrollPane = new JScrollPane(worldPanel);
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

      statusLabel = new JLabel("Load a game configuration file to start.");
      add(statusLabel, BorderLayout.NORTH);

      infoTextArea = new JTextArea(5, 20);
      infoTextArea.setEditable(false);
      JScrollPane infoScrollPane = new JScrollPane(infoTextArea);

      JPanel gamePanel = new JPanel(new BorderLayout());
      gamePanel.add(scrollPane, BorderLayout.CENTER);
      gamePanel.add(infoScrollPane, BorderLayout.SOUTH);

      mainPanel.add(gamePanel, "GamePanel");
      add(mainPanel, BorderLayout.CENTER);

      JPanel setupPanel = createSetupPanel();
      mainPanel.add(setupPanel, "SetupPanel");
      cardLayout.show(mainPanel, "SetupPanel");
  }



    private JPanel createSetupPanel() {
      JPanel setupPanel = new JPanel();
      setupPanel.setLayout(new BoxLayout(setupPanel, BoxLayout.Y_AXIS));

      JButton addHumanPlayerButton = new JButton("Add Human Player");
      JButton addComputerPlayerButton = new JButton("Add Computer Player");
      JButton startGameButton = new JButton("Start Game");

      addHumanPlayerButton.addActionListener(e -> {
        try {
          addPlayer(false);
        } catch (InterruptedException e1) {
          e1.printStackTrace();
        }
      });
      addComputerPlayerButton.addActionListener(e -> {
        try {
          addPlayer(true);
        } catch (InterruptedException e1) {
          e1.printStackTrace();
        }
      });
      startGameButton.addActionListener(e -> startGame());

      setupPanel.add(addHumanPlayerButton);
      setupPanel.add(addComputerPlayerButton);
      setupPanel.add(startGameButton);

      return setupPanel;
  }

    
    private void updateGameInfo(String message) {
        SwingUtilities.invokeLater(() -> {
            infoTextArea.append(message + "\n");
            infoTextArea.setCaretPosition(infoTextArea.getDocument().getLength());
        });
    }
    
    private void addPlayer(boolean isComputer) throws InterruptedException {
      String playerName = JOptionPane.showInputDialog(this, "Enter player name:");
      if (playerName != null && !playerName.trim().isEmpty()) {
          try {
              int roomIndex = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter starting room ID:"));
              gameController.addPlayer(playerName, roomIndex, isComputer);
              updateGameInfo(playerName + " added as " + (isComputer ? "computer" : "human") + " player.");
          } catch (NumberFormatException ex) {
              JOptionPane.showMessageDialog(this, "Invalid room number. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
          } catch (IOException ex) {
              JOptionPane.showMessageDialog(this, "Error adding player: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          }
      }
  }


    private void startGame() {
      gameController.startGame();
      updateGameInfo("Game started with players.");
      cardLayout.show(mainPanel, "GamePanel");  // Switch back to game panel to start the game
  }


}
