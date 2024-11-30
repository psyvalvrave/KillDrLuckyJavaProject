package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.io.FileNotFoundException;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


import controller.Controller;
import controller.GameController;
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
        gameController.setGameFrame(this);
        setTitle("Game World");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(300, 300));

        initializeMenu();
        initializeComponents();
        setupOutputHandler();
        setupKeyListeners();
        
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

    public void refreshWorldDisplay() {
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

    
    private void setupOutputHandler() {
      TextOutputHandler outputHandler = new TextOutputHandler(text -> {
          SwingUtilities.invokeLater(() -> infoTextArea.append(text));
      });
      gameController.setOutput(outputHandler);
  }
    
    @Override
    public void onPlayerClick(int playerId) throws InterruptedException {
      try {
        StringBuilder output = new StringBuilder();
          gameController.displayPlayerInfo(playerId, output);
          JOptionPane.showMessageDialog(this, output.toString(), "Move", JOptionPane.INFORMATION_MESSAGE);
      } catch (IOException e) {
          JOptionPane.showMessageDialog(this, "Error displaying player info: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }

    
    @Override
    public void onRoomClick(int roomId) throws IOException, InterruptedException {
        if (gameController.getRunning()) {
            try {
              StringBuilder output = new StringBuilder();
                gameController.movePlayerToRoom(roomId, output);
                JOptionPane.showMessageDialog(this, output.toString(), "Move", JOptionPane.INFORMATION_MESSAGE);
                refreshWorldDisplay();  
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Invalid move: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } 
        } 
    }




    private void setupWorldPanel() {
        worldPanel.setClickListener(this);
    }
    
    private void setupKeyListeners() {
      this.setFocusable(true);
      this.requestFocusInWindow();
      this.addKeyListener(new KeyAdapter() {
          @Override
          public void keyPressed(KeyEvent e) {
              try {
                  if (e.getKeyChar() == 'p') {
                      showItemPickupDialog();
                  } else if (e.getKeyChar() == 'l' || e.getKeyChar() == 'L') {
                      performLookAround();
                  } else if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A') {
                    showAttackDialog();
                  } else if (e.getKeyChar() == 'm' || e.getKeyChar() == 'M') {
                    showPetMoveDialog();
                  }
                  
              } catch (IOException | InterruptedException ex) {
                  JOptionPane.showMessageDialog(GameFrame.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
              }
          }
      });
      this.setFocusable(true);
      this.requestFocusInWindow();
  }

    
    private void showItemPickupDialog() throws IOException, InterruptedException {
      int currentPlayerId = gameController.getCurrentPlayerId();
      List<String> items = gameController.passRoomItem(currentPlayerId); 

      if (items.isEmpty()) {
          JOptionPane.showMessageDialog(this, "No items available to pick up in this room. You waste your time on searching!", "Information", JOptionPane.INFORMATION_MESSAGE);
          gameController.doNothing();
      } else {
          ItemPickupDialog dialog = new ItemPickupDialog(this, "Pick an Item", true, items, (GameController) gameController, currentPlayerId);
          dialog.setVisible(true);
      }
      refreshWorldDisplay(); 
  }
    
    private void performLookAround() throws IOException, InterruptedException {
      int currentPlayerId = gameController.getCurrentPlayerId();
      StringBuilder output = new StringBuilder();
      gameController.performLookAround(currentPlayerId, output); 
      JOptionPane.showMessageDialog(this, output.toString(), "Look Around", JOptionPane.INFORMATION_MESSAGE);
      refreshWorldDisplay(); 
  }
    
    private void showAttackDialog() throws IOException, InterruptedException {
      int currentPlayerId = gameController.getCurrentPlayerId();
      List<String> items = gameController.passPlayerItems(currentPlayerId); 
      try {
      if (items.isEmpty()) {      
            StringBuilder output = new StringBuilder();
            gameController.attackTargetWithItem(currentPlayerId, "", output); 
              JOptionPane.showMessageDialog(this, "No items available to use for the attack. Attack by poking eyes.", "Attack", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(this, output.toString(), "Attack", JOptionPane.INFORMATION_MESSAGE);
        } else {
            AttackDialog dialog = new AttackDialog(this, (GameController) gameController, currentPlayerId, items);
            dialog.setVisible(true);
        }
      } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, "Invalid attack: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } 
      refreshWorldDisplay(); 
  }
    
    public void showPetMoveDialog() throws IOException, InterruptedException {
      try {
      int currentPlayerId = gameController.getCurrentPlayerId();
      new PetMoveDialog(this, gameController, currentPlayerId).setVisible(true);
      } catch (IllegalArgumentException e) {
      JOptionPane.showMessageDialog(this, "Invalid attack: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
  } 
      refreshWorldDisplay(); 
  }




    
    



    




    

}
