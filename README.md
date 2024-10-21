# CS5010-DrLuckyGame
This is a class project to kill a game base with java. It is similar to the board game Kill Doctor Lucky, but it has some different rules. 

## Milestone 1: Game Board Creation

In this initial milestone, the focus was on laying the foundation of the game environment. We successfully implemented the creation of the game board using the `World` class, and defined the `Room` and `Target` entities within the game.

### Updates from preliminary design m1:
1. Detailed implementation of the `World` class to manage the overall game environment.
2. Setup and configuration of the `Room` entities within the game world, including new parameters for each room's location to determine visibility and neighbors upon creation.
3. Definition and characteristics of the `Target` entity, specifying its behavior and properties. Removed coordinates-related information for simplification.
4. Further enhancements and features that were integrated based on initial feedback.
5. Decided not to implement the Player class in this milestone.
6. Updated the method names for outputting information.
7. Created private methods `areNeighbors` and `canSeeFrom` to establish neighbor and visible relationships between rooms.
8. Added a new method in the `World` class to draw the world for visualized outputs.
9.  Created additional methods such as `establishRoomNeighbors` to add neighbors to each room during the world setup.
10. Introduced a method to move the Target around within the World class.
11. Added more getter methods across all classes to facilitate access to fields needed across different classes.

## Milestone 2: Controller Added

In this initial milestone, the focus was on adding a cmd controller to play the model

### Updates from preliminary design m2:
1. Only create one big GameController class, add many private helper method in this class instead of create many sub classes for interface. Due to the structure change, add new method in Player class, World class, and Room Class to work with the GameController class. Update the related interface as well. 
2. Remove ConsoleController interface.
3. Add FakeRandomNumberGenerator as sub class of RandomNumberGenerator.
4. Create a Mock model class to test. 
5. Add new test with updated design. The computer player is mimic as a human player now with random input.
6. Remove the methods which will return the object itself in WorldOutline interface. 
7. Remove console input class, add Controller Driver to run the code with main
8. Check the pdf with revised in res folder for updated design. (Project_Design_Paper_Prototyping 2_m2_revised.pdf)

### Key Features
- **World Class:** Constructs the game board and initializes the rooms based on predefined configurations.
- **Room and Target Setup:** Rooms are strategically placed within the world, and a target is assigned to one of these rooms.
- **Controller Class:** Using Command Line to run the game now. 

### How to Run My Code with Jar file
Find KillDoctorLucky.jar in the res folder, open the terminal in this folder. Execute the following command in terminal:
```bash
java -jar KillDoctorLucky.jar mansion.txt 1000
```
mansion.txt can be switch to other world model text file. 
1000 means number of turn, once the turn reach, the game will end. Each player's action represent a turn. 
There will be a txt file to show the example run to show some text info about how a test run is. The code you are running is in the src/WorldDriver.java

### Citation
1. https://www.codejava.net/ides/eclipse/how-to-create-jar-file-in-eclipse
2. https://www.tutorialspoint.com/java_dip/java_buffered_image.htm
3. https://introcs.cs.princeton.edu/java/windows/manual.php
4. https://www.theserverside.com/blog/Coffee-Talk-Java-News-Stories-and-Opinions/Run-JAR-file-example-windows-linux-ubuntu
5. https://www.educative.io/answers/how-to-generate-random-numbers-in-java
6. https://dev.to/drathi/how-to-mock-without-using-mockito-or-any-frameworks-1b5g

### Assumptions 
1. The pick up action will consume a turn though there is nothing to pick up.
2. Two rooms are in the same horizontal or vertical line without empty block between them, they can see each other. 

### Limitation
1. Computer player can only run randomly without trying to approach near Dr. Lucky.  

### Example Run Explain for M2
1. example_run.txt show example run with all requirements: adding a human-controlled player to the world
adding a computer-controlled player to the world
the player moving around the world
the player picking up an item
the player looking around
taking turns between multiple players
displaying the description of a specific player
displaying information about a specific space in the world
creating and saving a graphical representation of the world map to the current directory
2. example_run_one_room.txt show the game can still work with only one room. Look around can see the target in the same room. 
3. example_run_game_end.txt show the game will end once reach the max allowed turn. 
