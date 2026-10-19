//imports
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicBoolean;


public class CardDeck {

    private static int numberOfDecks = 0;
    private final Deque<Card> cards; //stores cards currently in the deck
    private final int deckId;

    //constructor
    public CardDeck() {
        this.deckId = ++numberOfDecks;
        this.cards = new ArrayDeque<>();
    }

    //adds card to the back of the deck
    //synchronised ensures that only one thread at a time can access this particular deck object
    public synchronized void addCard(Card card) {
        cards.addLast(card);
        notifyAll();
    }

    //waits until a card is available in the deck
    //only one threw at a time can execute it
    public synchronized void waitForCard(AtomicBoolean gameOver)
            throws InterruptedException {
        //pauses current thread until deck has a card or game is over
        while (cards.isEmpty() && !gameOver.get()) {
            wait();
        }
    }

    //draws a card from the front of the deck
    public synchronized Card drawCard() {
        if (cards.isEmpty()) { //checks if there is a card at the front of the deck
            return null;
        }

        return cards.removeFirst();
    }

    //checks whether the deck is empty
    public synchronized boolean isEmpty() {
        return cards.isEmpty();
    }

    //returns the number of cards in the deck
    public synchronized int size() {
        return cards.size();
    }

    //returns the deck's id
    public int getDeckId() {
        return deckId;
    }

    //writes the deck's contents to a text file after the game finishes
    public void deckOutput() throws IOException {
        String filename = "deck" + deckId + "_output.txt";

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(filename))) {

            writer.write("Deck " + deckId + " contents: ");

            boolean firstCard = true;
            for (Card card : cards) {
                if (!firstCard) {
                    writer.write(" ");
                }
                writer.write(String.valueOf(card.getValue()));
                firstCard = false;
            }
        }
    }

    public Card getTopCard() {
        return cards.getFirst();
    }

    public Card getBottomCard() {
        return cards.getLast();
    }
}
