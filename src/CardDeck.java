import java.util.ArrayList;

public class CardDeck {
    int numberOfDecks = 0;
    ArrayList<Card> cards;
    int deckId;

    public CardDeck(int numberOfDecks) {
        this.deckId = ++numberOfDecks;
    }
}
