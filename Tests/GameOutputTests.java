import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;


public class GameOutputTests {
    // This runs the main method and starts a full game to generate full output files
    @BeforeEach
    void runGame() throws IOException {
        CardGame.main();
    }

    // This test checks that the output files for the players are present in the directory and are named correctly once
    // the program terminates
    @Test
    void testPlayerOutput() {
        File file1 = new File("src/player1_output.txt");
        File file2 = new File("src/player2_output.txt");
        assertTrue(file1.exists());
        assertTrue(file2.exists());

    }

    // This test checks that the output files for the decks are present in the directory and are named correctly once
    // the program terminates
    @Test
    void testDeckOutput() {
        File file1 = new File("src/deck1_output.txt");
        File file2 = new File("src/deck2_output.txt");
        assertTrue(file1.exists());
        assertTrue(file2.exists());
    }
}
