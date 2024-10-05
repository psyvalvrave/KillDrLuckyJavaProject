# CS5010-DrLuckyGame
This is a class project to kill a game base with java. It is similar to the board game Kill Doctor Lucky, but it has some different rules. 

## Milestone 1: Game Board Creation

In this initial milestone, the focus was on laying the foundation of the game environment. We successfully implemented the creation of the game board using the `World` class, and defined the `Room` and `Target` entities within the game.

### Updates from preliminary design:
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



### Key Features
- **World Class:** Constructs the game board and initializes the rooms based on predefined configurations.
- **Room and Target Setup:** Rooms are strategically placed within the world, and a target is assigned to one of these rooms.

### How to View the Output
First able, you have to move KillDoctorLucky.jar in the res folder in to the root directory of this project, which is the same directory as src, test, res folder.
Then, to see the game board and the target's location, run the provided JAR file with the following command:

```bash
java -jar KillDoctorLucky.jar.jar
```

There will be a txt file to show the example run to show some text info about how a test run is. The code you are running is in the src/WorldDriver.java

### Citation