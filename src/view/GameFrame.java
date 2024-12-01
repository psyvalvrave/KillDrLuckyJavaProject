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

public class GameFrame extends JFrame implements FrameView {
    private ReadOnlyWorld world;
    private JLabel statusLabel;
    private WorldPanel worldPanel;
    private JTextArea infoTextArea;
    private JPanel setupPanel;
    private int maxturn;
    private File currentGameFile;
    private JPanel infoPanel;
    private JTextArea infoDisplay;

    public GameFrame(Controller gameController, int maxTurn, String file) throws IOException {
        currentGameFile = new File(file);
        this.maxturn = maxTurn;
        gameController.setGameFrame(this);
        setTitle("Kill Dr.Lucky");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(300, 300));
     
        initializeMenu(gameController);
        initializeComponents(gameController);
        setupOutputHandler(gameController);
        setupKeyListeners(gameController);
        loadDefaultWorld(gameController);
        initializeWelcomeScreen(gameController);
        
    }
    
    private void loadDefaultWorld(Controller gameController) throws IOException {
      setUpInstruction();
      FileReader fileReader = new FileReader(this.currentGameFile);
      this.world = new World(fileReader);
      gameController.loadNewWorld(world);
      gameController.setMaxTurn(maxturn);  
      refreshWorldDisplay(gameController);
      statusLabel.setText("Default Game loaded. Please set up the game.");
  }
    
    void restartWorld(Controller gameController) throws IOException {
      setUpInstruction();
      FileReader fileReader = new FileReader(this.currentGameFile);
      this.world = new World(fileReader);
      gameController.loadNewWorld(world);
      gameController.setMaxTurn(maxturn);
      gameController.setEnd(false);
      setupInitialGamePanel(gameController);
      refreshWorldDisplay(gameController);
      statusLabel.setText("Restart Game. Please set up the game.");
      
      
  }
    

    private void initializeMenu(Controller gameController) {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        JMenuItem loadGame = new JMenuItem("Load Game Configuration");
        JMenuItem restartGame = new JMenuItem("Restart Game");
        JMenuItem quitGame = new JMenuItem("Quit Game");

        loadGame.addActionListener(e -> loadWorldFromFile(gameController));
        restartGame.addActionListener(e -> {
          try {
            restartWorld(gameController);
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        });
        quitGame.addActionListener(e -> System.exit(0));

        gameMenu.add(loadGame);
        gameMenu.add(restartGame);
        gameMenu.add(quitGame);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
        menuBar.setVisible(false);
    }

    void loadWorldFromFile(Controller gameController) {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setDialogTitle("Select Game Configuration File");
      fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
      setUpInstruction();
      int result = fileChooser.showOpenDialog(this);
      if (result == JFileChooser.APPROVE_OPTION) {
          File selectedFile = fileChooser.getSelectedFile();
          try {
              FileReader fileReader = new FileReader(selectedFile);
              ReadOnlyWorld world = new World(fileReader);
              this.world = world;
              gameController.loadNewWorld(world);  
              gameController.setMaxTurn(maxturn);
              gameController.setEnd(false);
              setupInitialGamePanel(gameController);
              refreshWorldDisplay(gameController);
              statusLabel.setText("Selected Game loaded. Please set up the game.");
              this.currentGameFile = selectedFile;
          } catch (FileNotFoundException e) {
              JOptionPane.showMessageDialog(this, "File not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          } catch (IOException e) {
              JOptionPane.showMessageDialog(this, "Error loading world: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          }
      }
  }


    private void initializeComponents(Controller gameController) {
      statusLabel = new JLabel("Load a game configuration file to start.");
      statusLabel.setPreferredSize(new Dimension(getWidth(), 30));

      infoTextArea = new JTextArea(8, 20);
      infoTextArea.setEditable(false);

      worldPanel = new WorldPanel(gameController);
      setupWorldPanel(); 

      setupPanel = createSetupPanel(gameController);
      setupPanel.setVisible(false);
      setupPanel.setPreferredSize(new Dimension(200, getHeight()));
      
      createInfoPanel();
  }


    private JPanel createSetupPanel(Controller gameController) {
        JPanel setupPanel = new JPanel();
        setupPanel.setLayout(new BoxLayout(setupPanel, BoxLayout.Y_AXIS));

        JButton addHumanPlayerButton = new JButton("Add Human Player");
        JButton addComputerPlayerButton = new JButton("Add Computer Player");
        JButton startGameButton = new JButton("Start Game");

        addHumanPlayerButton.addActionListener(e -> {
            try {
                addPlayer(gameController, false);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        addComputerPlayerButton.addActionListener(e -> {
            try {
                addPlayer(gameController, true);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        startGameButton.addActionListener(e -> {
          try {
            startGame(gameController);
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

    private void addPlayer(Controller gameController, boolean isComputer) throws InterruptedException {
        String playerName = JOptionPane.showInputDialog(this, "Enter player name:");
        if (playerName != null && !playerName.trim().isEmpty()) {
            try {
                int roomIndex = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter starting room ID:"));
                gameController.addPlayer(playerName, roomIndex, isComputer);
                refreshWorldDisplay(gameController);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid room number. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error adding player: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
              JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void refreshWorldDisplay(Controller gameController) {
      if (!gameController.getEnd()) {
        BufferedImage image = gameController.saveWorldImg();
        worldPanel.setWorldImage(image);
        worldPanel.setRoomCoordinates(gameController.getRoomCoordinates());
        worldPanel.setPlayerCoordinates(gameController.getPlayerCoordinates());
        if (gameController.getRunning()) {
            statusLabel.setText("Player ID " + gameController.getCurrentPlayerId() + "'s turn");
        }
    } else {
        getContentPane().removeAll(); 
        GameEndPanel gameEndPanel = new GameEndPanel("Game Over! \n" + gameController.getResult(), gameController);
        add(gameEndPanel, BorderLayout.CENTER);
        validate();
        repaint();
    }
  }

    private void startGame(Controller gameController) throws IOException, InterruptedException {
      try {
        gameController.startGame();
        if (gameController.getRunning()) {
          statusLabel.setText("Player ID " + gameController.getCurrentPlayerId() + "'s turn");
        }
        showInfoPanel();
      } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }

    
    private void setupOutputHandler(Controller gameController) {
      TextOutputHandler outputHandler = new TextOutputHandler(text -> {
          SwingUtilities.invokeLater(() -> infoTextArea.append(text));
      });
      gameController.setOutput(outputHandler);
  }
    
    @Override
    public void onPlayerClick(Controller gameController, int playerId) throws InterruptedException {
      try {
        StringBuilder output = new StringBuilder();
          String playerInfo = gameController.displayPlayerInfo(playerId, output);
          infoDisplay.setText("Player Info: \n"+playerInfo);
          JOptionPane.showMessageDialog(this, output.toString(), "Player Info", JOptionPane.INFORMATION_MESSAGE);
      } catch (IOException e) {
          JOptionPane.showMessageDialog(this, "Error displaying player info: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }

    
    @Override
    public void onRoomClick(Controller gameController, int roomId) throws IOException, InterruptedException {
        if (gameController.getRunning()) {
            try {
              StringBuilder output = new StringBuilder();
                gameController.movePlayerToRoom(roomId, output);
                JOptionPane.showMessageDialog(this, output.toString(), "Move", JOptionPane.INFORMATION_MESSAGE);
                refreshWorldDisplay(gameController);  
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Invalid move: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } 
        } 
        setUpInstruction();
    }




    private void setupWorldPanel() {
        worldPanel.setClickListener(this);
    }
    
    private void setupKeyListeners(Controller gameController) {
      this.setFocusable(true);
      this.requestFocusInWindow();
      this.addKeyListener(new KeyAdapter() {
          @Override
          public void keyPressed(KeyEvent e) {
              try {
                  if (e.getKeyChar() == 'p') {
                      showItemPickupDialog(gameController);
                  } else if (e.getKeyChar() == 'l' || e.getKeyChar() == 'L') {
                      performLookAround(gameController);
                  } else if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A') {
                    showAttackDialog(gameController);
                  } else if (e.getKeyChar() == 'm' || e.getKeyChar() == 'M') {
                    showPetMoveDialog(gameController);
                  }
                  
              } catch (IOException | InterruptedException ex) {
                  JOptionPane.showMessageDialog(GameFrame.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
              }
          }
      });
      this.setFocusable(true);
      this.requestFocusInWindow();
  }

    
    private void showItemPickupDialog(Controller gameController) throws IOException, InterruptedException {
      int currentPlayerId = gameController.getCurrentPlayerId();
      List<String> items = gameController.passRoomItem(currentPlayerId); 

      if (items.isEmpty()) {
          JOptionPane.showMessageDialog(this, "No items available to pick up in this room. You waste your time on searching!", "Information", JOptionPane.INFORMATION_MESSAGE);
          gameController.doNothing();
      } else {
          ItemPickupDialog dialog = new ItemPickupDialog(this, "Pick an Item", true, items, (GameController) gameController, currentPlayerId);
          dialog.setVisible(true);
      }
      setUpInstruction();
      refreshWorldDisplay(gameController); 
  }
    
    private void performLookAround(Controller gameController) throws IOException, InterruptedException {
      int currentPlayerId = gameController.getCurrentPlayerId();
      StringBuilder output = new StringBuilder();
      String lookAroundInfo = gameController.performLookAround(currentPlayerId, output); 
      infoDisplay.setText("Look Around Result: \n"+lookAroundInfo);
      JOptionPane.showMessageDialog(this, output.toString(), "Look Around", JOptionPane.INFORMATION_MESSAGE);
      refreshWorldDisplay(gameController); 
  }
    
    private void showAttackDialog(Controller gameController) throws IOException, InterruptedException {
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
      setUpInstruction();
      refreshWorldDisplay(gameController); 
  }
    
    public void showPetMoveDialog(Controller gameController) throws IOException, InterruptedException {
      try {
      int currentPlayerId = gameController.getCurrentPlayerId();
      new PetMoveDialog(this, gameController, currentPlayerId).setVisible(true);
      } catch (IllegalArgumentException e) {
      JOptionPane.showMessageDialog(this, "Invalid attack: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
  } 
      setUpInstruction();
      refreshWorldDisplay(gameController); 
  }
    
    private void initializeWelcomeScreen(Controller gameController) throws IOException {
      WelcomePanel welcomePanel = new WelcomePanel("Welcome to the Game Kill Doctor Lucky", "Credit: Zhecheng Li");
      welcomePanel.setPreferredSize(new Dimension(800, 600));
      getContentPane().removeAll(); 
      setLayout(new BorderLayout());
      add(welcomePanel, BorderLayout.CENTER);  
      pack();
      setLocationRelativeTo(null);

      Timer timer = new Timer(3000, e -> {
          setupInitialGamePanel(gameController);
          getJMenuBar().setVisible(true);
      });
      timer.setRepeats(false);
      timer.start();
  }
    
    private void setupInitialGamePanel(Controller gameController) {
      getContentPane().removeAll();  
      setLayout(new BorderLayout());  

      JScrollPane scrollPane = new JScrollPane(worldPanel);
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

      JScrollPane infoScrollPane = new JScrollPane(infoTextArea);
      infoScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

      add(statusLabel, BorderLayout.NORTH);
      add(scrollPane, BorderLayout.CENTER);
      add(infoScrollPane, BorderLayout.SOUTH);
      add(setupPanel, BorderLayout.WEST);
      add(infoPanel, BorderLayout.EAST);
      infoPanel.setVisible(false); 
      worldPanel.setVisible(true);
      infoTextArea.setVisible(true);
      statusLabel.setVisible(true);
      setupPanel.setVisible(true);

      validate();
      repaint();

      refreshWorldDisplay(gameController);  
  }
    
    private void createInfoPanel() {
      infoPanel = new JPanel();
      infoPanel.setLayout(new BorderLayout());
      infoPanel.setPreferredSize(new Dimension(300, getHeight()));
      infoDisplay = new JTextArea();
      infoDisplay.setEditable(false);
      infoDisplay.setLineWrap(true);
      JScrollPane scrollPane = new JScrollPane(infoDisplay);
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

      infoPanel.add(scrollPane, BorderLayout.CENTER);
      infoPanel.setBackground(Color.CYAN);
      infoPanel.setVisible(false);
  }
    
    private void showInfoPanel() {
      setupPanel.setVisible(false);
      infoPanel.setVisible(true);
      setUpInstruction();
  }
    
    private void setUpInstruction() {
      infoDisplay.setText("Game Instruction:\nBlue dot is the current player\nBlack dot is not current player\n"
          + "Click a room to move\nClick player ICON(blue or black dot) to display player info\n"
          + "Press 'P' on keyboard to pick up item\n"
          + "Press 'L' on keyboard to look around to gather information\n"
          + "Press 'A' on keyboard to attack target(red dot)\n"
          + "Press 'M' on keyboard to move the pet the other room\n"
          + "enjoy the game!");
    }


    





    
    



    




    

}
