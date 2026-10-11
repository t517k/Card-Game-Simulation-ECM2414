import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class CardGame {
    ArrayList<Card> pack;
    ArrayList<Card> decks;
    int playerTotal;
    int deckTotal;
    String packPath;

    public static void main(String[] args) {
        CardGame currentGame = new CardGame();
        Scanner in = new Scanner(System.in);
        System.out.println("Game Start!");
        while (true) {
            System.out.println("Enter number of players:");
            int input = in.nextInt(); //change input to numPlayers so that it is clearer?
            if (input < 1 || )
        }
        System.out.println("Enter path of pack to load:");
        String packPath = in.next();
        //Thread[] playerThreads = new Thread[]

    }

    public void loadPack(int playerTotal, String packPath) {
        File packFile = new File(packPath);
        try(Scanner fileReader = new Scanner(packFile)) {
            while (fileReader.hasNextLine()) {
                int cardValue = fileReader.nextInt();
                Card card = new Card(playerTotal, cardValue);
                pack.add(card);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }

    public Boolean checkPack(int playerTotal, String packPath) {

    }
}
