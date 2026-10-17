import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Player implements  Runnable {
    static int winnerId;
    int playerId;
    private final AtomicBoolean gameOver;
    private final CardDeck leftDeck;
    private final CardDeck rightDeck;
    private final Object turnLock;
    private BufferedWriter playerOutput;
    ArrayList<Card> playerHand = new ArrayList<>();

    public Player(int playerId, AtomicBoolean gameOver, CardDeck leftDeck, CardDeck rightDeck, Object turnLock) {
        this.playerId = playerId;
        this.gameOver = gameOver;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.turnLock = turnLock;
    }

    private boolean hasWon() {
        if (playerHand.size() != 4) {
            return false;
        }

        int firstValue = playerHand.get(0).getValue();

        for (int i = 1; i < playerHand.size(); i++) {
            if (playerHand.get(i).getValue() != firstValue) {
                return false;
            }
        }
        winnerId = playerId;
        return true;
    }

    public void addInitialCard(Card card) {
        playerHand.add(card);
    }

    private void createPlayerOutput() throws IOException {
        String filename = "player" + playerId + "_output.txt";
        playerOutput = new BufferedWriter(new FileWriter(filename));
    }

    private void writeToOutput(String message) throws IOException {
        playerOutput.write(message);
        playerOutput.newLine();
        playerOutput.flush();
    }

    private void takeTurn() throws IOException {

        synchronized (turnLock) {

            // Another player may have won while this thread waited
            if (gameOver.get()) {
                return;
            }

            // Draw a card from the left deck
            Card drawnCard = leftDeck.drawCard();

            if (drawnCard == null) {
                throw new IllegalStateException("Left deck is empty for player " + playerId);
            }

            playerHand.add(drawnCard);

            writeToOutput("Player " + playerId + " draws a " + drawnCard.getValue() + " from deck " + leftDeck.getDeckId());

            // Select and remove a card to discard
            Card discardedCard = null;

            for (int i = 0; i < playerHand.size(); i++) {
                Card card = playerHand.get(i);

                if (card.getValue() != playerId) {
                    discardedCard = playerHand.remove(i);
                    break;
                }
            }

            if (discardedCard == null) {
                // No card could be discarded under the current rule
                throw new IllegalStateException("No discard available for player " + playerId);
            }

            // Add the discarded card to the right deck
            rightDeck.addCard(discardedCard);

            writeToOutput("Player " + playerId + " discards a " + discardedCard.getValue() + " to deck " + rightDeck.getDeckId());

            writeToOutput("Player " + playerId + "'s current hand is: " + playerHand);

            // Check for a winner before allowing another turn
            if (hasWon()) {
                writeToOutput("Player " + playerId + " wins!");
                System.out.println("Player " + playerId + " wins!");
                gameOver.set(true);
            }
        }
    }

    @Override
    public void run() {

        try {
            createPlayerOutput();

            writeToOutput("Player " + playerId + " has started");
            writeToOutput("Player " + playerId + " initial hand: " + playerHand);

            synchronized (turnLock) {
                if (hasWon()) {
                    writeToOutput("Player " + playerId + " wins!");
                    System.out.println("Player " + playerId + " wins!");
                    gameOver.set(true);
                }
            }

            while (!gameOver.get()) {
                takeTurn();
            }
            if (playerId != winnerId) {
                writeToOutput("Player " + winnerId + " has informed Player " + playerId + " that Player " + winnerId + " has won!");
            }
            writeToOutput("Player " + playerId + " has finished");
            writeToOutput("Player " + playerId + " final hand: " + playerHand);

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