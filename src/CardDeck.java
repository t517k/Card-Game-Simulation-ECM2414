import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;

public class CardDeck {
    int numberOfDecks = 0;
    ArrayList<Card> cards;
    int deckId;

    public CardDeck(int numberOfDecks) {
        this.deckId = ++numberOfDecks;
    }

    public int addCard(int cards) {
        cards.addLast(cards);
    }

    public int drawCard() {
        return cards.removeFirst();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }

}