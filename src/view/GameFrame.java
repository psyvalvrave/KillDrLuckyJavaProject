package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.Controller;
import controller.TextOutputHandler;
import world.ReadOnlyWorld;
import world.World;

public class GameFrame extends JFrame implements ClickListener {
    private ReadOnlyWorld world;
    private JLabel statusLabel;
    private WorldPanel worldPanel;
    private JTextArea infoTextArea;
    private Controller gameController;
    private JPanel setupPanel;
    private JPanel gamePanel;

    public GameFrame(Controller gameController) {
        this.gameController = gameController;
        setTitle("Game World");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(300, 300));

        initializeMenu();
        initializeComponents();
        setupOutputHandler();
        
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
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setDialogTitle("Select Game Configuration File");
      fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

      int result = fileChooser.showOpenDialog(this);
      if (result == JFileChooser.APPROVE_OPTION) {
          File selectedFile = fileChooser.getSelectedFile();
          try {
              FileReader fileReader = new FileReader(selectedFile);
              ReadOnlyWorld world = new World(fileReader);
              this.world = world;
              gameController.loadNewWorld(world);  
              refreshWorldDisplay();
              setupPanel.setVisible(true);
              statusLabel.setText("Game loaded. Please set up the game.");
          } catch (FileNotFoundException e) {
              JOptionPane.showMessageDialog(this, "File not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          } catch (IOException e) {
              JOptionPane.showMessageDialog(this, "Error loading world: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          }
      }
  }


    private void initializeComponents() {
        statusLabel = new JLabel("Load a game configuration file to start.");
        statusLabel.setPreferredSize(new Dimension(getWidth(), 30));
        infoTextArea = new JTextArea(8, 20);
        
        worldPanel = new WorldPanel();
        setupWorldPanel();
        JScrollPane scrollPane = new JScrollPane(worldPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        infoTextArea.setEditable(false);
        JScrollPane infoScrollPane = new JScrollPane(infoTextArea);
        infoScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        gamePanel = new JPanel(new BorderLayout());
        gamePanel.add(statusLabel, BorderLayout.NORTH);
        gamePanel.add(scrollPane, BorderLayout.CENTER);
        gamePanel.add(infoScrollPane, BorderLayout.SOUTH);

        setupPanel = createSetupPanel();
        setupPanel.setVisible(false);
        setupPanel.setPreferredSize(new Dimension(200, getHeight()));

        setLayout(new BorderLayout());
        add(setupPanel, BorderLayout.WEST);
        add(gamePanel, BorderLayout.CENTER);
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
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        addComputerPlayerButton.addActionListener(e -> {
            try {
                addPlayer(true);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        startGameButton.addActionListener(e -> {
          try {
            startGame();
          } catch (IOException e1) {
            e1.printStackTrace();
          } catch (InterruptedException e1) {
            e1.printStackTrace();
          }
        });

        setupPanel.add(addHumanPlayerButton);
        setupPanel.add(addComputerPlayerButton);
        setupPanel.add(startGameButton);

        return setupPanel;
    }

    private void addPlayer(boolean isComputer) throws InterruptedException {
        String playerName = JOptionPane.showInputDialog(this, "Enter player name:");
        if (playerName != null && !playerName.trim().isEmpty()) {
            try {
                int roomIndex = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter starting room ID:"));
                gameController.addPlayer(playerName, roomIndex, isComputer);
                refreshWorldDisplay();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid room number. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error adding player: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
              JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshWorldDisplay() {
          BufferedImage image = gameController.saveWorldImg();
          worldPanel.setWorldImage(image);
          worldPanel.setRoomCoordinates(gameController.getRoomCoordinates());
          worldPanel.setPlayerCoordinates(gameController.getPlayerCoordinates());
  }

    private void startGame() throws IOException, InterruptedException {
      try {
        gameController.startGame();
        setupPanel.setVisible(false);
      } catch(IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }

    private void updateGameInfo(String message) {
        SwingUtilities.invokeLater(() -> {
            infoTextArea.append(message + "\n");
            infoTextArea.setCaretPosition(infoTextArea.getDocument().getLength());
        });
    }
    
    private void setupOutputHandler() {
      TextOutputHandler outputHandler = new TextOutputHandler(text -> {
          SwingUtilities.invokeLater(() -> infoTextArea.append(text));
      });
      gameController.setOutput(outputHandler);
  }
    
    @Override
    public void onPlayerClick(int playerId) {
      System.out.println("Player clicked: " + playerId);
        infoTextArea.append("Player " + playerId + " clicked.\n");
    }

    @Override
    public void onRoomClick(int roomId) {
      System.out.println("Room clicked: " + roomId);
        infoTextArea.append("Room " + roomId + " clicked.\n");
    }

    private void setupWorldPanel() {
        worldPanel.setClickListener(this);
    }
    

}
