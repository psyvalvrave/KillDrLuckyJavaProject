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

## Milestone 3: Gameplay

In this initial milestone, the goal is finish the full game can run with command line. Adding one more variable for the game pet to players need to consider more things while trying to murder the target. In this milestone 3, the target can be killed and end the game.

### Updates from preliminary design m3:
1. Following command pattern design, instead of calling private method inside the controller class, create new corresponding command object when each human player decide what to do. Creating a Command interface with multiple concrete command class for different purpose while running the game.
2. Add more methods in my WorldOutline interface to assist the controller. Also make more functions in world class to call different methods to make other class can work with world model properly. (`canInteractWithPet(int playerId)`, `getTargetHealthPoint()`, `getPlayerLocation(int playerId)`, etc )
3. For pet path, creating `+ initializePetDFS()`, `- dfsVisit(Block room, Set<Integer> visited, Stack<Block> path)` in world class to create DFS path with Stack. Also add methods in pet class to update the path (we are using stack, so update the path with `.pop` instead of a loop).
4. Add more private fields in monk model to help testing part.
5. Add more test to check can the game run well with different condition. Such as the success of murder should determined by other players' location. Add more tests to make sure the expected result is correct when players are in different location.
6. Check the pdf with revised in res folder for updated design. (m3_Revisited_Design.pdf)

### Key Features
- **World Class:** Constructs the game board and initializes the rooms based on predefined configurations.
- **Room and Target Setup:** Rooms are strategically placed within the world, and a target is assigned to one of these rooms.
- **Player:** player can perform actions to play the game. Use correct strategy to win! We have computer player can do something like human player to player with.
- **Controller Class:** Using Command Line to run the game now. 
- **Game End:** The game can end when max turn reach or target's hp drop to 0 or below. We have a full CLI game now.

### How to Run My Code with Jar file
Find KillDoctorLucky.jar in the res folder, open the terminal in this folder. Execute the following command in terminal:
```bash
java -jar KillDoctorLucky.jar mansion.txt 1000
```
mansion.txt can be switch to other world model text file. I have one_room.txt and three_rooms_player_test.txt to play around and test in res folder.
1000 means number of turn, once the turn reach, the game will end. Each player's action represent a turn. 
There will be a txt file to show the example run to show some text info about how a test run is. The code you are running is in the src/WorldDriver.java
Once you see the line say "Setting up the game.", the CLI game is starting, and you can follow the instruction(text) to play the game.

### Citation
1. https://www.codejava.net/ides/eclipse/how-to-create-jar-file-in-eclipse
2. https://www.tutorialspoint.com/java_dip/java_buffered_image.htm
3. https://introcs.cs.princeton.edu/java/windows/manual.php
4. https://www.theserverside.com/blog/Coffee-Talk-Java-News-Stories-and-Opinions/Run-JAR-file-example-windows-linux-ubuntu
5. https://www.educative.io/answers/how-to-generate-random-numbers-in-java
6. https://dev.to/drathi/how-to-mock-without-using-mockito-or-any-frameworks-1b5g
7. https://www.baeldung.com/java-depth-first-search

### Assumptions 
1. The pick up action will consume a turn though there is nothing to pick up.
2. Two rooms are in the same horizontal or vertical line without empty block between them, they can see each other. 
3. When players try to check detail of rooms by looking around, they cannot see the detail of room with the pet in it. 
4. The pet will move by a DFS pattern. Once it reaches the last room, it will create new DFS path and move again. If the map has the room cannot never be reach, then the pet will not run through all the rooms since it only can move into neighbor room unlike target.
5. When player stays in the same room with pet and try to do a murder attempt. Though there are other players in neighbor room, the attack should still be successful since the pet will block the sight of other players. 
6. The minimal damage can be done to the target is 1 if the attack is successful.                           
7. Player need to use look around to know the item murder point and location. This will take a turn as gathering information. 
8. For human player, they can know where they are and what they are carrying at start of their turn. 

### Limitation
1. Computer player can only run randomly without trying to approach near Dr. Lucky, although the computer player always try to attack when they got chance.
2. Player need to keep save and update the world.png to know the visualization of the game. Only text in terminal may be difficult to understand. 

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

### Example Run Explain for m3
I have two different example run to show the game
1. example_run_m3.txt is a run with all new add game functions display (Move Pet, Attack, Pet, Display info for each turn, etc). We can see for each human turn, they will see their location and what items they are carrying. In turn 2, computer player just start attacking since he is in the same room with the target. Since he does not have any item, so he just attack with bare hand and make 1 damage. Though there are other player(Move Pet Human) in neighbor room Dinning Hall, the pet in the target room block his sight so the attack was successful. In turn 3, the Move Pet Human player is in the same room as the pet, so he successfully move the pet to the other position(Lancaster Room with ID 10). In turn 7, the player Attack Human put 10 in command line to check the target info, and once he realizes he is in the same room with target, he simply uses Trowel he previously picked up to attack and deal 2 damage. Since it took too long to finish the entire game, the game is being forced to quit by putting 0.
2. example_run_m3 - One_Shot.txt is the other run with modified item damage. The two item in Carriage House Big Red Hammer and Chain Saw were being modified to have murder point 51, which potentially will one shot the target. The first 2 turns just allow the player to pick up both item. In turn 3, he just one of item to kill the target. The game end message is displayed when target's health drops to 0. It declares winner name One Shot.
