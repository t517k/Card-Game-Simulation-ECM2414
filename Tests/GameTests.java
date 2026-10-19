import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameTests {
    ArrayList<CardDeck> decks = new ArrayList<>();
    ArrayList<Player> players = new  ArrayList<>();
    private final AtomicBoolean gameOver = new AtomicBoolean(false);
    private final Object turnLock = new Object();

    // Sets up the decks and players and allocates card objects from the specified files to the objects ready for the
    // tests of the game functionality overall
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

        Scanner input1 = new Scanner(new File("Tests/test_resources/test_player1_instant_win_setup.txt"));
        Player player1 = new Player(1, gameOver, decks.getFirst(), decks.get(1), turnLock);
        while (input1.hasNextLine()) {
            int cardValue = Integer.parseInt(input1.nextLine());
            player1.addInitialCard(new Card(cardValue));
        }
        input1.close();
        players.add(player1);
        Scanner input2 = new Scanner(new File("Tests/test_resources/test_player2_instant_win_setup.txt"));
        Player player2 = new Player(2, gameOver, decks.getFirst(), decks.get(1), turnLock);
        while (input2.hasNextLine()) {
            int cardValue = Integer.parseInt(input2.nextLine());
            player2.addInitialCard(new Card(cardValue));
        }
        input2.close();
        players.add(player2);
    }

    // This test checks that the code will handle an instant win condition where it should set gameOver to true immediately.
    // It is important to test this as the game needs to finish and other threads must be stopped for a clean stop
    @Test
    void instantWinTest() throws NoSuchMethodException {
        Method takeTurnMethod = Player.class.getDeclaredMethod("takeTurn");
        takeTurnMethod.setAccessible(true);
        Method createPlayerOutputMethod = Player.class.getDeclaredMethod("createPlayerOutput");
        createPlayerOutputMethod.setAccessible(true);

        players.getFirst().run();

        assertTrue(gameOver.get());
    }
}
