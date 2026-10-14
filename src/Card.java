public class Card {
    static int numOfCards = 0;
    int cardId;
    int cardValue;

    public Card(int numOfCards, int cardValue) {
        this.cardId = ++numOfCards;
        this.cardValue = cardValue;
    }

    public int getValue(){
        return cardValue;
    }
}
