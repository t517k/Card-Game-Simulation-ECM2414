
import java.util.ArrayDeque;
import java.util.Deque;

public class CardDeck {

    private static int numberOfDecks = 0;

    private final Deque<Card> cards;
    private final int deckId;

    // Constructor: creates a new, uniquely numbered deck
    public CardDeck() {
        this.deckId = ++numberOfDecks;
        this.cards = new ArrayDeque<>();
    }

    // Adds a card to the back of the deck
    public synchronized void addCard(Card card) {
        cards.addLast(card);
    }

    // Draws a card from the front of the deck
    public synchronized Card drawCard() {
        if (cards.isEmpty()) {
            return null;
        }

        return cards.removeFirst();
    }

    // Checks whether the deck is empty
    public synchronized boolean isEmpty() {
        return cards.isEmpty();
    }

    // Returns the number of cards in the deck
    public synchronized int size() {
        return cards.size();
    }

    // Returns this deck's ID
    public int getDeckId() {
        return deckId;
    }
}
