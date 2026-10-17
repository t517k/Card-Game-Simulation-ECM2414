import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class CardGame {

    ArrayList<Card> pack = new ArrayList<>();
    ArrayList<CardDeck> decks = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<>();

    int playerTotal;
    int deckTotal;
    String packPath;

    private final AtomicBoolean gameOver = new AtomicBoolean(false);
    private final Object turnLock = new Object();

    public static void main(String[] args) throws IOException {
        CardGame currentGame = new CardGame();

        try (Scanner input = new Scanner(System.in)) {

            System.out.println("Game Start!");

            // Get the number of players
            while (true) {
                try {
                    System.out.print("Enter number of players: ");
                    int number = Integer.parseInt(input.nextLine());

                    if (number > 1) {
                        currentGame.playerTotal = number;
                        break;
                    }

                    System.out.println("Enter a number greater than 1.");

                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid integer.");
                }
            }

            // Get a valid pack file
            while (true) {
                System.out.println("Enter path of the chosen pack file:");
                String path = input.nextLine();

                if (currentGame.checkPack(currentGame.playerTotal, path)) {
                    currentGame.packPath = path;
                    break;
                }

                System.out.println("Invalid pack. Please try again.");
            }
        }

        currentGame.loadPack(
                currentGame.playerTotal,
                currentGame.packPath
        );

        currentGame.initialiseGame();
        currentGame.startGame();
    }

    // Validate the pack file
    private boolean checkPack(int playerTotal, String packPath) {
        int requiredCards = playerTotal * 8;
        int count = 0;

        try (Scanner reader = new Scanner(new File(packPath))) {

            while (reader.hasNext()) {
                if (!reader.hasNextInt()) {
                    System.out.println(
                            "Invalid pack: every entry must be an integer."
                    );
                    return false;
                }

                int value = reader.nextInt();

                if (value < 0) {
                    System.out.println(
                            "Invalid pack: card values cannot be negative."
                    );
                    return false;
                }

                count++;
            }

            if (count != requiredCards) {
                System.out.println(
                        "Invalid pack: expected " + requiredCards
                                + " cards, found " + count + "."
                );
                return false;
            }

            return true;

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            return false;
        }
    }

    // Load cards from the pack file
    public void loadPack(int playerTotal, String packPath) {
        pack.clear();

        try (Scanner reader = new Scanner(new File(packPath))) {

            while (reader.hasNextInt()) {
                int cardValue = reader.nextInt();

                // Preserving your existing Card constructor
                Card card = new Card(cardValue);
                pack.add(card);
            }

        } catch (FileNotFoundException e) {
            throw new IllegalStateException(
                    "Could not load the validated pack.", e
            );
        }
    }

    // Create players and decks, then deal the cards
    private void initialiseGame() {

        deckTotal = playerTotal;

        // Create one deck and one player for each player number
        for (int i = 0; i < playerTotal; i++) {
            decks.add(new CardDeck());
        }

        for (int i = 0; i < playerTotal; i++) {

            CardDeck leftDeck = decks.get(i);
            CardDeck rightDeck = decks.get(
                    (i + 1) % playerTotal
            );

            Player player = new Player(
                    i + 1,
                    gameOver,
                    leftDeck,
                    rightDeck,
                    turnLock
            );

            players.add(player);
        }

        // Deal four cards to each player, round-robin
        for (int round = 0; round < 4; round++) {
            for (Player player : players) {
                player.addInitialCard(pack.remove(0));
            }
        }

        // Distribute remaining cards to decks, round-robin
        int deckIndex = 0;

        while (!pack.isEmpty()) {
            decks.get(deckIndex).addCard(pack.remove(0));

            deckIndex = (deckIndex + 1) % deckTotal;
        }

        System.out.println("Cards dealt successfully.");
    }

    // Start every player thread
    private void startGame() throws IOException {

        ArrayList<Thread> threads = new ArrayList<>();

        for (Player player : players) {
            Thread thread = new Thread(player);
            threads.add(thread);
            thread.start();
        }

        // Wait for all player threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                gameOver.set(true);
                return;
            }
        }

        for (CardDeck deck : decks) {
            deck.deckOutput();
        }
        System.out.println("Game finished.");
    }
}
