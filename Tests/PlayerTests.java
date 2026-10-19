import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerTests {
    ArrayList<CardDeck> decks = new ArrayList<>();
    ArrayList<Player> players = new  ArrayList<>();
    private final AtomicBoolean gameOver = new AtomicBoolean(false);
    private final Object turnLock = new Object();

    // Sets up the decks and players and allocates card objects from the specified files to the objects ready for the
    // tests of the Player class and its functionality
    @BeforeEach
    void setUp() throws FileNotFoundException {
        decks.add(new CardDeck());
        decks.add(new CardDeck());
        Scanner deckInput = new Scanner(new File("Tests/test_resources/test_deck_setup.txt"));
        while (deckInput.hasNextLine()) {
            int cardValue = Integer.parseInt(deckInput.nextLine());
            decks.getFirst().addCard(new Card(cardValue));
            decks.get(1).addCard(new Card(cardValue));
        }
        deckInput.close();

        Scanner input1 = new Scanner(new File("Tests/test_resources/test_player1_setup.txt"));
        Player player1 = new Player(1, gameOver, decks.getFirst(), decks.get(1), turnLock);
        while (input1.hasNextLine()) {
            int cardValue = Integer.parseInt(input1.nextLine());
            player1.addInitialCard(new Card(cardValue));
        }
        input1.close();
        players.add(player1);
        Scanner input2 = new Scanner(new File("Tests/test_resources/test_player2_setup.txt"));
        Player player2 = new Player(2, gameOver, decks.getFirst(), decks.get(1), turnLock);
        while (input2.hasNextLine()) {
            int cardValue = Integer.parseInt(input2.nextLine());
            player2.addInitialCard(new Card(cardValue));
        }
        input2.close();
        players.add(player2);
    }

    // This test makes sure that the player hand retains a consistent value of 4 cards after an atomic action (the whole
    // turn of the current player). The test is required as the win condition requires that the player has a full hand
    // of their preferred cards. The tests compare the size of the player's hand ArrayList before the turn and then again
    // afterwards to check consistency
    @Test
    void playerHandSizeConsistency() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method takeTurnMethod = Player.class.getDeclaredMethod("takeTurn");
        takeTurnMethod.setAccessible(true);
        Method createPlayerOutputMethod = Player.class.getDeclaredMethod("createPlayerOutput");
        createPlayerOutputMethod.setAccessible(true);

        Player testPlayer = players.getFirst();
        int initialSize = testPlayer.handSize();
        createPlayerOutputMethod.invoke(testPlayer);
        takeTurnMethod.invoke(testPlayer);
        int finalSize = testPlayer.handSize();
        assertEquals(initialSize, finalSize);
    }

    // This test checks that the cards that have been loaded from the pack still remain in play once a full round of turns
    // has been completed. This is to make sure that no cards are completely discarded from play by checking the total
    // size of both player hands and also deck contents against a predetermined value, based on the total number of values
    // provided in the files
    @Test
    void cardConsistency() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method takeTurnMethod = Player.class.getDeclaredMethod("takeTurn");
        takeTurnMethod.setAccessible(true);
        Method createPlayerOutputMethod = Player.class.getDeclaredMethod("createPlayerOutput");
        createPlayerOutputMethod.setAccessible(true);

        createPlayerOutputMethod.invoke(players.getFirst());
        takeTurnMethod.invoke(players.getFirst());
        createPlayerOutputMethod.invoke(players.get(1));
        takeTurnMethod.invoke(players.get(1));

        int total = 0;

        for (Player player : players) {
            total = total + player.handSize();
        }
        for (CardDeck deck : decks) {
            total = total + deck.size();
        }

        assertEquals(16, total);
    }

    // This tests the player functionality of card discarding. It checks that the player will discard the first card in
    // their hand that does not match the preferred value. The test compares the second value of the hand before and
    // after a turn checking the value against a pre-defined number that should be present if the functionality is correct
    @Test
    void preferredDiscardTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method takeTurnMethod = Player.class.getDeclaredMethod("takeTurn");
        takeTurnMethod.setAccessible(true);
        Method createPlayerOutputMethod = Player.class.getDeclaredMethod("createPlayerOutput");
        createPlayerOutputMethod.setAccessible(true);

        assertEquals(2, players.getFirst().getPlayerHand().get(1).getValue());

        createPlayerOutputMethod.invoke(players.getFirst());
        takeTurnMethod.invoke(players.getFirst());

        assertEquals(1, players.getFirst().getPlayerHand().get(1).getValue());
    }
}
