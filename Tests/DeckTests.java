import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DeckTests {
    private CardDeck deck;

    @BeforeEach // Sets up the deck and allocates card objects from the specified file
    void setUp() throws FileNotFoundException {
        deck = new CardDeck();
        Scanner input = new Scanner(new File("Tests/test_resources/test_deck_setup.txt"));
        while (input.hasNextLine()) {
            int cardValue = Integer.parseInt(input.nextLine());
            deck.addCard(new Card(cardValue));
        }
        input.close();
    }

    // Test to make sure the created deck is of the correct length of 4 cards
    @Test
    void checkSize() {
        assertFalse(deck.isEmpty());
        assertEquals(4,  deck.size());
    }

    // Test to check that the deck behaves like as they should, based on the spec. This means that they should behave
    // as a double ended queue and cards are removed from the front (top of deck) and added to the back (bottom of deck).
    // It is important to test this to make sure that cards move around the circle of players and decks correctly
    @Test
    void  checkFIFOofDeck() {
        Card card = new Card(5);
        deck.addCard(card);
        assertEquals(1, deck.drawCard().getValue());
        assertEquals(5, deck.getBottomCard().getValue());
        assertEquals(2, deck.getTopCard().getValue());
    }
}