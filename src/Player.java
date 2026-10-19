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

    public int handSize() {
        return playerHand.size();
    }

    public ArrayList<Card> getPlayerHand() {
        return playerHand;
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

        return true; //player wins if all 4 cards are the same
    }

    //only first player that wins can declare victory to ensure that only one winner is declared
    private boolean declareWinner() {
        synchronized (turnLock) {
            if (!gameOver.get() && hasWon()) {
                winnerId = playerId;
                gameOver.set(true);
                return true;
            }

            return false;
        }
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


    private void takeTurn() throws IOException, InterruptedException {

        while (true) {
            //waits for a card
            synchronized (turnLock) {
                while (leftDeck.isEmpty() && !gameOver.get()) {
                    turnLock.wait();
                }
                if (gameOver.get()) {
                    return;
                }
            }

            //locks both decks to avoid deadlock
            CardDeck firstDeck =
                    leftDeck.getDeckId() < rightDeck.getDeckId()
                            ? leftDeck : rightDeck;

            CardDeck secondDeck =
                    firstDeck == leftDeck ? rightDeck : leftDeck;

            //when this becomes true, the player has finished drawing and discarding
            boolean actionCompleted = false;

            //makes sure the player has the locks for both decks before proceeding
            synchronized (firstDeck) {
                synchronized (secondDeck) {

                    //checks if another player has already won
                    if (gameOver.get()) {
                        return;
                    }
                    //checks again because another player could've drawn the last card between this check and the last
                    if (leftDeck.isEmpty()) {
                        continue;
                    }

                    //draws a card and adds it to players hand
                    Card drawnCard = leftDeck.drawCard();
                    playerHand.add(drawnCard);
                    writeToOutput("Player " + playerId + " draws a " + drawnCard.getValue() + " from deck " + leftDeck.getDeckId());

                    //chooses card to discard from player's hand
                    Card discardedCard = null;

                    //finds the first card that differs from the player id and discards it
                    for (int i = 0; i < playerHand.size(); i++) {
                        if (playerHand.get(i).getValue() != playerId) {
                            discardedCard = playerHand.remove(i);
                            break;
                        }
                    }
                    //if no suitable card found then exception thrown
                    if (discardedCard == null) {
                        throw new IllegalStateException(
                                "No discard available for player " + playerId
                        );
                    }

                    //discards a card before releasing deck lock
                    rightDeck.addCard(discardedCard);

                    writeToOutput("Player " + playerId + " discards a " + discardedCard.getValue() + " to deck " + rightDeck.getDeckId());

                    writeToOutput("Player " + playerId + "'s current hand is: " + playerHand);
                    //actionCompleted is now true so the player's go is over
                    actionCompleted = true;
                }
            }
            //checks if player has a winning hand and wakes threads waiting on turnlock
            if (actionCompleted) {
                synchronized (turnLock) {

                    if (declareWinner()) {
                        writeToOutput("Player " + playerId + " wins!");
                        System.out.println("Player " + playerId + " wins!");
                    }


                    turnLock.notifyAll();
                }
            }

            return;
        }
    }


    @Override
    public void run() {
        //initialises the player output
        try {
            //player output is created
            createPlayerOutput();

            writeToOutput("Player " + playerId + " has started");
            writeToOutput("Player " + playerId + " initial hand: " + playerHand);

            boolean alreadyFinished;

            synchronized (turnLock) {
                //checks the initial hand
                if (!gameOver.get()) {
                    declareWinner();
                }

                alreadyFinished = gameOver.get();
            }

            //used to check if a player has already won at the start of the game
            if (alreadyFinished) {
                if (playerId == winnerId) {
                    writeToOutput("Player " + playerId + " wins");
                    System.out.println("Player " + playerId + " wins");
                } else {
                    writeToOutput("Player " + winnerId
                            + " has informed Player " + playerId
                            + " that Player " + winnerId + " has won!");
                }

                //outputs player's final hand
                writeToOutput("Player " + playerId + " final hand: " + playerHand);
                return;
            }

            //players keep on taking turns until a player has one
            while (!gameOver.get()) {
                takeTurn();
            }
            //outputs once player has won
            if (playerId != winnerId) {
                writeToOutput("Player " + winnerId + " has informed Player " + playerId + " that Player " + winnerId + " has won");
            }
            writeToOutput("Player " + playerId + " has finished");
            writeToOutput("Player " + playerId + " final hand: " + playerHand);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (playerOutput != null) {
                    playerOutput.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
