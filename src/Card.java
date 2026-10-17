public class Card {
    private static int numOfCards = 0;

    private final int cardId;
    private final int cardValue;

    public Card(int cardValue) {
        this.cardId = ++numOfCards;
        this.cardValue = cardValue;
    }

    public int getValue() {
        return cardValue;
    }

    @Override
    public String toString() {
        return String.valueOf(cardValue);
    }
}