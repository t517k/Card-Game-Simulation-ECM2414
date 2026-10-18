public class Card {
    private static int numOfCards = 0;
    private final int cardId;
    private final int cardValue;
    //final attributes ensure that values are not changed after the constructor sets a value

    //constructor
    public Card(int cardValue) {
        this.cardId = ++numOfCards;
        this.cardValue = cardValue;
    }

    //getter method  to get cardValue as it is private
    public int getValue() {
        return cardValue;
    }

    //overrides default toString method so that printing a card displays its value instead of its object reference
    @Override
    public String toString() {
        return String.valueOf(cardValue);
    }
}