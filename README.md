# Card Game README

This circular card game that involves players getting a full hand of preferred cards by drawing from a deck to the left of each player and depositing unwanted cards to a deck on the right. This game uses threads to run the code for each separate player concurrently. [Github repository](https://github.com/t517k/Card-Game-Simulation-ECM2414)

This README is bundled in a zip with all source code files, bytecode files and other required files that allow the program to run. It also contains JUnit test classes and the files required to run those correctly.

There is also an executable jar file named cards.jar which is part of the main coursework zip that is handed in and running instruction will be provided in this README.

## Running Instructions
To compile this code and access tests and IDE of your choosing is required along with the zip files provided on ele or the github repo.

### Dependencies
- Microsoft OpenJDK 25.0.4
- JUnit 5
- Compatible IDE

### Main program
1. Extract the zip named `cardsTest` to a location on the device
2. Open the `cardsTest` folder that was previously extracted using an IDE that is compatible with java
3. Select the `CardGame` file which contains the main method
4. Make sure the correct JDK is selected and run the program
5. Follow instructions in the terminal and pass in correct inputs
6. Observe results in output files and in the terminal

### Tests
1. Extract the zip named `cardsTest` to a location on the device
2. Open the `cardsTest` folder that was previously extracted using an IDE that is compatible with java
3. Navigate the the `Tests` folder to find each JUnit test class
4. Run all or run separately and observe test results within the IDE

### Executable Jar
1. Make sure the latest JDK is installed on the system to allow the file to be executed
2. Place the `cards.jar` file in a location that is convenient
3. Open a command line in this location and and enter the following command:
	- `java -jar cards.jar`
4. Follow instructions in the command line and pass in correct inputs
5. Files with results will be created in the same directory as the `cards.jar`  file

## File Structure
The following layout is the correct file structure after extracting `cardsTest.zip`

cardsTest
-  src 
	-  Card.class
	-  Card.java
	-  CardDeck.class
	-  CardDeck.java
	-  CardGame.class
	-  CardGame.java
	-  Player.class
	-  Player.java
	-  two.txt
	-  three.txt
	-  four.txt
	-  five.txt
- Tests
	- test_resources
		- test_deck_setup.txt
		- test_player1_instant_win_setup.txt
		- test_player1_setup.txt
		- test_player2_instant_win_setup.txt
		- test_player2_setup.txt
	- DeckTests.java
	- GameOutputTests,java
	- GameTests.java
	- PlayerTests.java
 
## Contributors
Oliver San-Blas

Trystan Keigher