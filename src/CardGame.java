//imports
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class CardGame {
    //variables used throughout the game
    ArrayList<Card> pack = new ArrayList<>();
    ArrayList<CardDeck> decks = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<>();

    int playerTotal;
    int deckTotal;
    String packPath;

    private final AtomicBoolean gameOver = new AtomicBoolean(false);
    private final Object turnLock = new Object();

    //execution begins here
    public static void main() throws IOException {
        CardGame currentGame = new CardGame();

        try (Scanner input = new Scanner(System.in)) {
        //starts the game
            System.out.println("Game Start!");

            // gets the number of players and will loop infinitely until a valid input is entered
            while (true) {
                try {
                    System.out.print("Enter number of players: ");
                    int number = Integer.parseInt(input.nextLine()); //converts text entered into an integer

                    if (number > 1) { //checks game has at least 2 players
                        currentGame.playerTotal = number;
                        break;
                    }

                    System.out.println("Enter a number greater than 1.");

                } catch (NumberFormatException e) { //checks that input is an integer
                    System.out.println("Please enter a valid integer.");
                }
            }

            // gets input file path and will also loop until valid path is entered
            while (true) {
                System.out.println("Enter path of the chosen pack file:");
                String path = input.nextLine();

                if (currentGame.checkPack(currentGame.playerTotal, path)) {
                    currentGame.packPath = path; //input file path is saved if it is valid
                    break;
                }

                System.out.println("Invalid pack. Please try again.");
            }
        }

        currentGame.loadPack( currentGame.playerTotal, currentGame.packPath); // loads cards from the deck
        currentGame.initialiseGame(); //starts game and creates players, decks and starting hands
        currentGame.startGame(); //starts player threads
    }

    //checks if the pack file contains 8n cards
    private boolean checkPack(int playerTotal, String packPath) {
        int requiredCards = playerTotal * 8;
        int count = 0; //tracks the number of cards in the file

        //opens file
        try (Scanner reader = new Scanner(new File(packPath))) {

            //checks if the next entry in the file is an integer
            while (reader.hasNext()) {
                if (!reader.hasNextInt()) {
                    System.out.println("Invalid pack: every entry must be an integer.");
                    return false;
                }
                int value = reader.nextInt();

                //checks for negative values
                if (value < 0) {
                    System.out.println("Invalid pack: card values cannot be negative.");
                    return false;
                }

                count++;
            }
            //checks if the count is equal to the 8n number of cards required
            if (count != requiredCards) {
                System.out.println("Invalid pack: expected " + requiredCards + " cards, found " + count + ".");
                return false;
            }

            return true;

        //if file doesn't exist or can't be opened the exception deals with it
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            return false;
        }
    }

    // load cards from the pack file
    public void loadPack(int playerTotal, String packPath) {
        pack.clear(); //removes cards that are already in the list

        try (Scanner reader = new Scanner(new File(packPath))) {
            //reads the cards value
            while (reader.hasNextInt()) {
                int cardValue = reader.nextInt();

                //creates new card object and adds it to the pack
                Card card = new Card(cardValue);
                pack.add(card);
            }

        // if file cannot be opened during loading exception is thrown
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Could not load the validated pack.", e);
        }
    }

    // creates players and decks and then deal the cards
    private void initialiseGame() {
        //setting number of decks
        deckTotal = playerTotal;

        // creates one deck for each player number
        for (int i = 0; i < playerTotal; i++) {
            decks.add(new CardDeck());
        }

        for (int i = 0; i < playerTotal; i++) {

            //assigns the players left and right deck
            CardDeck leftDeck = decks.get(i);
            CardDeck rightDeck = decks.get(
                    (i + 1) % playerTotal //modulo is used to wrap the final player's right deck back to one
            );

            //creates player object
            Player player = new Player(
                    i + 1,
                    gameOver,
                    leftDeck,
                    rightDeck,
                    turnLock //shared so turns can be synchronised
            );

            players.add(player);
        }

        // deals initial four cards to each player
        for (int round = 0; round < 4; round++) {
            for (Player player : players) {
                player.addInitialCard(pack.remove(0));
            }
        }

        // distributes remaining cards to decks
        int deckIndex = 0;

        while (!pack.isEmpty()) {
            decks.get(deckIndex).addCard(pack.remove(0));

            deckIndex = (deckIndex + 1) % deckTotal;
        }

        System.out.println("Cards dealt successfully.");
    }

    // starts every player's thread
    private void startGame() throws IOException {

        ArrayList<Thread> threads = new ArrayList<>(); //stores threads so the main program can wait for them to finish

        for (Player player : players) {
            Thread thread = new Thread(player); //creates thread that will execute player's run method
            threads.add(thread); //saves the thread reference
            thread.start(); //starts execution on a separate thread
        }

        // waits for all player threads to finish
        for (Thread thread : threads) {
            try {
                thread.join(); //makes main thread wait until the relevant player has finished
            } catch (InterruptedException e) { //if the main thread is interrupted while waiting then the game is over
                Thread.currentThread().interrupt();
                gameOver.set(true);
                return;
            }
        }

        //records the final state of each deck and outputs that the game has finished
        for (CardDeck deck : decks) {
            deck.deckOutput();
        }
        System.out.println("Game finished.");
    }
}
