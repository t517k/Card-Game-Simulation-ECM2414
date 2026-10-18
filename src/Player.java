//imports
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

//
public class Player implements  Runnable {
    static int winnerId; //stores id of winning player
    int playerId;
    private final AtomicBoolean gameOver; //shared flag
    private final CardDeck leftDeck; //draw deck
    private final CardDeck rightDeck; // discard deck
    private final Object turnLock; //lock that ensures that only one player can execute their turn logic at a time
    private BufferedWriter playerOutput;
    ArrayList<Card> playerHand = new ArrayList<>();

    //constructor
    public Player(int playerId, AtomicBoolean gameOver, CardDeck leftDeck, CardDeck rightDeck, Object turnLock) {
        this.playerId = playerId;
        this.gameOver = gameOver;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.turnLock = turnLock;
    }

    //checks for winning hand
    private boolean hasWon() {
        if (playerHand.size() != 4) { //is hand exactly 4 card
            return false;
        }

        int firstValue = playerHand.get(0).getValue(); //saves value of first card

        for (int i = 1; i < playerHand.size(); i++) { //compares every other card with value of first card
            if (playerHand.get(i).getValue() != firstValue) {
                return false;
            }
        }
        winnerId = playerId; //player wins if all 4 cards are the same
        return true;
    }

    //adds card to players hand
    public void addInitialCard(Card card) {
        playerHand.add(card);
    }

    //creates the player output file for each player
    private void createPlayerOutput() throws IOException {
        String filename = "player" + playerId + "_output.txt";
        playerOutput = new BufferedWriter(new FileWriter(filename));
    }

    //records player actions
    private void writeToOutput(String message) throws IOException {
        playerOutput.write(message);
        playerOutput.newLine();
        playerOutput.flush(); //pushes text to the file so that it is available to read without waiting for buffer to fill
    }

    private void takeTurn() throws IOException {
        //ensures that only one player at a time can execute the protected section
        synchronized (turnLock) {

            //prevents a player from taking a go if another player has won
            if (gameOver.get()) {
                return;
            }

            //draws card from the deck on the left
            Card drawnCard = leftDeck.drawCard();

            //checks if deck is empty
            if (drawnCard == null) {
                throw new IllegalStateException("Left deck is empty for player " + playerId);
            }
            //card added to player's hand
            playerHand.add(drawnCard);
            //records the card drawn in the output file
            writeToOutput("Player " + playerId + " draws a " + drawnCard.getValue() + " from deck " + leftDeck.getDeckId());

            // selects and discards a card
            Card discardedCard = null;

            //searches for a card that doesn't match the player's id
            for (int i = 0; i < playerHand.size(); i++) {
                Card card = playerHand.get(i);

                //when a card with a different value is found it is removed from the hand
                if (card.getValue() != playerId) {
                    discardedCard = playerHand.remove(i);
                    break;
                }
            }

            //in the case where all 5 cards match the player's id
            if (discardedCard == null) {
                // No card could be discarded under the current rule
                throw new IllegalStateException("No discard available for player " + playerId);
            }

            // adds the discarded card to the right deck
            rightDeck.addCard(discardedCard);
            //records discarded card and plauer hand in corresponding output file
            writeToOutput("Player " + playerId + " discards a " + discardedCard.getValue() + " to deck " + rightDeck.getDeckId());
            writeToOutput("Player " + playerId + "'s current hand is: " + playerHand);

            //checks for a winner before another turn
            if (hasWon()) {
                writeToOutput("Player " + playerId + " wins!");
                System.out.println("Player " + playerId + " wins!");
                gameOver.set(true);
            }
        }
    }

    @Override
    public void run() {
        //initialises the player output
        try {
            createPlayerOutput();
            writeToOutput("Player " + playerId + " has started");
            writeToOutput("Player " + playerId + " initial hand: " + playerHand);

            //checks the initial hand
            synchronized (turnLock) {
                if (hasWon()) {
                    writeToOutput("Player " + playerId + " wins!");
                    System.out.println("Player " + playerId + " wins!");
                    gameOver.set(true);
                }
            }

            //repeats turn until there is a winnr
            while (!gameOver.get()) {
                takeTurn();
            }
            //informs other players that a player has won
            if (playerId != winnerId) {
                writeToOutput("Player " + winnerId + " has informed Player " + playerId + " that Player " + winnerId + " has won");
            }
            //records the final hand
            writeToOutput("Player " + playerId + " has finished");
            writeToOutput("Player " + playerId + " final hand: " + playerHand);

        //catches any file related errors
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if (playerOutput != null) {
                    playerOutput.close(); //closes player output file
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}